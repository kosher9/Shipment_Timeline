package com.github.kosher9.shipmenttimeline

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ShipmentAdapter(private val mShipments: List<Shipment>) :
    RecyclerView.Adapter<ShipmentAdapter.ViewHolder>() {

    private val format = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US)
    private var dateSaver: String = ""

    init {
        format.timeZone = TimeZone.getTimeZone("America")
    }

    class ViewHolder(listItem: View) : RecyclerView.ViewHolder(listItem) {
        val dateTextView: TextView = itemView.findViewById(R.id.date_txt_view)
        val hourTextView: TextView = itemView.findViewById(R.id.hour_txt_view)
        val deliverStateTextView: TextView = itemView.findViewById(R.id.deliver_state_txt_view)
        val commentTextView: TextView = itemView.findViewById(R.id.comment_txt_view)
        val cityTextView: TextView = itemView.findViewById(R.id.city_txt_view)
        val circleView: CircleImageView = itemView.findViewById(R.id.circle_item)
        val coloredCircleView: View = itemView.findViewById(R.id.circle_scd)
        val lineView: View = itemView.findViewById(R.id.line_fst)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val shipmentView = inflater.inflate(R.layout.shipment_item, parent, false)
        return ViewHolder(shipmentView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shipment: Shipment = mShipments[position]
        val date = holder.dateTextView
        val hour = holder.hourTextView
        val deliverState = holder.deliverStateTextView
        val comment = holder.commentTextView
        val city = holder.cityTextView
        val circle = holder.circleView
        val coloredCircle = holder.coloredCircleView
        val lineView = holder.lineView

        // On considère le bleu comme étant les couleurs par défaut des lignes et cercles
        lineView.setBackgroundResource(R.drawable.rectangle_blue)
        coloredCircle.setBackgroundResource(R.drawable.circle_blue)

        if (shipment.shipmentIsDelayed) {
            lineView.setBackgroundResource(R.drawable.rectangle_orange)
            coloredCircle.setBackgroundResource(R.drawable.circle_orange)
        }

        // Permet de colorer la ligne actuelle en sachant que l'item suivant est delayed
        if (position < mShipments.size - 1) {
            val s = mShipments[position + 1]
            if (s.shipmentIsDelayed) {
                lineView.setBackgroundResource(R.drawable.rectangle_orange)
            }
        }

        // Rend invisible la ligne du dernier item
        if (position == mShipments.size - 1) {
            lineView.visibility = View.GONE
        }

        when {
            shipment.eventPosition?.status?.toLowerCase(Locale.ROOT) == "arrived" -> {
                circle.setImageResource(R.drawable.ic_map_pin)
                coloredCircle.visibility = View.GONE
            }
            shipment.eventPosition?.status?.toLowerCase(Locale.ROOT) == "delivered" -> {
                circle.setImageResource(R.drawable.ic_check)
                coloredCircle.visibility = View.GONE
            }
            else -> {
                circle.visibility = View.GONE
                coloredCircle.visibility = View.VISIBLE
            }
        }

        // Formattage de date et separation date et heure
        try {
            val mDate = format.parse(shipment.eventDateTime!!)
            val dateFormat = SimpleDateFormat("MMM dd hh:mmaa", Locale.US).format(mDate!!)
            date.text = dateFormat.subSequence(0, 6)
            hour.text = dateFormat.subSequence(7, 14).toString().toLowerCase(Locale.ROOT)
        } catch (e: ParseException) {
            Log.i("ShipmentAdapter", "${e.message}")
        }

        if (date.text != dateSaver) {
            dateSaver = date.text.toString()
            date.visibility = View.VISIBLE
        } else if (dateSaver == date.text) {
            date.visibility = View.GONE
        }

        deliverState.text =
            shipment.eventPosition?.status?.toLowerCase(Locale.ROOT)?.capitalize(Locale.ROOT)
        val com = shipment.eventPosition?.comments
        if (com == "null") {
            comment.visibility = View.GONE
        } else {
            comment.text = com?.toLowerCase(Locale.ROOT)?.capitalize(Locale.ROOT)
        }
        if (shipment.eventPosition?.state != null){
            val c = "${
                shipment.eventPosition.city!!.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT)
            }, ${shipment.eventPosition.state}"
            city.text = c
        }
    }

    override fun getItemCount() = mShipments.size
}