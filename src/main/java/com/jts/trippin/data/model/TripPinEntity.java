package com.jts.trippin.data.model;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

public abstract class TripPinEntity {

    public abstract FullQualifiedName getEtFqn();
    public abstract String getEsName();
    public abstract Object getId();
    public abstract Entity createEntity();

}
