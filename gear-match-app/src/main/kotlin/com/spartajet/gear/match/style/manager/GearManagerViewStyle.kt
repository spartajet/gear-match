package com.spartajet.gear.match.style.manager


import javafx.scene.layout.BorderStrokeStyle
import tornadofx.*

/**
 * @description
 * @create 2017-05-28 下午11:58
 * @email spartajet.guo@gmail.com
 */
class GearManagerViewStyle : Stylesheet() {
    companion object {
        val select_pane_item by cssclass()
        val content_pane by cssclass()
    }

    init {
        select_pane_item {
            padding = box(5.px)
            prefWidth = infinity
            prefHeight = 28.px
        }
        content_pane {
            borderStyle += BorderStrokeStyle.SOLID
            borderColor += box(c("#9c9c9c"))
        }
    }
}