package com.dunzo.assignment.CoffeeMachine.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import com.dunzo.assignment.CoffeeMachine.dto.BeverageRequest;
import com.dunzo.assignment.CoffeeMachine.dto.CoffeeMachineInitialState;
import com.dunzo.assignment.CoffeeMachine.exception.CoffeeMachineException;
import com.dunzo.assignment.CoffeeMachine.utils.CoffeeMachineStateInitializerUtil;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This is the main class, which creates the instance of coffee machine, and provide all the functionality of it, this
 * is an interactive class, where user needs to input some values based upon their choices, it supports all features
 * like prepare a beverage, show current quantities of ingredients, refill ingredients, show available outlets etc.
 */
public class CoffeeApplication {

    // input file
    private static final String FILE_NAME = "input.json";

    public static void main(String[] args) throws Exception {
        CoffeeApplication coffeeApplication = new CoffeeApplication();
        coffeeApplication.run();
    }

    private void run() throws ParseException, IOException, CoffeeMachineException {
        CoffeeMachine coffeeMachine = initializeCoffeeMachine();
        Scanner console = new Scanner(System.in);
        int timer = 0;
        boolean cont = true;
        while (cont) {
            System.out.println("\n Type 1 to show the current quantities of all the ingredients"
                    + "\n Type 2 to show all the current available outlets in the machine"
                    + "\n Type 3 to show all the beverages supported by the machine" + "\n Type 4 to prepare a beverage"
                    + "\n Type 5 to show all the ingredients which are running low in quantities"
                    + "\n Type 6 to refill the ingredient" + "\n Type 0 to exit \n");

            int choice = console.nextInt();
            switch (choice) {
                case 1:
                    showIngredientsQuantities(coffeeMachine);
                    break;
                case 2:
                    showAllAvailableOutlets(coffeeMachine, timer);
                    break;
                case 3:
                    showAllBeveragesSupported(coffeeMachine);
                    break;
                case 4:
                    prepareBeverage(coffeeMachine, timer, console);
                    break;
                case 5:
                    showIngredientsRunningLow(coffeeMachine);
                    break;
                case 6:
                    refillIngredients(coffeeMachine, console);
                    break;
                case 0:
                    cont = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
            ++timer;
        }
    }

    /* This method is responsible for Initializes the coffee machine. */
    private CoffeeMachine initializeCoffeeMachine() throws ParseException, IOException {
        JSONObject jsonObject = parseJsonObjectFromResourceFilePath(FILE_NAME);
        final CoffeeMachineInitialState coffeeMachineInitialState =
                CoffeeMachineStateInitializerUtil.extractInitializerStateInputFromJsonObject(jsonObject);
        return new CoffeeMachine(coffeeMachineInitialState.getNumOutlets(),
                coffeeMachineInitialState.getIngredientsQuantity(), coffeeMachineInitialState.getBeverageInfo());
    }

    private void showIngredientsQuantities(CoffeeMachine coffeeMachine) {
        System.out.println("Showing the current quantity for each of the ingredients in the coffee machine");
        for (Map.Entry<String, Double> entry : coffeeMachine.getIngredientsQuantity().entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " ml");
        }
    }

    private void showAllBeveragesSupported(CoffeeMachine coffeeMachine) {
        System.out.println("Showing all the beverages supported by the coffee machine");
        for (Map.Entry<String, Map<String, Double>> entry : coffeeMachine.getBeverageInfo().entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    private void showAllAvailableOutlets(CoffeeMachine coffeeMachine, int time) {
        System.out.println(String.format("Showing all the available outlets at the time %d", time));
        for (Integer outletNumber : coffeeMachine.allAvailableOutlets(time)) {
            System.out.println("Outlet " + outletNumber + " is available");
        }
    }

    private void prepareBeverage(CoffeeMachine coffeeMachine, int time, Scanner console) throws CoffeeMachineException {
        System.out.println("Which outlet you want the beverage to be prepared from?");
        int outletNumber = console.nextInt();
        System.out.println("What beverage do you want to have?");
        String beverage = console.next();
        final BeverageRequest.BeverageRequestBuilder beverageRequestBuilder = BeverageRequest.builder();
        System.out.println(coffeeMachine.prepareBeverage(
                beverageRequestBuilder.beverage(beverage).outletNumber(outletNumber).time(outletNumber).build()));
    }

    private void showIngredientsRunningLow(CoffeeMachine coffeeMachine) {
        if (coffeeMachine.showIngredientsRunningLow().isEmpty()) {
            System.out.println("Currently there are no ingredients which are running low in quantity");
            return;
        }
        System.out.println("Showing the ingredients which are currently running low in quantity.");
        for (String ingredient : coffeeMachine.showIngredientsRunningLow()) {
            System.out.println("Ingredient " + ingredient + " is running low.");
        }
    }

    private void refillIngredients(CoffeeMachine coffeeMachine, Scanner console) throws CoffeeMachineException {
        System.out.println("Which ingredient you want to refill?");
        String ingredient = console.next();
        System.out.println("Refill quantity?");
        Double refillQuantity = console.nextDouble();
        coffeeMachine.refillIngredient(ingredient, refillQuantity);
        System.out.println(String.format("Ingredient %s has been refilled by %f ml", ingredient, refillQuantity));
    }

    private JSONObject parseJsonObjectFromResourceFilePath(String filePath) throws ParseException, IOException {
        File file = new File(this.getClass().getClassLoader().getResource(filePath).getFile());
        return (JSONObject) new JSONParser().parse(new FileReader(file));
    }
}
