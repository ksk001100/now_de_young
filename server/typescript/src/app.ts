import express from "express";
import { PrismaClient } from "@prisma/client";
import cors from "cors";


const app = express();
app.use(express.json());
app.use(cors());

const prisma = new PrismaClient();

app.get("/todos", async (req, res) => {
  const todos = await prisma.todo.findMany();
  res.json(todos);
});

app.post("/todos", async (req, res) => {
  const { body } = req.body;
  const todo = await prisma.todo.create({ data: { body } });
  res.json(todo);
});

app.delete("/todos/:id", async (req, res) => {
  const { id } = req.params;
  const todo = await prisma.todo.delete({ where: { id: Number(id) } });
  res.json(todo);
});

export default app;
