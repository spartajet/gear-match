package com.spartajet.gear.match.view.match

import com.github.abel533.echarts.series.Line
import com.google.gson.Gson
import com.spartajet.gear.match.base.hl.Haliang
import javafx.geometry.Orientation
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane
import javafx.scene.web.WebEngine
import javafx.scene.web.WebView
import tornadofx.*
import java.io.File

/**
 * @description
 * @create 2017-06-07 上午10:32
 * @email spartajet.guo@gmail.com
 */
class HaliangMatchView : View("哈量配对") {

    var drivingMeasureId: Long = 0L
    var drivedMeasureId: Long = 0L
    lateinit var drivingGear: Haliang
    lateinit var drivedGear: Haliang

    val drivingGearSelectButton: Button by fxid("drivingGearSelectButton")
    val drivingGearIdField: TextField by fxid("drivingGearIdField")
    val drivingMeasureIdField: TextField by fxid("drivingMeasureIdField")
    val drivingNoteField: TextArea by fxid("drivingNoteField")
    val drivedGearSelectButton: Button by fxid("drivedGearSelectButton")
    val drivedGearIdField: TextField by fxid("drivedGearIdField")
    val drivedMeasureIdField: TextField by fxid("drivedMeasureIdField")
    val drivedNoteField: TextArea by fxid("drivedNoteField")
    val matchParaGridPane: GridPane by fxid("matchParaGridPane")
    val drivingGearView: WebView by fxid("drivingGearView")
    val drivedGearView: WebView by fxid("drivedGearView")
    val gpieView: WebView by fxid("gpieView")

    lateinit var drivingWebEngine: WebEngine
    lateinit var drivedWebEngine: WebEngine
    lateinit var gpieWebEngine: WebEngine


    override val root: GridPane by fxml("/view/match/HaliangMatch.fxml")

    init {
        with(drivingGearSelectButton) {
            action {
                val model = find(HaliangMatchGearChooserView::class).apply { openModal(block = true) }
                drivingMeasureId = model.selectId
                val file = File("/Users/spartajet/Documents/gear_match/haliang/$drivingMeasureId.hl")
                val fileString = file.readText()
                drivingGear = Gson().fromJson(fileString, Haliang::class.java)
                showDrivingGearDetail()
            }

        }
        with(drivedGearSelectButton) {
            action {
                val model = find(HaliangMatchGearChooserView::class).apply { openModal(block = true) }
                drivedMeasureId = model.selectId
                val file = File("/Users/spartajet/Documents/gear_match/haliang/$drivedMeasureId.hl")
                val fileString = file.readText()
                drivedGear = Gson().fromJson(fileString, Haliang::class.java)
                showDrivedGearDetail()
            }
        }
        val drivingToggleGroup = ToggleGroup()
        val drivedToggleGroup = ToggleGroup()
        val setForm = form {
            fieldset("齿面选择") {
                field("主动齿轮", Orientation.VERTICAL) {
                    vbox {
                        radiobutton("左齿面", drivingToggleGroup, "DrivingL") { }
                        radiobutton("右齿面", drivingToggleGroup, "DrivingR") { }
                    }
                }
                field("被动齿轮", Orientation.VERTICAL) {
                    vbox {
                        radiobutton("左齿面", drivedToggleGroup, "DrivedL") { }
                        radiobutton("右齿面", drivedToggleGroup, "DrivedR") { }
                    }
                }
            }
        }
        with(matchParaGridPane) {
            add(setForm, 0, 1)
        }

        with(drivingGearView) {
            drivingWebEngine = engine
            engine.userAgent = "Chrome/58.0.3029.110"
            engine.load("http://localhost:8089/match/HaliangGear.html")
            //这个高度绑定还需要处理一下
            heightProperty().onChange {
                val chartHeight = it - 10
                val aaa = chartHeight.toInt()
                val js = "reSetDivHeight('$aaa')"
//                engine.executeScript("reSetDivHeight('180')")
            }
        }
        with(drivedGearView) {
            drivedWebEngine = engine
            engine.userAgent = "Chrome/58.0.3029.110"
            engine.load("http://localhost:8089/match/HaliangGear.html")
        }
    }

    private fun showDrivingGearDetail() {
        drivingGearIdField.text = drivingGear.gearid.toString()
        drivingMeasureIdField.text = drivingGear.id.toString()
        drivingNoteField.text = drivingGear.note
        val drivingLeftSeries = Line()
        with(drivingLeftSeries) {
            name = "左齿面 GIE"
//            data = drivingGear.giel.filterIndexed { index, _ -> index % 10 == 0 }
            data = mutableListOf<Array<Long>>()
            drivingGear.giel.forEachIndexed { index, d ->
                if (index % 10 == 0) {
                    data.add(arrayOf(index / 10.0, d))
                }
            }
            symbolSize = 1
        }
        val lineString = Gson().toJson(drivingLeftSeries)
        drivingWebEngine.executeScript("addSeries($lineString)")
    }

    private fun showDrivedGearDetail() {
        drivedGearIdField.text = drivedGear.gearid.toString()
        drivedMeasureIdField.text = drivedGear.id.toString()
        drivedNoteField.text = drivedGear.note
    }


}
