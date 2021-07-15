package com.knightshell.agedcare.fragment

import android.R.attr.entries
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.knightshell.agedcare.R
import com.knightshell.agedcare.adapter.GlucoseGraphAdapter
import com.knightshell.agedcare.database.AgedCaredDatabaseHelper
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class GlucoseGraphFragment : Fragment() {

    lateinit var adapter: GlucoseGraphAdapter
    lateinit var lineChart: LineChart

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_glucose_graph, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.history_recycleview)
        recyclerView.layoutManager = LinearLayoutManager(context)


        recyclerView.layoutManager = LinearLayoutManager(context)
        val databaseHandler: AgedCaredDatabaseHelper? = context?.let { AgedCaredDatabaseHelper(it) }
        val listDatabase = databaseHandler!!.queryAllGlucose()
        adapter = GlucoseGraphAdapter(listDatabase)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        lineChart = view.findViewById(R.id.lineChart)

        setLineChartData()
        setupAxes()
        setData()
        return view
    }

    private fun setLineChartData() {

        lineChart.description.isEnabled = true
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)
        lineChart.setScaleEnabled(true)
        lineChart.setDrawGridBackground(false)
        // lineChart.setBackgroundColor(Color.DKGRAY)

    }

    private fun setupAxes(){
        val xAxis = lineChart.xAxis
        xAxis.setTextColor(Color.WHITE)
        xAxis.setDrawGridLines(false)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.setEnabled(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setValueFormatter(object : ValueFormatter() {
            val pattern = "dd MMM"
            private val mFormat = SimpleDateFormat(pattern)
            private val inputFormat = SimpleDateFormat("MM-dd")
            override fun getFormattedValue(value: Float): String {
                val millis = TimeUnit.HOURS.toMillis(value.toLong())
                return mFormat.format(Date(millis));
            }
        })

//        val yAxis: YAxis = lineChart.getAxisLeft()
//        yAxis.setAxisMinimum(40.0f);
//        yAxis.setAxisMaximum(300.0f);

    }

    private fun setData(){

        val databaseHandler: AgedCaredDatabaseHelper= AgedCaredDatabaseHelper(requireContext())
        val Entries: java.util.ArrayList<Entry> = databaseHandler.glucoseXData()

        val set1 = LineDataSet(Entries, "Glucose")
         set1.setAxisDependency(YAxis.AxisDependency.LEFT)
        set1.setColor(ColorTemplate.getHoloBlue())
        set1.setValueTextColor(ColorTemplate.getHoloBlue())
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(false)
        set1.setDrawValues(false)
        set1.setFillAlpha(65)
         set1.setFillColor(ColorTemplate.getHoloBlue())
         set1.setHighLightColor(Color.rgb(244, 117, 117))
        set1.setDrawCircleHole(false)


        val data = LineData(set1)
        lineChart.setNoDataText("No Data Added!")


        lineChart.setData(data)
        lineChart.invalidate()
        lineChart.notifyDataSetChanged()

    }


}
