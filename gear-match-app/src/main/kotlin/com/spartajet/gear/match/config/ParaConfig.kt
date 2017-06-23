package com.spartajet.gear.match.config

import com.spartajet.gear.match.api.utility.SnowFlake
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @description
 * @create 2017-05-29 下午10:37
 * @email spartajet.guo@gmail.com
 */
@Configuration
class ParaConfig {
    @Value("\${org.bpl.gear.match.ui.gear.table.size}") var gearTableShowLength: Int? = null

    @Bean fun snowFlake() = SnowFlake(1, 1)

}