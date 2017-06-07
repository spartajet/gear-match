package com.spartajet.gear.match.view.manager

import com.google.gson.Gson
import com.spartajet.gear.match.mybatis.bean.Haliang
import com.spartajet.gear.match.mybatis.mapper.HaliangMapper
import javafx.collections.FXCollections
import javafx.scene.chart.LineChart
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.layout.GridPane
import tornadofx.*
import java.io.File

/**
 * @description
 * @create 2017-05-30 下午9:48
 * @email spartajet.guo@gmail.com
 */
class HaLiangMeasureManagerView : View() {
    override val root: GridPane by fxml("/view/manager/HaLiangMeasureManager.fxml")
    val haLiangTable: TableView<Haliang> by fxid("tableView")
    val addMeasureButton: Button by fxid("addMeasureButton")
    val chart: LineChart<Number, Number> by fxid("chart")

    val haLiangList = FXCollections.observableArrayList<Haliang>()
    val haliangMapper: HaliangMapper by di()

    init {
        with(haLiangTable) {
            column("测量ID", Haliang::id)
            column("齿轮ID", Haliang::gearid)
            column("仪器 ID", Haliang::instrumentid)
            column("备注", Haliang::note)
            column("mn2", Haliang::mn2)
            column("z2", Haliang::z2)
            column("d2", Haliang::d2)
            column("da2", Haliang::da2)
            column("dm2", Haliang::dm2)
            column("alpha2", Haliang::alpha2)
            column("beta2", Haliang::beta2)
            column("x2", Haliang::x2)
            column("mn", Haliang::mn)
            column("z", Haliang::z)
            column("d", Haliang::d)
            column("da", Haliang::da)
            column("df", Haliang::df)
            column("alpha", Haliang::alpha)
            column("beta", Haliang::beta)
            column("sigma", Haliang::sigma)
            column("pitchl", Haliang::pitchl)
            column("pitchr", Haliang::pitchr)
            selectionModel.selectedItemProperty().onChange {
                showHaliangDetails(it)
            }
        }
        with(addMeasureButton) {
            action {
                val modal = find(AddHaLiangDialog::class).apply { openModal(block = true) }
                if (modal.result) showHaliangMeasureTable()
            }
        }
        this.showHaliangMeasureTable()

    }


    private fun showHaliangMeasureTable() {
        this.haLiangList.clear()
        this.haLiangList.addAll(this.haliangMapper.selectAll())
        this.haLiangTable.items = this.haLiangList

    }

    private fun showHaliangDetails(haliang: Haliang?) {
        if (haliang == null) return
        val file: File = File("/Users/spartajet/Documents/gear_match/haliang/${haliang.id}.hl")
        val fileString = file.readText()
        val hlfile = Gson().fromJson(fileString, com.spartajet.gear.match.base.hl.Haliang::class.java)
        val giel = hlfile.giel
        val gier = hlfile.gier
        with(chart) {
            data.clear()
            title = "齿轮整体误差"
            createSymbols = false
            isLegendVisible = true

            multiseries("左齿面", "右齿面") {
                for (i in 0..giel.size / 10 - 1) {
                    data(i, giel[i * 10], gier[i * 10])
                }
            }

        }
    }

}
