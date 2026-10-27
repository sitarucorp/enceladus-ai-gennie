# Enceladus AI Gennie

Enceladus AI Gennie is a Java-based AI assistant demo for asking questions about documents. It combines a Spring Boot REST backend, a Vaadin dashboard, and a Spring AI retrieval augmented generation (RAG) service. The RAG service ingests PDF content, stores embeddings in PostgreSQL with pgvector, retrieves relevant passages, and asks Ollama to answer from that context.

The active Maven modules are `commons`, `dashboard`, `backend`, and `rag`. The commented modules in the root POM mark planned Java work, including an MCP client and MCP server for connecting the assistant to external tools and context providers. Document-ingest and admin modules are also planned.

## Stack

| Component   | Version / role                                       |
|-------------|------------------------------------------------------|
| Java        | 25 LTS                                               |
| Spring Boot | 4.0.9                                                |
| Spring AI   | 2.0.1                                                |
| Vaadin      | 25.0.0 (Boot 4.0 compatible line)                    |
| Ollama      | Local model runtime; `llama3.2` is the chat model    |
| Embeddings  | `nomic-embed-text` for document and question vectors |
| PostgreSQL  | 17 with pgvector                                     |

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

```gemma4 version

{

"question": "What is capital of Aussi",

"answer": "Canberra",

"sources": [

{ 1 key }

]
}
```
