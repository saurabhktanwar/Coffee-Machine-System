package com.dunzo.assignment.CoffeeMachine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import com.dunzo.assignment.CoffeeMachine.core.CoffeeMachine;
import com.dunzo.assignment.CoffeeMachine.dto.BeverageRequest;
import com.dunzo.assignment.CoffeeMachine.dto.CoffeeMachineInitialState;
import com.dunzo.assignment.CoffeeMachine.exception.CoffeeMachineException;
import com.dunzo.assignment.CoffeeMachine.exception.ErrorCode;
import com.dunzo.assignment.CoffeeMachine.utils.CoffeeMachineStateInitializerUtil;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

public class CoffeeMachineTest {

    private static final String FILE_NAME = "input.json";

    public static void main(String[] args) throws Exception {
        CoffeeMachineTest coffeeMachineTest = new CoffeeMachineTest();
        coffeeMachineTest.runTests();
    }

    private void runTests() throws Exception {
        happy_test1();
        happy_test_2();
        happy_test_3();
        negative_test_for_exception();
    }

    /**
     * Tries to prepare the following beverages - hot_tea from outlet 1 at time=0 => Should be prepared hot_coffee from
     * outlet 2 at time=0 => Should be prepared green_tea from outlet 3 at time=5 => Should not be prepared, because
     * some ingredients are insufficient. black_tea from outlet 2 at time=10 => Should not be prepared, because some
     * ingredients are insufficient.
     * 
     * @throws Exception
     */
    private void happy_test1() throws Exception {
        System.out.println("Test1...");
        CoffeeMachine coffeeMachine = initializeCoffeeMachine();
        System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(1, "hot_tea", 0)));
        System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(2, "hot_coffee", 0)));
        System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(3, "green_tea", 5)));
        System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(2, "black_tea", 10)));
        System.out.println("===========================");
    }

    /**
     * hot_tea from outlet 1 at time=0 => Should be prepared black_tea from outlet 2 at time=0 => Should be prepared
     * green_tea from outlet 3 at time=10 => Should not be prepared, because some ingredients are insufficient.
     * hot_coffee from outlet 2 at time=20 => Should not be prepared, because some ingredients are insufficient.
     * 
     * @throws Exception
     */
    private void happy_test_2() throws Exception {
        System.out.println("Test2...");
        CoffeeMachine coffeeMachine = initializeCoffeeMachine();
        System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(1, "hot_tea", 0)));
        System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(2, "black_tea", 0)));
        System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(3, "green_tea", 10)));
        System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(2, "hot_coffee", 20)));
        System.out.println("===========================");
    }

    /**
     * Tries to prepare the beverage from the outlet, if the ingredients are insufficient, refills the insufficient
     * ingredients and prepares the beverage. Shows all available outlets Shows ingredients running low (which have
     * quantity < threshold), where threshold = 100ml. Shows the quantities of all the ingredients in the coffee
     * machine.
     * 
     * @throws Exception
     */
    private void happy_test_3() throws Exception {
        System.out.println("Test3...");
        CoffeeMachine coffeeMachine = initializeCoffeeMachine();
        System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(1, "hot_tea", 0)));
        System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(2, "black_tea", 1)));
        System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(3, "green_tea", 2)));
        coffeeMachine.refillIngredient("green_mixture", 30.0);
        System.out.println(String.format("Ingredient %s has been refilled by %f ml", "green_mixture", 30.0));
        coffeeMachine.refillIngredient("sugar_syrup", 50.0);
        System.out.println(String.format("Ingredient %s has been refilled by %f ml", "sugar_syrup", 50.0));
        coffeeMachine.refillIngredient("hot_water", 500.0);
        System.out.println(String.format("Ingredient %s has been refilled by %f ml", "hot_water", 500.0));
        System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(3, "green_tea", 2)));
        showAllAvailableOutlets(coffeeMachine, 6);
        showIngredientsRunningLow(coffeeMachine);
        showIngredientsQuantities(coffeeMachine);
        System.out.println("===========================");
    }

    /**
     * Tries to prepare the beverage from the outlet, which is currently unavailable, throws CoffeeManagementException
     * hot_tea from outlet 1 at time=0 => should be prepared black_tea from outlet 1 at time=4 => should throw error,
     * because the outlet 1 is currently being used, and will be available only from time=5. black_tea from outlet 1 at
     * time=5 => should be prepared, as now the outlet is available.
     *
     * @throws Exception
     */
    private void negative_test_for_exception() throws Exception {
        System.out.println("Test4...");
        CoffeeMachine coffeeMachine = initializeCoffeeMachine();
        try {
            System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(1, "hot_tea", 0)));
            System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(1, "black_tea", 2)));
            System.out.println(coffeeMachine.prepareBeverage(getBeverageRequest(1, "black_tea", 5)));
            System.out.println("===========================");
        } catch (CoffeeMachineException cme) {
            Assert.assertEquals(ErrorCode.OUTLET_CURRENTLY_UNAVAILABLE.getErrorCode(),
                    cme.getErrorCode().getErrorCode());
            if (!cme.getErrorCode().equals(ErrorCode.OUTLET_CURRENTLY_UNAVAILABLE)) {
                throw new AssertionError("The error thrown should be - outlet currently unavailable.");
            }
        }
    }

    private BeverageRequest getBeverageRequest(final Integer outletNumber, final String beverage, final Integer time) {
        final BeverageRequest.BeverageRequestBuilder beverageRequestBuilder = BeverageRequest.builder();
        return beverageRequestBuilder.beverage(beverage).outletNumber(outletNumber).time(outletNumber).build();
    }

    private void showAllAvailableOutlets(CoffeeMachine coffeeMachine, int time) {
        System.out.println(String.format("Showing all the available outlets at the time %d", time));
        for (Integer outletNumber : coffeeMachine.allAvailableOutlets(time)) {
            System.out.println("Outlet " + outletNumber + " is available");
        }
    }

    private void showIngredientsRunningLow(CoffeeMachine coffeeMachine) {
        System.out.println("Showing the ingredients which are currently running low in quantity.");
        if (coffeeMachine.showIngredientsRunningLow().isEmpty()) {
            System.out.println("Currently there are no ingredients which are running low in quantity");
            return;
        }
        for (String ingredient : coffeeMachine.showIngredientsRunningLow()) {
            System.out.println("Ingredient " + ingredient + " is running low.");
        }
    }

    private void showIngredientsQuantities(CoffeeMachine coffeeMachine) {
        System.out.println("Current quantity for each of the ingredients in the coffee machine");
        for (Map.Entry<String, Double> entry : coffeeMachine.getIngredientsQuantity().entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " ml");
        }
    }

    private CoffeeMachine initializeCoffeeMachine() throws ParseException, IOException {
        JSONObject jsonObject = parseJsonObjectFromResourceFilePath(FILE_NAME);
        CoffeeMachineInitialState coffeeMachineInitialState =
                CoffeeMachineStateInitializerUtil.extractInitializerStateInputFromJsonObject(jsonObject);
        CoffeeMachine coffeeMachine = new CoffeeMachine(coffeeMachineInitialState.getNumOutlets(),
                coffeeMachineInitialState.getIngredientsQuantity(), coffeeMachineInitialState.getBeverageInfo());
        return coffeeMachine;
    }

    private JSONObject parseJsonObjectFromResourceFilePath(String filePath) throws ParseException, IOException {
        File file = new File(this.getClass().getClassLoader().getResource(filePath).getFile());
        return (JSONObject) new JSONParser().parse(new FileReader(file));
    }
}
