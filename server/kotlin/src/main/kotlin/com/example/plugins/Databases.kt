package com.example.plugins

import org.jetbrains.exposed.sql.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureDatabases() {
    val database = Database.connect("jdbc:sqlite:../dev.db", "org.sqlite.JDBC")
    val todoService = TodoService(database)
    routing {
        get("/todos") {
            val todos = todoService.all()
            call.respond(HttpStatusCode.OK, todos)
        }

        post("/todos") {
            val newTodo = call.receive<NewTodo>()
            val id = todoService.create(newTodo)
            val todo = todoService.read(id)
            if (todo != null) {
                call.respond(HttpStatusCode.Created, todo)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/todos/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val todo = todoService.read(id)
            if (todo != null) {
                call.respond(HttpStatusCode.OK, todo)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        put("/todos/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val todo = call.receive<Todo>()
            todoService.update(id, todo)
            call.respond(HttpStatusCode.OK)
        }

        delete("/todos/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            todoService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
}
