package com.spartajet.gear.match.api.utility

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @description
 * @create 2017-06-10 下午11:13
 * @email spartajet.guo@gmail.com
 */
fun Double.round(scale: Int): Double {
    return BigDecimal(this).setScale(scale, RoundingMode.HALF_UP).toDouble()
}