package wtf.lucasmellof.devnics.listeners

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.future.await
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
        event.reaction.removeReaction(event.user).queue()

        if (event.reactionEmote.idLong == data.reactionEmoteId && event.messageIdLong == data.reactionMessageId) {
            event.guild.textChannels.firstOrNull {
                it.name.startsWith("ticket-") && it.topic != null && it.topic!!.startsWith("Ticket| ") && it.topic!!.split(
                    "\\|"
                )[1] == event.userId
            } ?: return
            GlobalScope.async {
                val ticketChannel = TicketUtils.createTicket(event.member)!!
                ticketChannel.sendMessage(event.member.asMention).queue()
            }
        }

        if (event.reactionEmote.idLong == data.reactionEmoteId && event.messageIdLong == data.closeReactionEmoteId) {
            event.guild.textChannels.firstOrNull {
                it.name.startsWith("ticket-") && it.topic != null && it.topic!!.startsWith("Ticket| ") && it.topic!!.split(
                    "\\|"
                )[1] == event.userId
            } ?: return


            GlobalScope.async {
                // Sanity Check
                val firstMessage = event.channel.getHistoryAround(event.messageIdLong, 1).submit().await().retrievedHistory[0]
                if(firstMessage.author.id == event.jda.selfUser.id) {
                    // TODO: check if i need to post logs to somewhere
                }
            }
        }
    }
}
