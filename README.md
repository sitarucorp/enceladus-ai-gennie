# Enceladus AI Gennie - 
### An in house air tight LLM based AI intelligence for scanning the documents and providing insights or respond to user queries 

[![Maven](https://github.com/sitarucorp/enceladus-ai-gennie/actions/workflows/maven.yml/badge.svg)](https://github.com/sitarucorp/enceladus-ai-gennie/actions/workflows/maven.yml)

#### AI Gennie Demo (Spring AI + Ollama + Vaadin + Java Microservices & Modern reactive dashboard)

## Prerequisites
| Component      | Version         |
|----------------|-----------------|
| Spring Boot    | 4.0.9           |
| Spring AI      | 2.0.1           |
| Maven          | 3.9.9           |
| Java           | 22.0.2          |
| Vaadin         | 24.7.6          |
| LLM (local)    | Ollama:0.40.4   |
| Model Default  | llma3.2:latest  |
| Model Switch   | gemma4:latest   |
| Quantization   | Q4_K_M          | 
| RAG Dimensions | COSINE topK(5)  |
| vector-search  | pgvector:pg17   | 


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