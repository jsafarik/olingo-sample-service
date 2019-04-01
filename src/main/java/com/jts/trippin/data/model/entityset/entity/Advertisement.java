package com.jts.trippin.data.model.entityset.entity;

import com.jts.trippin.data.model.AbstractEntity;
import com.jts.trippin.data.model.Util;
import com.jts.trippin.service.DemoEdmProvider;
import lombok.AllArgsConstructor;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.format.ContentType;

import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
public class Advertisement extends AbstractEntity {


    private UUID id;
    private String name;
    private Timestamp AirDate;

    /**
     * Special property to store the media content
     **/
    public static final String MEDIA_PROPERTY_NAME = "$value";

    private byte[] media;

    public final static FullQualifiedName ET_FQN
            = new FullQualifiedName(DemoEdmProvider.NAMESPACE, Advertisement.class.getSimpleName());

    public static final String ES_NAME = "Advertisements";

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
    public org.apache.olingo.commons.api.data.Entity createEntity() {
        org.apache.olingo.commons.api.data.Entity entity = new org.apache.olingo.commons.api.data.Entity();
        entity.setType(ET_FQN.getFullQualifiedNameAsString());
        entity.addProperty(new Property(null, "ID", ValueType.PRIMITIVE,
                this.id));
        entity.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, this.name));
        entity.addProperty(new Property(null, "AirDate", ValueType.PRIMITIVE, this.AirDate));
        entity.addProperty(new Property(null, MEDIA_PROPERTY_NAME, ValueType.PRIMITIVE, this.media));
        entity.setMediaContentType(ContentType.parse("text/plain").toContentTypeString());
        entity.setId(Util.createId(this));
        return entity;
    }
}
