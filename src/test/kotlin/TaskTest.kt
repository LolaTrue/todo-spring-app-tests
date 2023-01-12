import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import ru.otus.TaskHttpClient
import ru.otus.dto.Priority
import ru.otus.dto.Task

class TaskTest : FeatureSpec({

    feature("A todo app") {
        val taskApi = TaskHttpClient()
        val testTask = Task(id = null, name = "test", priority = Priority.HIGH, completed = null)
        val listOfTasks = mutableListOf(
            Task(id = null, "TaskC", Priority.LOW),
            Task(id = null, "TaskB", Priority.HIGH),
            Task(id = null, "TaskA", Priority.MEDIUM)
        )

        scenario("Add task") {
            val task = taskApi.createTask(testTask)
            task.id shouldNotBe null
            task.completed shouldBe false
        }
        scenario("Complete task") {
            val task = taskApi.createTask(testTask)
            task.id?.let { taskApi.completeTask(it) }
            taskApi.getTasks(true).find { it.id == task.id }?.completed shouldBe true
        }
        scenario("Delete task") {
            val task = taskApi.createTask(testTask)
            task.id?.let { taskApi.deleteTask(it) }
            taskApi.getTasks(false).find { it.id == task.id } shouldBe null
        }
        scenario("Uncomplete task") {
            val task = taskApi.createTask(testTask)
            task.id?.let {
                taskApi.completeTask(it)
                taskApi.uncompleteTask(it)
            }
            taskApi.getTasks(false).find { it.id == task.id }?.completed shouldBe false
        }
        scenario("Sort tasks by name") {
            listOfTasks.forEach { taskApi.createTask(it) }
            val rawList = taskApi.getTasks(false)
            val sortedListOfTasks = rawList.sortedBy { it.name }
            rawList shouldBe sortedListOfTasks
        }
        scenario("Sort tasks by priority") {
            listOfTasks.forEach { taskApi.createTask(it) }
            val rawList = taskApi.getTasks(false)
            val sortedListOfTasks = rawList.sortedByDescending { it.priority }
            rawList shouldBe sortedListOfTasks
        }
        scenario("Add completed task") {
            taskApi.createTask(testTask.copy(completed = true)) shouldNotBe null
        }
        scenario("Check error delete completed task") {
            val task = taskApi.createTask(testTask)
            task.id?.let {
                taskApi.completeTask(it)
                taskApi.deleteTask(it) shouldBe true
            }
        }
        scenario("Check error complete already completed task") {
            val task = taskApi.createTask(testTask)
            task.id?.let {
                taskApi.completeTask(it)
                taskApi.completeTask(it) shouldBe true
            }
        }
        scenario("Check error uncomplete already uncompleted task") {
            val task = taskApi.createTask(testTask)
            task.id?.let { taskApi.uncompleteTask(it) shouldBe true }
        }
    }
})