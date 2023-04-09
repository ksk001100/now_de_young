package com.example.plugins

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*

@Serializable
data class Todo(val id: Int, val body: String)
@Serializable
data class NewTodo(val body: String)
class TodoService(private val database: Database) {
    object Todos : Table() {
        val id = integer("id").autoIncrement()
        val body = varchar("body", length = 50)

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Todos)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun all(): List<Todo> {
        return dbQuery {
            Todos.selectAll()
                .map { Todo(it[Todos.id], it[Todos.body]) }
        }
    }

    suspend fun create(todo: NewTodo): Int = dbQuery {
        Todos.insert {
            it[body] = todo.body
        }[Todos.id]
    }

    suspend fun read(id: Int): Todo? {
        return dbQuery {
            Todos.select { Todos.id eq id }
                .map { Todo(it[Todos.id], it[Todos.body]) }
                .singleOrNull()
        }
    }

    suspend fun update(id: Int, todo: Todo) {
        dbQuery {
            Todos.update({ Todos.id eq id }) {
                it[body] = todo.body
            }
        }
    }

    suspend fun delete(id: Int) {
        dbQuery {
            Todos.deleteWhere { Todos.id.eq(id) }
        }
    }
}