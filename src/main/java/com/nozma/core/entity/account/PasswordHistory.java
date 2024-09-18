package com.nozma.core.entity.account;

import com.nozma.core.entity.BaseDomain;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "t_password_histories", schema = "s_account")
@Entity(name = "passwordHistories")
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PasswordHistory extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long accountId;
    
}
