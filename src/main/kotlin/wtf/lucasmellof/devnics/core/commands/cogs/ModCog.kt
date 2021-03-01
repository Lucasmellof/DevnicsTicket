package wtf.lucasmellof.devnics.core.commands.cogs

import me.devoxin.flight.api.CommandFunction
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.entities.Cog

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 28/02/2021
 */
interface ModCog : Cog {
    fun needManage() = false
    override fun name() = ":lock: | ➥ Moderation"
    override fun localCheck(ctx: Context, command: CommandFunction): Boolean {
        val member = ctx.member!!
        if (ctx.commandClient.ownerIds.contains(member.idLong)) return true
        return true
    }
}
