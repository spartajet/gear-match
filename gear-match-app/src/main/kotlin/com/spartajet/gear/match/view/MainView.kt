package com.spartajet.gear.match.view

import com.spartajet.gear.match.style.MainViewStyle.Companion.fun_menu_button
import com.spartajet.gear.match.style.MainViewStyle.Companion.function_menu_tabpane
import com.spartajet.gear.match.style.MainViewStyle.Companion.tab_flow_pane
import com.spartajet.gear.match.view.manager.GearManagerView
import javafx.geometry.Side
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import tornadofx.*

/**
 * @description
 * @create 2017-05-28 下午7:14
 * @email spartajet.guo@gmail.com
 */
class MainView : View("齿轮配对系统") {

    val dashBoardView: DashBoardView by inject()
    val gearManagerView: GearManagerView by inject()

    val mainTabPane = tabpane()
    //    lateinit var gearManagerTab: Tab
    var isShowGearManageTab: Boolean = false


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
                tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
                addClass(function_menu_tabpane)
                side = Side.LEFT
                tab("系统管理") {
                    flowpane {
                        button("齿轮管理", graphic = imageview("/img/function/manager/gear_manage.png")) {
                            addClass(fun_menu_button)
                            action { showGearManager() }
                        }
                        addClass(tab_flow_pane)
                        button("哈量测量", graphic = imageview("/img/function/manager/instrument_manage.png")) {
                            addClass(fun_menu_button)
                            action { showHaLiangMeasure() }
                        }
                    }
                }
                tab("GPIE配对") { }
                tab("贝叶斯配对") { }
            }
        }
        center = mainTabPane
    }

    init {
        val dashTab: Tab = Tab("欢迎界面")
        with(dashTab) {
            content = dashBoardView.root
        }
        mainTabPane.tabs.add(dashTab)
    }

    private fun showGearManager() {
        if (isShowGearManageTab) return
        val gearManagerTab = Tab("齿轮管理")
        with(gearManagerTab) {
            content = gearManagerView.root
            setOnClosed { isShowGearManageTab = false }
        }
        mainTabPane.tabs.add(gearManagerTab)
        mainTabPane.selectionModel.select(gearManagerTab)
        isShowGearManageTab = true
    }

    private fun showHaLiangMeasure() {

    }
}

