package wtf.lucasmellof.devnics.datastore.tables

import wtf.lucasmellof.devnics.DevnicsLauncher.config

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 27/02/2021
 */
object GuildSettings : SnowflakeTable() {
    val prefix = text("prefix").default(config.prefix)
    val reactionChannelId = long("reactionChannelId").default(0)
}
