package wtf.lucasmellof.devnics.core.commands.cogs

import me.devoxin.flight.api.CommandFunction
import me.devoxin.flight.api.Context
import me.devoxin.flight.api.entities.Cog
import net.dv8tion.jda.api.Permission
import wtf.lucasmellof.devnics.utils.extensions.getGuildData

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 28/02/2021
 */
interface SupportCog : Cog {
    fun needManage() = false
    override fun name() = ":lock: | ➥ Moderation"
    override fun localCheck(ctx: Context, command: CommandFunction): Boolean {
        val data = ctx.getGuildData()
        val member = ctx.member!!
        if (ctx.commandClient.ownerIds.contains(member.idLong)) return true
        if (!member.isOwner || !member.hasPermission(Permission.MANAGE_SERVER) || !member.hasPermission(Permission.ADMINISTRATOR) || member.roles.contains(
                ctx.jda.getRoleById(data.supportRole)
            ) || member.roles.contains(
                    ctx.jda.getRoleById(data.moderationRole)
                )
        ) {
            ctx.send("\uD83D\uDE1F Whoops, You must have permission to execute this command!")
            return false
        }
        return true
    }
}
