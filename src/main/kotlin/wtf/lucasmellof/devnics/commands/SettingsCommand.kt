package wtf.lucasmellof.devnics.commands

import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.annotations.Greedy
import me.devoxin.flight.api.annotations.SubCommand
import net.dv8tion.jda.api.entities.Role
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import wtf.lucasmellof.devnics.core.commands.cogs.ModCog
import wtf.lucasmellof.devnics.utils.extensions.deleteIn10
import wtf.lucasmellof.devnics.utils.extensions.getGuildData
import wtf.lucasmellof.devnics.utils.extensions.send
import java.awt.Color

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 28/02/2021
 */
class SettingsCommand : ModCog {
    @Command
    fun settings(ctx: Context) {
        val data = ctx.getGuildData()
        return ctx.send {
            setColor(Color.ORANGE)
            setTitle("Guild configuration")
            setDescription("You can change any configuration using: ${ctx.trigger}config [key] [value]")
            addField("\uD83D\uDD11 Prefix **:**", "``${data.prefix}``", true)
            addField("\uD83D\uDEC3 Moderation Role **:**", "``${data.supportRole}``", true)
        }
    }

    @SubCommand
    suspend fun prefix(ctx: Context, @Greedy value: String?) {
        val data = ctx.getGuildData()
        val current = data.prefix
        if (value == null) {
            return ctx.send("\uD83D\uDD11 My prefix is: $current") { it.deleteIn10() }
        }
        if (value.matches(Regex("\"<@!?(\\d+)>|<#(\\d+)>|<@&(\\d+)>\""))) {
            return ctx.send("\uD83D\uDE1F Whoops, you cannot define my prefix for a mention!") { it.deleteIn10() }
        }
        if (value == current) {
            return ctx.send("\uD83D\uDE1F Whoops, my prefix is already $current!") { it.deleteIn10() }
        }
        newSuspendedTransaction {
            data.prefix = value
        }
        ctx.send("✅ You have succesfully changed the guild prefix to ``$value``.") { it.deleteIn10() }
    }
    @SubCommand
    suspend fun moderation(ctx: Context, role: Role) {
        val data = ctx.getGuildData()
        newSuspendedTransaction {
            data.moderationRole = role.idLong
        }
        ctx.send("✅ You have succesfully changed the guild prefix to ``${role.id}``.") { it.deleteIn10() }
    }
}
