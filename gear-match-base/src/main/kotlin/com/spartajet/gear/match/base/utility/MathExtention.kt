package com.spartajet.gear.match.base.utility

/**
 * @description
 * @create 2017-06-12 下午8:20
 * @email spartajet.guo@gmail.com
 */
fun Math.sind(degree: Double) = Math.sin(degree * Math.PI / 180.0)

fun Math.cosd(degree: Double) = Math.cos(degree * Math.PI / 180.0)

fun Math.tand(degree: Double) = Math.tan(degree * Math.PI / 180.0)

fun Math.asind(value: Double) = Math.asin(value) * 180 / Math.PI

fun Math.acosd(value: Double) = Math.acos(value) * 180 / Math.PI

fun Math.atand(value: Double) = Math.atan(value) * 180 / Math.PI

