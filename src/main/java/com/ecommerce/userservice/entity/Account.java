package com.ecommerce.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
@Table(name = "t_accounts", schema = "s_account")
@Entity(name = "accounts")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Account extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String accountName;
    
    private String password;
    
    private String email;
    
    private Long userId;
    
    private int roleId;

    private String roleName;
    
    private boolean isLocked;
    
    private LocalDateTime lastLocked;
    
    private LocalDate fromDate;
    
    private LocalDate toDate;
    
    private boolean isPasswordGenerated;

}
