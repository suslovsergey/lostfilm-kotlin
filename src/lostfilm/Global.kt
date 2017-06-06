package lostfilm

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

class Global {
    companion object {
        val mainPageUrl = "https://www.lostfilm.tv"
        val loginUrl = "https://www.lostfilm.tv/ajaxik.php"
        val mapper = ObjectMapper(YAMLFactory()).apply { registerModule(KotlinModule()) }
        val config = loadConfig()
        fun loadConfig(): ConfigurationCommon = File(System.getenv("HOME") + "/.lostfilm.yaml").bufferedReader()
                .use { mapper.readValue(it, ConfigurationCommon::class.java) }

        fun pageForSerial(serial: String): String = "$mainPageUrl/series/$serial/seasons"

    }
}
