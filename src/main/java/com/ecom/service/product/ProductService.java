package com.ecom.service.product;

import com.ecom.Model.Category;
import com.ecom.Model.Product;
import com.ecom.exceptions.ProductNotFoundException;
import com.ecom.repository.CategoryRepository;
import com.ecom.repository.ProductRepository;
import com.ecom.request.AddProductRequest;
import com.ecom.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product addproduct(AddProductRequest request) {
        // 1. Find category
        Category category = categoryRepository.findByName(request.getCategory().getName());

        // 2. If category doesn't exist, create it
        if (category == null) {
            category = new Category(request.getCategory().getName());
            category = categoryRepository.save(category);
        }
        // 3. Create product
        Product product = new Product();
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setInventory(request.getInventory());
        product.setDescription(request.getDescription());
        // 4. Connect product to category
        product.setCategory(category);
        // 5. Save product
        return productRepository.save(product);
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Product not found"));
    }
    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                ()->{throw new ProductNotFoundException("Product Not Found");});
    }
    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Product not found"));
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return productRepository.save(existingProduct);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }
    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }
    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }
    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }
    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }
}
