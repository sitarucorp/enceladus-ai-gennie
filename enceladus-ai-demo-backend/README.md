Health check
```bash
$ curl -i localhost/howru/health
HTTP/1.1 200 
Content-Type: application/vnd.spring-boot.actuator.v3+json
Transfer-Encoding: chunked
Date: Sun, 15 Jun 2025 11:28:52 GMT
{
  "status": "UP",
  "groups": [
    "liveness",
    "readiness"
  ],
  "components": {
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 367001595904,
        "free": 87903051776,
        "threshold": 10485760,
        "path": "G:\\workspace\\enceladus\\enceladus-demo\\.",
        "exists": true
      }
    },
    "livenessState": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    },
    "r2dbc": {
      "status": "UP",
      "details": {
        "database": "H2",
        "validationQuery": "validate(REMOTE)"
      }
    },
    "readinessState": {
      "status": "UP"
    },
    "ssl": {
      "status": "UP",
      "details": {
        "validChains": [],
        "invalidChains": []
      }
    }
  }
}
```
Pods poking endpoints.

```bash
$ curl localhost/howru/health/liveness
{"status":"UP"}

$ curl localhost/howru/health/readiness
{"status":"UP"}
```

Get Version ("v1" is the Path API version here)
```bash
$ curl -i -X 'GET'   'http://localhost/api/version'   -H 'accept: application/json'   -H 'x-api-key: Z2lyaXNoLm1haGFqYW5AbXBoYXNpcy5jb20K'
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Mon, 16 Jun 2025 12:06:08 GMT
{
"data": {
    "name": "enceladus-ai-demo-backend",
    "group": "com.enceladus.delivery",
    "artifact": "enceladus-ai-demo-backend",
    "version": "0.0.1-SNAPSHOT",
    "apiPathVersion": "v1",
    "time": "2025-06-16T10:59:07.827Z"
},
"status": "SUCCESS",
"message": "Version retrieved successfully."
}
```

List Tasks (Using reactive R2DBC repositories with desc sort)
```bash
$ curl -i -X 'POST' \
  'http://localhost/api/v1/reactive/task/lists?size=1&page=1' \
  -H 'accept: application/json' \
  -H 'x-api-key: Z2lyaXNoLm1haGFqYW5AbXBoYXNpcy5jb20K' \
  -H 'Content-Type: application/json' \
  -d '{
  "payload": "string"
}'
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Tue, 17 Jun 2025 05:47:15 GMT
{
  "data": [
    {
      "id": 100,
      "description": "Task 100 description",
      "creationDate": "2025-06-17T05:43:26.200820Z",
      "dueDate": "2025-09-25"
    }
  ],
  "status": "SUCCESS",
  "message": "Tasks retrieved successfully."
}
```

Create Task:
```bash
curl -X 'POST' \
  'http://localhost/api/v1/reactive/task/create' \
  -H 'accept: application/json' \
  -H 'x-api-key: Z2lyaXNoLm1haGFqYW5AbXBoYXNpcy5jb20K' \
  -H 'Content-Type: application/json' \
  -d '{
  "payload": {
    "description": "this is my developer test",
    "creationDate": "2025-06-17T05:48:25.510Z",
    "dueDate": "2025-06-30"
  }
}'
{
  "data": {
    "id": 101,
    "description": "this is my developer test",
    "creationDate": "2025-06-17T05:48:25.510Z",
    "dueDate": "2025-06-30"
  },
  "status": "SUCCESS",
  "message": "Created"
}
```

#### Tomcat is running with virtual thread pool executor

```bash
2025-06-18 15:18:05 2025-06-18T09:48:05.049Z  INFO 116 --- [     virtual-78] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
```



| Repository describes below practices                                                                                                                                 |
|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Modernization approach towards a multi module java application, eg. Trends like docker / k8s, observability, real world tests, business use case (a dashboard) etc.  |                                
| Maven architecture to avoid dependency convergence systematically                                                                                                    |
| Reactive relational database repositories to avoid blocking during read/write                                                                                        |
| Java JVM optimization for G1GC, CDS, Metaspace tuning, tired compilation, boost in performance due to use of virtual threads etc.                                    |
| Open Telemetry optimizations                                                                                                                                         |
| Documentation approach (Learning curves)                                                                                                                             |
| Applications versioning practices for chaos engineering (artifact level + api level)                                                                                 |

girish.mahajan@enceladus.com