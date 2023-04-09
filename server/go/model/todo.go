package model

import (
	"fmt"
	"server/database"
)

type Todo struct {
	Id   int    `json:"id" gorm:"primary_key auto_increment column:id"`
	Body string `json:"body"`
}

func TodosGetAll() (todos []Todo) {
	database.Db.Find(&todos)
	return
}

func TodoCreate(body string) (todo Todo) {
	todo = Todo{Body: body}
	result := database.Db.Create(&todo)
	if result.Error != nil {
		fmt.Println(result.Error)
	}
	return
}

func TodoDelete(id int) {
	todo := Todo{Id: id}
	database.Db.Delete(&todo)
}
