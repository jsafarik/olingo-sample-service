package com.jts.trippin.data.model;

import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductSet {

    public static final String NAME = "Products";
    private CsdlEntitySet entitySet = new CsdlEntitySet();

    public ProductSet(Map<String, String> navigationPropertyBinding) {

        this.entitySet.setName(NAME);
        this.entitySet.setType(Product.FQN);

        List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<>();
        for (String key : navigationPropertyBinding.keySet()) {
            navPropBindingList.add(new CsdlNavigationPropertyBinding()
                    .setTarget(key)
                    .setPath(navigationPropertyBinding.get(key)));
        }

        this.entitySet.setNavigationPropertyBindings(navPropBindingList);
    }
}
