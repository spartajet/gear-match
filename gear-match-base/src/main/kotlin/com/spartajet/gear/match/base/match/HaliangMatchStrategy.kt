package com.spartajet.gear.match.base.match

import com.spartajet.gear.match.base.gie.GIEMeshRegion
import com.spartajet.gear.match.base.gie.GIESeries
import com.spartajet.gear.match.base.gie.GIEThreadSeries
import com.spartajet.gear.match.base.gie.Pitch
import com.spartajet.gear.match.base.hl.*

/**
 * @description 配对策略类
 * @create 2017-06-10 上午9:43
 * @email spartajet.guo@gmail.com
 */
class HaliangMatchStrategy {
    /**
     * 主动轮
     */
    lateinit var drivingGear: Haliang
    /**
     * 被动轮
     */
    lateinit var drivedGear: Haliang
    /**
     * 主动轮齿面选择，默认左齿面
     */
    var drivingLeftFlank: Boolean = true
    /**
     * 被动轮选择，默认左齿面
     */
    var drivedLeftFlank: Boolean = true
    /**
     * 主动轮左齿面 GIE 分段序列
     */
    lateinit var gieThreadSeries_Driving_L: Array<GIEThreadSeries>
    /**
     * 主动轮右齿面 GIE 分段序列
     */
    lateinit var gieThreadSeries_Driving_R: Array<GIEThreadSeries>
    /**
     * 被动轮左齿面 GIE 分段序列
     */
    lateinit var gieThreadSeries_Drived_L: Array<GIEThreadSeries>
    /**
     * 被动轮右齿面 GIE 分段序列
     */
    lateinit var gieThreadSeries_Drived_R: Array<GIEThreadSeries>
    /**
     * 主动轮左齿面节点选择
     */
    lateinit var pitches_Driving_L: Array<Pitch>
    /**
     * 主动轮右齿面节点选择
     */
    lateinit var pitches_Driving_R: Array<Pitch>
    /**
     * 被动轮左齿面节点选择
     */
    lateinit var pitches_Drived_L: Array<Pitch>
    /**
     * 被动轮右齿面节点选择
     */
    lateinit var pitches_Drived_R: Array<Pitch>
    /**
     * 主动轮的啮合范围
     */
    lateinit var meshRegion_Driving: GIEMeshRegion
    /**
     * 被动轮的啮合范围
     */
    lateinit var meshRegion_Drived: GIEMeshRegion
    /**
     * 主动轮左齿面原始序列
     */
    lateinit var GIESeries_Original_Driving_L: GIESeries
    /**
     * 主动轮右齿面原始序列
     */
    lateinit var GIESeries_Original_Driving_R: GIESeries
    /**
     * 被动轮左齿面原始序列
     */
    lateinit var GIESeries_Original_Drived_L: GIESeries
    /**
     * 被动轮右齿面原始序列
     */
    lateinit var GIESeries_Original_Drived_R: GIESeries

    /**
     * 主动轮左齿面啮合序列
     */
    lateinit var GIESeries_Mesh_Driving_L: GIESeries
    /**
     * 主动轮右齿面啮合序列
     */
    lateinit var GIESeries_Mesh_Driving_R: GIESeries
    /**
     * 被动轮左齿面啮合序列
     */
    lateinit var GIESeries_Mesh_Drived_L: GIESeries
    /**
     * 被动轮右齿面啮合序列
     */
    lateinit var GIESeries_Mesh_Drived_R: GIESeries

    /**
     * 主动轮左齿面配对序列
     */
    lateinit var GIESeries_Match_Driving_L: GIESeries
    /**
     * 主动轮右齿面配对序列
     */
    lateinit var GIESeries_Match_Driving_R: GIESeries
    /**
     * 被动轮左齿面配对序列
     */
    lateinit var GIESeries_Match_Drived_L: GIESeries
    /**
     * 被动轮右齿面配对序列
     */
    lateinit var GIESeries_Match_Drived_R: GIESeries


    /**
     * 预处理主动轮曲线，分成三段
     */
    fun preOperateDrivingGear(haliang: Haliang) {
        drivingGear = haliang
        this.gieThreadSeries_Driving_L = calculateGIEThreadSeries(drivingGear.interval, drivingGear.giel)
        this.gieThreadSeries_Driving_R = calculateGIEThreadSeries(drivingGear.interval, drivingGear.gier)
    }

    /**
     * 预处理被动轮曲线，分成三段
     */
    fun preOperateDrivedGear(haliang: Haliang) {
        drivedGear = haliang
        this.gieThreadSeries_Drived_L = calculateGIEThreadSeries(drivedGear.interval, drivedGear.giel)
        this.gieThreadSeries_Drived_R = calculateGIEThreadSeries(drivedGear.interval, drivedGear.gier)

    }

    /**
     * 提取节点
     */
    fun extractPitches() {
        this.pitches_Driving_L = calculatePitches(drivingGear.pitchl, drivingGear.interval, drivingGear.z2, drivingGear.giel)
        this.pitches_Driving_R = calculatePitches(drivingGear.pitchr, drivingGear.interval, drivingGear.z2, drivingGear.gier)
        this.pitches_Drived_L = calculatePitches(drivedGear.pitchl, drivedGear.interval, drivedGear.z2, drivedGear.giel)
        this.pitches_Drived_R = calculatePitches(drivedGear.pitchr, drivedGear.interval, drivedGear.z2, drivedGear.gier)
    }


    /**
     * 提取啮合区域
     */
    fun extractMeshRegion() {
        this.GIESeries_Original_Driving_L = GIESeries(calculateGIEStackPoints(drivingGear.interval, gieThreadSeries_Driving_L), pitches_Driving_L)
        this.GIESeries_Original_Driving_R = GIESeries(calculateGIEStackPoints(drivingGear.interval, gieThreadSeries_Driving_R), pitches_Driving_R)
        this.GIESeries_Original_Drived_L = GIESeries(calculateGIEStackPoints(drivedGear.interval, gieThreadSeries_Drived_L), pitches_Drived_L)
        this.GIESeries_Original_Drived_R = GIESeries(calculateGIEStackPoints(drivedGear.interval, gieThreadSeries_Drived_R), pitches_Drived_R)
        this.meshRegion_Driving = calculateMeshRegion(drivingGear, 67.5)
        this.meshRegion_Drived = calculateMeshRegion(drivedGear, 67.5)
        this.GIESeries_Mesh_Driving_L = operateMeshRegion(pitches_Driving_L, meshRegion_Driving, drivingGear.interval, GIESeries_Original_Driving_L)
        this.GIESeries_Mesh_Driving_R = operateMeshRegion(pitches_Driving_R, meshRegion_Driving, drivingGear.interval, GIESeries_Original_Driving_R)
        this.GIESeries_Mesh_Drived_L = operateMeshRegion(pitches_Drived_L, meshRegion_Drived, drivedGear.interval, GIESeries_Original_Drived_L)
        this.GIESeries_Mesh_Drived_R = operateMeshRegion(pitches_Drived_R, meshRegion_Drived, drivedGear.interval, GIESeries_Original_Drived_R)
    }

    /**
     * 合成 GPIE 曲线
     */
    fun combineGPIE() {
        this.GIESeries_Match_Driving_L = this.GIESeries_Mesh_Driving_L.calculateMatchGIESeries(drivingGear.z2, drivedGear.z2, drivingGear.interval)
        this.GIESeries_Match_Drived_L = this.GIESeries_Mesh_Drived_L.calculateMatchGIESeries(drivedGear.z2, drivingGear.z2, drivedGear.interval)

    }

}