package wtf.lucasmellof.devnics.core.config.instances

import wtf.lucasmellof.devnics.core.config.Configurable

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 27/02/2021
 */
@Configurable("config")
class CoreConfig {
    val prefix = "!"
    val ownerID = ""
    val token = ""

    val datastoreUser = ""
    val datastorePassword = ""
    val datastoreDatabase = ""
    val databaseHost = "localhost"
    val databasePort = 3306
}
