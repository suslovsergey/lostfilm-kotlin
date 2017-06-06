package lostfilm

data class Forced(val serial: String,
                  var episode: Int = 0,
                  var season: Int = 0,
                  var fullSeason: Boolean = false,
                  var fullSerial: Boolean = false)