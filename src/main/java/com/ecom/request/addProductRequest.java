package com.ecom.request;

import com.ecom.Model.Category;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
@Entity
@Data
public class addProductRequest {
        private Long id;
        private String name;
        private String brand;
        private BigDecimal price;
        private int inventory;
        private String description;
        private Category category;
}
