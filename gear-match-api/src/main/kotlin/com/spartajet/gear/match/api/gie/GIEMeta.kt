package com.spartajet.gear.match.api.gie

/**
 * @description
 * @create 2017-06-10 下午10:51
 * @email spartajet.guo@gmail.com
 */

/**
 * GIE图形的点
 *
 * @param index 索引
 * @param x x 坐标的值
 * @param y y 坐标的值
 */
data class Point(val index: Int, val x: Double, val y: Double)

/**
 * 节点对象
 * @param index 360度范围内的 index
 * @param thread 标志节点在哪个 GIE 序列上
 * @param teethId 轮齿序号
 * @param point 节点所在的点对象
 */
data class Pitch(var index: Int, val thread: Int, var teethId: Int, val point: Point)

/**
 * GIE单个齿对象
 * @param teethId 轮齿序号
 * @param pitch 节点
 * @param start 啮合开始点
 * @param end 啮合结束点
 * @param points 测量点集合
 */
data class GIETeeth(val teethId: Int, val pitch: Pitch, val start: Point, val end: Point, val points: Array<Point>)

/**
 * 根据蜗杆头数而划分的 GIE 序列，指的是将测量划分为360度的范围之内
 * @param id 第几个头数
 * @param points 齿上的点
 */
data class GIEThreadSeries(val id: Int, val points: Array<Point>)

data class GIEStackPoint(val index: Int, val x: Double, val values: Array<Double>)

/**
 * 齿轮整体误差
 */
data class GIESeries(val points: Array<GIEStackPoint>, val pitches: Array<Pitch>)

/**
 * GIE 中啮合区域
 * @param haRegion 齿顶区角度
 * @param hfRegion 齿根区角度
 */
data class GIEMeshRegion(val haRegion: Double, val hfRegion: Double)