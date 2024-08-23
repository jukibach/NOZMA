package com.ecommerce.userservice.mybatis.mapper;

import com.ecommerce.userservice.dto.request.PagePayload;
import com.ecommerce.userservice.dto.response.AccountDetailResponse;
import com.ecommerce.userservice.mybatis.sqlprovider.MybatisUserSqlProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface MybatisUserMapper {
    @SelectProvider(type = MybatisUserSqlProvider.class, method = "selectFields")
    List<AccountDetailResponse> selectFields(@Param("pagePayload") PagePayload pagePayload);
    
    @SelectProvider(type = MybatisUserSqlProvider.class, method = "getAccountDetail")
    AccountDetailResponse getAccountDetail(@Param("accountId") Long accountId);
}
