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
@Table(name = "m_role_privileges", schema = "s_account")
@Entity(name = "RolePrivileges")
@Getter
@Setter
public class RolePrivilege extends BaseDomain {
    @EmbeddedId
    private RolePrivilegeId id;
    
    @Embeddable
    @Data
    public static class RolePrivilegeId implements Serializable {
        private Long roleId;
        private Long privilegeId;
    }
}
