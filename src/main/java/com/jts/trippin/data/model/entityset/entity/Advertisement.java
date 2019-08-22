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
import org.apache.olingo.commons.api.format.ContentType;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class Advertisement extends AbstractEntity {

    /**
     * Special property to store the media content
     **/
    public static final String MEDIA_PROPERTY_NAME = "$value";


    public final static FullQualifiedName ET_FQN
            = new FullQualifiedName(DemoEdmProvider.NAMESPACE, Advertisement.class.getSimpleName());

    private Entity entity;

    public Advertisement(UUID id, String name, Timestamp AirDate, byte[] media) {
        this.entity = new Entity();

        entity.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, id));
        entity.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, name));
        entity.addProperty(new Property(null, "AirDate", ValueType.PRIMITIVE, AirDate));
        entity.addProperty(new Property(null, MEDIA_PROPERTY_NAME, ValueType.PRIMITIVE, media));
        entity.setMediaContentType(ContentType.parse("text/plain").toContentTypeString());

        entity.setType(ET_FQN.getFullQualifiedNameAsString());
        entity.setId(Util.createId(this));
    }

    @Override
    public FullQualifiedName getEtFqn() {
        return ET_FQN;
    }

    @Override
    public String getEsName() {
        return ODataEntity.ADVERTISEMENT.getEsName();
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
        final List<Entity> advertisements = manager.getEntityCollection(ODataEntity.ADVERTISEMENT.getEsName());

        advertisements.add(new Advertisement(UUID.fromString("f89dee73-af9f-4cd4-b330-db93c25ff3c7"),
            "Old School Lemonade Store, Retro Style",
            Timestamp.valueOf("2012-11-07 00:00:00"),
            "Advertisement numero uno".getBytes()).getEntity());

        advertisements.add(new Advertisement(UUID.fromString("db2d2186-1c29-4d1e-88ef-a127f521b9c67"),
            "Early morning start, need coffee",
            Timestamp.valueOf("2000-02-29 00:00:00"),
            "Super ad numero dos".getBytes()).getEntity());
    }

}
