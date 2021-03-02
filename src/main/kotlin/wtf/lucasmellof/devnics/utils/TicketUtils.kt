package wtf.lucasmellof.devnics.utils

import kotlinx.coroutines.future.await
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.TextChannel
import wtf.lucasmellof.devnics.datastore.DatastoreUtils
import java.awt.Color

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 01/03/2021
 */
object TicketUtils {
    const val open = "✅"
    const val close = "❎"

    suspend fun createTicket(member: Member): TextChannel? {
        val guild = member.guild
        val data = DatastoreUtils.getOrCreateGuildProfile(guild)
        val category = guild.getCategoryById(data.ticketCategory)!!
        val role = guild.getRoleById(data.supportRole)

        val action = guild.createTextChannel("ticket-${member.idLong}", category).apply {
            setTopic("Ticket|${member.idLong}")
            addPermissionOverride(
                member,
                listOf(
                    Permission.MESSAGE_READ,
                    Permission.MESSAGE_WRITE,
                    Permission.MESSAGE_ATTACH_FILES,
                    Permission.MESSAGE_EMBED_LINKS
                ),
                emptyList()
            )
            addPermissionOverride(
                guild.publicRole, emptyList(),
                listOf(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE)
            )
            addPermissionOverride(
                guild.getMember(guild.jda.selfUser)!!,
                listOf(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE), emptyList()
            )
            if (role != null) {
                addPermissionOverride(
                    role,
                    listOf(
                        Permission.MESSAGE_READ,
                        Permission.MESSAGE_WRITE,
                        Permission.MESSAGE_ATTACH_FILES,
                        Permission.MESSAGE_EMBED_LINKS
                    ),
                    emptyList()
                )
            }
        }.submit().await()
        role!!.manager.setMentionable(true).submit().await()

        val embed = EmbedBuilder().apply {
            setTitle("Hello ${member.asMention}!")
            setColor(Color.ORANGE)
            setDescription("Enter the reason for the ticket!\n")
        }
        action.sendMessage(embed.build()).queue {
            it.addReaction(close).queue()
        }
        action.sendMessage(role.asMention).submit().await()
        return action
    }

    suspend fun closeTicket(channel: TextChannel, logChannel: TextChannel, member: Member, s: String) {
        val embedBuilder = EmbedBuilder().apply {
            setTitle("Ticket closed")
            val opener = channel.jda.getUserById(s)!!
            setDescription("Opened by: ${opener.name}(${opener.id})\nClosed by: ${member.effectiveName}(${member.id})")
            setColor(Color.ORANGE)
        }
        logChannel.sendMessage(embedBuilder.build()).queue()
        channel.delete().submit().await()
    }
}
