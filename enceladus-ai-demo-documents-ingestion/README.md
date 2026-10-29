# Documents ingestion service

This module loads the bundled PDF guides, creates embeddings, and stores them in PostgreSQL with pgvector. It runs as a one-shot preload job before the RAG API starts.

The configured embedding model is `nomic-embed-text`.

## Run with Docker Compose

From the repository root:

```powershell
docker compose up --build -d documents-ingestion
```

## API examples

```text
GET http://localhost:8080/api/rag?question=What%20is%20Spring%20Boot
GET http://localhost:8080/api/chat?message=Summarize%20Spring%20Boot
```

See the root README for local dependencies, full-stack startup, Maven verification, and JaCoCo reports.
