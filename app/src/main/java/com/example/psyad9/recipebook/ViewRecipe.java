package com.example.psyad9.recipebook;

import android.content.ContentValues;
import android.database.Cursor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.example.psyad9.recipebook.RecipeContract.*;

public class ViewRecipe extends AppCompatActivity {

    private RadioGroup radiogroup;
    private String ingredientstring;
    int recipeid;

    //onCreate method which loads the activity, takes all data from the bundle and loads all the data in the respective fieldssplit a comma seperate string into two inetegers java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.viewrecipe);

        //Getting recipe data from bundle
        String name = getIntent().getStringExtra(RECIPE_NAME);
        recipeid = getIntent().getIntExtra(RECIPE_ID,0);
        String steps = getIntent().getStringExtra(RECIPE_INSTRUCTIONS);
        int rating = getIntent().getIntExtra(RECIPE_RATING,0);

        //getting instances of the textviews to display the information
        TextView nameview = findViewById(R.id.viewtitletext);
        TextView stepsview = findViewById(R.id.viewinstructionstext);
        TextView ingrview = findViewById(R.id.viewingredientstext);
        radiogroup = findViewById(R.id.ratinggroup);

        //setting each textview instance to the bundle data
        nameview.setText(name);

        stepsview.setText(steps);

        //sets the radiogroup to the rating value using a switch case
        switch(rating) {
            case 1:
                radiogroup.check(R.id.radioOne);
                break;
            case 2:
                radiogroup.check(R.id.radioTwo);
                break;
            case 3:
                radiogroup.check(R.id.radioThree);
                break;
            case 4:
                radiogroup.check(R.id.radioFour);
                break;
            case 5:
                radiogroup.check(R.id.radioFive);
                break;
            default:
                radiogroup.clearCheck();
        }


        try {
            //Creates a cursor which is used to make a list of all ingredient ids that have a record in the singredient recipe table with the recipe id passed to the activity
            String[] idprojection = new String[]{RECIPE_INGREDIENTS_RECIPE_ID,RECIPE_INGREDIENTS_INGREDIENT_ID};
            final Cursor idcursor = getContentResolver().query(RECIPE_INGREDIENTS_URI, idprojection, null, null, null);
            ArrayList<Integer> ingredientids = new ArrayList<>();
            while (idcursor.moveToNext()) {
                if (recipeid==idcursor.getInt(idcursor.getColumnIndex(RECIPE_INGREDIENTS_RECIPE_ID))) {
                    ingredientids.add(idcursor.getInt(idcursor.getColumnIndex(RECIPE_INGREDIENTS_INGREDIENT_ID)));
                }
            }
            //Uses a cursor to retrieve all ingredient strings from the ingredient table using the list of ingredient ids from the last while loop
            String[] nameprojection = new String[]{INGREDIENT_ID,INGREDIENT_NAME};
            final Cursor namecursor = getContentResolver().query(INGREDIENT_URI, nameprojection, null, null, null);
            ingredientstring ="";
            while(namecursor.moveToNext()) {
                if(ingredientids.contains(namecursor.getInt(namecursor.getColumnIndex(INGREDIENT_ID)))){
                    ingredientstring+=(namecursor.getString(namecursor.getColumnIndex(INGREDIENT_NAME)))+" ";
                }
            }
        } catch(NullPointerException e){
            Log.d("log", "Null pointer ingredients check");
        }
        //sets ingredient textview to the retrieved ingredient strings
        ingrview.setText(ingredientstring);
    }

    public void onClickDone(View view)
    {
        //when the activity is exited by pressing the done button, this method retrieves the rating from the radiobutton
        //and uses the content provider to set the value of the recipes rating in case this has been modified
        int rating = Integer.parseInt(((RadioButton) findViewById(radiogroup.getCheckedRadioButtonId())).getText().toString());
        ContentValues ratingvalue = new ContentValues();
        ratingvalue.put(RECIPE_RATING, rating);
        getContentResolver().update(RECIPE_URI,ratingvalue,RECIPE_ID+"="+recipeid,null);
        finish();
    }
}
