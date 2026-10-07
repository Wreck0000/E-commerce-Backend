package com.ecom.controller;

import com.ecom.exception.ResourceNotFoundException;
import com.ecom.model.Cart;
import com.ecom.response.ApiResponse;
import com.ecom.security.user.CustomUserDetails;
import com.ecom.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;
    private Long getAuthenticatedUserId(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails=(CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }
    private boolean isMyCart(Cart cart,Long userId){
        return cart.getUser()!=null&&cart.getUser().getId().equals(userId);

    }

//    @PostMapping("/initialize")
//    public ResponseEntity<ApiResponse> initializeCart() {
//        try {
//            Long cartId = cartService.initializeNewCart();
//            return ResponseEntity.ok(new ApiResponse("Cart initialized successfully!", cartId));
//        } catch (Exception e) {
//            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
//        }
//    }

    @GetMapping("/mycart")
    public ResponseEntity<ApiResponse> getMyCart() {
        try {
            Long myUserId=getAuthenticatedUserId();
            Cart cart = cartService.getCartByUserId(myUserId);
            return ResponseEntity.ok(new ApiResponse("Success", cart));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        try {
            Long myUserId=getAuthenticatedUserId();
            Cart cart = cartService.getCart(cartId);
            if(!isMyCart(cart,myUserId)){
                return ResponseEntity.status(FORBIDDEN).body(new ApiResponse("your are not authorized",null));
            }
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Clear Cart Success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{cartId}/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        try {
            Long myUserId=getAuthenticatedUserId();
            Cart cart = cartService.getCart(cartId);
            if (!isMyCart(cart, myUserId)) {
                return ResponseEntity.status(FORBIDDEN).body(new ApiResponse("You are not authorized to view this cart.", null));
            }
            BigDecimal cartPrice = cartService.getTotalAmount(cartId);
            return ResponseEntity.ok(new ApiResponse("Total Price", cartPrice));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
