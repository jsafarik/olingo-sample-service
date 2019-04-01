package com.jts.trippin.data.model.entityset.entity;

import com.jts.trippin.data.model.AbstractEntity;
import com.jts.trippin.data.model.Util;
import com.jts.trippin.data.model.entityset.Products;
import com.jts.trippin.service.DemoEdmProvider;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

public class Product extends AbstractEntity {

    public final static FullQualifiedName FQN
            = new FullQualifiedName(DemoEdmProvider.NAMESPACE, Product.class.getSimpleName());

    private int id;

    private String name;

    private String description;

    private Entity entity;

    public Product(int id, String name, String description) {
        this.entity = new Entity();

        entity.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, this.id));
        entity.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, this.name));
        entity.addProperty(new Property(null, "Description", ValueType.PRIMITIVE, this.description));

        entity.setType(FQN.getFullQualifiedNameAsString());
        entity.setId(Util.createId(this));
    }

    @Override
    public Entity createEntity() {

        Entity entity = new Entity();

        entity.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, this.id));
        entity.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, this.name));
        entity.addProperty(new Property(null, "Description", ValueType.PRIMITIVE, this.description));

        entity.setType(FQN.getFullQualifiedNameAsString());
        entity.setId(Util.createId(this));

        return entity;
    }

    @Override
    public FullQualifiedName getEtFqn() {
        return FQN;
    }

    @Override
    public String getEsName() {
        return Products.NAME;
    }

    @Override
    public Object getId() {
        return this.entity.getProperty("ID");
    }
}