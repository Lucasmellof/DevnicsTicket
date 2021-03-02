package wtf.lucasmellof.devnics

import wtf.lucasmellof.devnics.core.config.instances.CoreConfig
import wtf.lucasmellof.devnics.core.config.loadConfig

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 27/02/2021
 */
object DevnicsLauncher {
    val bot = DevnicBot()
    val config = loadConfig<CoreConfig>(CoreConfig::class)!!
    @ExperimentalStdlibApi
    @JvmStatic
    fun main(args: Array<String>) {
        bot.start(config)
    }
}
