package wtf.lucasmellof.devnics.commands

import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import wtf.lucasmellof.devnics.core.commands.cogs.SupportCog
import wtf.lucasmellof.devnics.utils.TicketUtils
import wtf.lucasmellof.devnics.utils.extensions.deleteIn10
import wtf.lucasmellof.devnics.utils.extensions.getGuildData
import wtf.lucasmellof.devnics.utils.extensions.send

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 02/03/2021
 */
class CloseCommand : SupportCog {
    @Command
    suspend fun close(ctx: Context) {
        val data = ctx.getGuildData()
        val tc = ctx.textChannel!!
        if (tc.name.startsWith("ticket-") && tc.topic!!.startsWith("Ticket|")) {
            TicketUtils.closeTicket(
                tc, ctx.jda.getTextChannelById(data.logChannel)!!, ctx.member!!,
                tc.name.replace("ticket-", "")
            )
        } else {
            ctx.send(
                {
                    setTitle("Tickets: Close")
                    setDescription("This channel isn't a ticket")
                },
                {
                    it.deleteIn10()
                }
            )
        }
    }
}
