package wtf.lucasmellof.devnics.utils.extensions

import net.dv8tion.jda.api.entities.Message
import java.util.concurrent.TimeUnit

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 28/02/2021
 */
fun Message.deleteInSeconds(value: Long) {
    delete().queueAfter(value, TimeUnit.SECONDS)
}
fun Message.deleteIn5() {
    deleteInSeconds(5)
}
fun Message.deleteIn10() {
    deleteInSeconds(10)
}
