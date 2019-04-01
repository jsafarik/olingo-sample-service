package com.jts.trippin.data.model;

import java.util.List;

public abstract class AbstractEntitySet {

    private List<AbstractEntity> entities;

    public void addEntity(AbstractEntity entity){
        this.entities.add(entity);
    }

}
