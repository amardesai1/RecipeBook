package com.example.psyad9.recipebook;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by pszmdf on 19/11/20
 */

public class RecipeContract{

    //table names have been appended as final strings so that they can be used in various methods and if statements
    public static final String AUTHORITY = "com.example.psyad9.recipebook";
    public static final Uri RECIPE_URI = Uri.parse("content://"+AUTHORITY+"/recipes/");
    public static final Uri INGREDIENT_URI = Uri.parse("content://"+AUTHORITY+"/ingredients/");
    public static final Uri RECIPE_INGREDIENTS_URI = Uri.parse("content://"+AUTHORITY+"/recipe_ingredients/");

    public static final String DATABASE_RECIPE_NAME = "recipes";
    public static final String RECIPE_ID = "_id";
    public static final String RECIPE_NAME = "name";
    public static final String RECIPE_INSTRUCTIONS = "instructions";
    public static final String RECIPE_RATING = "rating";

    public static final String DATABASE_INGREDIENTS_NAME = "ingredients";
    public static final String INGREDIENT_ID = "_id";
    public static final String INGREDIENT_NAME = "ingredientname";

    public static final String DATABASE_RECIPE_INGREDIENTS_NAME = "recipe_ingredients";
    public static final String RECIPE_INGREDIENTS_RECIPE_ID = "recipe_id";
    public static final String RECIPE_INGREDIENTS_INGREDIENT_ID = "ingredient_id";

    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/recipes.data.text";
    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/recipes.data.text";



}