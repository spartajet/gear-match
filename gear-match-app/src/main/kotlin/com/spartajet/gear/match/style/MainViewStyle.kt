package com.spartajet.gear.match.style

import javafx.scene.control.ContentDisplay
import javafx.scene.layout.BorderStrokeStyle
import javafx.scene.paint.Color
import tornadofx.*

/**
 * @description
 * @create 2017-05-28 下午8:03
 * @email spartajet.guo@gmail.com
 */
class MainViewStyle : Stylesheet() {
    companion object {
        val tab_flow_pane by cssclass()
        val fun_menu_button by cssclass()
        val function_menu_tabpane by cssclass()
    }

    init {
        tab_flow_pane {
            hgap = 5.px
            vgap = 5.px
            padding = box(5.px)
        }
        fun_menu_button {
            contentDisplay = ContentDisplay.TOP
            prefHeight = 80.px
            prefWidth = 70.px

        }
        function_menu_tabpane {
            borderStyle += (BorderStrokeStyle.SOLID)
            borderColor += box(Color.GREY)
            prefWidth = 115.px
        }
    }
}
