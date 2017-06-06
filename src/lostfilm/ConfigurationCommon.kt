package lostfilm

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class ConfigurationCommon(val login: String,
                               val password: String,
                               val serials: List<String>,
                               val outputPath: String
)
