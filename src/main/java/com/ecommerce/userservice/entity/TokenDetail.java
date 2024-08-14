package com.ecommerce.userservice.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TokenDetail {
    
    private Long accountId;
    private String accountName;
    private Date issueDate;
    private Date expiryDate;
}
