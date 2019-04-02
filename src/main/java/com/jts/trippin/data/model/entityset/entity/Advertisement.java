package com.jts.trippin.data.model.entityset.entity;

import com.jts.trippin.data.model.AbstractEntity;
import com.jts.trippin.data.model.Util;
import com.jts.trippin.service.DemoEdmProvider;
import lombok.AllArgsConstructor;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.format.ContentType;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
public class Advertisement extends AbstractEntity {

    /**
     * Special property to store the media content
     **/
    public static final String MEDIA_PROPERTY_NAME = "$value";


    public final static FullQualifiedName ET_FQN
            = new FullQualifiedName(DemoEdmProvider.NAMESPACE, Advertisement.class.getSimpleName());

    public static final String ES_NAME = "Advertisements";
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
        return ES_NAME;
    }

    @Override
    public Object getId() {
        return this.entity.getProperty("ID");
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }
}
