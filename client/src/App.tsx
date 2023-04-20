import axios from 'axios';
import { useEffect, useState } from 'react';

interface Todo {
  id: number;
  body: string;
}

export default function App() {
  const [todos, setTodos] = useState<Todo[]>([]);
  const [isFetching, setIsFetching] = useState<boolean>(true);
  const [newTodo, setNewTodo] = useState<string>('');

  useEffect(() => {
    if (isFetching) {
      axios.get<Todo[]>('http://localhost:8080/todos')
        .then((response) => {
          setTodos(response.data);
          setIsFetching(false);
        })
        .catch((error) => {
          setTodos([{ id: 0, body: 'Test' }]);
          setIsFetching(false);
        });
    }
  }, [setTodos, setIsFetching, isFetching]);

  const submitTodo = () => {
    axios.post<Todo>('http://localhost:8080/todos', { body: newTodo })
      .then((response) => {
        setTodos([...todos, response.data]);
        setNewTodo('');
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const deleteTodo = (id: number) => {
    axios.delete<Todo>(`http://localhost:8080/todos/${id}`)
      .then((response) => {
        setTodos(todos.filter((todo) => todo.id !== id));
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <div style={{ width: '300px' }} >
      <div style={{ margin: '30px auto', width: 'fit-content' }}>

        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {todos.map((todo) => (
              <tr key={todo.id}>
                <td>{todo.id}</td>
                <td>{todo.body}</td>
                <td>
                  {todo.id !== 0 && (
                    <input type="button" value={'x'} onClick={() => deleteTodo(todo.id)} />
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        <div style={{ display: 'flex' }}>
          <input type="text" value={newTodo} onChange={(e) => setNewTodo(e.target.value)} />
          <input type="button" value={'Add'} onClick={submitTodo} />
        </div>
      </div>
    </div>
  );
}
