package com.spartajet.gear.match

import com.spartajet.gear.match.style.MainViewStyle
import com.spartajet.gear.match.view.MainView
import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import tornadofx.*
import kotlin.reflect.KClass

/**
 * @description
 * @create 2017-05-28 下午7:11
 * @email spartajet.guo@gmail.com
 */
@SpringBootApplication class GearMatchApp : App(MainView::class, MainViewStyle::class) {
    override fun init() {
        super.init()
        val applicationContext = SpringApplication.run(GearMatchApp::class.java)
        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>): T = applicationContext.getBean(type.java)
            override fun <T : Any> getInstance(type: KClass<T>, name: String): T = applicationContext.getBean(type.java, name)
        }
    }

    override fun start(stage: Stage) {
        super.start(stage)
        stage.icons.add(Image("/img/icons/main-icon-48.png"))
        //        stage.title="fadfasdfafas"
        stage.isMaximized = true
    }
}

fun main(args: Array<String>) {
    Application.launch(GearMatchApp::class.java, *args)
}
