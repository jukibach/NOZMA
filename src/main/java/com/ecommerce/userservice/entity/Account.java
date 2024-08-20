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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
@Table(name = "t_accounts", schema = "s_account")
@Entity(name = "accounts")
@Getter
@Setter
public class Account extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String accountName;
    
    private String password;
    
    private String email;
    
    private Long userId;
    
    private boolean isLocked;
    
    private LocalDateTime lastLocked;
    
    private LocalDate fromDate;
    
    private LocalDate toDate;
    
    private boolean isPasswordGenerated;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
    private List<AccountRole> accountRoles;
    
}
