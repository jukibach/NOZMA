package com.nozma.core.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagePayload {
    private Integer pageSize;
    private Integer pageIndex;
    
    public Integer getPageSize() {
        return Optional.ofNullable(pageSize).orElse(20);
    }
    
    public Integer getPageIndex() {
        return Optional.ofNullable(pageIndex).orElse(0) * getPageSize();
    }
}
