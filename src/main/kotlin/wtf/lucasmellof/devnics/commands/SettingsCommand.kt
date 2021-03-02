package wtf.lucasmellof.devnics.commands

import me.devoxin.flight.api.Context
import me.devoxin.flight.api.annotations.Command
import me.devoxin.flight.api.annotations.Greedy
import me.devoxin.flight.api.annotations.SubCommand
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
        val current = data.prefix
        return ctx.send {
            setColor(Color.decode("#8A2BE2"))
            setTitle("Guild configuration")
            setDescription("You can change any configuration using: ${ctx.trigger}config [key] [value]")
            addField("\uD83D\uDD11 Prefix **:**", "``$current``", true)
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
}
