package com.github.kosher9.shipmenttimeline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gson = Gson()
        val shipments = mutableListOf<Shipment>()

        val jsonFileString = getJsonDataFromAssets(applicationContext, "data.json")

        val shipmentHistory = gson.fromJson(jsonFileString, ShipmentHistory::class.java)
        shipmentHistory.shipmentHistory?.forEach {
            shipments.add(it)
        }
        Log.i(TAG, "$shipments")

        val rShipment = findViewById<RecyclerView>(R.id.rcShipment)
        val adapter = ShipmentAdapter(shipments)
        rShipment.adapter = adapter
        rShipment.layoutManager = LinearLayoutManager(this)

    }

    companion object{
        val TAG = MainActivity::class.java.simpleName
    }
}