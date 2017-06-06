package lostfilm


import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import lostfilm.db.DBEpisode
import org.jsoup.Jsoup
import java.io.File
import java.net.HttpCookie

/**
 * Created by suslovs on 04.06.17.
 */

class Client {
    companion object {
        var cookies = listOf<HttpCookie>()

        fun visitMainPage() {
            val (_, response, _) = Global.mainPageUrl.httpGet().responseString()
            cookies = response.httpResponseHeaders["Set-Cookie"]?.flatMap { HttpCookie.parse(it) } ?: listOf()
        }

        fun loginToSite() {
            val requestForm = listOf(
                    "act" to "users",
                    "type" to "login",
                    "mail" to Global.config.login,
                    "pass" to Global.config.password
            )

            val (_, response, _) = Global.loginUrl.httpPost(requestForm).responseString()
            cookies = response.httpResponseHeaders["Set-Cookie"]?.flatMap { HttpCookie.parse(it) }?.filter { !it.hasExpired() } ?: listOf()
        }

        fun findBestResolution(episode: DBEpisode) {


            val (_, _, result) = nrdrLinkForEpisode(episode)
                    .httpGet()
                    .header(Pair("Cookie", cookies.joinToString("; ")))
                    .responseString()

            val redirectUrl: String? = Jsoup.parse(result.get()).select("meta").first().attr("content").substring(7)
            redirectUrl?.let {
                val (_, _, resultRedirect) = it.httpGet().header(Pair("Cookie", cookies.joinToString("; "))).responseString()
                Jsoup.parse(resultRedirect.get()).select("a").forEach { a ->
                    if (episode.bestTorrentUri == null && a.text().trim().contains(""".+?(1080p|720p).+?""".toRegex()))
                        episode.bestTorrentUri = a.attr("href")

                }
            }
        }

        fun nrdrLinkForEpisode(episode: DBEpisode)
                = "${Global.mainPageUrl}/v_search.php?c=${episode.serialId}&s=${episode.season}&e=${episode.episode}"

        fun downloadTorrentFile(episode: DBEpisode) {
            val outputPath = "${Global.config.outputPath}/lostfilm-${episode.serial}-${episode.season}-${episode.episode}.torrent"


            episode.bestTorrentUri?.let {
                Fuel.download(it)
                        .destination { _, _ -> File(outputPath) }
                        .header(Pair("Cookie", cookies.joinToString("; ")))
                        .response { _, _, _ -> }
            }
        }

        fun serialPage(serial: String) = Global.pageForSerial(serial)
                .httpGet()
                .header(Pair("Cookie", Client.cookies.joinToString("; ")))
                .responseString()
                .third.get()

    }
}