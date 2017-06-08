package com.spartajet.gear.match.view.match

import com.google.gson.Gson
import com.spartajet.gear.match.base.hl.Haliang
import com.spartajet.gear.match.view.utility.JFreeChartCanvas
import javafx.geometry.Orientation
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane
import org.jfree.chart.ChartFactory
import org.jfree.chart.JFreeChart
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeriesCollection
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
    val mainGridPane: GridPane by fxid("mainGridPane")


    val drivingChartSeries = XYSeriesCollection()
    val drivedChartSeries = XYSeriesCollection()
    val matchChartSeries = XYSeriesCollection()

    val drivingGearChart: JFreeChart = ChartFactory.createXYLineChart("主动轮", "角度", "误差", drivingChartSeries, PlotOrientation.VERTICAL, true, true, false)
    val drivedGearChart: JFreeChart = ChartFactory.createXYLineChart("被动轮", "角度", "误差", drivedChartSeries, PlotOrientation.VERTICAL, true, true, false)
    val matchChart: JFreeChart = ChartFactory.createXYLineChart("齿轮配对", "角度", "误差", matchChartSeries, PlotOrientation.VERTICAL, true, true, false)

    val drivingGearCanvas = JFreeChartCanvas(drivingGearChart)
    val drivedGearCanvas = JFreeChartCanvas(drivedGearChart)
    val matchCanvas = JFreeChartCanvas(matchChart)

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


        with(drivingGearCanvas) {
            widthProperty().bind(mainGridPane.widthProperty().subtract(320))
            heightProperty().bind(mainGridPane.heightProperty().multiply(0.3))
        }
        with(drivedGearCanvas) {
            widthProperty().bind(mainGridPane.widthProperty().subtract(320))
            heightProperty().bind(mainGridPane.heightProperty().multiply(0.3))
        }
        with(matchCanvas) {
            widthProperty().bind(mainGridPane.widthProperty().subtract(170))
            heightProperty().bind(mainGridPane.heightProperty().multiply(0.4))
        }

        with(mainGridPane) {
            add(drivingGearCanvas, 1, 0)
            add(drivedGearCanvas, 1, 1)
            add(matchCanvas, 0, 2, 2, 1)
        }
        with(matchParaGridPane) {
            add(setForm, 0, 1)
        }
    }

    private fun showDrivingGearDetail() {
        drivingGearIdField.text = drivingGear.gearid.toString()
        drivingMeasureIdField.text = drivingGear.id.toString()
        drivingNoteField.text = drivingGear.note
    }

    private fun showDrivedGearDetail() {
        drivedGearIdField.text = drivedGear.gearid.toString()
        drivedMeasureIdField.text = drivedGear.id.toString()
        drivedNoteField.text = drivedGear.note
    }


}
