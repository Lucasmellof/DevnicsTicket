package wtf.lucasmellof.devnics.commands

import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.annotations.SubCommand
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.TextChannel
import org.jetbrains.exposed.sql.transactions.transaction
import wtf.lucasmellof.devnics.core.commands.cogs.ModCog
import wtf.lucasmellof.devnics.utils.TicketUtils
import wtf.lucasmellof.devnics.utils.extensions.deleteIn10
import wtf.lucasmellof.devnics.utils.extensions.getGuildData
import wtf.lucasmellof.devnics.utils.extensions.send
import java.awt.Color

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 01/03/2021
 */
class PanelCommand : ModCog {
    @Command
    fun panel(ctx: Context) {
        val data = ctx.getGuildData()
        return ctx.send {
            setColor(Color.ORANGE)
            setTitle("Ticket configuration")
            setDescription("You can change any configuration using: ${ctx.trigger}panel [key] [value]\n Use `${ctx.trigger}panel` message to generate react message")
            addField("#️⃣ Log channel **:**", "(Key: channel) → ``${data.logChannel}``", false)
            addField("\uD83D\uDEC3 Support role **:**", "(Key: support) → ``${data.supportRole}``", false)
            addField("\uD83C\uDDE8 Ticket category **:**", "(Key: category) → ``${data.ticketCategory}``", false)
            addField("\u2139 Message Id **:**", "(Key: message) → ``${data.reactionMessageId}``", false)
        }
    }

    @SubCommand
    fun category(ctx: Context, category: Long?) {
        val data = ctx.getGuildData()
        if (category == null) {
            ctx.send("Use: ${ctx.trigger}panel category <Category ID>") { it.deleteIn10() }
            return
        }
        transaction {
            data.ticketCategory = category
        }
        ctx.send("✅ You have succesfully changed the ticket category to ``${ctx.jda.getCategoryById(category)!!.name}``.") { it.deleteIn10() }
    }

    @SubCommand
    fun support(ctx: Context, role: Role) {
        val data = ctx.getGuildData()
        transaction {
            data.supportRole = role.idLong
        }
        ctx.send("✅ You have succesfully changed support role to ``${role.id}``.") { it.deleteIn10() }
    }

    @SubCommand
    fun message(ctx: Context) {
        val data = ctx.getGuildData()
        ctx.send(
            {
                setTitle("Open ticket")
                setDescription("Click the ✅ to create a ticket")
            },
            {
                it.addReaction(TicketUtils.open).queue()
                transaction {
                    data.reactionMessageId = it.idLong
                }
            }
        )
    }

    @SubCommand
    fun channel(ctx: Context, channel: TextChannel) {
        val data = ctx.getGuildData()
        transaction {
            data.logChannel = channel.idLong
        }
        ctx.send("✅ You have succesfully changed the log channel to: ${channel.asMention}") { it.deleteIn10() }
    }
}
