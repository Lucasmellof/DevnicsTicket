package com.jagrosh.jdautilities.menu

import com.jagrosh.jdautilities.waiter.EventWaiter
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent
import wtf.lucasmellof.devnics.core.cancelText
import java.awt.Color
import java.util.concurrent.TimeUnit

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 20/01/2021
 */
class Questions(
    waiter: EventWaiter,
    user: User?,
    title: String?,
    description: String?,
    color: Color?,
    fields: List<MessageEmbed.Field>,
    val options: List<Entry>,
    timeout: Long,
    unit: TimeUnit,
    finally: (Message?) -> Unit
) : Menu(waiter, user, title, description, color, fields, timeout, unit, finally) {

    val cancel = "\u274C"
    val accept = ":white_check_mark:"
    var message: Message? = null
    var questionMessage: Message? = null
    var current = 0

    fun display(channel: TextChannel) {
        if (!channel.guild.selfMember.hasPermission(channel, Permission.MESSAGE_ADD_REACTION)) {
            channel.sendMessage("Error: The bot requires permission to add reactions for selection menus.").queue()
            return finally(null)
        }

        channel.sendMessage(
            EmbedBuilder().apply {
                setColor(channel.guild.selfMember.color)
                setTitle(title)
                setDescription(description)
                addField("Type the answers", "Type the answers corresponding to the options", false)
                super.fields.forEach { addField(it) }
                setFooter("This question will time out in $timeout ${unit.toString().toLowerCase()}.", null)
            }.build()
        ).queue(
            {
                channel.sendMessage(options[current].name).queue { qMessage ->
                    questionMessage = qMessage
                }
                message = it
                waitFor()
            },
            {
            }
        )
    }

    private fun waitFor() {
        waiter.waitFor(GuildMessageReceivedEvent::class.java) {
            val content = it.message.contentDisplay
            if (cancelText.contains(content)) {
                return@waitFor
            }

            it.channel.retrieveMessageById(it.messageIdLong).queue(
                { message1 ->
                    options[current].action(message1)
                    message1.delete().queue()
                    nextQuestion()
                },
                {
                }
            )
        }.predicate {
            when {
                it.channel.id != message!!.channel.id -> false
                it.author.isBot -> false
                user != null && it.author != user -> false
                else -> true
            }
        }.noTimeout()
    }

    fun sendConfirmation() {
        questionMessage?.editMessage(
            EmbedBuilder().apply {
                setTitle("Is correct?")
                setDescription("Use $accept to accept or $cancel no decline")
            }.build()
        )?.queue {
            it.addReaction(cancel).queue()
            it.addReaction(accept).queue()
        }
        waiter.waitFor(GuildMessageReactionAddEvent::class.java) {
            finally(message)
        }.predicate {
            when {
                it.messageIdLong != message?.idLong -> false
                it.user.isBot -> false
                user != null && it.user != user -> {
                    if (it.guild.selfMember.hasPermission(Permission.MESSAGE_MANAGE)) {
                        it.reaction.removeReaction(it.user).queue()
                    }

                    false
                } else -> {
                    it.reaction.reactionEmote.name == cancel || it.reaction.reactionEmote.name == accept
                }
            }
        }.noTimeout()
    }

    fun nextQuestion() {
        current++
        if (current == options.size) {
            if (message != null) message!!.delete().queue()
            if (questionMessage != null) questionMessage!!.delete().queue()
            sendConfirmation()
            return
        }
        questionMessage?.editMessage(options[current].name)?.queue()
        waitFor()
    }

    data class Entry(val name: String, val action: (Message) -> Unit)
}
