package com.jts.trippin.data.model.entityset.entity;

import com.jts.trippin.data.TransactionalEntityManager;
import com.jts.trippin.data.model.AbstractEntity;
import com.jts.trippin.data.model.Util;
import com.jts.trippin.processor.DemoEdmProvider;
import lombok.AllArgsConstructor;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

import java.util.List;

@AllArgsConstructor
public class Category extends AbstractEntity {

    public final static FullQualifiedName ET_FQN
            = new FullQualifiedName(DemoEdmProvider.NAMESPACE, Category.class.getSimpleName());

    private Entity entity;

    public Category(int id, String name){
        this.entity = new Entity();

        entity.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, id));
        entity.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, name));

        entity.setType(ET_FQN.getFullQualifiedNameAsString());
        entity.setId(Util.createId(this));
    }

    @Override
    public FullQualifiedName getEtFqn() {
        return ET_FQN;
    }

    @Override
    public String getEsName() {
        return ODataEntity.CATEGORY.getEsName();
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
        final List<Entity> categoryList = manager.getEntityCollection(ODataEntity.CATEGORY.getEsName());
        categoryList.add(new Category(0, "Notebooks").getEntity());
        categoryList.add(new Category(1, "Organizers").getEntity());
        categoryList.add(new Category(2, "Monitors").getEntity());
    }

}
