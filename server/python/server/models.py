from typing import List, Optional
from sqlmodel import Field, Session, SQLModel, create_engine, select

class TodoBase(SQLModel):
    __tablename__ = "todos"

    body: str = Field(index=True)

class Todo(TodoBase, table=True):
    id: Optional[int] = Field(default=None, primary_key=True)

class TodoCreate(TodoBase):
    pass

class TodoRead(TodoBase):
    id: int
