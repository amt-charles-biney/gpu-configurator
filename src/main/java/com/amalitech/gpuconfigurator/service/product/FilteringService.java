package com.amalitech.gpuconfigurator.service.product;

import com.amalitech.gpuconfigurator.model.Product;

import java.util.List;

public interface FilteringService {
<<<<<<< HEAD
    List<Product> filterProduct(String productCase, String price, String productType, String processor,String category);
=======
    List<Product> filterProduct(String productCase, String price, String productType, String processor,String categories, String brand);
>>>>>>> 2c77d56 (fix: filtering recieves categories instead of category)

}
