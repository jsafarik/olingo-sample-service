package com.jts.trippin.data;

import org.apache.olingo.commons.api.data.Entity;

import com.jts.trippin.data.model.entityset.Products;
import com.jts.trippin.data.model.entityset.entity.Advertisement;
import com.jts.trippin.data.model.entityset.entity.Category;
import com.jts.trippin.data.model.entityset.entity.Product;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class PrepopulatedData {

    private TransactionalEntityManager manager;

    public PrepopulatedData(TransactionalEntityManager manager) {
        this.manager = manager;
    }

    public void initProductSampleData() {
        final List<Entity> productList = manager.getEntityCollection(Products.NAME);

        productList.add(new Product(0, "Notebook Basic 15",
            "Notebook Basic, 1.7GHz - 15 XGA - 1024MB DDR2 SDRAM - 40GB").getEntity());

        productList.add(new Product(1, "Notebook Professional 17",
            "Notebook Professional, 2.8GHz - 15 XGA - 8GB DDR3 RAM - 500GB").getEntity());

        productList.add(new Product(2, "1UMTS PDA",
            "Ultrafast 3G UMTS/HSDPA Pocket PC, supports GSM network").getEntity());

        productList.add(new Product(3, "Comfort Easy",
            "32 GB Digital Assitant with high-resolution color screen").getEntity());

        productList.add(new Product(4, "Ergo Screen",
            "19 Optimum Resolution 1024 x 768 @ 85Hz, resolution 1280 x 960").getEntity());

        productList.add(new Product(5, "Flat Basic",
            "Optimum Hi-Resolution max. 1600 x 1200 @ 85Hz, Dot Pitch: 0.24mm").getEntity());
    }

    public void initCategorySampleData() {
        final List<Entity> categoryList = manager.getEntityCollection(Category.ES_NAME);
        categoryList.add(new Category(0, "Notebooks").getEntity());
        categoryList.add(new Category(1, "Organizers").getEntity());
        categoryList.add(new Category(2, "Monitors").getEntity());
    }

    public void initAdvertisementSampleData() {
        final List<Entity> advertisements = manager.getEntityCollection(Advertisement.ES_NAME);

        advertisements.add(new Advertisement(UUID.fromString("f89dee73-af9f-4cd4-b330-db93c25ff3c7"),
            "Old School Lemonade Store, Retro Style",
            Timestamp.valueOf("2012-11-07 00:00:00"),
            "Advertisement numero uno".getBytes()).getEntity());

        advertisements.add(new Advertisement(UUID.fromString("db2d2186-1c29-4d1e-88ef-a127f521b9c67"),
            "Early morning start, need coffee",
            Timestamp.valueOf("2000-02-29 00:00:00"),
            "Super ad numero dos".getBytes()).getEntity());
    }

}
