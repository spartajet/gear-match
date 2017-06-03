package com.spartajet.gear.match.view.manager

import com.spartajet.gear.match.mybatis.bean.Haliang
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.layout.GridPane
import javafx.stage.StageStyle
import tornadofx.*

/**
 * @description
 * @create 2017-05-30 下午9:48
 * @email spartajet.guo@gmail.com
 */
class HaLiangMeasureManagerView : View() {
    override val root: GridPane by fxml("/view/manager/HaLiangMeasureManager.fxml")

    val haLiangTable: TableView<Haliang> by fxid("tableView")

    val addMeasureButton: Button by fxid("addMeasureButton")


    init {
        with(haLiangTable) {
            column("测量ID", Haliang::id)
            column("齿轮ID", Haliang::gearid)
            column("仪器 ID", Haliang::instrumentid)
            column("mn", Haliang::mn)
            column("z", Haliang::z)
            column("d", Haliang::d)
            column("da", Haliang::da)
            column("df", Haliang::df)
            column("alpha", Haliang::alpha)
            column("beta", Haliang::beta)
            column("sigma", Haliang::sigma)
            column("mn2", Haliang::mn2)
            column("z2", Haliang::z2)
            column("d2", Haliang::d2)
            column("da2", Haliang::da2)
            column("dm2", Haliang::dm2)
            column("alpha2", Haliang::alpha2)
            column("beta2", Haliang::beta2)
            column("x2", Haliang::x2)
            column("备注", Haliang::note)
        }

        with(addMeasureButton) {
            action {
                find(AddHaLiangDialog::class).openModal(stageStyle = StageStyle.UTILITY)
            }
        }

    }
}
