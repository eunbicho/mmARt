## 개발 환경

- Version Control
    - GitLab
- Agile Tool
    - Jira
- Communication
    - Mattermost
    - Webex
    - Notion
- API Documentation
    - Swagger UI
- OS
    - Windows 10
- UI/UX
    - Figma
- IDE
    - Visual Studio Code 1.75
    - Android Studio 2022.1.1
    - Intellij IDEA 2022.3.1
    - Google Colab
- DB
    - MySQL 8
    - Redis 7
    - AWS S3
- Server
    - AWS EC2
        - Ubuntu 20.04 LTS
        - Docker 23.0.4
        - Docker Compose 2.17.2
        - Jenkins 2.387.1
    - GCP GKE
        - client 1.27.1
        - server 1.24.10-gke.2300
        - helm 3.9.3
- WAS
    - Apache Tomcat 9.0.71
- AI
    - Python 3.9
    - TensorFlow 2.12.0
    - FastAPI 0.95.0
- FE
    - Unity 2021.3.22f1
    - Kotlin 1.8.20
    - Jetpack Compose 1.2.0
- BE
    - OpenJDK 11
    - Kotlin 1.8.20
    - Spring Boot Gradle(Kotlin) 2.7.11
        - Spring Data JPA
        - Spring Data Redis
        - Spring Security

## EC2

1. install Docker 23.0.4

2. install Docker Compose 2.17.2

3. git clone
    
    ```bash
    git clone https://lab.ssafy.com/s08-ai-image-sub2/S08P22A401.git
    ```
    
4. build project in dir("backend//mmart")
    
    ```bash
    ./gradlew clean build -x test
    ```

6. build docker in dir("backend/mmart")

    ```bash
    docker build -t teqteqteqteq/red-limo-backend .
    ```

7. docker-compose in dir("backend/mmart")
    
    ```bash
    docker compose up -d --build
    ```
    
## Properties

1. Spring Boot properties in dir("backend/mmart/src/main/resources")

    ```bash
    vim application.properties
    ```
    ```
    spring.datasource.username={MYSQL_USERNAME}
    spring.datasource.password={MYSQL_PASSWORD}
    spring.datasource.url=jdbc:mysql://{MYSQL_CONTAINER}:{MYSQL_PORT}/{MYSQL_DATABASE}?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.hibernate.ddl-auto=update

    spring.jpa.generate-ddl=true
    spring.jpa.show-sql=true

    spring.mvc.pathmatch.matching-strategy=ant_path_matcher

    cloud.aws.s3.bucket={S3_BUCKET_NAME}
    cloud.aws.credentials.access-key={S3_ACCESS_KEY}
    cloud.aws.credentials.secret-key={S3_SECRET_KEY}
    cloud.aws.region.static={S3_REGION}
    cloud.aws.region.auto=false
    cloud.aws.stack.auto=false

    aws-cloud.aws.s3.bucket.url= {S3_BUCKET_URL}
    spring.servlet.multipart.maxFileSize=10MB
    spring.servlet.multipart.maxRequestSize=10MB

    logging.level.com.amazonaws.util.EC2MetadataUtils= error

    spring.cache.type=redis

    spring.cache.redis.time-to-live=43200
    spring.cache.redis.cache-null-values=true
    spring.redis.host={REDIS_CONTAINER_NAME}
    spring.redis.port={REDIS_PORT}
    ```
    

## 외부 서비스 문서

### AWS S3

[클라우드 스토리지 | 웹 스토리지| Amazon Web Services](https://aws.amazon.com/ko/s3/?did=ap_card&trk=ap_card)

## DB dump
