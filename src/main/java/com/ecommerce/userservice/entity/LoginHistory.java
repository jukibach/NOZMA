package com.ecommerce.userservice.entity;

import com.ecommerce.userservice.enums.DeviceType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "t_login_histories", schema = "s_account")
@Entity(name = "loginHistories")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class LoginHistory extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long accountId;
    
    private Long userId;
    
    private String accountName;
    
    private String ipAddress; // Supports both IPv4 and IPv6 addresses
    
    private LocalDateTime loginTimestamp;
    
    @Enumerated(EnumType.STRING)
    private DeviceType deviceType;
    
    private String deviceOs;
    
    private String browserName;
    
    private String browserVersion;
    
    private boolean isLoginSuccessful;
    
    private String failureReason;
}
