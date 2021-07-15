package com.knightshell.agedcare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.knightshell.agedcare.R
import com.knightshell.agedcare.model.PressureModel


class PressureGraphAdapter(val glucosedata: List<PressureModel>): RecyclerView.Adapter<PressureGraphAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PressureGraphAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_pressure_history, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: PressureGraphAdapter.ViewHolder, position: Int) {
        holder.bindItems(glucosedata[position])
    }

    override fun getItemCount(): Int = glucosedata.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(pressure: PressureModel) {
            val diasystolicTextView = itemView.findViewById(R.id.diastolic) as TextView
            val systolicTextView: TextView = itemView.findViewById(R.id.systolic) as TextView
            val textViewDate: TextView = itemView.findViewById(R.id.date) as TextView
            diasystolicTextView.text = pressure.diastolic.toString()
            systolicTextView.text = pressure.systolic.toString()
            textViewDate.text = pressure.date

        }

    }
}
