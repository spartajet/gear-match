package com.spartajet.gear.match.view.match

import com.google.gson.Gson
import com.spartajet.gear.match.base.hl.Haliang
import com.spartajet.gear.match.base.match.HaliangMatchStrategy
import javafx.collections.FXCollections
import javafx.geometry.Orientation
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane
import org.controlsfx.control.Notifications
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
    val CalculatePitchButton: Button by fxid("CalculatePitchButton")
    val CalculateMeshRegionButton: Button by fxid("CalculateMeshRegionButton")

    val drivingGearChart: LineChart<Number, Number> by fxid("drivingGearChart")
    val drivedGearChart: LineChart<Number, Number> by fxid("drivedGearChart")
    val matchChart: LineChart<Number, Number> by fxid("matchChart")
    val matchStrategy = HaliangMatchStrategy()


    override val root: GridPane by fxml("/view/match/HaliangMatch.fxml")

    init {
        with(drivingGearSelectButton) {
            action {
                val model = find(HaliangMatchGearChooserView::class).apply { openModal(block = true) }
                drivingMeasureId = model.selectId
                val file = File("/Users/spartajet/Documents/gear_match/haliang/$drivingMeasureId.hl")
                val fileString = file.readText()
                Notifications.create().title("任务提示").text("正在预处理主动齿轮").showInformation()
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
                Notifications.create().title("任务提示").text("正在预处理被动齿轮").showInformation()
                drivedGear = Gson().fromJson(fileString, Haliang::class.java)
                showDrivedGearDetail()
            }
        }
        val drivingToggleGroup = ToggleGroup()
        val drivedToggleGroup = ToggleGroup()
        val setForm = form {
            fieldset("齿面选择", labelPosition = Orientation.VERTICAL) {
                field("主动轮", Orientation.VERTICAL) {
                    hbox {
                        radiobutton("左齿面", drivingToggleGroup, "DrivingL") { isSelected = true }
                        radiobutton("右齿面", drivingToggleGroup, "DrivingR") { }
                    }
                }
                field("被动轮", Orientation.VERTICAL) {
                    hbox {
                        radiobutton("左齿面", drivedToggleGroup, "DrivedL") { isSelected = true }
                        radiobutton("右齿面", drivedToggleGroup, "DrivedR") { }
                    }
                }
            }
            fieldset("起始位置") {
                field("主动轮", Orientation.VERTICAL) {
                    combobox<Int> {
                        items = FXCollections.observableArrayList(1, 2, 3, 4)
                        value = 1
                    }
                }
                field("被动轮", Orientation.VERTICAL) {
                    combobox<Int> {
                        items = FXCollections.observableArrayList(1, 2, 3, 4)
                        value = 1
                    }
                }
            }
        }
        with(matchParaGridPane) {
            add(setForm, 0, 1, 1, 2)
        }
        with(CalculatePitchButton) {
            action {
                matchStrategy.extractPitches()
                showPitches()
            }
        }
        with(CalculateMeshRegionButton) {
            action {
                calculateMeshRegion()
            }
        }
    }

    /**
     * 显示主动轮详情
     */
    private fun showDrivingGearDetail() {
        drivingGearIdField.text = drivingGear.gearid.toString()
        drivingMeasureIdField.text = drivingGear.id.toString()
        drivingNoteField.text = drivingGear.note
        matchStrategy.preOperateDrivingGear(drivingGear)
        with(drivingGearChart) {
            data.clear()
//            createSymbols = false
            isLegendVisible = true
            val gieThreadSeries_L = matchStrategy.gieThreadSeries_Driving_L
            gieThreadSeries_L.forEachIndexed { index, gieThreadSeries ->
                val XYSeries = XYChart.Series<Number, Number>()
                XYSeries.name = "GIE_${index + 1}"
                gieThreadSeries.points.forEachIndexed { index, (_, x, y) ->
                    if (index % 10 == 0) XYSeries.data.add(XYChart.Data(x, y))
                }
                data.add(XYSeries)
            }
            (xAxis as NumberAxis).let {
                it.isAutoRanging = false
                it.lowerBound = 0.0
                it.upperBound = 360.0
                it.tickUnit = 20.0
            }
            (yAxis as NumberAxis).let {
                it.isAutoRanging = false
                it.lowerBound = drivingGear.giel.min() ?: -50.0 - 5.0
                it.upperBound = drivingGear.giel.max() ?: 50.0 + 5.0
            }
        }
    }

    /**
     * 显示被动轮详情
     */
    private fun showDrivedGearDetail() {
        drivedGearIdField.text = drivedGear.gearid.toString()
        drivedMeasureIdField.text = drivedGear.id.toString()
        drivedNoteField.text = drivedGear.note
        matchStrategy.preOperateDrivedGear(drivedGear)
        with(drivedGearChart) {
            data.clear()
//            createSymbols = false
            isLegendVisible = true
            matchStrategy.gieThreadSeries_Drived_L.forEachIndexed { index, gieThreadSeries ->
                val XYSeries = XYChart.Series<Number, Number>()
                XYSeries.name = "GIE_${index + 1}"
                gieThreadSeries.points.forEachIndexed { index, (_, x, y) ->
                    if (index % 10 == 0) XYSeries.data.add(XYChart.Data(x, y))
                }
                data.add(XYSeries)
            }
            (xAxis as NumberAxis).let {
                it.isAutoRanging = false
                it.lowerBound = 0.0
                it.upperBound = 360.0
                it.tickUnit = 20.0
            }
            (yAxis as NumberAxis).let {
                it.isAutoRanging = false
                it.lowerBound = drivedGear.giel.min() ?: -50.0 - 5.0
                it.upperBound = drivedGear.giel.max() ?: 50.0 + 5.0
            }
        }
    }

    /**
     * 显示节点
     */
    private fun showPitches() {

        val pitches_Driving_L = this.matchStrategy.pitches_Driving_L
        val pitches_Drived_L = this.matchStrategy.pitches_Drived_L
        val XYSeries_Driving_Pitch = XYChart.Series<Number, Number>()
        XYSeries_Driving_Pitch.name = "节点"
        pitches_Driving_L.forEachIndexed { _, pitch ->
            XYSeries_Driving_Pitch.data.add(XYChart.Data(pitch.point.x, pitch.point.y))
        }
        this.drivingGearChart.data.add(XYSeries_Driving_Pitch)
        val XYSeries_Drived_Pitch = XYChart.Series<Number, Number>()
        XYSeries_Drived_Pitch.name = "节点"
        pitches_Drived_L.forEachIndexed { _, pitch ->
            XYSeries_Drived_Pitch.data.add(XYChart.Data(pitch.point.x, pitch.point.y))
        }
        this.drivedGearChart.data.add(XYSeries_Drived_Pitch)
    }

    /**
     * 处理啮合部分
     */
    private fun calculateMeshRegion() {
        matchStrategy.extractMeshRegion()
        val GIESeries_Mesh_Driving_L = matchStrategy.GIESeries_Mesh_Driving_L
        val GIESeries_Mesh_Drived_L = matchStrategy.GIESeries_Mesh_Drived_L
        with(drivingGearChart) {
            data.remove(0, 3)
            val gie_mesh_1 = XYChart.Series<Number, Number>()
            gie_mesh_1.name = "GIE_MESH_1"
            val gie_mesh_2 = XYChart.Series<Number, Number>()
            gie_mesh_2.name = "GIE_MESH_2"
            val gie_mesh_3 = XYChart.Series<Number, Number>()
            gie_mesh_3.name = "GIE_MESH_3"
            matchStrategy.GIESeries_Mesh_Driving_L.points.forEach {
                if (it.index % 10 == 0) {
                    gie_mesh_1.data.add(XYChart.Data(it.x, it.values[0]))
                    gie_mesh_2.data.add(XYChart.Data(it.x, it.values[1]))
                    gie_mesh_3.data.add(XYChart.Data(it.x, it.values[2]))
                }
            }
            data.addAll(gie_mesh_1, gie_mesh_2, gie_mesh_3)
        }
        with(drivedGearChart) {
            data.remove(0, 3)
            val gie_mesh_1 = XYChart.Series<Number, Number>()
            gie_mesh_1.name = "GIE_MESH_1"
            val gie_mesh_2 = XYChart.Series<Number, Number>()
            gie_mesh_2.name = "GIE_MESH_2"
            val gie_mesh_3 = XYChart.Series<Number, Number>()
            gie_mesh_3.name = "GIE_MESH_3"
            matchStrategy.GIESeries_Mesh_Drived_L.points.forEach {
                if (it.index % 10 == 0) {
                    gie_mesh_1.data.add(XYChart.Data(it.x, it.values[0]))
                    gie_mesh_2.data.add(XYChart.Data(it.x, it.values[1]))
                    gie_mesh_3.data.add(XYChart.Data(it.x, it.values[2]))
                }
            }
            data.addAll(gie_mesh_1, gie_mesh_2, gie_mesh_3)
        }
    }
}
