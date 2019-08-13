package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        if (!json.isEmpty()) {
            Sandwich sandwich = new Sandwich();
            try {
                JSONObject sandwichObject = new JSONObject(json);

                // Parse Name fields
                JSONObject sandwichNameObject = sandwichObject.optJSONObject("name");
                String mainName = sandwichNameObject.optString("mainName");
                sandwich.setMainName(mainName);
                JSONArray sandwichNameArray = sandwichNameObject.optJSONArray("alsoKnownAs");
                List<String> commonNames = new ArrayList<>();
                parseJSONArray(sandwichNameArray, commonNames);
                sandwich.setAlsoKnownAs(commonNames);

                // Parse Place of Origin field
                String placeOfOrigin = sandwichObject.optString("placeOfOrigin");
                sandwich.setPlaceOfOrigin(placeOfOrigin);

                // Parse Description field
                String description = sandwichObject.optString("description");
                sandwich.setDescription(description);

                // Parse Image Field
                String image = sandwichObject.optString("image");
                sandwich.setImage(image);

                // Parse Ingredients field
                JSONArray ingredientsJSONArray = sandwichObject.optJSONArray("ingredients");
                List<String> ingredients = new ArrayList<>();
                parseJSONArray(ingredientsJSONArray, ingredients);
                sandwich.setIngredients(ingredients);

                return sandwich;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static void parseJSONArray(JSONArray jsonArray, List<String> list) {
        for (int i = 0; i < jsonArray.length(); i++) {
            list.add(jsonArray.optString(i));
        }
    }
}
