from typing import List
from fastapi import Depends, FastAPI, HTTPException, Query
from fastapi.middleware.cors import CORSMiddleware
from sqlmodel import Field, Session, SQLModel, create_engine, select

from server.models import Todo, TodoCreate, TodoRead
from server.database import get_session

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=['*'],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/todos", response_model=List[TodoRead])
def todos_index(*, db: Session = Depends(get_session)):
    todos = db.exec(select(Todo)).all()
    return todos

@app.post("/todos", response_model=TodoRead)
def todos_create(*, db: Session = Depends(get_session), todo: TodoCreate):
    db_todo = Todo.from_orm(todo)
    db.add(db_todo)
    db.commit()
    db.refresh(db_todo)
    return db_todo

@app.delete("/todos/{todo_id}")
def todos_delete(*, db: Session = Depends(get_session), todo_id: int):
    db_todo = db.get(Todo, todo_id)
    if not db_todo:
        raise HTTPException(status_code=404, detail="Todo not found")
    db.delete(db_todo)
    db.commit()
    return {"message": "Todo deleted"}
