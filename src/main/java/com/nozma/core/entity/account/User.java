package com.nozma.core.entity.account;

import com.nozma.core.entity.BaseDomain;
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
import org.hibernate.annotations.Formula;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
@Table(name = "t_users", schema = "s_account")
@Entity(name = "users")
@Getter
@EntityListeners(AuditingEntityListener.class)
@Setter
public class User extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String firstName;
    
    private String middleName;
    
    private String lastName;
    
    private String phoneNumber;
    
    private String birthdate;
    
    // 26-08 < 15-01 is false, so the birthday has already passed this year.
    // else, minus 1 because the birthday has not passed this year.
    @Formula("""
            (EXTRACT(YEAR FROM CURRENT_DATE) - EXTRACT(YEAR FROM TO_DATE(birthdate, 'DD-MM-YYYY')) -
            CASE WHEN (TO_CHAR(CURRENT_DATE, 'MMDD') < TO_CHAR(TO_DATE(birthdate, 'DD-MM-YYYY'), 'MMDD'))
            THEN 1
            ELSE 0
            END
            )
            """)
    private Integer age;
    
}
