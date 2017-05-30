package com.spartajet.gear.match.view.manager

import com.spartajet.gear.match.mybatis.bean.HaLiangMeasure
import javafx.scene.control.TableView
import javafx.scene.layout.GridPane
import tornadofx.*

/**
 * @description
 * @create 2017-05-30 下午9:48
 * @email spartajet.guo@gmail.com
 */
class HaLiangMeasureManagerView : View() {
    override val root: GridPane by fxml("/view/manager/HaLiangMeasureManager.fxml")

    val haLiangTable: TableView<HaLiangMeasure> by fxid("tableView")

    init {
        with(haLiangTable) {
            column("测量ID", HaLiangMeasure::id)
            column("齿轮ID", HaLiangMeasure::gearId)
            column("仪器 ID", HaLiangMeasure::instrumentId)
            column("mn", HaLiangMeasure::mn)
            column("z", HaLiangMeasure::z)
            column("d", HaLiangMeasure::d)
            column("da", HaLiangMeasure::da)
            column("df", HaLiangMeasure::df)
            column("alpha", HaLiangMeasure::alpha)
            column("beta", HaLiangMeasure::beta)
            column("sigma", HaLiangMeasure::sigma)
            column("mn2", HaLiangMeasure::mn2)
            column("z2", HaLiangMeasure::z2)
            column("d2", HaLiangMeasure::d2)
            column("da2", HaLiangMeasure::da2)
            column("dm2", HaLiangMeasure::dm2)
            column("alpha2", HaLiangMeasure::alpha2)
            column("beta2", HaLiangMeasure::beta2)
            column("x2", HaLiangMeasure::x2)
            column("备注", HaLiangMeasure::note)
        }

    }
}
