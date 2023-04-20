# ナウでヤングなバックエンド言語検証用リポジトリ

雑なTODOアプリを実装して開発体験や開発効率を検証

## 実装概要
- 一覧の取得、追加、削除のみの雑なTODOアプリ
- DBは全てのアプリ共通のSQLiteデータベースを使用
- nginxとか入れてリバースプロキシするのめんどかったので雑にCORS周り設定している

## フロントエンドアプリの起動

```bash
$ cd client
$ npm install
$ npm run dev
```

## バックエンドアプリの起動

### TypeScript (ts-node + Express + Prisma)

```bash
$ cd server/typescript
$ npm install
$ npm run dev
```

### Go (Gin + GORM)

```bash
$ cd server/go
$ go mod tidy
$ go run ./main.go
```

### Python (FastAPI + SQLModel)

```bash
$ cd server/python
$ poetry install
$ poetry run uvicorn server.main:app --reload --port 8080
```

### Rust (Axum + SeaORM)

```bash
$ cd server/rust
$ cargo run # or cargo watch -x run
```

### Kotlin (Ktor + Exposed)

```bash
$ cd server/kotlin
$ ./gradlew installDist
$ ./gradlew run
```

### Swift (Vapor)

```bash
$ cd server/swift
$ swift run
```
