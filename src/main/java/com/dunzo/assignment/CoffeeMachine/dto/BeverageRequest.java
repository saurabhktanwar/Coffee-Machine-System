package com.dunzo.assignment.CoffeeMachine.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeverageRequest {
    private Integer outletNumber;
    private String  beverage;
    private Integer time;
}
