package com.dunzo.assignment.CoffeeMachine.constant;

/*
 * Here we are taking 2 major assumptions. 1. Time to prepare beverages = 10 units. 2. Threshold quantity = 100.0 which
 * means if the quantity of any ingredient is below this threshold value then it is running low in quantity and we have
 * to refill it.
 */
public class Constants {
    public static final Integer TIME_TO_PREPARE_BEVERAGE       = 10;
    public static final Double  INGREDIENTS_THRESHOLD_QUANTITY = 100.0;
}
