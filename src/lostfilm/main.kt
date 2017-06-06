package lostfilm

import me.tongfei.progressbar.ProgressBar
import org.jsoup.Jsoup
import lostfilm.db.DBEpisode
import me.tongfei.progressbar.ProgressBarStyle


fun main(args: Array<String>) {

    var forced: Forced? = null

    if (args.count() > 0) {
        forced = Forced(serial = args[0])
        if (args.count() > 1) {
            if (args[1].toLowerCase() == "all")
                forced.fullSerial = true
            else
                forced.season = args[1].toInt()
        }

        if (args.count() > 2) {
            if (args[2].toLowerCase() == "all")
                forced.fullSeason = true
            else
                forced.episode = args[1].toInt()
        }
    }

    work(forced)
}


private fun work(forced: Forced? = null) {
    val regexZeta = """PlayEpisode\('(\d+)','(\d+)','(\d+)'\)""".toRegex()
    val pb = ProgressBar("Serials", Global.config.serials.count().toLong(), ProgressBarStyle.ASCII)

    pb.start()
    Client.visitMainPage()
    Client.loginToSite()
    val downloaded = mutableListOf<DBEpisode>()
    val serialList: List<String> = if (forced != null) listOf(forced.serial) else Global.config.serials

    serialList.forEach { serial ->

        try {
            val html = Jsoup.parse(Client.serialPage(serial))
            html.select(".zeta")
                    .map { it.childNode(1).attr("onclick") }
                    .map {

                        var output: DBEpisode? = null
                        val groups = regexZeta.matchEntire(it)?.groups
                        groups?.apply {
                            if (size == 4) {
                                val serialId = get(1)?.value?.toInt()
                                val season = get(2)?.value?.toInt()
                                val episode = get(3)?.value?.toInt()

                                if (serialId != null && season != null && episode != null) {
                                    output = DBEpisode(
                                            serial = serial,
                                            serialId = serialId,
                                            season = season,
                                            episode = episode
                                    )
                                }

                            }
                        }
                        output
                    }
                    .filterNotNull()
                    .filter { it.episode != 999 }
                    .filter {
                        when {
                            forced?.fullSeason != null && forced.season == it.season -> true
                            forced?.fullSerial != null && forced.fullSerial -> true
                            !DB.isExists(it) -> true
                            else -> false
                        }
                    }
                    .map { Client.findBestResolution(it); it }
                    .map { Client.downloadTorrentFile(it); it }
                    .forEach {
                        downloaded.add(it)
                        DB.add(it)
                    }

        } catch (e: Throwable) {
            println("Error => $e")
        } finally {
            pb.step()
        }
    }
    pb.stop()
    if (downloaded.count() > 0) {
        downloaded.forEach {
            println("Serial: ${it.serial}, Season: ${it.season}, Episode: ${it.episode}")
        }
    } else
        println("No new series!!!")

    DB.client.close()
    System.exit(0)
}