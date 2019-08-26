package com.jts.trippin.data.model.entityset.entity;

import org.apache.olingo.commons.api.data.ComplexValue;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

import com.jts.trippin.data.TransactionalEntityManager;
import com.jts.trippin.data.model.AbstractEntity;
import com.jts.trippin.data.model.Util;
import com.jts.trippin.processor.DemoEdmProvider;

import java.util.List;

public class Product extends AbstractEntity {

    public final static FullQualifiedName FQN
        = new FullQualifiedName(DemoEdmProvider.NAMESPACE, Product.class.getSimpleName());

    private Entity entity;

    public Product(int id, String name, String description, Configuration config) {
        this.entity = new Entity();

        ComplexValue configComplex = null;
        if (config != null) {
            configComplex = new ComplexValue();
            configComplex.getValue().add(new Property(null, "CPU", ValueType.PRIMITIVE, config.getCpu()));
            configComplex.getValue().add(new Property(null, "GPU", ValueType.PRIMITIVE, config.getGpu()));
            configComplex.getValue().add(new Property(null, "RAM", ValueType.PRIMITIVE, config.getRam()));
        }

        entity.addProperty(new Property(null, "ID", ValueType.PRIMITIVE, id));
        entity.addProperty(new Property(null, "Name", ValueType.PRIMITIVE, name));
        entity.addProperty(new Property(null, "Description", ValueType.PRIMITIVE, description));
        entity.addProperty(new Property(null, "Configuration", ValueType.COMPLEX, configComplex));

        entity.setType(FQN.getFullQualifiedNameAsString());
        entity.setId(Util.createId(this));
    }

    public static void initSampleData(TransactionalEntityManager manager) {
        final List<Entity> productList = manager.getEntityCollection(ODataEntity.PRODUCT.getEsName());

        productList.add(new Product(0, "Notebook Basic 15",
            "Notebook Basic, 1.7GHz - 15 XGA - 1024MB DDR2 SDRAM - 40GB", new Configuration("1.7GHz", null, "1GB DDR2")).getEntity());

        productList.add(new Product(1, "Notebook Professional 17",
            "Notebook Professional, 2.8GHz - 15 XGA - 8GB DDR3 RAM - 500GB", new Configuration("2.8GHz", "GTX2080Ti", "8GB DDR3")).getEntity());

        productList.add(new Product(2, "1UMTS PDA",
            "Ultrafast 3G UMTS/HSDPA Pocket PC, supports GSM network", new Configuration("3GHz", "MX150", "4GB")).getEntity());

        productList.add(new Product(3, "Comfort Easy",
            "32 GB Digital Assitant with high-resolution color screen", new Configuration("1GHz", null, "1GB")).getEntity());

        productList.add(new Product(4, "Ergo Screen",
            "19 Optimum Resolution 1024 x 768 @ 85Hz, resolution 1280 x 960", new Configuration("0.5GHz", null, "512MB")).getEntity());

        productList.add(new Product(5, "Flat Basic",
            "Optimum Hi-Resolution max. 1600 x 1200 @ 85Hz, Dot Pitch: 0.24mm", new Configuration("1GHz", null, "256MB")).getEntity());
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public FullQualifiedName getEtFqn() {
        return FQN;
    }

    @Override
    public String getEsName() {
        return ODataEntity.PRODUCT.getEsName();
    }

    @Override
    public Object getId() {
        return this.entity.getProperty("ID").getValue();
    }
}
