package com.spartajet.gear.match.base.match

import com.spartajet.gear.match.base.gie.GIEThreadSeries
import com.spartajet.gear.match.base.gie.Pitch
import com.spartajet.gear.match.base.hl.Haliang

/**
 * @description
 * @create 2017-06-10 上午9:43
 * @email spartajet.guo@gmail.com
 */
class HaliangMatchStrategy {
    lateinit var drivingGear: Haliang
    lateinit var drivedGear: Haliang
    var drivingLeftFlank: Boolean = true
    var drivedLeftFlank: Boolean = true

    lateinit var gieThreadSeries_Driving_L: MutableList<GIEThreadSeries>
    lateinit var gieThreadSeries_Driving_R: MutableList<GIEThreadSeries>

    lateinit var gieThreadSeries_Drived_L: MutableList<GIEThreadSeries>
    lateinit var gieThreadSeries_Drived_R: MutableList<GIEThreadSeries>

    //    val drivingGie_01: MutableList<Double> = mutableListOf()
//    val drivingGie_02: MutableList<Double> = mutableListOf()
//    val drivingGie_03: MutableList<Double> = mutableListOf()
//
//    val drivedGie_01: MutableList<Double> = mutableListOf()
//    val drivedGie_02: MutableList<Double> = mutableListOf()
//    val drivedGie_03: MutableList<Double> = mutableListOf()
    val drivingGearPitches: MutableList<Pitch> = mutableListOf()
    val drivedGearPitches: MutableList<Pitch> = mutableListOf()
    /**
     * 预处理主动轮曲线，分成三段
     */
    fun preOperateDrivingGear(haliang: Haliang) {
        drivingGear = haliang
        drivingGear.calculateGIEThreadSeries()
        this.gieThreadSeries_Driving_L = drivingGear.gieThreadSeries_L
        this.gieThreadSeries_Driving_R = drivingGear.gieThreadSeries_R
    }

    /**
     * 预处理被动轮曲线，分成三段
     */
    fun preOperateDrivedGear(haliang: Haliang) {
        drivedGear = haliang
        drivedGear.calculateGIEThreadSeries()
        this.gieThreadSeries_Drived_L = drivedGear.gieThreadSeries_L
        this.gieThreadSeries_Drived_R = drivedGear.gieThreadSeries_R
    }

    fun extractPitches() {
        this.drivingGear.calculatePitches()
        this.drivedGear.calculatePitches()
//        val firstDrivingPitchXValue = this.drivingGear.pitchl.round(1)
//        val firstDrivingPitchIndex = (firstDrivingPitchXValue / 0.1).toInt()
//        (0..drivingGear.z2 - 1).mapTo(drivingGearPitches) { Pitch(it,Point(firstDrivingPitchIndex+ it *)) }
    }


}