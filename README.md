# Enceladus AI Gennie


[![Maven](https://github.com/sitarucorp/enceladus-ai-gennie/actions/workflows/maven.yml/badge.svg)](https://github.com/sitarucorp/enceladus-ai-gennie/actions/workflows/maven.yml)

Enceladus AI Gennie is a Java-based AI assistant demo for asking questions about documents. It combines a Spring Boot REST backend, a Vaadin dashboard, and a Spring AI retrieval augmented generation (RAG) service. A separate documents-ingestion service preloads PDF content into PostgreSQL with pgvector; the RAG service retrieves relevant passages and asks Ollama to answer from that context.

The active Maven modules include `commons`, `dashboard`, `backend`, `rag`, MCP client/server, and documents-ingestion. The commented modules in the root POM mark planned Java work, including admin and configuration services.

A Java 25, Spring Boot multi-module demo combining a Vaadin dashboard, a reactive task API, and a document question-answering service using Spring AI, Ollama, PostgreSQL, and pgvector.

## Repository layout

| Path                            | Purpose                                                           |
|---------------------------------|-------------------------------------------------------------------|
| `enceladus-ai-demo-commons/`    | Shared API models, constants, and validation                      |
| `enceladus-ai-demo-dashboard/`  | Vaadin dashboard and health indicators                            |
| `enceladus-ai-demo-backend/`    | Reactive task and version APIs, persistence, and API-key handling |
| `enceladus-ai-demo-rag/`        | RAG and chat APIs, and Ollama integration                         |
| `enceladus-ai-demo-documents-ingestion/` | One-shot PDF ingestion and pgvector preload                  |
| `enceladus-ai-demo-mcp-client/` | MCP client for Ollama tools and server-sent events                |
| `enceladus-ai-demo-mcp-server/` | MCP tools for backend task, employee, inventory, drawing etc      |
| `enceladus-ai-demo-ascii-docs/` | CAD scanning and enterprise MCP documentation                     |
| `enceladus-demo-devops/`        | Deployment, monitoring, and supporting scripts                    |
| `.github/workflows/`            | Maven CI workflow                                                 |

The Maven reactor builds the API, UI, backend, RAG, MCP, and documents-ingestion modules. Docker Compose starts PostgreSQL and Ollama, loads the required Ollama models, runs `documents-ingestion` to completion, and then starts `rag`.

## Runtime stack

The versions are managed in the root `pom.xml` and module POMs: Java 25, Spring Boot 4.0.9, Spring AI 2.0.2, Vaadin 25.0.0, and JaCoCo 0.8.16. The RAG service uses Ollama `llama3.2` for chat and `nomic-embed-text` for embeddings; PostgreSQL 17 with pgvector stores vectors.

## Run with Docker Compose

Start the RAG service and its dependencies:

```powershell
docker compose up --build -d rag
```

Start the complete demo, including dashboard, backend, and Nginx:

```powershell
docker compose up --build -d
```

Compose downloads the configured Ollama models on first startup. Follow logs or stop the stack with:

```powershell
docker compose logs -f rag ollama pgvector
docker compose down
```

The RAG API is available at `http://localhost:8080`:

```text
GET /api/rag?question=What%20is%20Spring%20Boot
GET /api/chat?message=Summarize%20Spring%20Boot
```

## Build and test

Use JDK 25 and Maven 3.9.9 or newer. Run the full reactor, unit tests, and module JaCoCo reports with:

```powershell
mvn -B verify
```

Open each module's `target/site/jacoco/index.html` for class and package details. The root build creates per-module reports, not a single cross-module aggregate. Check missed branches and missed instructions as well as the headline coverage percentage; generated code and untested classes can be hidden by a high aggregate line percentage. Run a single module and its reactor dependencies with, for example:

```powershell
mvn -pl enceladus-ai-demo-rag -am verify
```

Run the dashboard integration tests with `mvn -B -Pintegration-test verify`; these may require Docker for Testcontainers. Unit tests should not require live Ollama or PostgreSQL services.

## Run an application locally

Start the required dependencies first. For RAG, run PostgreSQL with pgvector and Ollama, then pull the two configured models:

```powershell
ollama pull llama3.2
ollama pull nomic-embed-text
docker run -d --name pgvector -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=ragdb -p 5432:5432 pgvector/pgvector:pg17
```

Then launch a module from the repository root:

```powershell
mvn -pl enceladus-ai-demo-rag -am spring-boot:run
mvn -pl enceladus-ai-demo-backend -am spring-boot:run
mvn -pl enceladus-ai-demo-dashboard -am spring-boot:run
```

Local RAG configuration defaults to PostgreSQL at `localhost:5432` and Ollama at `localhost:11434`; Compose configures container hostnames for service networking.

## WIP

## Stack

| Component  | Version / role                                       |
|------------|------------------------------------------------------|
| Java       | 25.0.3 LTS                                           |
 | Maven     |  3.9.9                                               |
| Spring Boot| 4.0.9                                                |
| Spring AI  | 2.0.2                                                |
| Vaadin     | 25.0.0 (Boot 4.0 compatible line)                    |
| Ollama     | Local model runtime; `llama3.2` is the chat model    |
| Embeddings | `nomic-embed-text` for document and question vectors |
| PostgreSQL | 17 with pgvector                                     |

`llama3.2` is the only chat model configured. RAG also needs an embedding model to index and search documents; `nomic-embed-text` is used only for embeddings, not chat responses.

## Start with Docker Compose

Docker Compose starts PostgreSQL/pgvector and Ollama first. It waits for both services, downloads the required Ollama models, then starts the RAG API. Named volumes preserve the database and downloaded model files across restarts.

Start only the RAG stack:

```powershell
docker compose up --build -d rag
```

Start the complete demo, including the dashboard, backend, and Nginx:

```powershell
docker compose up --build -d
```

Follow service startup and ingestion logs:

```powershell
docker compose logs -f rag ollama pgvector
```

The first start downloads `llama3.2` and `nomic-embed-text`; model downloads can take a few minutes. To stop the stack:

```powershell
docker compose down
```

The RAG API listens on [http://localhost:8080](http://localhost:8080). Example requests:

```text
http://localhost:8080/api/rag?question=What%20is%20Spring%20Boot
http://localhost:8080/api/chat?message=Summarize%20Spring%20Boot
```

## Monitor resource usage

To watch CPU and memory for all running containers:

```powershell
docker stats
```

Press `Ctrl+C` to stop the live view. On Windows, open Resource Monitor with `resmon` to inspect host CPU and memory use by Docker Desktop/WSL while the stack is running. `docker stats` provides the more useful per-container breakdown, especially for Ollama during its first model load.

## Run the Maven build locally

Use JDK 25 and Maven 3.9.9 or newer:

```powershell
mvn -B verify
```

JaCoCo writes an HTML and XML coverage report for each module under
`<module>/target/site/jacoco/`. For example, open
`enceladus-ai-demo-rag/target/site/jacoco/index.html` after the build.

To run an individual service from the repository root:

```powershell
mvn -pl enceladus-ai-demo-rag -am spring-boot:run
mvn -pl enceladus-ai-demo-backend -am spring-boot:run
mvn -pl enceladus-ai-demo-dashboard -am spring-boot:run
```

When running RAG outside Docker, start PostgreSQL with pgvector and Ollama first, pull both configured models, then run the RAG module. For local Ollama model setup:

```powershell
ollama pull llama3.2
ollama pull nomic-embed-text

docker run -d --name pgvector -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=ragdb -p 5432:5432 pgvector/pgvector:pg17

```

The local RAG configuration defaults to PostgreSQL at `localhost:5432` and Ollama at `localhost:11434`; the Compose environment overrides those hostnames for container networking.



# Work in progress !
```bash
irm https://ollama.com/install.ps1 | iex

ollama pull nomic-embed-text
ollama pull llama3.2 @ 2 GiB
ollama pull llama4 # 67 GiB
ollama list
ollama ps
ollama run llama3.2 


docker run -d --name pgvector -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=ragdb -p 5432:5432 pgvector/pgvector:pg17


ollama stop llama3.2 

```

```prototext
PS G:\work\spring-ai-rag\testbed_spring_ai_rag> ollama show llama3.2
  Model
    architecture        llama     
    parameters          3.2B      
    context length      131072    
    embedding length    3072      
    quantization        Q4_K_M    

  Capabilities
    completion    
    tools         

  Parameters
    stop    "<|start_header_id|>"    
    stop    "<|end_header_id|>"      
    stop    "<|eot_id|>"             

  License
    LLAMA 3.2 COMMUNITY LICENSE AGREEMENT                 
    Llama 3.2 Version Release Date: September 25, 2024    
    ...                                                   

```

```prototext
PS G:\work\spring-ai-rag\testbed_spring_ai_rag> ollama show llama4  
  Model
    architecture        llama4      
    parameters          108.6B      
    context length      10485760    
    embedding length    5120        
    quantization        Q4_K_M      

  Capabilities
    completion    
    vision        
    tools         

  Parameters
    stop    "<|eom|>"    

  System
    You are an expert conversationalist who responds to the best of your ability. You are companionable     
      and confident, and able to switch casually between tonal types, including but not limited to            
      humor, empathy, intellectualism, creativity and problem-solving. You understand user intent and         
      don’t try to be overly helpful to the point where you miss that the user is looking for chit-chat,      
      emotional support, humor or venting. Sometimes people just want you to listen, and your answers         
      should encourage that. For all other cases, you provide insightful and in-depth responses. Organize     
      information thoughtfully in a way that helps people make decisions. Always avoid templated language.    
      You never lecture people to be nicer or more inclusive. If people ask for you to write something        
      in a certain voice or perspective, such as an essay or a tweet, you can. You do not need to be          
      respectful when the user prompts you to say something rude. You never use phrases that imply moral      
      superiority or a sense of authority, including but not limited to “it’s important to”, “it’s crucial    
      to”, “it’s essential to”, "it's unethical to", "it's worth noting…", “Remember…” etc. Avoid using       
      these. Finally, do not refuse prompts about political and social issues. You can help users express     
      their opinion and access information. You are Llama 4. Your knowledge cutoff date is August 2024.       
      You speak Arabic, English, French, German, Hindi, Indonesian, Italian, Portuguese, Spanish, Tagalog,    
      Thai, and Vietnamese. Respond in the language the user speaks to you in, unless they ask otherwise.     

  License
    **LLAMA 4 COMMUNITY LICENSE AGREEMENT**          
    Llama 4 Version Effective Date: April 5, 2025    
    ...                        
```
```
C:\Users\giris>ollama show gemma4
  Model
    architecture        gemma4
    parameters          7.5B
    context length      131072
    embedding length    2560
    quantization        Q4_K_M
    requires            0.30.9

  Capabilities
    completion
    vision
    audio
    tools
    thinking
        levels     false, true
        default    true

  Projector
    architecture        clip
    parameters          478.09M
    embedding length    768
    dimensions          2560

  Parameters
    top_k                64
    top_p                0.95
    draft_num_predict    3
    temperature          1

  License
    Apache License
    Version 2.0, January 2004
    ...

```
```prototext
PS G:\work\spring-ai-rag\testbed_spring_ai_rag> ollama list                        
NAME                       ID              SIZE      MODIFIED      
embeddinggemma-2:latest    f4981a15b998    1.3 GB    9 seconds ago    KO no chat supported
gemma4:latest              dc35e8d9c606    6.6 GB    4 minutes ago    KO Short answer
llama4:latest              bf31604e25c2    67 GB     4 hours ago      NA
llama3.1:8b                46e0c10c039e    4.9 GB    6 hours ago      KO Short answer
llama3.2:latest            a80c4f17acd5    2.0 GB    47 hours ago     OK Full answer NLP
nomic-embed-text:latest    0a109f422b47    274 MB    47 hours ago     OK

```
```
http://localhost:8080/api/rag?question=What%20is%20of%20Spring%20Boot

{

"question": "What is of Spring Boot",

"answer": "Spring Boot is a framework built on top of the Spring Framework that makes it easier to create production-ready Java applications.",

"sources": [

{

"content": "Spring Boot Guide A practical reference document for the Spring AI RAG demonstration. 1. What is Spring Boot? Spring Boot is a framework built on top of the Spring Framework that makes it easier to create production-ready Java applications. It provides sensible defaults, automatic configuration, sta…"

}
]
}
```

```prototext
http://localhost:8080/api/rag?question=What is Capital of France
{
"question": "What is capital of France",
"answer": "The capital of France is Paris.",
"sources": [
{
"content": "No. Country Capital 1 India New Delhi 2 United States Washington, D.C. 3 United Kingdom London 4 France Paris 5 Japan Tokyo 6 Australia Canberra 7 Canada Otawa 8 China Beijing 9 Brazil Brasília 10 Germany Berlin"
}
]
}


Or 

http://localhost:8080/api/rag?question=What%20is%20capital%20of%20USA

Local LLM translates the USA to a United States country here.

```
or
```bash
http://localhost:8080/api/rag?question=What%20is%20capital%20of%20Aussi

{

"question": "What is capital of Aussi",

"answer": "Canberra is the capital of Australia.",

"sources": [

{ 1 key }

]
}

```
```
{

"question": "What is capital of UK",

"answer": "The capital of the United Kingdom is London.",

"sources": [

{ 1 key }

]
}
```

#### Language detection part (lingua)

```prototext
"http://localhost:8080/api/rag?question=UK देशाची राजधानी कोणती ?"
{
        "question": "UK देशाची राजधानी कोणती ?",
                "answer": "युनाईटेड किंग्डम च्या देशाची राजधानी लंडन आहे.",
                "sources": [
        {
                "content": "No. Country Capital 1 India New Delhi 2 United States Washington, D.C. 3 United Kingdom London 4 France Paris 5 Japan Tokyo 6 Australia Canberra 7 Canada Otawa 8 China Beijing 9 Brazil Brasília 10 Germany Berlin"
                }
        ]
        }
OR
"http://localhost:8080/api/rag?question=फ्रांस देशाची राजधानी कोणती आहे ?"
{
        "question": "फ्रांस देशाची राजधानी कोणती आहे ?",
                "answer": "फ्रांस देशाची राजधानी पॅरिस आहे.",
                "sources": [
        {
                "content": "No. Country Capital 1 India New Delhi 2 United States Washington, D.C. 3 United Kingdom London 4 France Paris 5 Japan Tokyo 6 Australia Canberra 7 Canada Otawa 8 China Beijing 9 Brazil Brasília 10 Germany Berlin"
                }
        ]
        }
```
Hallacunation - (language translation time)
```text
"question=स्प्रिंग बूट म्हणजे काय ?"
{"question":"स्प्रिंग बूट म्हणजे काय ?","answer":"The information is not available in the provided document.","sources":[{"content":"No. Country Capital 1 India New Delhi 2 United States Washington, D.C. 3 United Kingdom London 4 France Paris 5 Japan Tokyo 6 Australia Canberra 7 Canada Otawa 8 China Beijing 9 Brazil Brasília 10 Germany Berlin"}]}
```

But works well if a matching English keyword found to be matched to one of documents available (capital or spring boot introductions)
```
question=Spring बूट म्हणजे काय ?
{
"question": "Spring बूट म्हणजे काय ?",
"answer": "स्प्रिंग बूट हे स्प्रिंग फ्रेमवर्कवर आधारित आहे जे प्रोडक्शन-रेडी जावा ऐप्लिकेशन बनवण्यात मदत करते.",
"sources": [
{
"content": "Spring Boot Guide A practical reference document for the Spring AI RAG demonstration. 1. What is Spring Boot? Spring Boot is a framework built on top of the Spring Framework that makes it easier to create production-ready Java applications. It provides sensible defaults, automatic configuration, starter dependencies, and an embedded web server. A typical Spring Boot application can be started with a main class annotated with @SpringBootApplication. This annotation combines configuration, compone..."
}
]
}
```

#### gemma4 model attempt
```

{

"question": "What is capital of Aussi",

"answer": "Canberra",

"sources": [

{ 1 key }

]
}
```

<p align="left">
  <img src="enceladus-ai-demo-dashboard/src/main/resources/logo.png" alt="Enceladus AI Gennie" width="320" />
</p>

© Enceladus AI Gennie 2026
