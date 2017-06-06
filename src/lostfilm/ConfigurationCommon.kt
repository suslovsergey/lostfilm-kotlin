package lostfilm

data class ConfigurationCommon(val login: String,
                               val password: String,
                               val serials: List<String>,
                               val outputPath: String
)
