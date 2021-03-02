package wtf.lucasmellof.devnics.datastore.tables

import wtf.lucasmellof.devnics.DevnicsLauncher.config

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 27/02/2021
 */
object GuildSettings : SnowflakeTable() {
    val prefix = text("prefix").default(config.prefix)
    val reactionMessageId = long("reactionMessageId").default(0)
    val ticketCategory = long("ticketCategory").default(0)
    val supportRole = long("supportRole").default(0)
    val logChannel = long("logChannel").default(0)
    val moderationRole = long("moderationRole").default(0)
}
