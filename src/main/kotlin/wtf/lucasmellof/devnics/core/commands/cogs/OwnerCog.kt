package wtf.lucasmellof.devnics.core.commands.cogs

import me.devoxin.flight.api.CommandFunction
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.entities.Cog
import wtf.lucasmellof.devnics.utils.extensions.user

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 28/02/2021
 */
interface OwnerCog : Cog {
    override fun name() = ":crystal_ball: | ➥ Owner"
    override fun localCheck(ctx: Context, command: CommandFunction): Boolean {
        val user = ctx.user
        if (!ctx.commandClient.ownerIds.contains(user.idLong)) {
            ctx.send("\uD83D\uDE1F Whoops, you aren't my owner!")
            return false
        }
        return true
    }
}
