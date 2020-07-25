package com.dunzo.assignment.CoffeeMachine.core;

import static com.dunzo.assignment.CoffeeMachine.constant.Constants.INGREDIENTS_THRESHOLD_QUANTITY;
import static com.dunzo.assignment.CoffeeMachine.exception.ErrorCode.BEVERAGE_NOT_SUPPORTED;
import static com.dunzo.assignment.CoffeeMachine.exception.ErrorCode.INVALID_OUTLET;
import static com.dunzo.assignment.CoffeeMachine.exception.ErrorCode.REFILL_QUANTITY_SHOULD_NOT_BE_NEGATIVE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dunzo.assignment.CoffeeMachine.dto.BeverageRequest;
import com.dunzo.assignment.CoffeeMachine.dto.Outlet;
import com.dunzo.assignment.CoffeeMachine.dto.OutletResponse;
import com.dunzo.assignment.CoffeeMachine.exception.CoffeeMachineException;

/**
 * Used the observer design pattern here, where {@link CoffeeMachine} is a subject and outlets is the list of Observers
 * to this subject, where {@link Outlet} is the Observer. Whenever, a new beverage request comes up, it has three fields
 * - outletNumber, beverage and time Now, CoffeeMachine forwards the beverage request to the respective outlet to
 * prepare it. If the outletNumber is invalid, it throws {@link CoffeeMachineException} error. If the beverage is not
 * supported by the machine, it throws {@link CoffeeMachineException} error.
 */
public class CoffeeMachine {
    private List<Outlet>                     outlets;
    private Map<String, Double>              ingredientsQuantity;
    private Map<String, Map<String, Double>> beverageInfo;

    // Initializes with by creating numOutlets instances of the Outlet, which gets attached to this machine.
    public CoffeeMachine(int numOutlets, Map<String, Double> ingredientsQuantity,
            Map<String, Map<String, Double>> beverageInfo) {
        this.outlets = new ArrayList<Outlet>();

        for (int outlet = 1; outlet <= numOutlets; ++outlet) {
            new Outlet(this);
        }
        this.ingredientsQuantity = ingredientsQuantity;
        this.beverageInfo = beverageInfo;
    }

    public void attach(Outlet outlet) {
        this.outlets.add(outlet);
    }

    public void detach(Outlet outlet) {
        this.outlets.remove(outlet);
    }

    /**
     * Takes in the prepare beverage request, forwards the request to the respective outlet. Returns the string which
     * indicates whether the beverage have been prepared or could not be prepared with some reasons.
     * 
     * @param beverageRequest
     * @return
     * @throws CoffeeMachineException
     */
    public String prepareBeverage(BeverageRequest beverageRequest) throws CoffeeMachineException {
        if (beverageRequest.getOutletNumber() < 1 || beverageRequest.getOutletNumber() > this.outlets.size()) {
            throw new CoffeeMachineException(INVALID_OUTLET);
        }
        if (!beverageInfo.containsKey(beverageRequest.getBeverage())) {
            throw new CoffeeMachineException(BEVERAGE_NOT_SUPPORTED);
        }
        Outlet servingOutlet = this.outlets.get(beverageRequest.getOutletNumber() - 1);
        OutletResponse outletResponse =
                servingOutlet.prepareBeverage(beverageRequest.getBeverage(), beverageRequest.getTime());

        if (outletResponse.getUnAvailableIngredients().isEmpty()) {
            return String.format("The requested beverage %s has been prepared from the outlet number %d at time %d",
                    beverageRequest.getBeverage(), beverageRequest.getOutletNumber(), beverageRequest.getTime());
        }
        return String.format(
                "The requested beverage %s could not be prepared because the quantity of below ingredients are insufficient, please refill these ingredients and try again.\n%s",
                beverageRequest.getBeverage(), outletResponse.getUnAvailableIngredients().toString());
    }

    /**
     * Shows all the ingredients which are currently running low in quantity.
     * 
     * @return
     */
    public List<String> showIngredientsRunningLow() {
        List<String> ingredientsRunningLow = new ArrayList<>();
        for (Map.Entry<String, Double> entry : this.ingredientsQuantity.entrySet()) {
            if (entry.getValue() < INGREDIENTS_THRESHOLD_QUANTITY) {
                ingredientsRunningLow.add(entry.getKey());
            }
        }
        return ingredientsRunningLow;
    }

    /**
     * Refills the quantity of some ingredient.
     * 
     * @param ingredient the ingredient to be refilled
     * @param fillQuantity the quantity by which it should be refilled
     * @throws CoffeeMachineException if the fillQuantity is negative value
     */
    public void refillIngredient(String ingredient, Double fillQuantity) throws CoffeeMachineException {
        if (fillQuantity < 0.0) {
            throw new CoffeeMachineException(REFILL_QUANTITY_SHOULD_NOT_BE_NEGATIVE);
        }
        this.ingredientsQuantity.put(ingredient, ingredientsQuantity.getOrDefault(ingredient, 0.0) + fillQuantity);
    }

    /**
     * Returns the list of all the available outlets at the given time.
     * 
     * @param time time for which we want to see the available outlets
     * @return
     */
    public List<Integer> allAvailableOutlets(int time) {
        List<Integer> availableOutlets = new ArrayList<>();
        int index = 1;
        for (Outlet outlet : this.outlets) {
            if (outlet.isAvailable(time)) {
                availableOutlets.add(index);
            }
            index++;
        }
        return availableOutlets;
    }

    public Map<String, Double> getIngredientsQuantity() {
        return ingredientsQuantity;
    }

    public Map<String, Map<String, Double>> getBeverageInfo() {
        return beverageInfo;
    }
}
