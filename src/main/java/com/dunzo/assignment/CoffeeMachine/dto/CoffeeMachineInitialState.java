package com.dunzo.assignment.CoffeeMachine.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoffeeMachineInitialState {
    private int                              numOutlets;
    private Map<String, Double>              ingredientsQuantity;
    private Map<String, Map<String, Double>> beverageInfo;
}
