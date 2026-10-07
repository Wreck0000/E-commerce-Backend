package com.ecom.controller;

import com.ecom.exception.ResourceNotFoundException;
import com.ecom.model.Order;
import com.ecom.response.ApiResponse;
import com.ecom.security.user.CustomUserDetails;
import com.ecom.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    
    private Long getAuthenticatedUserId(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails=(CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder() {
        try {
            Long userId = getAuthenticatedUserId();
            Order order = orderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Order Placed Successfully!", order));
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            Long userId = getAuthenticatedUserId();
            Order order = orderService.getOrder(orderId);
            
            // Security check: Make sure this order belongs to the user!
            if (order.getUser() == null || !order.getUser().getId().equals(userId)) {
                return ResponseEntity.status(FORBIDDEN).body(new ApiResponse("You are not authorized to view this order.", null));
            }
            
            return ResponseEntity.ok(new ApiResponse("Success", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
