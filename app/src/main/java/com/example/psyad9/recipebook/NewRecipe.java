package com.example.psyad9.recipebook;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import static com.example.psyad9.recipebook.RecipeContract.*;

public class NewRecipe extends AppCompatActivity {

    private TextView warningText;
    private int recipeid;
    String editName;
    String editIngr;
    String editSteps;
    private ArrayList<Long> ingredientids;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.newrecipe);

        warningText = (TextView) findViewById(R.id.warningText);


    }

    //This method retrieves all values entered by the user, after checking that all fields have been filled and then calls two methods.
    public void onClickSubmit(View view) {
        editName = ((EditText) findViewById(R.id.titletext)).getText().toString();
        editIngr = ((EditText) findViewById(R.id.ingredientstext)).getText().toString();
        editSteps = ((EditText) findViewById(R.id.instructionstext)).getText().toString();

        if (editName.equals("") || editIngr.equals("") || editSteps.equals("")) {
            warningText.setVisibility(View.VISIBLE);
        } else {
            Log.d("log", "Submitted");
            insertRecipe();
            insertIngredients();
        }

    }

    //this first method called by the submit button adds all recipe table values to a contentvalue and inserts this into the table using the Content provider
    //It also records the recipe id of the new recipe entry as this is used later to add data to the recipe_ingredients table
    private void insertRecipe() {
        ContentValues RecipeValues = new ContentValues();
        RecipeValues.put(RECIPE_NAME, editName);
        RecipeValues.put(RECIPE_INSTRUCTIONS, editSteps);
        RecipeValues.put(RECIPE_RATING, 0);
        Uri result = getContentResolver().insert(RECIPE_URI, RecipeValues);
        recipeid = Integer.parseInt(result.getLastPathSegment());

    }

    //This method handles all ingredients and recipe_ingredients modification
    public void insertIngredients() {
        //Takes the ingredients, splits into an arraylist, removes duplicates and makes an array for ingredient integer id values
        List<String> ingredientstemp = Arrays.asList(editIngr.split("\\r?\\n"));
        ArrayList<String> ingredients1 = new ArrayList<String>(ingredientstemp);
        Set<String> hashSet = new LinkedHashSet(ingredients1);
        ArrayList<String> ingredients = new ArrayList(hashSet);
        ingredientids = new ArrayList<>();

        //makes a cursor containing all the existing ingredients in the database.
        //This list is compared to the ingredients entered
        //if any ingredient already is stored, the id is found and is added to a list
        String[] projection = new String[]{INGREDIENT_ID, INGREDIENT_NAME};
        final Cursor cursor = getContentResolver().query(INGREDIENT_URI, projection, null, null, null);
        try {
            while (cursor.moveToNext()) {
                System.out.println(cursor.getString(cursor.getColumnIndex(INGREDIENT_NAME)));
                if (ingredients.contains(cursor.getString(cursor.getColumnIndex(INGREDIENT_NAME)))) {
                    ingredientids.add((long) cursor.getInt(cursor.getColumnIndex(INGREDIENT_ID)));
                    ingredients.remove(cursor.getString(cursor.getColumnIndex(INGREDIENT_NAME)));
                }
            }
            //The ingredients that are new to the database are added to a contentvalue and added into the ingredients table
            //The id of the new ingredient entry is retrieved from the content provider and added to the id list
            ListIterator<String> listItr = ingredients.listIterator();
            while (listItr.hasNext()) {
                String current = listItr.next();
                ContentValues IngrValues = new ContentValues();
                IngrValues.put(INGREDIENT_NAME, current);
                System.out.println(current + "is not stored");
                Uri result = getContentResolver().insert(INGREDIENT_URI, IngrValues);
                ingredientids.add(Long.parseLong(result.getLastPathSegment()));

            }
            //This final loop takes all of the ingredient ids found and uses these to create entries for the recipe_ingredients database
            ListIterator<Long> listItr2 = ingredientids.listIterator();
            while (listItr2.hasNext()) {
                ContentValues recipeingrvalues = new ContentValues();
                recipeingrvalues.put(RECIPE_INGREDIENTS_RECIPE_ID, recipeid);
                recipeingrvalues.put(RECIPE_INGREDIENTS_INGREDIENT_ID, listItr2.next().intValue());
                getContentResolver().insert(RECIPE_INGREDIENTS_URI, recipeingrvalues);
            }


            } catch(NullPointerException e){
                Log.d("log", "Null pointer ingredients check");
            }
            finish();


    }
}