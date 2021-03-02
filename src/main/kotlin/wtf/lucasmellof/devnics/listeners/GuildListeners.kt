package wtf.lucasmellof.devnics.listeners

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import wtf.lucasmellof.devnics.datastore.DatastoreUtils
import wtf.lucasmellof.devnics.utils.TicketUtils

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 28/02/2021
 */
class GuildListeners : ListenerAdapter() {
    override fun onGuildMessageReactionAdd(event: GuildMessageReactionAddEvent) {
        val data = DatastoreUtils.getOrCreateGuildProfile(event.guild)
        if (event.user.isBot) return
        if (event.reactionEmote.asReactionCode == TicketUtils.open) {
            if (event.messageIdLong != data.reactionMessageId) return
            event.reaction.removeReaction(event.user).queue()
            val any = event.guild.textChannels.firstOrNull {
                it.name.startsWith("ticket-") && it.topic != null && it.topic!!.startsWith("Ticket| ") && it.topic!!.split(
                    "\\|"
                )[1] == event.userId
            }
            if (any != null) return
            GlobalScope.async {
                val ticketChannel = TicketUtils.createTicket(event.member)!!
                ticketChannel.sendMessage(event.member.asMention).queue()
            }
        }

        if (event.reactionEmote.asReactionCode == TicketUtils.close) {
            event.reaction.removeReaction(event.user).queue()
            event.guild.textChannels.firstOrNull {
                it.name.startsWith("ticket-") && it.topic != null && it.topic!!.startsWith("Ticket|")
            } ?: return
            GlobalScope.async {
                TicketUtils.closeTicket(
                    event.channel,
                    event.jda.getTextChannelById(data.logChannel)!!,
                    event.member,
                    event.channel.name.replace("ticket-", "")
                )
            }
        }
    }
}
