package com.spartajet.gear.match.base.hl

/**
 * @description
 * @create 2017-06-04 下午9:33
 * @email spartajet.guo@gmail.com
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
