package com.knightshell.agedcare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.knightshell.agedcare.R
import com.knightshell.agedcare.model.WeightModel


class WeightGraphAdapter(val weightdata: ArrayList<WeightModel>): RecyclerView.Adapter<WeightGraphAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightGraphAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_weight_history, parent, false)
        return ViewHolder(view)
    }



    override fun onBindViewHolder(holder: WeightGraphAdapter.ViewHolder, position: Int) {
        holder.bindItems(weightdata[position])
    }

    override fun getItemCount(): Int = weightdata.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(weight: WeightModel) {
            val textViewWeight = itemView.findViewById(R.id.weight) as TextView
            val textViewWeightDate: TextView = itemView.findViewById(R.id.date) as TextView
            textViewWeight.text = weight.weight.toString()
            textViewWeightDate.text = weight.date

        }

    }
}
