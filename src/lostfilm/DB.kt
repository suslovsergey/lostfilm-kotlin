package lostfilm

import lostfilm.db.DBEpisode
import ninja.sakib.pultusorm.core.PultusORM
import ninja.sakib.pultusorm.core.PultusORMCondition

class DB {
    companion object {
        val client = PultusORM(".lostfilm.db", System.getenv("HOME"))
        fun isExists(item: DBEpisode): Boolean {
            val condition = PultusORMCondition.Builder()
                    .eq("serialId", item.serialId)
                    .and().eq("season", item.season)
                    .and().eq("episode", item.episode)
                    .build()

            return client.find(DBEpisode(), condition).count() >= 1
        }

        fun add(item: DBEpisode) {
            if (!isExists(item)) {
                client.save(item)
            }
        }
    }
}