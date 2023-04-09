pub mod entity;
pub mod handlers;

use axum::{extract::Extension, routing::{get, delete}, Router};
use sea_orm::*;
use std::{net::SocketAddr, sync::Arc};
use tower_http::cors::{AllowHeaders, AllowMethods, Any, CorsLayer};

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let db = Arc::new(Database::connect("sqlite://../dev.db?mode=rwc").await?);

    let cors = CorsLayer::new()
        .allow_methods(AllowMethods::any())
        .allow_headers(AllowHeaders::any())
        .allow_origin(Any);

    let app = Router::new()
        .route(
            "/todos",
            get(handlers::todos::index).post(handlers::todos::create),
        )
        .route("/todos/:id", delete(handlers::todos::delete))
        .layer(cors)
        .layer(Extension(db));

    let addr = SocketAddr::from(([127, 0, 0, 1], 8080));
    println!("listening on {}", addr);
    axum::Server::bind(&addr)
        .serve(app.into_make_service())
        .await?;

    Ok(())
}
