package com.spartajet.gear.match.view.manager

import com.spartajet.gear.match.base.utility.SnowFlake
import com.spartajet.gear.match.mybatis.bean.Haliang
import com.spartajet.gear.match.mybatis.mapper.HaliangMapper
import javafx.scene.control.Button
import javafx.scene.control.DatePicker
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.stage.FileChooser
import org.controlsfx.control.Notifications
import org.json.simple.JSONObject
import org.json.simple.JSONValue
import tornadofx.*
import java.io.File
import java.time.ZoneId
import java.util.*


/**
 * @description
 * @create 2017-06-03 下午1:15
 * @email spartajet.guo@gmail.com
 */
class AddHaLiangDialog : Fragment("添加哈量测量结果") {

    val snowFlake: SnowFlake by di()

    val haliangMapper: HaliangMapper by di()

    var file: File? = null

    override val root: GridPane by fxml("/view/manager/AddHaLiangMeasure.fxml")
    private val generateIdButton: Button by fxid("generateIdButton")
    private val fileChooserButton: Button by fxid("fileChooserButton")
    private val instrumentSelectButton: Button by fxid("instrumentSelectButton")
    private val gearSelectButton: Button by fxid("gearSelectButton")
    private val addMeasureButton: Button by fxid("addMeasureButton")
    private val measureIdTextField: TextField by fxid("measureIdTextField")
    private val filePathTextField: TextField by fxid("filePathTextField")
    private val pitchPointTextField: TextField by fxid("pitchPointTextField")
    private val cancelButton: Button by fxid("cancelButton")
    private val datePicker: DatePicker by fxid("datePicker")
    private val noteTextArea: TextArea by fxid("noteTextArea")

    init {
        with(generateIdButton) {
            action {
                measureIdTextField.text = snowFlake.nextId().toString()
            }
        }
        with(fileChooserButton) {
            action {
                val chooser: FileChooser = FileChooser()
                with(chooser) {
                    title = "请选择哈量测量文件"
                    extensionFilters.add(FileChooser.ExtensionFilter("Haliang File", "*.jsd"))
                }
                file = chooser.showOpenDialog(root.scene.window)
                if (file != null) filePathTextField.text = file!!.name

            }
        }
        with(instrumentSelectButton) {
            action {
                Notifications.create().title("提示").text("目前的设备编号为0").owner(root).showInformation()
            }
        }
        with(gearSelectButton) {
            action {
                Notifications.create().text("提示").text("目前的齿轮编号为0").owner(root).showInformation()
            }
        }
        with(addMeasureButton) {
            action {
                Notifications.create().title("提示").text("开始添加测量信息").owner(root).showInformation()
                if (addHaLiangMeasure()) {
                    Notifications.create().title("成功").text("添加齿轮成功").owner(root).showInformation()
                    close()
                }
            }
        }

        with(cancelButton) {
            action { close() }
        }
    }

    private fun addHaLiangMeasure(): Boolean {
        if (measureIdTextField.text.trim().isEmpty()) {
            Notifications.create().title("警告").text("请生成测量 ID").showWarning()
            return false
        }
        if (this.file == null) {
            Notifications.create().title("警告").text("请选择文件").showWarning()
            return false
        }
        if (pitchPointTextField.text.trim().isEmpty()) {
            Notifications.create().title("警告").text("请输入节点").showWarning()
            return false
        }
        if (this.datePicker.value == null) {
            Notifications.create().title("警告").text("请选择日期").showWarning()
            return false
        }

        val id = this.measureIdTextField.text.toLong()
        val instrumentId = 0L
        val gearId = 0L
        val pitchPoint = this.pitchPointTextField.text.toDouble()
        val date = this.datePicker.value
        val note = file!!.nameWithoutExtension + ":" + this.noteTextArea.text

        val fileString = file!!.readText()

        val obj = JSONValue.parse(fileString)
        val haliang = obj as JSONObject
        val mn1 = this.any2Double(haliang["mn1"])
        val z1 = this.any2Int(haliang["z1"])
        val d1 = this.any2Double(haliang["d1"])
        val da1 = this.any2Double(haliang["da1"])
        val df1 = this.any2Double(haliang["df1"])
        val alpha1 = this.any2Double(haliang["alpha1"])
        val beta1 = this.any2Double(haliang["beta1"])
        val sigma = this.any2Double(haliang["sigma"])
        val mn2 = this.any2Double(haliang["mn2"])
        val z2 = this.any2Int(haliang["z2"])
        val d2 = this.any2Double(haliang["d2"])
        val da2 = this.any2Double(haliang["da2"])
        val dM2 = this.any2Double(haliang["dM2"])
        val alpha2 = this.any2Double(haliang["alpha2"])
        val beta2 = this.any2Double(haliang["beta2"])
        val x2 = this.any2Double(haliang["x2"])
        val Fal = this.any2Double(haliang["Fal"])
        val ffaL = this.any2Double(haliang["ffaL"])
        val fHaL = this.any2Double(haliang["fHaL"])
        val FpL = this.any2Double(haliang["FpL"])
        val FpkL = this.any2Double(haliang["FpkL"])
        val fpL = this.any2Double(haliang["fpL"])
        val fuL = this.any2Double(haliang["fuL"])
        val FaR = this.any2Double(haliang["FaR"])
        val ffaR = this.any2Double(haliang["ffaR"])
        val fHaR = this.any2Double(haliang["fHaR"])
        val FpR = this.any2Double(haliang["FpR"])
        val FpkR = this.any2Double(haliang["FpkR"])
        val fpR = this.any2Double(haliang["fpR"])
        val fuR = this.any2Double(haliang["fuR"])
        val Fr = this.any2Double(haliang["Fr"])
        val Rs = this.any2Double(haliang["Rs"])
        val FisL = this.any2Double(haliang ["FisL"])
        val fis_max_L = this.any2Double(haliang ["fis_max_L"])
        val FisR = this.any2Double(haliang["FisR"])
        val fis_max_R = this.any2Double(haliang["fis_max_R"])
        val GIE_L_First_Unit_zero_angle = this.any2Double(haliang["GIE_L_First_Unit_zero_angle"])
        val GIE_R_First_Unit_zero_angle = this.any2Double(haliang["GIE_R_First_Unit_zero_angle"])
        val Class_FaL = this.any2Int(haliang["Class_FaL"])
        val Class_ffaL = this.any2Int(haliang["Class_ffaL"])
        val Class_fHaL = this.any2Int(haliang["Class_fHaL"])
        val Class_FpL = this.any2Int(haliang["Class_FpL"])
        val Class_FpkL = this.any2Int(haliang["Class_FpkL"])
        val Class_fpL = this.any2Int(haliang["Class_fpL"])
        val Class_fuL = this.any2Int(haliang["Class_fuL"])
        val Class_FaR = this.any2Int(haliang["Class_FaR"])
        val Class_ffaR = this.any2Int(haliang["Class_ffaR"])
        val Class_fHaR = this.any2Int(haliang["Class_fHaR"])
        val Class_FpR = this.any2Int(haliang["Class_FpR"])
        val Class_FpkR = this.any2Int(haliang["Class_FpkR"])
        val Class_fpR = this.any2Int(haliang["Class_fpR"])
        val Class_fuR = this.any2Int(haliang["Class_fuR"])
        val Class_Fr = this.any2Int(haliang["Class_Fr"])
        val Class_Rs = this.any2Int(haliang["Class_Rs"])
        val Class_FisL = this.any2Int(haliang["Class_FisL"])
        val Class_fis_max_L = this.any2Int(haliang["Class_fis_max_L"])
        val Class_FisR = this.any2Int(haliang["Class_FisR"])
        val Class_fis_max_R = this.any2Int(haliang["Class_fis_max_R"])
        val createDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())

        val haliangBean = Haliang(id, gearId, instrumentId, mn1, z1, d1, da1, df1, alpha1, beta1, sigma, mn2, z2, d2, da2, dM2, alpha2, beta2, x2, note, GIE_L_First_Unit_zero_angle, GIE_R_First_Unit_zero_angle, createDate, Fal, Class_FaL, ffaL, Class_ffaL, fHaL, Class_fHaL, fpL, Class_fpL, FpkL, Class_FpkL, FpL, Class_FpL, fuL, Class_fuL, FaR, Class_FaR, ffaR, Class_ffaR, fHaR, Class_fHaR, fpR, Class_fpR, FpkR, Class_FpkR, FpR, Class_FpR, fuR, Class_fuR, Fr, Class_Fr, Rs, Class_Rs, FisL, Class_FisL, fis_max_L, Class_fis_max_L, FisR, Class_FisR, fis_max_R, Class_fis_max_R)


        try {
            file!!.copyTo(File("/Users/spartajet/Documents/gear_match/haliang/$id.jsd"))
            this.haliangMapper.insert(haliangBean)
            Notifications.create().title("success").text("add HaLiang Measure successfully! ")
            return true
        } catch(e: Exception) {
            Notifications.create().title("fail").text("add HaLiang Measure failure! ")
        }


        return true
    }


    private fun any2Int(any: Any?) = when (any) {
        is Long -> any.toInt()
        is Int -> any
        else -> 0
    }

    private fun any2Double(any: Any?) = when (any) {
        is Long -> any.toDouble()
        is Int -> any.toDouble()
        is Float -> any.toDouble()
        is Double -> any
        else -> 0.0
    }

}