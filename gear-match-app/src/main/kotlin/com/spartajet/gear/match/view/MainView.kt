package com.spartajet.gear.match.view

import com.spartajet.gear.match.style.MainViewStyle.Companion.fun_menu_button
import com.spartajet.gear.match.style.MainViewStyle.Companion.function_menu_tabpane
import com.spartajet.gear.match.style.MainViewStyle.Companion.tab_flow_pane
import javafx.geometry.Side
import javafx.scene.control.TabPane
import javafx.scene.paint.Color
import tornadofx.*

/**
 * @description
 * @create 2017-05-28 下午7:14
 * @email spartajet.guo@gmail.com
 */
class MainView : View("齿轮配对系统") {
    override val root = borderpane {
        top {
            menubar {
                menu("File") {
                    item("New")
                    separator()
                }
                menu("关于") {
                    item("版权信息")
                }

            }
        }
        left {
            tabpane {
                style {
                    prefWidth = 190.px
                }
                tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
                addClass(function_menu_tabpane)
                side = Side.LEFT
                tab("系统管理") {
                    flowpane {
                        button("齿轮管理", graphic = imageview("/img/function/manager/gear_manage.png")) {
                            addClass(fun_menu_button)
                        }
                        addClass(tab_flow_pane)
                        button("哈量测量", graphic = imageview("/img/function/manager/instrument_manage.png")) {
                            addClass(fun_menu_button)
                        }
                    }
                }
                tab("GPIE配对") { }
                tab("贝叶斯配对") { }
            }
        }
        center {
            val mainTabPane = tabpane {
                style { backgroundColor += Color.RED }
            }
        }
    }
}

