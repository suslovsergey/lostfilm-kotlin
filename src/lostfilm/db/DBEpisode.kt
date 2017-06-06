package lostfilm.db

import ninja.sakib.pultusorm.annotations.AutoIncrement
import ninja.sakib.pultusorm.annotations.Ignore
import ninja.sakib.pultusorm.annotations.PrimaryKey


class DBEpisode() {
    @PrimaryKey
    @AutoIncrement
    var recordId: Int = 0
    var serial: String? = null
    var serialId: Int = 0
    var season: Int = 0
    var episode: Int = 0
    @Ignore
    var bestTorrentUri: String? = null

    constructor(serial: String, serialId: Int, season: Int, episode: Int) : this() {
        this.serial = serial
        this.serialId = serialId
        this.season = season
        this.episode = episode
    }
}