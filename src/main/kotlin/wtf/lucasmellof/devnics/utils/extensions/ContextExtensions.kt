package wtf.lucasmellof.devnics.utils.extensions

import me.devoxin.flight.api.Context
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.User
import wtf.lucasmellof.devnics.datastore.DatastoreUtils
import wtf.lucasmellof.devnics.datastore.dao.GuildSetting
import java.util.function.Consumer

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 28/02/2021
 */
val Context.selfMember: Member?
    get() = guild?.selfMember
val Context.member: Member?
    get() = member
val Context.user: User
    get() = member?.user!!

suspend fun Context.getGuildDataAsync(): GuildSetting = DatastoreUtils.getOrCreateGuildProfileAsync(guild!!)
fun Context.getGuildData(): GuildSetting = DatastoreUtils.getOrCreateGuildProfile(guild!!)

fun Context.send(embed: EmbedBuilder.() -> Unit, consumer: Consumer<Message> = Consumer { }) {
    messageChannel.sendMessage(EmbedBuilder().apply(embed).build()).queue(consumer)
}
fun Context.send(message: String, consumer: Consumer<Message> = Consumer { }) {
    messageChannel.sendMessage(message).queue(consumer)
}