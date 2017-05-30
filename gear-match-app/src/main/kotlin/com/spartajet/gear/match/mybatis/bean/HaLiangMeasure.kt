package com.spartajet.gear.match.mybatis.bean

/**
 * @description
 * @create 2017-05-30 下午10:16
 * @email spartajet.guo@gmail.com
 */
data class HaLiangMeasure(val id: Long, val gearId: Long, val instrumentId: Long, val note: String, val mn: Double, val z: Int, val d: Double, val da: Double, val df: Double, val alpha: Double, val beta: Double, val sigma: Double, val mn2: Double, val z2: Int, val d2: Double, val da2: Double, val dm2: Double, val alpha2: Double, val beta2: Double, val x2: Double) {
}