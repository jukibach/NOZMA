package com.ecommerce.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "m_roles", schema = "s_account")
@Entity(name = "Roles")
@Getter
@Setter
public class Role extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    
    private String description;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private List<UserRole> userRoles;
    
    @OneToMany(mappedBy = "role")
    private List<RolePrivilege> rolePrivileges;
}
