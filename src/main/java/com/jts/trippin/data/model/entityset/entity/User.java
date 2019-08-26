package com.jts.trippin.data.model.entityset.entity;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

import com.jts.trippin.data.TransactionalEntityManager;
import com.jts.trippin.data.model.AbstractEntity;
import com.jts.trippin.data.model.Util;
import com.jts.trippin.processor.DemoEdmProvider;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class User extends AbstractEntity {

    public final static FullQualifiedName ET_FQN
        = new FullQualifiedName(DemoEdmProvider.NAMESPACE, User.class.getSimpleName());

    private Entity entity;

    public User(String id, String firstName, String lastName, int gender){
        this.entity = new Entity();

        entity.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, id));
        entity.addProperty(new Property(null, "FirstName", ValueType.PRIMITIVE, firstName));
        entity.addProperty(new Property(null, "LastName", ValueType.PRIMITIVE, lastName));
        entity.addProperty(new Property(null, "Gender", ValueType.ENUM, gender));

        entity.setType(ET_FQN.getFullQualifiedNameAsString());
        entity.setId(Util.createId(this));
    }

    @Override
    public FullQualifiedName getEtFqn() {
        return ET_FQN;
    }

    @Override
    public String getEsName() {
        return ODataEntity.USER.getEsName();
    }

    @Override
    public Object getId() {
        return this.entity.getProperty("ID").getValue();
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    public static void initSampleData(TransactionalEntityManager manager) {
        final List<Entity> userList = manager.getEntityCollection(ODataEntity.USER.getEsName());
        userList.add(new User("coolBob", "Bob", "CoolName", 0).getEntity());
        userList.add(new User("whatever", "Frank", "Nobody", 2).getEntity());
    }

}
