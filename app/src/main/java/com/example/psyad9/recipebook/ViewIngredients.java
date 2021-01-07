package com.example.psyad9.recipebook;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import static com.example.psyad9.recipebook.RecipeContract.*;

public class ViewIngredients extends AppCompatActivity {
    String log = "log";

    @Override
    //onCreate method uses a cursor to retrieve all ingredient names from the unique ingredient tables from the content provider
    //These are all put in a list and displayed in a listview using an an adaptor
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewingredients);
        ListView lv = findViewById(R.id.ingredientlist);
        Log.d(log, "setRecipeList()");
        String[] projection = new String[] {INGREDIENT_ID, INGREDIENT_NAME};
        String colsToDisplay [] = new String[] {INGREDIENT_NAME};
        int[] colResIds = new int[] {R.id.value1};
        final Cursor cursor = getContentResolver().query(INGREDIENT_URI, projection, null, null, null);
        SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.itemlist, cursor, colsToDisplay, colResIds, 0);
        lv.setAdapter(dataAdapter);
    }
}
