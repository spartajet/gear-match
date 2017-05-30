package com.spartajet.gear.match.view.manager

import com.github.pagehelper.PageHelper
import com.google.gson.Gson
import com.spartajet.gear.match.base.utility.SnowFlake
import com.spartajet.gear.match.config.ParaConfig
import com.spartajet.gear.match.mybatis.bean.Gear
import com.spartajet.gear.match.mybatis.mapper.GearMapper
import com.spartajet.gear.match.style.manager.GearManagerViewStyle
import javafx.collections.FXCollections
import javafx.geometry.HPos
import javafx.geometry.Orientation
import javafx.scene.control.Pagination
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Priority
import net.glxn.qrgen.core.image.ImageType
import net.glxn.qrgen.javase.QRCode
import org.controlsfx.control.Notifications
import tornadofx.*
import java.io.ByteArrayInputStream

/**
 * @description
 * @create 2017-05-28 下午11:57
 * @email spartajet.guo@gmail.com
 */
class GearManagerView : View() {

    val gearList = FXCollections.observableArrayList<Gear>()

    val gearMapper: GearMapper by di()
    val paraConfig: ParaConfig by di()
    val snowFlake: SnowFlake by di()
    var currentPageIndex: Int = 0


    lateinit var pagination: Pagination
    lateinit var gearTable: TableView<Gear>
    lateinit var gearIdTextField: TextField
    lateinit var gearNameTextField: TextField
    lateinit var gearTypeTextField: TextField
    lateinit var gearZTextField: TextField
    lateinit var gearMnTextField: TextField
    lateinit var gearPressureAngleTextField: TextField
    lateinit var gearHelixAngleTextField: TextField
    lateinit var gearHaTextField: TextField
    lateinit var gearHfTextField: TextField
    lateinit var gearNoteTextArea: TextArea
    lateinit var gearQrImageview: ImageView

    override val root = gridpane {
        style {
            vgap = 5.px
            hgap = 5.px
        }

        with(constraintsForColumn(0)) {
            percentWidth = 80.0
            halignment = HPos.CENTER
            hgrow = Priority.SOMETIMES
        }
        with(constraintsForColumn(1)) {
            percentWidth = 20.0
            halignment = HPos.CENTER
            hgrow = Priority.SOMETIMES
        }

        gridpane {
            style {
                hgap = 5.px
                vgap = 5.px
            }
            with(constraintsForColumn(0)) {
                percentWidth = 15.0
                halignment = HPos.CENTER
                hgrow = Priority.NEVER
                vgrow = Priority.NEVER
            }
            with(constraintsForColumn(1)) {
                percentWidth = 55.0
                halignment = HPos.CENTER
                hgrow = Priority.SOMETIMES
                vgrow = Priority.ALWAYS
            }
            with(constraintsForColumn(2)) {
                percentWidth = 15.0
                halignment = HPos.CENTER
                hgrow = Priority.NEVER
                vgrow = Priority.NEVER
            }
            with(constraintsForColumn(3)) {
                percentWidth = 15.0
                halignment = HPos.CENTER
                hgrow = Priority.NEVER
                vgrow = Priority.NEVER
            }
            row {
                choicebox<String> {
                    value = "编号"
                    items = FXCollections.observableArrayList("编号", "名称", "类型", "齿数", "模数", "压力角", "螺旋角", "齿顶高", "齿根高")
                    tooltip("选择查找项")
                    addClass(GearManagerViewStyle.select_pane_item)
                    gridpaneConstraints {
                        margin = insets(2.0)
                    }
                }
                textfield {
                    addClass(GearManagerViewStyle.select_pane_item)
                    gridpaneConstraints {
                        margin = insets(2.0)
                    }
                }
                button("查找") {
                    addClass(GearManagerViewStyle.select_pane_item)
                    gridpaneConstraints {
                        margin = insets(2.0)
                    }
                }
                button("显示全部") {
                    addClass(GearManagerViewStyle.select_pane_item)
                    gridpaneConstraints {
                        margin = insets(2.0)
                    }
                }
            }
            gridpaneConstraints {
                columnRowIndex(0, 0)
                vgrow = Priority.NEVER
                prefHeight = 30.0
            }
        }

        gearTable = tableview<Gear> {
            column("编号", Gear::id).pctWidth(16)
            column("名称", Gear::name).pctWidth(15)
            column("类型", Gear::type).pctWidth(9)
            column("齿数", Gear::z).pctWidth(5)
            column("模数", Gear::mn).pctWidth(5)
            column("压力角", Gear::alpha).pctWidth(8)
            column("螺旋角", Gear::beta).pctWidth(8)
            column("齿顶高", Gear::ha).pctWidth(8)
            column("齿根高", Gear::hf).pctWidth(8)
            column("备注", Gear::remark).pctWidth(18)

            selectionModel.selectedItemProperty().addListener { _, _, newValue ->
                if (newValue != null) showGearDetail(newValue)
            }

            gridpaneConstraints {
                vgrow = Priority.SOMETIMES
                fillHeight = true
                prefHeight = 1000.0
                columnRowIndex(0, 1)
            }
        }

        pagination = pagination {
            style {
                pageInformationVisible = false
            }
            pageCount = 100
            addClass(GearManagerViewStyle.content_pane)
            gridpaneConstraints {
                vgrow = Priority.NEVER
                prefHeight = 50.0
                minHeight = 50.0
                columnRowIndex(0, 2)
            }
            setPageFactory {
                showGearList(it)
                this.currentPageIndex = it
                return@setPageFactory pane()
            }
        }
        form {
            gridpaneConstraints {
                columnRowIndex(1, 0)
                rowSpan = 3
            }
            style { spacing = 1.px }
            fieldset("齿轮信息") {
                field("二维码", Orientation.VERTICAL) {
                    gearQrImageview = imageview("/img/icons/main-icon.png") {
                        style {
                            fillHeight = true
                            fillWidth = true
                        }
                    }
                }
                field("编号") {
                    gearIdTextField = textfield { isEditable = false }
                }
                field("名称") {
                    gearNameTextField = textfield { }
                }
                field("类型") {
                    gearTypeTextField = textfield { }
                }
                field("齿数") {
                    gearZTextField = textfield { }
                }
                field("模数") {
                    gearMnTextField = textfield { }
                }
                field("压力角") {
                    gearPressureAngleTextField = textfield { }
                }
                field("螺旋角") {
                    gearHelixAngleTextField = textfield { }
                }
                field("齿顶高") {
                    gearHaTextField = textfield { }
                }
                field("齿根高") {
                    gearHfTextField = textfield { }
                }
                field("备注", Orientation.VERTICAL) {
                    gearNoteTextArea = textarea {
                        prefRowCount = 3
                        vgrow = Priority.NEVER
                    }
                }
                field {
                    button("添加") {
                        maxWidth = Double.POSITIVE_INFINITY
                        action { addGear() }
                    }
                    button("修改") {
                        maxWidth = Double.POSITIVE_INFINITY
                        action { updateGear() }
                    }
                    button("删除") {
                        maxWidth = Double.POSITIVE_INFINITY
                        action { deleteGear() }
                    }
//                    button("清空") {
//                        maxWidth = Double.POSITIVE_INFINITY
//                        action { clearGearDetailsField() }
//                    }
                }
                field { button("查询测量信息") { maxWidth = Double.POSITIVE_INFINITY } }
                field { button("查询仪器信息") { maxWidth = Double.POSITIVE_INFINITY } }
                field { button("查询配对历史") { maxWidth = Double.POSITIVE_INFINITY } }
            }
        }
    }

    init {
        importStylesheet(GearManagerViewStyle::class)
    }

    private fun showGearList(start: Int) {
        this.gearList.clear()
        val total: Int = this.gearMapper.totalGearsSize()
        val pages = total / this.paraConfig.gearTableShowLength!! + if (total % this.paraConfig.gearTableShowLength!! == 0) 0 else 1
        with(pagination) {
            currentPageIndex = start
            maxPageIndicatorCount = pages
        }
        PageHelper.startPage<Gear>(start + 1, this.paraConfig.gearTableShowLength!!)
        val gears = this.gearMapper.selectAllGears()
        this.gearList.addAll(gears)
        this.gearTable.items = gearList
    }

    private fun showGearDetail(gear: Gear) {
        this.gearIdTextField.text = gear.id.toString()
        this.gearNameTextField.text = gear.name
        this.gearTypeTextField.text = gear.type
        this.gearZTextField.text = gear.z.toString()
        this.gearMnTextField.text = gear.mn.toString()
        this.gearPressureAngleTextField.text = gear.alpha.toString()
        this.gearHelixAngleTextField.text = gear.beta.toString()
        this.gearHaTextField.text = gear.ha.toString()
        this.gearHfTextField.text = gear.hf.toString()
        this.gearNoteTextArea.text = gear.remark
        this.showGearQrCode(gear)
    }

    private fun showGearQrCode(gear: Gear) {
        this.gearQrImageview.image = null
        val gearString = Gson().toJson(gear)
        val out = QRCode.from(gearString).to(ImageType.PNG).withCharset("utf-8").stream()
        val inputStream = ByteArrayInputStream(out.toByteArray())
        val image = Image(inputStream)
        this.gearQrImageview.image = image

    }


    private fun addGear() {
        if (!this.checkGearDetailsField()) return
        val gear = this.getGearModel()
        try {
            gear.id=this.snowFlake.nextId()
            this.gearMapper.insert(gear)
            this.showGearList(this.currentPageIndex)
            this.clearGearDetailsField()
        } catch (e: Exception) {
            Notifications.create().title("错误").text("齿轮添加错误，信息：${e.message}").owner(this).showError()
        }
    }

    private fun updateGear() {
        if (!this.checkGearDetailsField()) return
        val gear = this.getGearModel()
        try {
            this.gearMapper.updateByPrimaryKey(gear)
            this.showGearList(this.currentPageIndex)
            this.clearGearDetailsField()
        } catch (e: Exception) {
            Notifications.create().title("错误").text("齿轮更新错误，信息：${e.message}").owner(this).showError()
        }
    }

    private fun deleteGear() {
        val idString = gearIdTextField.text
        if (idString == null) {
            Notifications.create().title("警告").text("请选择要删除的齿轮").owner(this).showWarning()
        }
        val id = idString.toLong()
        try {
            this.gearMapper.deleteByPrimaryKey(id)
            this.showGearList(currentPageIndex)
            this.clearGearDetailsField()
        } catch (e: Exception) {
            Notifications.create().title("错误").text("齿轮删除错误，信息：${e.message}").owner(this).showError()
        }
    }

    private fun clearGearDetailsField() {
        this.gearQrImageview.image = Image("/img/icons/main-icon.png")
        this.gearIdTextField.clear()
        this.gearNameTextField.clear()
        this.gearTypeTextField.clear()
        this.gearZTextField.clear()
        this.gearMnTextField.clear()
        this.gearPressureAngleTextField.clear()
        this.gearHelixAngleTextField.clear()
        this.gearHaTextField.clear()
        this.gearHfTextField.clear()
        this.gearNoteTextArea.clear()
    }

    private fun checkGearDetailsField(): Boolean {
        if (this.gearNameTextField.text == null) {
            Notifications.create().title("警告").text("请输入齿轮名称").owner(this).showWarning()
            return false
        }
        if (this.gearTypeTextField.text == null) {
            Notifications.create().title("警告").text("请输入齿轮类型").owner(this).showWarning()
            return false
        }
        if (this.gearZTextField.text == null) {
            Notifications.create().title("警告").text("请输入齿轮齿数").owner(this).showWarning()
            return false
        }
        if (this.gearMnTextField.text == null) {
            Notifications.create().title("警告").text("请输入齿轮模数").owner(this).showWarning()
            return false
        }
        if (this.gearPressureAngleTextField.text == null) {
            Notifications.create().title("警告").text("请输入齿轮压力角").owner(this).showWarning()
            return false
        }
        if (this.gearHelixAngleTextField.text == null) {
            Notifications.create().title("警告").text("请输入齿轮螺旋角").owner(this).showWarning()
            return false
        }
        if (this.gearHaTextField.text == null) {
            Notifications.create().title("警告").text("请输入齿轮齿顶高").owner(this).showWarning()
            return false
        }
        if (this.gearHfTextField.text == null) {
            Notifications.create().title("警告").text("请输入齿轮齿根高").owner(this).showWarning()
            return false
        }
        return true
    }

    private fun getGearModel(): Gear {
        val id = this.gearIdTextField.text.toLong()
        val name = this.gearNameTextField.text
        val type = this.gearTypeTextField.text
        val z = this.gearZTextField.text.toInt()
        val mn = this.gearMnTextField.text.toDouble()
        val alpha = this.gearPressureAngleTextField.text.toDouble()
        val beta = this.gearHelixAngleTextField.text.toDouble()
        val ha = this.gearHaTextField.text.toDouble()
        val hf = this.gearHfTextField.text.toDouble()
        val remark = this.gearNoteTextArea.text
        return Gear(id, name, type, z, mn, alpha, beta, ha, hf, remark)

    }

    private fun sizeBind() {

    }


}


