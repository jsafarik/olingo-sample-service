package com.jts.trippin.data.model;

import com.jts.trippin.service.DemoEdmProvider;
import lombok.AllArgsConstructor;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

@AllArgsConstructor
public class Product extends TripPinEntity {

    public final static FullQualifiedName ET_FQN
            = new FullQualifiedName(DemoEdmProvider.NAMESPACE, Product.class.getSimpleName());

    public static final String ES_NAME = "Products";

    private long id;

    private String name;

    private String description;

    @Override
    public Entity getEntity() {

        Entity entity = new Entity();

        entity.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, this.id));
        entity.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, this.name));
        entity.addProperty(new Property(null, "Description", ValueType.PRIMITIVE, this.description));

        entity.setType(ET_FQN.getFullQualifiedNameAsString());
        entity.setId(Util.createId(this));

        return entity;
    }

    @Override
    public FullQualifiedName getEtFqn() {
        return ET_FQN;
    }

    @Override
    public String getEsName() {
        return ES_NAME;
    }

    @Override
    public Object getId() {
        return id;
    }
}
