package com.example.psyad9.recipebook;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import static com.example.psyad9.recipebook.RecipeContract.*;

public class MainActivity extends AppCompatActivity {



    //onCreate class to load the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        sortbyTitle(null);
    }
    //two methods that call the list loading method, each with a different sorting method
    public void sortbyTitle(View view)
    {
        loadlist(null,"name ASC");
    }
    public void sortbyRating(View view)
    {
        loadlist(null,"rating ASC");
    }


    //list loading method which makes a cursor which can access the content provider to get a list of recipes and display in the listview using an adapter in a defined order
    public void loadlist(View view, String s) {
        ListView lv = findViewById(R.id.recipelist);
        String[] projection = new String[] {RECIPE_ID, RECIPE_NAME, RECIPE_INSTRUCTIONS, RECIPE_RATING};
        String colsToDisplay [] = new String[] {RECIPE_NAME};
        int[] colResIds = new int[] {R.id.value1,};
        final Cursor cursor = getContentResolver().query(RECIPE_URI, projection, null, null, s);
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.itemlist, cursor, colsToDisplay, colResIds, 0);
        lv.setAdapter(dataAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> dataAdapter, View myView, int position, long id) {
                cursor.moveToPosition(position);
                String name =   cursor.getString(cursor.getColumnIndex(RECIPE_NAME));
                int idtemp =   cursor.getInt(cursor.getColumnIndex(RECIPE_ID));
                String steps =   cursor.getString(cursor.getColumnIndex(RECIPE_INSTRUCTIONS));
                int rating =   cursor.getInt(cursor.getColumnIndex(RECIPE_RATING));
                onViewRecipe(name, idtemp, steps, rating);
            }
        });
    }

    //method called by the new recipe button to open the newrecipe activity
    public void onNewRecipe(View view)
    {
        Intent intent = new Intent(this, NewRecipe.class);
        startActivity(intent);
    }
    //method called by pressing any list items which opens the recipe info activity page
    //passes the activity attributes of the selected recipe through a bundle to display in the activity
    public void onViewRecipe(String name, int id, String steps, int rating)
    {
        Intent intent = new Intent(this, ViewRecipe.class);
        intent.putExtra(RECIPE_NAME, name) ;
        intent.putExtra(RECIPE_ID, id) ;
        intent.putExtra(RECIPE_INSTRUCTIONS, steps) ;
        intent.putExtra(RECIPE_RATING, rating) ;
        startActivity(intent);
    }
    //method called by pressing the ingredients button to open an activity that lists all unique ingredients
    public void onViewIngredients(View view)
    {
        Intent intent = new Intent(this, ViewIngredients.class);
        startActivity(intent);
    }

    //method that refreshes the recipe last after returning to the activity, so that it reflects any recipes newly created
    @Override
    protected void onResume() {
        super.onResume();
        sortbyTitle(null);
    }
}
