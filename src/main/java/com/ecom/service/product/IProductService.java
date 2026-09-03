package com.ecom.service.product;

import com.ecom.Model.Product;
import com.ecom.request.AddProductRequest;
import com.ecom.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addproduct(AddProductRequest product);

    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest request, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByCategoryAndBrand(String category, String brand);
    List<Product> getProductByName(String name);
    List<Product> getProductByBrandAndName(String brand, String name);
    Long countProductByBrandAndName(String brand, String name);

}
