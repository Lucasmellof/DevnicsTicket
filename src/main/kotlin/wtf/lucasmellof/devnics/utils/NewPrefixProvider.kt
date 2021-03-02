package wtf.lucasmellof.devnics.utils

import me.devoxin.flight.api.entities.PrefixProvider
import net.dv8tion.jda.api.entities.Message
import wtf.lucasmellof.devnics.core.config.instances.CoreConfig
import wtf.lucasmellof.devnics.datastore.DatastoreUtils

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 27/02/2021
 */
class NewPrefixProvider(private val coreConfig: CoreConfig) : PrefixProvider {
    override fun provide(message: Message): List<String> {
        val data = DatastoreUtils.getOrCreateGuildProfile(message.guild)
        return listOf(
            data.prefix,
            "${message.jda.selfUser.name.toLowerCase()} ",
            "<@${message.jda.selfUser.id}> ",
            "<@!${message.jda.selfUser.id}> ",
        )
    }
}