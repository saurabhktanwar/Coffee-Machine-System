package com.dunzo.assignment.CoffeeMachine.utils;

import java.util.Map;

import com.dunzo.assignment.CoffeeMachine.dto.CoffeeMachineInitialState;

import org.json.simple.JSONObject;

/**
 * This class is responsible to create the object of {@link CoffeeMachineInitialState} by extracting the field values
 * from the json object provided.
 */
public class CoffeeMachineStateInitializerUtil {

    public static CoffeeMachineInitialState extractInitializerStateInputFromJsonObject(JSONObject jsonObject) {
        jsonObject = (JSONObject) jsonObject.get("machine");
        int numOutlets = ((Map<String, Long>) jsonObject.get("outlets")).get("count_n").intValue();
        Map<String, Double> ingredientsQuantity = ((Map<String, Double>) jsonObject.get("total_items_quantity"));
        for (Map.Entry<String, Double> entry : ingredientsQuantity.entrySet()) {
            entry.setValue(Double.valueOf(String.valueOf(entry.getValue())));
        }
        Map<String, Map<String, Double>> beveragesInfo =
                ((Map<String, Map<String, Double>>) jsonObject.get("beverages"));
        for (Map.Entry<String, Map<String, Double>> entry : beveragesInfo.entrySet()) {
            for (Map.Entry<String, Double> kv : entry.getValue().entrySet()) {
                kv.setValue(Double.valueOf(String.valueOf(kv.getValue())));
            }
        }

        final CoffeeMachineInitialState.CoffeeMachineInitialStateBuilder coffeeMachineInitialStateBuilder =
                CoffeeMachineInitialState.builder();
        return coffeeMachineInitialStateBuilder.numOutlets(numOutlets).beverageInfo(beveragesInfo)
                .ingredientsQuantity(ingredientsQuantity).build();
    }
}
