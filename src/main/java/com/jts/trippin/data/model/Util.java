package com.jts.trippin.data.model;

import org.apache.olingo.commons.api.ex.ODataRuntimeException;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Util {

    public static URI createId(AbstractEntity entity) {
        return createId(entity, null);
    }

    public static URI createId(AbstractEntity entity, String navigationName) {
        try {
            StringBuilder sb = new StringBuilder(entity.getEsName()).append("(");
            appendSingleQuoteIfString(sb, entity);
            sb.append(entity.getId());
            appendSingleQuoteIfString(sb, entity);
            sb.append(")");
            if (navigationName != null) {
                sb.append("/").append(navigationName);
            }
            return new URI(sb.toString());
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create (Atom) id for entity: " + entity, e);
        }
    }

    // TODO: try to ad ' to see if checking of single quotes can be ommited in util/Util.entityMatchesAllKeys()
    private static void appendSingleQuoteIfString(StringBuilder sb, AbstractEntity entity){
        if (entity.getId().getClass().equals(String.class)) {
            sb.append("");
        }
    }

}
