package com.labs.movielab2.room;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ObjectConverter {

    @TypeConverter
    public static String objToString(Object genres) {
        return new Gson().toJson(genres);
    }

    @TypeConverter
    public static Object toString(String parsIt) {
        Type genresType = new TypeToken<Object>() {}.getType();
        return new Gson().fromJson(parsIt,genresType);
    }
}
