package com.ecommerce.userservice.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "t_account_roles", schema = "s_account")
@Entity(name = "AccountRoles")
@Getter
@Setter
public class AccountRole extends BaseDomain {
    
    @EmbeddedId
    private AccountRoleId id;
    
    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AccountRoleId implements Serializable {
        private Long accountId;
        private Integer roleId;
    }
}
