package com.example.psyad9.recipebook;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.psyad9.recipebook.RecipeContract.*;

public class DBHelper extends SQLiteOpenHelper {

    //these strings store SQL statements for the creation of all three tables
    private static final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE recipes " +
            "(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,name VARCHAR(128) " +
            "NOT NULL,instructions VARCHAR(128) NOT NULL,rating INTEGER);";
    private static final String SQL_CREATE_INGREDIENTS_TABLE = "CREATE TABLE ingredients (\n" +
            " _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "ingredientname VARCHAR(128) NOT NULL); ";
    private static final String SQL_CREATE_RECIPE_INGREDIENTS_TABLE = "CREATE TABLE recipe_ingredients (\n" +
            " recipe_id INT NOT NULL,\n" +
            " ingredient_id INT NOT NULL,\n" +
            " CONSTRAINT fk1 FOREIGN KEY (recipe_id) REFERENCES recipes (_id),\n" +
            " CONSTRAINT fk2 FOREIGN KEY (ingredient_id) REFERENCES ingredients (_id),\n" +
            " CONSTRAINT _id PRIMARY KEY (recipe_id, ingredient_id) );";

    public static final int DATABASE_VERSION = 1;


    //Used to create instance of the DBHelper class for the content provider to access
    public DBHelper(@Nullable Context context) {
        super(context, "database", null, DATABASE_VERSION);
    }

    @Override
    //This method creates all tables using SQL on first launch of the app by calling the SQL strings
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RECIPE_TABLE);
        db.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
        db.execSQL(SQL_CREATE_RECIPE_INGREDIENTS_TABLE);
    }


    //This method handles upgrade of the database if the app is modified
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_RECIPE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_RECIPE_INGREDIENTS_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_INGREDIENTS_NAME);
        onCreate(db);
    }
}