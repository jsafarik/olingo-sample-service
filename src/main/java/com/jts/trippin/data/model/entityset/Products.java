package com.jts.trippin.data.model.entityset;

import com.jts.trippin.data.model.AbstractEntity;
import com.jts.trippin.data.model.entityset.entity.Product;
import lombok.Getter;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for CsdlEntitySet
 */
public class Products {

    private List<AbstractEntity> entities;
    private int idCounter = 0;
    public static final String NAME = Products.class.getSimpleName();
    @Getter
    private CsdlEntitySet entitySet;
    private List<CsdlNavigationPropertyBinding> navPropBindingList;

    public Products(){
        this.entitySet = new CsdlEntitySet();
        this.entitySet.setName(NAME);
        this.entitySet.setType(Product.FQN);
        this.entitySet.setNavigationPropertyBindings(getNavigationPropertyBindings());
    }

    private List<CsdlNavigationPropertyBinding> getNavigationPropertyBindings(){
        if (this.navPropBindingList != null) {
            return this.navPropBindingList;
        }

        this.navPropBindingList = new ArrayList<>();

        CsdlNavigationPropertyBinding navPropBinding = new CsdlNavigationPropertyBinding();
        // the target entity set where the navigation property points to
        navPropBinding.setTarget("Categories");
        // the path from entity type to navigation property (only 1 CategorY for item)
        navPropBinding.setPath("Category");

        navPropBindingList.add(navPropBinding);
        return navPropBindingList;
    }

    public void addEntity(String name, String description){

        Product product = new Product(createId(), name, description);
        this.entities.add(product);

    }

    public int createId(){
        return idCounter++;
    }

}
