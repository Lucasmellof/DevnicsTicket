package com.jagrosh.jdautilities.menu

import com.jagrosh.jdautilities.waiter.EventWaiter
import net.dv8tion.jda.api.entities.Message

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 20/01/2021
 */
class QuestionsBuilder(waiter: EventWaiter) : MenuBuilder<QuestionsBuilder>(waiter) {
    private val options: MutableList<Questions.Entry> = mutableListOf()

    fun addOption(option: String, action: (Message) -> Unit): QuestionsBuilder {
        options.add(Questions.Entry(option, action))
        return this
    }

    override fun build(): Questions {
        return Questions(waiter, user, title, description, color, fields, options, timeout, unit, finally)
    }
}