package wtf.lucasmellof.devnics.datastore

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import wtf.lucasmellof.devnics.core.config.instances.CoreConfig
import wtf.lucasmellof.devnics.datastore.tables.GuildSettings

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 27/02/2021
 */
class DatastoreManager(private val config: CoreConfig) {
    fun connect() {
        val datasource = HikariDataSource(
            HikariConfig().apply {
                jdbcUrl = "jdbc:mysql://${config.databaseHost}:${config.databasePort}/${config.datastoreDatabase}"
                username = config.datastoreUser
                password = config.datastorePassword
            }
        )
        Database.connect(datasource)
    }

    fun createTables() = transaction {
        SchemaUtils.createMissingTablesAndColumns(
            GuildSettings
        )
    }
}
