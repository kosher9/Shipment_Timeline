package com.github.kosher9.shipmenttimeline

import android.content.Context
import java.io.IOException


fun getJsonDataFromAssets(context: Context, fileName: String): String?{
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (e: IOException){
        e.printStackTrace()
        return null
    }
    return jsonString
}