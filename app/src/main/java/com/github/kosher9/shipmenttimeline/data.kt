package com.github.kosher9.shipmenttimeline

data class ShipmentHistory(
    val shipmentHistory: List<Shipment>? = null
)

data class Shipment(
    val eventDateTime: String? = null,
    val shipmentIsDelayed: Boolean,
    val eventPosition: Event? = null
)

data class Event(
    val status: String? = null,
    val comments: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null
)