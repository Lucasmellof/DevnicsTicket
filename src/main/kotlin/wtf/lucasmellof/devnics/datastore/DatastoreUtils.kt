package wtf.lucasmellof.devnics.datastore

import kotlinx.coroutines.Dispatchers
import net.dv8tion.jda.api.entities.Guild
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import wtf.lucasmellof.devnics.datastore.dao.GuildSetting

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 27/02/2021
 */
private fun _getOrCreateGuildProfile(guildId: Long): GuildSetting {
    return GuildSetting.findById(guildId) ?: GuildSetting.new(guildId) { }
}
fun getOrCreateGuildProfile(guildId: Long): GuildSetting {
    return transaction {
        _getOrCreateGuildProfile(guildId)
    }
}
fun getOrCreateGuildProfile(guild: Guild): GuildSetting = getOrCreateGuildProfile(guild.idLong)

suspend fun getOrCreateGuildProfileAsync(guildId: Long): GuildSetting {
    return newSuspendedTransaction(Dispatchers.IO) {
        _getOrCreateGuildProfile(guildId)
    }
}
suspend fun getOrCreateGuildProfileAsync(guild: Guild): GuildSetting {
    return getOrCreateGuildProfileAsync(guild.idLong)
}