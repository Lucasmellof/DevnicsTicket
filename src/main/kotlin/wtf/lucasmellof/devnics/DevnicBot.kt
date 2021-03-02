package wtf.lucasmellof.devnics

import com.jagrosh.jdautilities.waiter.EventWaiter
import me.devoxin.flight.api.CommandClient
import me.devoxin.flight.api.CommandClientBuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import wtf.lucasmellof.devnics.core.config.instances.CoreConfig
import wtf.lucasmellof.devnics.datastore.DatastoreManager
import wtf.lucasmellof.devnics.listeners.GuildListeners
import wtf.lucasmellof.devnics.utils.NewPrefixProvider
import java.util.EnumSet

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 27/02/2021
 */
@ExperimentalStdlibApi
class DevnicBot {
    lateinit var jda: ShardManager
    lateinit var commandClient: CommandClient
    lateinit var config: CoreConfig
    var eventWaiter = EventWaiter()
    fun start(config: CoreConfig) {
        this.config = config
        loadDatastore()
        loadFlight()
        loadJDA()
    }
    fun loadJDA() {
        jda = DefaultShardManagerBuilder.create(EnumSet.allOf(GatewayIntent::class.java))
            .setToken(config.token)
            .setActivity(Activity.watching("Loading..."))
            .addEventListeners(commandClient, eventWaiter, GuildListeners())
            .build()
    }
    fun loadFlight() {
        commandClient = CommandClientBuilder()
            .registerDefaultParsers()
            .setOwnerIds(config.ownerID)
            .configureDefaultHelpCommand { enabled = false }
            .setPrefixProvider(NewPrefixProvider(config))
            .build()
        commandClient.commands.register("wtf.lucasmellof.devnics.commands")
    }
    fun loadDatastore() {
        DatastoreManager(config).let {
            it.connect()
            it.createTables()
        }
    }
}