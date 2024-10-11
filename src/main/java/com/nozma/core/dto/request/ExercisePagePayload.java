package com.nozma.core.dto.request;

import com.nozma.core.enums.ExerciseColumnEnum;
import com.nozma.core.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Setter
@AllArgsConstructor
public class ExercisePagePayload {
    
    private Integer pageSize;
    private Integer pageIndex;
    @Getter
    private String searchName;
    
    @Getter
    private String[] sort;
    
    public Integer getPageSize() {
        return Optional.ofNullable(pageSize).orElse(20);
    }
    
    public Integer getPageIndex() {
        return Optional.ofNullable(pageIndex).orElse(0) * getPageSize();
    }
    
    private String sortOrders;
    
    public String getSortOrders() {
        List<String> orderByClauses = new ArrayList<>();
        if (CommonUtil.isNonNullOrNonEmpty(sort)) {
            for (String sortParam : sort) {
                String[] sortDetails = sortParam.split(" ");
                String sortBy = sortDetails.length > 0
                        ? Arrays.stream(ExerciseColumnEnum.values())
                        .filter(exerciseColumnEnum -> exerciseColumnEnum.getCode().equals(sortDetails[0]))
                        .findFirst()
                        .orElseThrow()
                        .getField()
                        : ExerciseColumnEnum.EXERCISE.getField();
                
                String direction = sortDetails.length > 1 && "desc".equalsIgnoreCase(sortDetails[1])
                        ? "DESC"
                        : "ASC";
                orderByClauses.add("%s %s".formatted(sortBy, direction));
            }
        }
        
        // Join the clauses to create the final orderBy string
        String orderBy = Optional.of(orderByClauses)
                .filter(CommonUtil::isNonNullOrNonEmpty)
                .map(clauses -> String.join(", ", clauses))
                .orElse(null);
        
        this.sortOrders = orderBy;
        return orderBy;
    }
    
}
