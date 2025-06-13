package com.example.thenewsapp.db

import androidx.room.TypeConverter
import com.example.thenewsapp.models.Source
import com.google.gson.Gson

class Converters {
    
    @TypeConverter
    fun fromSource(source: Source?): String? {
        return if (source == null) null else Gson().toJson(source)
    }
    
    @TypeConverter
    fun toSource(sourceString: String?): Source? {
        return if (sourceString == null) null else Gson().fromJson(sourceString, Source::class.java)
    }
}