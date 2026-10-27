# RAG service

This module provides the document question-answering API. It ingests the bundled PDF guides, creates embeddings, stores them in PostgreSQL with pgvector, retrieves relevant passages for each question, and asks Ollama to answer from those passages.

The configured chat model is `llama3.2` only. The service also uses `nomic-embed-text` as its embedding model; document search requires vector embeddings separate from chat generation.

## Run with Docker Compose

From the repository root, run:

```powershell
docker compose up --build -d rag
```

Compose starts pgvector and Ollama, pulls `llama3.2` and `nomic-embed-text`, and then starts this service. See the root README for full-stack startup and monitoring commands.

## API examples

```text
GET http://localhost:8080/api/rag?question=What%20is%20Spring%20Boot
GET http://localhost:8080/api/chat?message=Summarize%20Spring%20Boot
```

## Run locally

Start PostgreSQL with pgvector and Ollama, then download the configured models:

```powershell
ollama pull llama3.2
ollama pull nomic-embed-text
```

Run this module and its Maven dependencies from the repository root:

```powershell
mvn -pl enceladus-ai-demo-rag -am spring-boot:run
```
