package ru.ac.uniyar.simplexmethodtaskssolver.storage

import android.content.Context
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.MediaStore
import android.provider.MediaStore.Files
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.os.EnvironmentCompat
import com.google.gson.GsonBuilder
import ru.ac.uniyar.simplexmethodtaskssolver.MainActivity
import java.io.*

val tasksDirectory = File(MainActivity.instance.applicationContext.filesDir, "simplexTaskss")

val gson = GsonBuilder()
    .setPrettyPrinting()
    .create()

class SimplexData(val restriction: List<List<Pair<Boolean, String>>>, val function: List<Pair<Boolean, String>>)

fun saveTask(data: SimplexData, taskName: String) {
    val gsonObj = gson.toJson(data)
    if (!tasksDirectory.exists())
        tasksDirectory.mkdir()
    val taskFile = File(tasksDirectory, "${taskName}.task.json")
    taskFile.writeText(gsonObj)
}

fun readTask(taskName: String): SimplexData {
    val res = gson.fromJson(
        FileReader(File(tasksDirectory, "${taskName}.task.json")),
        SimplexData::class.java
    )
    return res
}

fun listTasks(): List<String> =
    (tasksDirectory.listFiles()?: emptyArray()).map { it.name.removeSuffix(".task.json") }

