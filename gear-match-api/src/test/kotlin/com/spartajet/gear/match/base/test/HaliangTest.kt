package com.spartajet.gear.match.base.test

import com.spartajet.gear.match.api.hl.initialize

/**
 * @description
 * @create 2017-06-09 上午11:57
 * @email spartajet.guo@gmail.com
 */

fun main(args: Array<String>) {
    val points = arrayOf(3.0, 1.0, 2.0, 0.4, 3.5, 2.1)
    val result = initialize(points, 2)
    result.forEach { print("$it ") }
}
