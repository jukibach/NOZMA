package com.nozma.core.dto.request;

import com.nozma.core.enums.ExerciseColumnEnum;
import com.nozma.core.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@AllArgsConstructor
public class ExercisePagePayload {
    @Getter
    private Integer pageSize;
    @Getter
    private Integer pageIndex;
    @Getter
    private String searchName;
    @Getter
    private String[] sort;
    
    private String sortOrders;
    
    public String getSortOrders() {
        List<String> orderByClauses = new ArrayList<>();
        if (CommonUtil.isNonNullOrNonEmpty(sort)) {
            for (String sortParam : sort) {
                String[] sortDetails = sortParam.split(" ");
                String sortBy = sortDetails.length > 0
                        ? Arrays.stream(ExerciseColumnEnum.values())
                        .filter(exerciseColumnEnum -> exerciseColumnEnum.getCode().equals(sortDetails[0])).findFirst()
                        .orElseThrow().getField()
                        : ExerciseColumnEnum.EXERCISE.getField();
                
                String direction = sortDetails.length > 1 && "desc".equalsIgnoreCase(sortDetails[1])
                        ? "DESC" : "ASC";
                orderByClauses.add("%s %s".formatted(sortBy, direction));
            }
        }
        
        // Join the clauses to create the final orderBy string
        String orderBy = CommonUtil.isNonNullOrNonEmpty(orderByClauses)
                ? String.join(", ", orderByClauses)
                : null;
        
        this.sortOrders = orderBy;
        return orderBy;
    }
    
}
