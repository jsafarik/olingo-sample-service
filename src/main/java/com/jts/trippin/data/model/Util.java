package com.jts.trippin.data.model;

import org.apache.olingo.commons.api.ex.ODataRuntimeException;

import java.net.URI;
import java.net.URISyntaxException;

public class Util {

    public static URI createId(TripPinEntity entity) {
        return createId(entity, null);
    }

    public static URI createId(TripPinEntity entity, String navigationName) {
        try {
            StringBuilder sb = new StringBuilder(entity.getEsName()).append("(");
            sb.append(entity.getId()).append(")");
            if (navigationName != null) {
                sb.append("/").append(navigationName);
            }
            return new URI(sb.toString());
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create (Atom) id for entity: " + entity, e);
        }
    }

}
