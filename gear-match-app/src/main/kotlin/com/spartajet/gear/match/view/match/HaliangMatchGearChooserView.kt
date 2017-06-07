package com.spartajet.gear.match.view.match

import com.spartajet.gear.match.mybatis.bean.Haliang
import com.spartajet.gear.match.mybatis.mapper.HaliangMapper
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.control.TableView
import org.controlsfx.control.Notifications
import tornadofx.*

/**
 * @description
 * @create 2017-06-07 上午11:36
 * @email spartajet.guo@gmail.com
 */
class HaliangMatchGearChooserView : Fragment("选择哈量齿轮测量结果") {
    var selectId: Long = 0L
    val haliangList = FXCollections.observableArrayList<Haliang>()
    val haliangMapper: HaliangMapper by di()

    lateinit var haliangTable: TableView<Haliang>

    override val root = borderpane {
        center {
            haliangTable = tableview<Haliang> {
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
                selectionModel.selectedItemProperty().onChange {
                    selectId = it?.id ?: 0L
                }
            }
        }

        bottom {
            hbox {
                alignment = Pos.CENTER
                spacing = 50.0
                button("确定") {
                    action {
                        if (selectId != 0L) close() else Notifications.create().title("警告").owner(parent).text("请选择齿轮测量结果").showWarning()
                    }
                }
                button("取消") {
                    action {
                        selectId = 0L
                        close()
                    }
                }
            }
        }
    }

    init {
        this.haliangList.clear()
        this.haliangList.addAll(this.haliangMapper.selectAll())
        this.haliangTable.items = this.haliangList
    }
}