package com.jts.trippin.data;

import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmKeyPropertyRef;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.deserializer.DeserializerException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;

import com.jts.trippin.data.model.entityset.Products;
import com.jts.trippin.data.model.entityset.entity.Advertisement;
import com.jts.trippin.data.model.entityset.entity.Category;
import com.jts.trippin.data.model.entityset.entity.Product;
import com.jts.trippin.util.Util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StorageUtil {

    private Storage storage;

    public StorageUtil(Storage storage) {
        this.storage = storage;
    }

    private Entity createSimpleEntity(Entity entity, int newId) {
        Entity newEntity = new Entity();

        if (entity.getType().equals(Product.FQN.getFullQualifiedNameAsString())) {

            log.info("Creating Product with the new Product class");

            Product product =
                new Product(newId, (String) entity.getProperty("Name").getValue(), (String) entity.getProperty("Description").getValue());
            newEntity = product.getEntity();
        } else if (entity.getType().equals(Category.ET_FQN.getFullQualifiedNameAsString())) {

            log.info("Creating Category with the new Category class");

            Category category = new Category(newId, (String) entity.getProperty("Name").getValue());
            newEntity = category.getEntity();
        } else {
            newEntity.setType(entity.getType());
            log.info("Entity type: " + entity.getType());

            // Add all provided properties
            newEntity.getProperties().addAll(entity.getProperties());

            // Add the key property
            newEntity.getProperties().add(new Property(null, "ID", ValueType.PRIMITIVE, newId));
            newEntity.setId(createId(newEntity, "ID"));
        }
        return newEntity;
    }

    public Entity createEntity(EdmEntitySet edmEntitySet, EdmEntityType edmEntityType, Entity entity,
        List<Entity> entityList, final String rawServiceUri) throws ODataApplicationException {
        // Create the new key of the entity
        int newId = 1;
        while (entityIdExists(newId, entityList)) {
            newId++;
        }

        // 1.) Create the entity
        Entity newEntity = createSimpleEntity(entity, newId);

        // 2.1.) Apply binding links
        for (final Link link : entity.getNavigationBindings()) {
            final EdmNavigationProperty edmNavigationProperty = edmEntityType.getNavigationProperty(link.getTitle());
            final EdmEntitySet targetEntitySet = (EdmEntitySet) edmEntitySet.getRelatedBindingTarget(link.getTitle());

            if (edmNavigationProperty.isCollection() && link.getBindingLinks() != null) {
                for (final String bindingLink : link.getBindingLinks()) {
                    final Entity relatedEntity = readEntityByBindingLink(bindingLink, targetEntitySet, rawServiceUri);
                    createLink(edmNavigationProperty, newEntity, relatedEntity);
                }
            } else if (!edmNavigationProperty.isCollection() && link.getBindingLink() != null) {
                final Entity relatedEntity = readEntityByBindingLink(link.getBindingLink(), targetEntitySet, rawServiceUri);
                createLink(edmNavigationProperty, newEntity, relatedEntity);
            }
        }

        // 2.2.) Create nested entities
        for (final Link link : entity.getNavigationLinks()) {
            final EdmNavigationProperty edmNavigationProperty = edmEntityType.getNavigationProperty(link.getTitle());
            final EdmEntitySet targetEntitySet = (EdmEntitySet) edmEntitySet.getRelatedBindingTarget(link.getTitle());

            if (edmNavigationProperty.isCollection() && link.getInlineEntitySet() != null) {
                for (final Entity nestedEntity : link.getInlineEntitySet().getEntities()) {
                    final Entity newNestedEntity = storage.createEntityData(targetEntitySet, nestedEntity, rawServiceUri);
                    createLink(edmNavigationProperty, newEntity, newNestedEntity);
                }
            } else if (!edmNavigationProperty.isCollection() && link.getInlineEntity() != null) {
                final Entity newNestedEntity = storage.createEntityData(targetEntitySet, link.getInlineEntity(), rawServiceUri);
                createLink(edmNavigationProperty, newEntity, newNestedEntity);
            }
        }

        entityList.add(newEntity);

        return newEntity;
    }

    private Entity readEntityByBindingLink(final String entityId, final EdmEntitySet edmEntitySet,
        final String rawServiceUri) throws ODataApplicationException {

        UriResourceEntitySet entitySetResource = null;
        try {
            entitySetResource = storage.getOdata().createUriHelper().parseEntityId(storage.getEdm(), entityId, rawServiceUri);

            if (!entitySetResource.getEntitySet().getName().equals(edmEntitySet.getName())) {
                throw new ODataApplicationException("Execpted an entity-id for entity set " + edmEntitySet.getName()
                    + " but found id for entity set " + entitySetResource.getEntitySet().getName(),
                    HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
            }
        } catch (DeserializerException e) {
            throw new ODataApplicationException(entityId + " is not a valid entity-Id",
                HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ENGLISH);
        }

        return storage.readEntityData(entitySetResource.getEntitySet(), entitySetResource.getKeyPredicates());
    }

    public EntityCollection getEntityCollection(final List<Entity> entityList) {

        EntityCollection retEntitySet = new EntityCollection();
        retEntitySet.getEntities().addAll(entityList);

        return retEntitySet;
    }

    public Entity getEntity(EdmEntityType edmEntityType, List<UriParameter> keyParams, List<Entity> entityList)
        throws ODataApplicationException {

        // the list of entities at runtime
        EntityCollection entitySet = getEntityCollection(entityList);

        /* generic approach to find the requested entity */
        Entity requestedEntity = Util.findEntity(edmEntityType, entitySet, keyParams);

        if (requestedEntity == null) {
            // this variable is null if our data doesn't contain an entity for the requested key
            // Throw suitable exception
            throw new ODataApplicationException("Product for requested key doesn't exist",
                HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        return requestedEntity;
    }

    private boolean entityIdExists(int id, List<Entity> entityList) {

        for (Entity entity : entityList) {
            int existingID = ((int) entity.getProperty("ID").getValue());
            if (existingID == id) {
                return true;
            }
        }

        return false;
    }

    public void updateEntity(EdmEntityType edmEntityType, List<UriParameter> keyParams, Entity updateEntity,
        HttpMethod httpMethod, List<Entity> entityList) throws ODataApplicationException {

        Entity entity = getEntity(edmEntityType, keyParams, entityList);
        if (entity == null) {
            throw new ODataApplicationException("Product not found", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        // loop over all properties and replace the values with the values of the given payload
        // Note: ignoring ComplexType, as we don't have it in our odata model
        List<Property> existingProperties = entity.getProperties();
        for (Property existingProp : existingProperties) {
            String propName = existingProp.getName();

            // ignore the key properties, they aren't updateable
            if (isKey(edmEntityType, propName)) {
                continue;
            }

            Property updateProperty = updateEntity.getProperty(propName);
            // the request payload might not consider ALL properties, so it can be null
            if (updateProperty == null) {
                // if a property has NOT been added to the request payload
                // depending on the HttpMethod, our behavior is different
                if (httpMethod.equals(HttpMethod.PATCH)) {
                    // as of the OData spec, in case of PATCH, the existing property is not touched
                    continue; // do nothing
                } else if (httpMethod.equals(HttpMethod.PUT)) {
                    // as of the OData spec, in case of PUT, the existing property is set to null (or to default value)
                    existingProp.setValue(existingProp.getValueType(), null);
                    continue;
                }
            }

            // change the value of the properties
            existingProp.setValue(existingProp.getValueType(), updateProperty.getValue());
        }
    }

    public void deleteEntity(EdmEntityType edmEntityType, List<UriParameter> keyParams, List<Entity> entityList)
        throws ODataApplicationException {

        Entity entity = getEntity(edmEntityType, keyParams, entityList);
        if (entity == null) {
            throw new ODataApplicationException("Product not found", HttpStatusCode.NOT_FOUND.getStatusCode(),
                Locale.ENGLISH);
        }

        entityList.remove(entity);
    }

    private boolean isKey(EdmEntityType edmEntityType, String propertyName) {
        List<EdmKeyPropertyRef> keyPropertyRefs = edmEntityType.getKeyPropertyRefs();
        for (EdmKeyPropertyRef propRef : keyPropertyRefs) {
            String keyPropertyName = propRef.getName();
            if (keyPropertyName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }

    public void linkProductsAndCategories(final int numberOfProducts) {
        final List<Entity> productList = storage.getManager().getEntityCollection(Products.NAME);
        final List<Entity> categoryList = storage.getManager().getEntityCollection(Category.ES_NAME);

        if (numberOfProducts >= 1) {
            setLink(productList.get(0), "Category", categoryList.get(0));
        }
        if (numberOfProducts >= 2) {
            setLink(productList.get(1), "Category", categoryList.get(0));
        }
        if (numberOfProducts >= 3) {
            setLink(productList.get(2), "Category", categoryList.get(1));
        }
        if (numberOfProducts >= 4) {
            setLink(productList.get(3), "Category", categoryList.get(1));
        }
        if (numberOfProducts >= 5) {
            setLink(productList.get(4), "Category", categoryList.get(2));
        }
        if (numberOfProducts >= 6) {
            setLink(productList.get(5), "Category", categoryList.get(2));
        }

        if (numberOfProducts >= 1) {
            setLinks(categoryList.get(0), "Products",
                productList.subList(0, Math.min(2, numberOfProducts)).toArray(new Entity[0]));
        }
        if (numberOfProducts >= 3) {
            setLinks(categoryList.get(1), "Products",
                productList.subList(2, Math.min(4, numberOfProducts)).toArray(new Entity[0]));
        }
        if (numberOfProducts >= 5) {
            setLinks(categoryList.get(2), "Products",
                productList.subList(4, Math.min(6, numberOfProducts)).toArray(new Entity[0]));
        }
    }

    private URI createId(Entity entity, String idPropertyName) {
        return createId(entity, idPropertyName, null);
    }

    private URI createId(Entity entity, String idPropertyName, String navigationName) {
        try {
            StringBuilder sb = new StringBuilder(getEntitySetName(entity)).append("(");
            final Property property = entity.getProperty(idPropertyName);
            sb.append(property.asPrimitive()).append(")");
            if (navigationName != null) {
                sb.append("/").append(navigationName);
            }
            return new URI(sb.toString());
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create (Atom) id for entity: " + entity, e);
        }
    }

    private String getEntitySetName(Entity entity) {
        if (Category.ET_FQN.getFullQualifiedNameAsString().equals(entity.getType())) {
            return Category.ES_NAME;
        } else if (Product.FQN.getFullQualifiedNameAsString().equals(entity.getType())) {
            return Products.NAME;
        } else if (Advertisement.ET_FQN.getFullQualifiedNameAsString().equals(entity.getType())) {
            return Advertisement.ES_NAME;
        }
        return entity.getType();
    }

    private void createLink(final EdmNavigationProperty navigationProperty, final Entity srcEntity,
        final Entity destEntity) {
        setLink(navigationProperty, srcEntity, destEntity);

        final EdmNavigationProperty partnerNavigationProperty = navigationProperty.getPartner();
        if (partnerNavigationProperty != null) {
            setLink(partnerNavigationProperty, destEntity, srcEntity);
        }
    }

    private void setLink(final EdmNavigationProperty navigationProperty, final Entity srcEntity,
        final Entity targetEntity) {
        if (navigationProperty.isCollection()) {
            setLinks(srcEntity, navigationProperty.getName(), targetEntity);
        } else {
            setLink(srcEntity, navigationProperty.getName(), targetEntity);
        }
    }

    private void setLink(final Entity entity, final String navigationPropertyName, final Entity target) {
        Link link = entity.getNavigationLink(navigationPropertyName);
        if (link == null) {
            link = new Link();
            link.setRel(Constants.NS_NAVIGATION_LINK_REL + navigationPropertyName);
            link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
            link.setTitle(navigationPropertyName);
            link.setHref(target.getId().toASCIIString());

            entity.getNavigationLinks().add(link);
        }
        link.setInlineEntity(target);
    }

    private void setLinks(final Entity entity, final String navigationPropertyName, final Entity... targets) {
        if (targets.length == 0) {
            return;
        }

        Link link = entity.getNavigationLink(navigationPropertyName);
        if (link == null) {
            link = new Link();
            link.setRel(Constants.NS_NAVIGATION_LINK_REL + navigationPropertyName);
            link.setType(Constants.ENTITY_SET_NAVIGATION_LINK_TYPE);
            link.setTitle(navigationPropertyName);
            link.setHref(entity.getId().toASCIIString() + "/" + navigationPropertyName);

            EntityCollection target = new EntityCollection();
            target.getEntities().addAll(Arrays.asList(targets));
            link.setInlineEntitySet(target);

            entity.getNavigationLinks().add(link);
        } else {
            link.getInlineEntitySet().getEntities().addAll(Arrays.asList(targets));
        }
    }
}
