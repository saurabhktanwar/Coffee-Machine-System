package com.dunzo.assignment.CoffeeMachine.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/*
 * This class contains unAvailableIngredients which Represents the list of ingredients which are insufficient or
 * unavailable, in order to prepare a beverage
 */
@Data
@Builder
public class OutletResponse {
    private List<String> unAvailableIngredients;
}
