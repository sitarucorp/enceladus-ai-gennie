# RAG service

This module provides the document question-answering API. It ingests the bundled PDF guides, creates embeddings, stores them in PostgreSQL with pgvector, retrieves relevant passages for each question, and asks Ollama to answer from those passages.

PDF startup ingestion currently runs from this module's `DataLoader` configuration. The root README tracks the planned move to a future document-ingest module.

The configured chat model is `llama3.2`; `nomic-embed-text` is used separately for embeddings.

## Run with Docker Compose

From the repository root:

```powershell
docker compose up --build -d rag
```

## API examples

```text
GET http://localhost:8080/api/rag?question=What%20is%20Spring%20Boot
GET http://localhost:8080/api/chat?message=Summarize%20Spring%20Boot
```

See the root README for local dependencies, full-stack startup, Maven verification, and JaCoCo reports.
