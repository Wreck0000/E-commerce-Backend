package com.ecom.controller;

import com.ecom.exception.ResourceNotFoundException;
import com.ecom.model.Cart;
import com.ecom.response.ApiResponse;
import com.ecom.security.user.CustomUserDetails;
import com.ecom.service.ICartItemService;
import com.ecom.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    
    private Long getAuthenticatedUserId(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails=(CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }

    private Long getMyCartId() {
        Long userId = getAuthenticatedUserId();
        Cart cart = cartService.getCartByUserId(userId);
        return cart.getId();
    }

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long productId,
                                                     @RequestParam int quantity) {
        try {
            Long cartId = getMyCartId();
            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item added to cart!!", cartId));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/item/{productId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long productId) {
        try {
            Long cartId = getMyCartId();
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("item removed from cart!!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/item/{productId}/quantity/{quantity}/update")
    public ResponseEntity<ApiResponse> updateItemFromCart(@PathVariable Long productId,
                                                          @PathVariable int quantity) {
        try {
            Long cartId = getMyCartId();
            cartItemService.updateItemFromCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Cart Updated", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
