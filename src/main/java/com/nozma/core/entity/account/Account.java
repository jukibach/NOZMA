package com.nozma.core.entity.account;

import com.nozma.core.entity.BaseDomain;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
@ToString(callSuper = true)
@Table(name = "t_accounts", schema = "s_account")
@Entity(name = "accounts")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Account extends BaseDomain implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 5616098585400912157L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 50, unique = true, nullable = false)
    private String accountName;
    
    private String password;
    
    @Email
    private String email;
    
    private boolean isLocked;
    
    private LocalDateTime lastLocked;
    
    private LocalDate fromDate;
    
    private LocalDate toDate;
    
    private boolean isPasswordGenerated;
    
    @OneToOne(fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @ToString.Exclude
    private Role role;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ToString.Exclude
    private User user;
    
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Account that)) {
            return false;
        }
        return getId() != null && Objects.equals(getId(), that.getId());
    }
    
    @Override
    public final int hashCode() {
        return getId().hashCode();
    }
}
