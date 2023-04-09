use crate::entity::{todo, todo::Entity as Todo};
use axum::{extract::{Extension, Path}, response::IntoResponse, Json};
use sea_orm::*;
use serde::{Deserialize, Serialize};
use std::sync::Arc;
use axum::http::StatusCode;

pub async fn index(Extension(db): Extension<Arc<DatabaseConnection>>) -> impl IntoResponse {
    let todos = Todo::find().all(db.as_ref()).await.unwrap();

    Json(todos)
}

#[derive(Deserialize)]
pub struct NewTodo {
    body: String,
}
pub async fn create(
    Extension(db): Extension<Arc<DatabaseConnection>>,
    Json(payload): Json<NewTodo>,
) -> impl IntoResponse {
    let todo = todo::ActiveModel {
        body: Set(payload.body),
        ..Default::default()
    };

    let res = todo.insert(db.as_ref()).await.unwrap();
    Json(res)
}

pub async fn delete(Extension(db): Extension<Arc<DatabaseConnection>>, Path(id): Path<i32>) -> impl IntoResponse {
    let todo = Todo::find_by_id(id).one(db.as_ref()).await.unwrap().unwrap();
    let res = todo.delete(db.as_ref()).await.unwrap();

    StatusCode::OK
}
