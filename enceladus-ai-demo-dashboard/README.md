# Mphasis Demo Dashboard

## Project : "PraanVaayu"
[![Praanvaayu](/enceladus-ai-demo-dashboard/src/main/resources/app-header.jpg)]


Simple Demo Microservices dashboard with reactive web and backend microservices 

- [ ] TODO Replace or update this README with instructions relevant to your application

### Spring Boot Version
* `Spring Boot 3.4.5`

#### Checkout code
```shell
git clone https://github.com/sitarucorp/mphas-demo-dashboard.git
```
#### Local Dev To Start Application
```bash
mvn clean package spring-boot:run
```

![Recap](/enceladus-ai-demo-devops/resources/img_1.png)]

![Recap](/enceladus-ai-demo-devops/resources/version.png)]


```bash 
$ curl -isS http://localhost:8080/howru/health
HTTP/1.1 200
Content-Type: application/vnd.spring-boot.actuator.v3+json
Transfer-Encoding: chunked
Date: Tue, 17 Jun 2025 11:18:41 GMT
{
  "status": "UP",
  "groups": [
    "liveness",
    "readiness"
  ],
  "components": {
    "dashboad": {
      "status": "UP",
      "details": {
        "nb.http.calls": 4,
        "nb.http.calls.failed": 0
      }
    },
    "db": {
      "status": "UP",
      "details": {
        "database": "H2",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 367001595904,
        "free": 87584030720,
        "threshold": 10485760,
        "path": "G:\\workspace\\enceladus-demos\\enceladus-delivery-demo\\.",
        "exists": true
      }
    },
    "livenessState": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
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

Here, this vv returning the performance of how the http wirings working with dashboard and backend. (total calls vs the failed ones) 
```bash
"dashboad": {
      "status": "UP",
      "details": {
        "nb.http.calls": 4,
        "nb.http.calls.failed": 0
      }
    }
```