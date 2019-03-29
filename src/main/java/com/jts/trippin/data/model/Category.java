package com.jts.trippin.data.model;

import com.jts.trippin.service.DemoEdmProvider;
import lombok.AllArgsConstructor;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

@AllArgsConstructor
public class Category extends TripPinEntity {

    private long id;

    private String name;

    public final static FullQualifiedName ET_FQN
            = new FullQualifiedName(DemoEdmProvider.NAMESPACE, Category.class.getSimpleName());

    public static final String ES_NAME = "Categories";

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
        return this.id;
    }

    @Override
    public Entity createEntity() {

        Entity entity = new Entity();

        entity.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, this.id));
        entity.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, this.name));
        entity.setType(ET_FQN.getFullQualifiedNameAsString());
        entity.setId(Util.createId(this));

        return entity;
    }
}
