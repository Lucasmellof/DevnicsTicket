package wtf.lucasmellof.devnics.core.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import wtf.lucasmellof.devnics.core.appFolder
import java.io.File
import java.nio.charset.StandardCharsets
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

/* 
 * @author Lucasmellof, Lucas de Mello Freitas created on 27/02/2021
 */
val gson: Gson = GsonBuilder().setPrettyPrinting().create()

inline fun <reified T : Any> loadConfig(clazz: KClass<*>): T? {
    val ann = clazz.findAnnotation<Configurable>() ?: return null
    val config = File(appFolder, "${ann.name}.json")

    if (!config.exists()) {
        val instance = clazz.createInstance() as T
        val json = gson.toJson(instance)
        config.writeText(json, StandardCharsets.UTF_8)
        return instance
    }
    return gson.fromJson(config.readText(StandardCharsets.UTF_8), T::class.java) as T
}
