package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        assert intent != null;
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        final ProgressBar progressBar = findViewById(R.id.pb_image);
        progressBar.setVisibility(View.VISIBLE);
        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {}
                });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        populateAlsoKnownTextView(sandwich);
        populatePlaceOfOriginTextView(sandwich);
        populateIngredientsTextView(sandwich);

        TextView descriptionTV = findViewById(R.id.description_tv);
        descriptionTV.append(sandwich.getDescription());
    }

    private void populateAlsoKnownTextView(Sandwich sandwich) {
        List<String> commonNames = sandwich.getAlsoKnownAs();
        if (!commonNames.isEmpty()) {
            TextView knownAsTV = findViewById(R.id.also_known_tv);
            int commonNamesCount = commonNames.size();
            for (int i = 0; i < commonNamesCount; i++) {
                String commonName = commonNames.get(i);
                boolean isLastElement = i == commonNamesCount - 1;
                knownAsTV.append(isLastElement ? commonName + "." : commonName + ", ");
            }
        } else {
            LinearLayout alsoKnownLayout = findViewById(R.id.also_known_ll);
            alsoKnownLayout.setVisibility(View.GONE);
        }
    }

    private void populatePlaceOfOriginTextView(Sandwich sandwich) {
        String placeOfOrigin = sandwich.getPlaceOfOrigin();
        if (!placeOfOrigin.isEmpty()) {
            TextView placeOfOriginTV = findViewById(R.id.origin_tv);
            placeOfOriginTV.setText(placeOfOrigin);
        } else {
            LinearLayout placeOfOriginLayout = findViewById(R.id.place_of_origin_ll);
            placeOfOriginLayout.setVisibility(View.GONE);
        }
    }

    private void populateIngredientsTextView(Sandwich sandwich) {
        TextView ingredientsTV = findViewById(R.id.ingredients_tv);
        List<String> ingredients = sandwich.getIngredients();
        int ingredientsCount = ingredients.size();
        for (int i = 0; i < ingredientsCount; i++) {
            String ingredient = ingredients.get(i);
            ingredientsTV.append(ingredient + "\n\n");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
