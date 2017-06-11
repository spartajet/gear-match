package com.spartajet.gear.match.base.hl

import com.spartajet.gear.match.base.gie.*
import com.spartajet.gear.match.base.utility.round

/**
 * @description
 * @create 2017-06-04 下午9:01
 * @email spartajet.guo@gmail.com
 */
data class Haliang(val id: Long, val gearid: Long, val instrumentid: Long, val mn: Double, val z: Int, val d: Double, val da: Double, val df: Double, val alpha: Double, val beta: Double, val sigma: Double, val mn2: Double, val z2: Int, val d2: Double, val da2: Double, val dm2: Double, val alpha2: Double, val beta2: Double, val x2: Double, val note: String, val pitchl: Double, val pitchr: Double, val fal: Double, val falc: Int, val ffal: Double, val ffalc: Int, val fhal: Double, val fhalc: Int, val fpl: Double, val fplc: Int, val fpkl: Double, val fpklc: Int, val ffpl: Double, val ffplc: Int, val ful: Double, val fulc: Int, val far: Double, val farc: Int, val ffar: Double, val ffarc: Int, val fhar: Double, val fharc: Int, val fpr: Double, val fprc: Int, val fpkr: Double, val fpkrc: Int, val ffpr: Double, val ffprc: Int, val fur: Double, val furc: Int, val fr: Double, val frc: Int, val rs: Double, val rsc: Int, val fisl: Double, val fislc: Int, val fismaxl: Double, val fismaxlc: Int, val fisr: Double, val fisrc: Int, val fismaxr: Double, val fismaxrc: Int, val giel: Array<Double>, val gier: Array<Double>, val interval: Double) {

    // 这个是单独的3个序列
    lateinit var gieThreadSeries_L: MutableList<GIEThreadSeries>
    lateinit var gieThreadSeries_R: MutableList<GIEThreadSeries>

    //节点序列
    lateinit var pitches_L: MutableList<Pitch>
    lateinit var pitches_R: MutableList<Pitch>

    //这个是 GIE叠加的点对象
    lateinit var gieStackPoints_L: MutableList<GIEStackPoint>
    lateinit var gieStackPoints_R: MutableList<GIEStackPoint>
    //GIE 的整体序列
    val gieSeries_L = GIESeries(gieStackPoints_L.toTypedArray(), pitches_L.toTypedArray())
    val gieSeries_R = GIESeries(gieStackPoints_R.toTypedArray(), pitches_R.toTypedArray())

    /**
     * 计算三个单独的序列
     */
    fun calculateGIEThreadSeries() {
        val points_L_00: MutableList<Point> = mutableListOf()
        val points_L_01: MutableList<Point> = mutableListOf()
        val points_L_02: MutableList<Point> = mutableListOf()

        val points_R_00: MutableList<Point> = mutableListOf()
        val points_R_01: MutableList<Point> = mutableListOf()
        val points_R_02: MutableList<Point> = mutableListOf()

        for (index in 0..3599) {
            points_L_00.add(Point(index, index * interval, giel[index]))
            points_L_01.add(Point(index, index * interval, giel[3600 + index]))
            points_L_02.add(Point(index, index * interval, giel[7200 + index]))
            points_R_00.add(Point(index, index * interval, gier[index]))
            points_R_01.add(Point(index, index * interval, gier[3600 + index]))
            points_R_02.add(Point(index, index * interval, gier[7200 + index]))
        }
        gieThreadSeries_L = mutableListOf()
        gieThreadSeries_R = mutableListOf()
        gieThreadSeries_L.add(GIEThreadSeries(0, points_L_00.toTypedArray()))
        gieThreadSeries_L.add(GIEThreadSeries(1, points_L_01.toTypedArray()))
        gieThreadSeries_L.add(GIEThreadSeries(2, points_L_02.toTypedArray()))
        gieThreadSeries_R.add(GIEThreadSeries(0, points_R_00.toTypedArray()))
        gieThreadSeries_R.add(GIEThreadSeries(1, points_R_01.toTypedArray()))
        gieThreadSeries_R.add(GIEThreadSeries(2, points_R_02.toTypedArray()))
    }

    /**
     * 计算节点
     */
    fun calculatePitches() {
        pitches_L = mutableListOf()
        pitches_R = mutableListOf()
        val firstPitch_L = this.pitchl.round(1)
        val firstPitchIndex_L = (firstPitch_L / interval).toInt()
        (0..z2 - 1).mapTo(pitches_L) {
            var tempIndex: Int = firstPitchIndex_L + (10800 / z2 * it)
            tempIndex = if (tempIndex >= 10800) tempIndex - 10800 else tempIndex
            val x = tempIndex * interval
            val tempXValue = when (x) {
                in 0.0..359.999 -> x
                in 360.0..719.999 -> x - 360.0
                else -> x - 720.0
            }
            val tempYValue = giel[tempIndex]
            val thread = when (tempIndex) {
                in 0..3599 -> 0
                in 3600..7199 -> 1
                else -> 2

            }
            val pitchIndex = when (tempIndex) {
                in 0..3599 -> tempIndex
                in 3600..7199 -> tempIndex - 3600
                else -> tempIndex - 7200
            }
            Pitch(pitchIndex, thread, it, Point(pitchIndex, tempXValue, tempYValue))
        }
        //找到节点后排序
        pitches_L.sortBy { it.point.index }
        //排序后重新标志节点顺序
        pitches_L.forEachIndexed { index, pitch -> pitch.teethId = index }


        val firstPitch_R = this.pitchr.round(1)
        val firstPitchIndex_R = (firstPitch_R / interval).toInt()
        (0..z2 - 1).mapTo(pitches_R) {
            var tempIndex: Int = firstPitchIndex_R + (10800 / z2 * it)
            tempIndex = if (tempIndex >= 10800) tempIndex - 10800 else tempIndex
            val x = tempIndex * interval
            val tempXValue = when (x) {
                in 0.0..359.999 -> x
                in 360.0..719.999 -> x - 360.0
                else -> x - 720.0
            }
            val tempYValue = giel[tempIndex]
            val thread = when (tempIndex) {
                in 0..3599 -> 0
                in 3600..7199 -> 1
                else -> 2

            }
            val pitchIndex = when (tempIndex) {
                in 0..3599 -> tempIndex
                in 3600..7199 -> tempIndex - 3600
                else -> tempIndex - 7200
            }
            Pitch(pitchIndex, thread, it, Point(pitchIndex, tempXValue, tempYValue))
        }
        //找到节点后排序
        pitches_R.sortBy { it.point.index }
        //排序后重新标志节点顺序
        pitches_R.forEachIndexed { index, pitch -> pitch.teethId = index }


    }

    /**
     * 计算 GIE 的叠加序列
     */
    fun calculateGIESeries() {
        (0..3599).mapTo(gieStackPoints_L) {
            GIEStackPoint(it, it * interval, arrayOf(gieThreadSeries_L[0].points[it].yValue, gieThreadSeries_L[1].points[it].yValue, gieThreadSeries_L[2].points[it].yValue))
        }
        (0..3599).mapTo(gieStackPoints_R) {
            GIEStackPoint(it, it * interval, arrayOf(gieThreadSeries_R[0].points[it].yValue, gieThreadSeries_R[1].points[it].yValue, gieThreadSeries_R[2].points[it].yValue))
        }
    }


}