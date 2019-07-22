package com.jts.trippin.data.model.entityset.entity;

public enum ODataEntity {

    CATEGORY(Category.class, "Categories", false),
    PRODUCT(Product.class, "Products", false),
    USER(User.class, "Users", false),
    ADVERTISEMENT(Advertisement.class, "Advertisements", true);

    private Class<?> entityClass;

    private final String ES_NAME;

    // mediaType = true is not refactored or checked, for testing use just mediaType = false (or refactor it and)
    // check if it is ok
    private boolean mediaType;

    public String getEsName() {
        return this.ES_NAME;
    }

    public Class<?> getEntityClass() {
        return this.entityClass;
    }

    public boolean isMediaType() {
        return this.mediaType;
    }

    private ODataEntity(Class<?> entityClass, String ES_NAME, boolean mediaType) {
        this.entityClass = entityClass;
        this.ES_NAME = ES_NAME;
        this.mediaType = mediaType;
    }

}
