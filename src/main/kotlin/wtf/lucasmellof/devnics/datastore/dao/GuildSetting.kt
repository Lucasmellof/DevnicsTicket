package wtf.lucasmellof.devnics.datastore.dao

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import wtf.lucasmellof.devnics.datastore.tables.GuildSettings

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 27/02/2021
 */
class GuildSetting(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, GuildSetting>(GuildSettings)

    val guildID = this.id.value
    var reactionMessageId by GuildSettings.reactionMessageId
    var ticketCategory by GuildSettings.ticketCategory
    var logChannel by GuildSettings.logChannel
    var supportRole by GuildSettings.supportRole
    var prefix by GuildSettings.prefix
    var moderationRole by GuildSettings.moderationRole
}
