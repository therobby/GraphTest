package com.robby.graphtest

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private val colors = ColorTemplate.VORDIPLOM_COLORS
    private val handler = Handler()
    private var dataRefreshThread = Runnable{}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //setTitle("DynamicalAddingActivity")

        //graph.setOnChartValueSelectedListener(this)
        graph.setDrawGridBackground(false)
        graph.description.isEnabled = false
        graph.animateXY(1000,1000)
        graph.setNoDataText("Loading data, please wait!")


//        chart.getXAxis().setDrawLabels(false);
//        chart.getXAxis().setDrawGridLines(false);

        graph.invalidate()
        du()
    }

    private fun du() {
        dataRefreshThread = Runnable {
            thread {

                addEntry()
                //if(graph.data.entryCount > 20)
                //    removeLastEntry()

                runOnUiThread {
                    handler.postDelayed(dataRefreshThread, 1000)
                }
            }
        }
        handler.postDelayed(dataRefreshThread, 1000)
    }
    private fun addEntry() {

        var data = graph.data

        if (data == null) {
            data = LineData()
            graph.data = data
        }

        var set: ILineDataSet? = data.getDataSetByIndex(0)
        // set.addEntry(...); // can be called as well

        if (set == null) {
            set = createSet()
            data.addDataSet(set)
        }

        // choose a random dataSet
        val randomDataSetIndex = (Math.random() * data.dataSetCount).toInt()
        val randomSet = data.getDataSetByIndex(randomDataSetIndex)
        val value = (Math.random() * 50).toFloat() + 50f * (randomDataSetIndex + 1)

        data.addEntry(Entry(randomSet.entryCount.toFloat(), value), randomDataSetIndex)
        data.notifyDataChanged()

        // let the chart know it's data has changed
        graph.notifyDataSetChanged()

        graph.setVisibleXRangeMaximum(6f)
        //chart.setVisibleYRangeMaximum(15, AxisDependency.LEFT);
        //
        //            // this automatically refreshes the chart (calls invalidate())
        graph.moveViewTo((data.entryCount - 7).toFloat(), 50f, AxisDependency.LEFT)
    }

    private fun removeLastEntry() {

        val data = graph.data

        if (data != null) {

            val set = data.getDataSetByIndex(0)

            if (set != null) {

                val e = set.getEntryForXValue((set.entryCount - (set.entryCount - 1)).toFloat(), java.lang.Float.NaN)

                data.removeEntry(e, 0)
                // or remove by index
                // mData.removeEntryByXValue(xIndex, dataSetIndex);
                data.notifyDataChanged()
                graph.notifyDataSetChanged()
                graph.invalidate()
            }
        }
    }


    private fun createSet(): LineDataSet {

        val set = LineDataSet(null, "DataSet 1")
        set.lineWidth = 2.5f
        set.circleRadius = 4.5f
        set.color = Color.rgb(240, 99, 99)
        set.setCircleColor(Color.rgb(240, 99, 99))
        set.highLightColor = Color.rgb(190, 190, 190)
        set.axisDependency = AxisDependency.LEFT
        set.valueTextSize = 10f

        return set
    }
}

