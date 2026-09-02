package com.ecom.service.product;

import com.ecom.Model.Product;
import com.ecom.request.addProductRequest;

import java.util.List;

public interface IProductService {
    Product addproduct(addProductRequest product);

    Product getProductById(Long id);
    void deleteProductById(Long id);
    void updateProduct(Product product,Long productId);
    List<Product> getAllProducts();
    List<Product> getProductByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByCategoryAndBrand(String category, String brand);
    List<Product> getProductByName(String name);
    List<Product> getProductByBrandAndName(String brand, String name);
    Long countProductByBrandAndName(String brand, String name);

}
