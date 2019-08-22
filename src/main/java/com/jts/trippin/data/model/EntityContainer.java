package com.jts.trippin.data.model;

import com.jts.trippin.processor.DemoEdmProvider;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlActionImport;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlFunctionImport;

import java.util.List;

public class EntityContainer {

    private CsdlEntityContainer entityContainer;
    private List<CsdlActionImport> actionImports;
    private List<CsdlFunctionImport> functionImports;
    private List<CsdlEntitySet> entitySets;

    public static final FullQualifiedName CONTAINER = new FullQualifiedName(DemoEdmProvider.NAMESPACE, "Container");

    public EntityContainer(String name) {
        this.entityContainer = new CsdlEntityContainer();
        this.entityContainer.setName(name);
    }

}
