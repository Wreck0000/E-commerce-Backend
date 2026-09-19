package com.ecom.model;

import com.ecom.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private BigDecimal orderTotalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @OneToMany(cascade = CascadeType.ALL,mappedBy="order",orphanRemoval = true)
    private Set<OrderItem> orderItems=new HashSet<>();


}
