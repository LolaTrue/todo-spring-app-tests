package ru.otus

import com.google.gson.reflect.TypeToken
import ru.otus.dto.Task
import java.lang.reflect.Type


class TaskHttpClient : BaseHttpClient() {

    private val url = "http://localhost:8080/api/v1/tasks"

    fun createTask(task: Task): Task {
        val rs = doPostRequest(url, task)?.body?.string()
        return gson.fromJson<Task>(rs)
    }

    fun completeTask(id: Int): Boolean {
        val url = "$url/status/$id?done=true"
        return doPutRequest(url)
    }

    fun getTasks(status: Boolean): List<Task> {
        val url = "$url?done=$status"
        val rs = doGetRequest(url).body?.string()
        val taskListType: Type = object : TypeToken<ArrayList<Task?>?>() {}.type
        return gson.fromJson(rs, taskListType)
    }

    fun deleteTask(id: Int): Boolean {
        val url = "$url/$id"
        return doDeleteRequest(url)
    }

    fun uncompleteTask(id: Int): Boolean {
        val url = "$url/status/$id?done=false"
        return doPutRequest(url)
    }
}