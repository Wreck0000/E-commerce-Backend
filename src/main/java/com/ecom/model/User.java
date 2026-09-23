package com.ecom.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.List;
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @NaturalId
    @Column(unique = true,nullable = false)
    private String email;
    private String password;
    @OneToOne(mappedBy="user" ,cascade=CascadeType.ALL,orphanRemoval=true)
    @JsonIgnore
    private Cart cart;
    @OneToMany(mappedBy="user",cascade=CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Order> orders;
    public User(Long id,String FirstName,String LastName,String Email,String Password){
        this.id=id;
        this.firstName=FirstName;
        this.lastName=LastName;
        this.email=Email;
        this.password=Password;
    }

}
