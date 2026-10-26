# Mphasis Demo

[![Maven](https://github.com/sitarucorp/enceladus-delivery-demo/actions/workflows/maven.yml/badge.svg)](https://github.com/sitarucorp/enceladus-delivery-demo/actions/workflows/maven.yml)

## Demo Delivery Console (Java Microservices & Modern reactive dashboard)

### Prerequisite
* `Maven 3.9.9`
* `Java 21+`
* `Intellij Ultimate/Community 2025.1 Edition`
* `Git for windows`

### Spring Boot Version
* `Spring Boot 3.4.5`

#### Checkout code
```bash
git clone https://github.com/sitarucorp/mphas-demo.git
```

### Demo Problem: 

 *Let's consider a genuine but simple use case to demonstrate here*

Introduction:  As a delivery engineer, we at m-phasis have multiple clients and tenants for which we
deliver innovative solutions including artificial intelligence platform.
Doing this there are chances that the deliverables which are deployed in public cloud or on premises
infrastructures has open window to be vulnerable for cyber violations/wrong-doing.

Considering this, it is recommended to have a quick security check with endpoints exposes to 
client or public domain.
For example: CSP voilations can be tested with:

https://securityheaders.com/?q=https://enceladus.xpand.io/&followRedirects=on

https://securityheaders.com/?q=https://www.amazon.in/&followRedirects=on

Here, we see Content-Security-Policy header that translates to an csp-report end-point whenever violations occurs.
Meaning that there a mechanism in place in modern browser which reports automatically to the one who serves the webpages.
We in our demo try to include such endpoint and try to see if we can show the notifications in real time.

*Note: This is just for the demo purpose*

## Development
### Modernization approach :-

##### We approach following to cover in the demo step by step:

* Java based web frontend *reactive*
    * Java 21+ (using virtual-threads drill till tomcat executor)
    * Single Page web UI responsive design (skeleton ready)
    * Hilla typescript react framework (completely java classes)
    * Vaadin java flow server (development)
    * Webclient integration to make reactive components / notifications with backend

* Backend Microservices
    * -Login with JWT & Oauth2 / Authorization server
    * -Uses cache for suspension tier
    * R2DBC repositories with in memory H2 database for persistence (sorted repos)
    * -Kakfa (or aws kinesis ftm) for realtime streaming data

* Observability
    * OpenTelemetry (include otel javaagent and collectors)
    * Prometheus to pull spring-boot actuators metrics
    * Grafana Dashboards (JVM / Kafka / Prometheus Metrics)
    * Jaeger Dashboard (Tracing)
    * Topology (Visualization of traffic on each elements)
  
* DevOps Practices
    * Project Loom : Java Virtual Thread correct configurations (based on ulimit -u and /cat/procinfo /cat/meminfo parameters)
    * Improve Java VM options (activating class-data-sharing and other key features)
    * Docker compose stack for local development (nginx + kafka + observability)
    * Python / Go-Lang helper rest service to capture threats with devops applications
    * Hashicorp's Terraform approach for aws / gcp / azure flips
    * Old-school bash scripts to prioritize automated tasks (generate test data etc.)

* Documentation
    * Restful openapi spec (swagger-ui)
    * README Markdowns

#### Local Dev To Start Application

- [ ] coming soon

#### OpenAPI Documentation (SpringDoc - Swagger) 
##### http://localhost/swagger-ui/index.html

![API Documentation](/enceladus-ai-demo-devops/resources/openapi.png)


#### Backend version aware reactive dashboard.

![Home](/enceladus-ai-demo-devops/resources/home.png)

![Demo Dashboard](/enceladus-ai-demo-devops/resources/img_1.png)


#### API Versioning

![API Version](/enceladus-ai-demo-devops/resources/version.png)



email: girish.mahajan@enceladus.com

| *to be move to local git repository*

######  (Team Application Delivery Unit)
