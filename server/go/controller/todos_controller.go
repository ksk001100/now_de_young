package todos

import (
	"server/model"

	"strconv"

	"github.com/gin-gonic/gin"
)

type NewTodoRequest struct {
	Body string `json:"body"`
}

func Index(c *gin.Context) {
	todos := model.TodosGetAll()
	c.JSON(200, todos)
}

func Create(c *gin.Context) {
	var json NewTodoRequest
	c.ShouldBindJSON(&json)
	todo := model.TodoCreate(json.Body)
	c.JSON(200, todo)
}

func Delete(c *gin.Context) {
	id, _ := strconv.Atoi(c.Param("id"))
	model.TodoDelete(id)
	c.JSON(200, gin.H{"status": "ok"})
}
