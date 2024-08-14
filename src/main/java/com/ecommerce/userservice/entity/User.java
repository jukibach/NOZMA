package com.ecommerce.userservice.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
@Table(name = "t_users", schema = "s_account")
@Entity(name = "users")
@Getter
@Setter
public class User extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    
    private String middleName;
    
    private String lastName;
    
    private String phoneNumber;
    
    private Integer age;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserRole> userRoles;
    
}
