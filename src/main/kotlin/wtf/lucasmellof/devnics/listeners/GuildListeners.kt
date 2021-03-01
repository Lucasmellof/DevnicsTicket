package wtf.lucasmellof.devnics.listeners

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import wtf.lucasmellof.devnics.datastore.DatastoreUtils

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 28/02/2021
 */
class GuildListeners : ListenerAdapter() {
    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
        val data = DatastoreUtils.getOrCreateGuildProfile(event.guild)
        if (event.guild.idLong != data.reactionChannelId) return
    }
}
