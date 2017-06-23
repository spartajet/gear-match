package com.spartajet.gear.match.base.hl

import com.spartajet.gear.match.base.gie.*
import com.spartajet.gear.match.base.utility.round

/**
 * @description
 * @create 2017-06-04 下午9:33
 * @email spartajet.guo@gmail.com
 */
/**
 * 重采样
 * @param thread 系列，三头蜗杆
 * @param interval 采样间隔
 * @param xArray x 坐标的值数组
 * @param yArray y 坐标的值数组
 */
fun resample(thread: Int, interval: Double, xArray: Array<Double>, yArray: Array<Double>): Array<Double> {
    val size: Int = ((360 * thread) / interval).toInt()
    var cursor: Int = 0
    val result: Array<Double> = Array(size) { i ->
        val x = i * interval
        var y: Double = 0.0
        if (cursor == 0 && x <= xArray[cursor]) {
            y = yArray[0]
        } else if (cursor == xArray.size - 1 && x > xArray[cursor]) {
            y = yArray[cursor]
        } else {
            while (x > xArray[cursor]) {
                if (cursor == xArray.size - 1) break
                cursor++
            }
            if (cursor == xArray.size - 1) {
                y = yArray[cursor]
            } else {
                val x1 = xArray[cursor - 1]
                val x2 = xArray[cursor]
                val y1 = yArray[cursor - 1]
                val y2 = yArray[cursor]
                y = y1 + ((x - x1) / (x2 - x1)) * (y2 - y1)
            }

        }
        return@Array y
    }
    val average: Double = result.average()
    result.forEachIndexed { index, d -> result[index] = d - average }
    return result
}

/**
 * 计算第一个齿的偏移值，曲线的开始位置是最低点
 * @param array 数组
 * @param z teeth number
 */
fun offset(array: Array<Double>, z: Int): Int {
    val pointPerTeeth = array.size / z
    val firstTeethPoints: Array<Double> = array.copyOfRange(0, pointPerTeeth)
    val offset = firstTeethPoints.indexOf(firstTeethPoints.min())
    return offset
}

/**
 * 初始化采样点，将曲线开始点转换为齿的开始点
 */
fun initialize(array: Array<Double>, offset: Int): Array<Double> {
    val beforeArray = array.take(offset)
    val afterArray = array.takeLast(array.size - offset)
    val result = (afterArray + beforeArray).toTypedArray()
    return result
}

/**
 * 计算三个单独的序列
 */
fun calculateGIEThreadSeries(interval: Double, array: Array<Double>): Array<GIEThreadSeries> {
    val points_L_00: MutableList<Point> = mutableListOf()
    val points_L_01: MutableList<Point> = mutableListOf()
    val points_L_02: MutableList<Point> = mutableListOf()

    for (index in 0..3599) {
        points_L_00.add(Point(index, index * interval, array[index]))
        points_L_01.add(Point(index, index * interval, array[3600 + index]))
        points_L_02.add(Point(index, index * interval, array[7200 + index]))
    }
    return arrayOf(GIEThreadSeries(0, points_L_00.toTypedArray()), GIEThreadSeries(1, points_L_01.toTypedArray()), GIEThreadSeries(2, points_L_02.toTypedArray()))
}


/**
 * 计算 GIE 的叠加序列
 */
fun calculateGIEStackPoints(interval: Double, gieThreadSeries: Array<GIEThreadSeries>): Array<GIEStackPoint> {
    val gieStackPoints: MutableList<GIEStackPoint> = mutableListOf()
    (0..3599).mapTo(gieStackPoints) {
        GIEStackPoint(it, it * interval, arrayOf(gieThreadSeries[0].points[it].y, gieThreadSeries[1].points[it].y, gieThreadSeries[2].points[it].y))
    }
    return gieStackPoints.toTypedArray()
}


/**
 * 计算节点
 */
fun calculatePitches(firstPitch: Double, interval: Double, z: Int, array: Array<Double>): Array<Pitch> {
    val pitches: MutableList<Pitch> = mutableListOf()
    val firstPitch_L = firstPitch.round(1)
    val firstPitchIndex_L = (firstPitch_L / interval).toInt()
    (0..z - 1).mapTo(pitches) {
        var tempIndex: Int = firstPitchIndex_L + (10800 / z * it)
        tempIndex = if (tempIndex >= 10800) tempIndex - 10800 else tempIndex
        val x = tempIndex * interval
        val tempXValue = when (x) {
            in 0.0..359.999 -> x
            in 360.0..719.999 -> x - 360.0
            else -> x - 720.0
        }
        val tempYValue = array[tempIndex]
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
    pitches.sortBy { it.point.index }
    //排序后重新标志节点顺序
    pitches.forEachIndexed { index, pitch -> pitch.teethId = index }
    return pitches.toTypedArray()
}

/**
 * 计算 GIE 中啮合部分
 * @param haliang haliang 的测量数据
 * @param a 中心距
 */
fun calculateMeshRegion(haliang: Haliang, a: Double): GIEMeshRegion {
    val mk2 = haliang.mn - (a - haliang.d / 2 - haliang.d2 / 2)
    val om = haliang.d2 / 2 - mk2
    val mk3 = mk2 / Math.tan(haliang.alpha2 / 180 * Math.PI)
    val phi5 = Math.atan(mk3 / om)
    val rx = mk3 / Math.sin(phi5)
    val alphax = Math.acos(haliang.d2 * Math.cos(haliang.alpha2 * Math.PI / 180.0) / 2 / rx)
    val theta2 = Math.tan(alphax) - alphax
    val alpha1 = Math.acos(haliang.d2 * Math.cos(haliang.alpha2 * Math.PI / 180.0) / haliang.d2)
    val theta1 = Math.tan(alpha1) - alpha1
    var phi2 = phi5 + theta1 - theta2
    val alpha0 = Math.acos(haliang.d2 * Math.cos(haliang.alpha2 * Math.PI / 180.0) / haliang.da2)
    val theta0 = Math.tan(alpha0) - alpha0
    val phi6 = Math.acos(haliang.d2 * Math.cos(haliang.alpha2 * Math.PI / 180) / haliang.da2)
    val phi3 = Math.acos(haliang.d2 * Math.cos(haliang.alpha2 * Math.PI / 180) / 2 / rx)
    var phi1 = theta0 + phi6 - phi5 - phi3 - theta1
    phi1 *= (180 / Math.PI) //齿顶区
    phi2 *= (180 / Math.PI) //齿根区
    return GIEMeshRegion(phi1, phi2)
}


/**
 * 操作 GIE 中的啮合部分，将啮合部分复制到新的 GIESeries 中
 * @param pitches 节点对象
 * @param gieMeshRegion 啮合范围对象
 * @param interval 重采样间隔
 * @param gieSeries 初始的测量数据序列
 *
 * @return 啮合的 GIESeries 对象
 */
fun operateMeshRegion(pitches: Array<Pitch>, gieMeshRegion: GIEMeshRegion, interval: Double, gieSeries: GIESeries): GIESeries {
    val meshStackPoints: MutableList<GIEStackPoint> = mutableListOf()
    (0..3599).mapTo(meshStackPoints) {
        GIEStackPoint(it, it * interval, arrayOf(-1000.0, -1000.0, -1000.0))
    }
    val meshGIESeries: GIESeries = GIESeries(meshStackPoints.toTypedArray(), pitches)
    pitches.forEachIndexed { index, pitch ->
        val pitchThread = pitch.thread
        val pitchIndex = pitch.index
        val haLength = (gieMeshRegion.haRegion.round(1) / interval).toInt()
        val hfLength = (gieMeshRegion.hfRegion.round(1) / interval).toInt()
        val startThread = if (pitchIndex - hfLength >= 0) pitchThread else lastThread(pitchThread)
        val startIndex = if (startThread == pitchThread) pitchIndex - hfLength else 3599 - (hfLength - pitchIndex)
        val endThread = if (pitchIndex + haLength <= 3599) pitchThread else nextThread(pitchThread)
        val endIndex = if (endThread == pitchThread) pitchIndex + haLength else haLength - (3599 - pitchIndex)
        //开始处理齿根区
        if (startThread != pitchThread) {
            (startIndex..3599).forEach {
                meshGIESeries.points[it].values[startThread] = gieSeries.points[it].values[startThread]
            }
            (0..pitchIndex).forEach {
                meshGIESeries.points[it].values[pitchThread] = gieSeries.points[it].values[pitchThread]
            }
        } else {
            (startIndex..pitchIndex).forEach {
                meshGIESeries.points[it].values[pitchThread] = gieSeries.points[it].values[pitchThread]
            }
        }
        //开始处理齿顶区
        if (endThread != pitchThread) {
            (0..endIndex).forEach {
                meshGIESeries.points[it].values[endThread] = gieSeries.points[it].values[endThread]
            }
            ((pitchIndex - 1)..3599).forEach {
                meshGIESeries.points[it].values[pitchThread] = gieSeries.points[it].values[pitchThread]
            }
        } else {
            ((pitchIndex - 1)..endIndex).forEach {
                meshGIESeries.points[it].values[pitchThread] = gieSeries.points[it].values[pitchThread]
            }
        }
    }
    return meshGIESeries
}

/**
 * 计算参与啮合的 GIE 序列，最小公倍数扩充
 * @param z 自身的齿数
 * @param z2 另一个齿轮的齿数
 * @param interval 采样间隔
 */
fun GIESeries.calculateMatchGIESeries(z: Int, z2: Int, interval: Double): GIESeries {
    val result = this.initialGIESeriesByPitch(interval) * ((z cm z2) / z)
    return result
}

/**
 * 根据节点位置来初始化 GIE 序列，将第一个节点放在开始位置
 */
fun GIESeries.initialGIESeriesByPitch(interval: Double): GIESeries {
    val offset = this.pitches[0].index
    val stackPoints: MutableList<GIEStackPoint> = mutableListOf()
    val pitches: MutableList<Pitch> = mutableListOf()
    (0..3599).mapTo(stackPoints) {
        var y0 = 0.0
        var y1 = 0.0
        var y2 = 0.0
        if (it + offset <= 3599) {
            y0 = this.points[it + offset].values[0]
            y1 = this.points[it + offset].values[1]
            y2 = this.points[it + offset].values[2]
        } else {
            y0 = this.points[it + offset - 3599].values[2]
            y1 = this.points[it + offset - 3599].values[0]
            y2 = this.points[it + offset - 3599].values[1]
        }
        GIEStackPoint(it, it * interval, arrayOf(y0, y1, y2))
    }
    this.pitches.forEach {
        val index = it.index - offset
        pitches.add(Pitch(index, it.thread, it.teethId, Point(index, it.point.x - offset * interval, it.point.y)))
    }
    return GIESeries(stackPoints.toTypedArray(), pitches.toTypedArray())
}

/**
 * 以节点为中心，交换齿根齿顶
 * @param radius 交换的半径，一般是 齿根方向的啮合部分，指的是点数
 */
fun GIESeries.reverse(radius: Int): GIESeries {
    val stackPoints: MutableList<GIEStackPoint> = mutableListOf()
    val pitches: MutableList<Pitch> = mutableListOf()
    this.pitches.forEach {
        pitches.add(Pitch(it.index, it.thread, it.teethId, Point(it.point.index, it.point.x, it.point.y)))
    }
    this.points.forEach {
        stackPoints.add(GIEStackPoint(it.index, it.x, arrayOf(it.values[0], it.values[1], it.values[2])))
    }
    pitches.forEach {

    }

    return GIESeries(stackPoints.toTypedArray(), pitches.toTypedArray())


}

/**
 * GIESeries 的相加，叠加
 */
operator fun GIESeries.plus(gieSeries: GIESeries): GIESeries {
    val stackPoints: MutableList<GIEStackPoint> = mutableListOf()
    val pitches: MutableList<Pitch> = mutableListOf()
    this.points.forEachIndexed { i, (index, x, values) ->
        stackPoints.add(GIEStackPoint(index, x, arrayOf(values[0] + gieSeries.points[i].values[0], values[1] + gieSeries.points[i].values[1], values[2] + gieSeries.points[i].values[2])))
    }
    this.pitches.forEachIndexed { i, (index, thread, teethId, point) ->
        pitches.add(Pitch(index, thread, teethId, Point(index, point.x, point.y + gieSeries.pitches[i].point.y)))
    }
    return GIESeries(stackPoints.toTypedArray(), pitches.toTypedArray())
}

/**
 * GIESeries 的扩充，最小公倍数
 */
operator fun GIESeries.times(timer: Int): GIESeries {
    val stackPoints: MutableList<GIEStackPoint> = mutableListOf()
    val pitches: MutableList<Pitch> = mutableListOf()
    for (i in 0..timer) {
        val indexOffset = i * 3600
        val xOffset = i * 360.0
        val teethOffset = i * this.pitches.size
        this.pitches.forEach {
            pitches.add(Pitch(it.index + indexOffset, it.thread, it.teethId + teethOffset, Point(it.index + indexOffset, it.point.x + xOffset, it.point.y)))
        }
        this.points.forEach {
            stackPoints.add(GIEStackPoint(it.index + indexOffset, it.x + xOffset, it.values.clone()))
        }
    }
    return GIESeries(stackPoints.toTypedArray(), pitches.toTypedArray())
}

/**
 * Common Divider 最小公约数
 */
infix fun Int.cd(int: Int): Int {
    var a = this
    var b = int
    if (this < int) {
        a += b
        b = a - b
        a -= b
    }
    return if (a % b == 0) b else a % b cd b
}

/**
 * Common Multiple 最小公倍数
 * @param int 另一个数
 */
infix fun Int.cm(int: Int) = this * int / (this cd int)


/**
 * 上一个序列
 */
fun lastThread(index: Int) = when (index) {
    0 -> 2
    1 -> 0
    2 -> 1
    else -> 0
}

/**
 * 下一个序列
 */
fun nextThread(index: Int) = when (index) {
    0 -> 1
    1 -> 2
    2 -> 0
    else -> 0
}
