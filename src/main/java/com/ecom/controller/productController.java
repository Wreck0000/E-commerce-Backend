package com.ecom.controller;

import com.ecom.Model.Product;
import com.ecom.exceptions.ResourceNotFoundException;
import com.ecom.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import response.apiResponse;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class productController {

    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<apiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(new apiResponse("SUCCESS!", products));
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<apiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(new apiResponse("SUCCESS!", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new apiResponse(e.getMessage(), null));
        }
    }
}
