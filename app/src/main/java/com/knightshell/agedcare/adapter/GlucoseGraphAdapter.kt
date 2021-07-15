package com.knightshell.agedcare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.knightshell.agedcare.R
import com.knightshell.agedcare.model.GlucoseModel


class GlucoseGraphAdapter(val glucosedata: ArrayList<GlucoseModel>): RecyclerView.Adapter<GlucoseGraphAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GlucoseGraphAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_glucose_history, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: GlucoseGraphAdapter.ViewHolder, position: Int) {
        holder.bindItems(glucosedata[position])
    }

    override fun getItemCount(): Int = glucosedata.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(glucose: GlucoseModel) {
            val textViewPressure = itemView.findViewById(R.id.glucose) as TextView
            val textViewPressureDate: TextView = itemView.findViewById(R.id.date) as TextView
            textViewPressure.text = glucose.glucose.toString()
            textViewPressureDate.text = glucose.date

        }

    }
}

