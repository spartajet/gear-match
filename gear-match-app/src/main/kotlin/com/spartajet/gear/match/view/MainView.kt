package com.spartajet.gear.match.view

import com.spartajet.gear.match.style.MainViewStyle.Companion.fun_menu_button
import com.spartajet.gear.match.style.MainViewStyle.Companion.function_menu_tabpane
import com.spartajet.gear.match.style.MainViewStyle.Companion.tab_flow_pane
import com.spartajet.gear.match.view.manager.GearManagerView
import com.spartajet.gear.match.view.manager.HaLiangMeasureManagerView
import com.spartajet.gear.match.view.match.HaliangMatchView
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
    val haLiangMeasureManagerView: HaLiangMeasureManagerView by inject()
    val haliangMatchView: HaliangMatchView by inject()

    val mainTabPane = tabpane()

    var isShowGearManageTab: Boolean = false
    var isShowHaLiangMeasureTab: Boolean = false
    var isShowHaliangMatchTab: Boolean = false


    override val root = borderpane {
//        top {
//            menubar {
//                menu("File") {
//                    item("New")
//                    separator()
//                }
//                menu("关于") {
//                    item("版权信息")
//                }
//
//            }
//        }
        left {
            tabpane {
                tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
                addClass(function_menu_tabpane)
                side = Side.LEFT
                tab("系统管理") {
                    flowpane {
                        addClass(tab_flow_pane)
                        button("齿轮管理", graphic = imageview("/img/function/manager/gear_manage.png")) {
                            addClass(fun_menu_button)
                            action { showGearManager() }
                        }
                        button("哈量测量", graphic = imageview("/img/function/manager/instrument_manage.png")) {
                            addClass(fun_menu_button)
                            action { showHaLiangMeasure() }
                        }
                    }
                }
                tab("GPIE配对") {
                    flowpane {
                        addClass(tab_flow_pane)
                        button("哈量配对", graphic = imageview("/img/function/manager/gear_manage.png")) {
                            addClass(fun_menu_button)
                            action { showHaliangMatch() }
                        }
                    }
                }
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
        if (isShowHaLiangMeasureTab) return
        val haLiangMeasureTab = Tab("哈量管理")
        with(haLiangMeasureTab) {
            content = haLiangMeasureManagerView.root
            setOnClosed { isShowHaLiangMeasureTab = false }
        }
        mainTabPane.tabs.add(haLiangMeasureTab)
        mainTabPane.selectionModel.select(haLiangMeasureTab)
        isShowHaLiangMeasureTab = true
    }

    private fun showHaliangMatch() {
        if (isShowHaliangMatchTab) return
        val haliangMatchTab = Tab("哈量配对")
        with(haliangMatchTab) {
            content = haliangMatchView.root
            setOnClosed { isShowHaliangMatchTab = false }
        }
        with(mainTabPane) {
            tabs.add(haliangMatchTab)
            selectionModel.select(haliangMatchTab)
        }
        isShowHaliangMatchTab = true
    }

}

