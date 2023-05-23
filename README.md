# 목차

0. **[apk 다운로드 하기](#apk 다운로드 하기)**
1. **[서비스 소개](#서비스-소개)**
2. **[기획 배경](#기획-배경)**
3. **[디자인 컨셉](#디자인-컨셉)**
4. **[시연 영상](#시연-영상)**
5. **[기술 스택](#기술-스택)**
6. **[프로젝트 일정 및 산출물](#프로젝트-일정-및-산출물)**
7. **[프로젝트 폴더 구조](#프로젝트-폴더-구조)**
8. **[개발 멤버](#개발-멤버)**

---

# 🚙 apk 다운로드 하기


# 🚙 서비스 소개

[![AdventureEveryday](https://img.youtube.com/vi/RDSZdaFqegk/0.jpg)](https://www.youtube.com/watch?v=RDSZdaFqegk)




### 🔹 개요

- 한줄 소개 : 편리한 마트 장보기
- 서비스명 : mmARt

### 🔹 타겟

- 영어를 흥미롭게 접하고 싶은 아이
- 일상생활에서 영어를 자주 접하게 하고 그 과정에서 저비용, 고효율의 영어 교육을 원하는 부모님

# 🚙 기획 배경

### 🔹 배경



### 🔹 목적

- 아이들에게는
    - 실생활에서 자연스럽고 즐겁게 영어단어와 문장 학습 가능
    - 영어에 대한 흥미 유발 가능
- 학부모에게는
    - 자녀의 영어 교육 고민 완화
    - 고비용을 들이지 않고 영어 교육이 가능

### 🔹 의의

- 내 주변 사물을 직접 촬영해서 단어를 확인하는 과정을 통해 나의 주변에 있는 사물들을 영어로 친숙하게 받아들이게 하며 AR을 이용해 3D로 단어를 띄워 흥미를 유발한다. 영어에 대한 흥미로운 경험이 지속되게 하여 이후의 영어에 대한 경험들을 긍정적으로 받아들이도록 한다.
- 특정 동화에서 제시하는 그림을 퀴즈 형식으로 맞히는 경험을 통해 재미있게 단어를 익히게 하며, 사물을 완성하면 보상으로 동화를 재생해주어 성취감을 느끼게 한다.
- 영어 예문을 스스로 만들어서 듣고, 동화를 만들어서 읽어보고 들어보는 경험을 통해 정확한 영어 발음을 계속 접하게 되므로 원어민 선생님에게 영어를 배우도록 하기 위해 지출하던 많은 비용을 절약할 수 있다.

# 🚙 디자인 컨셉
![designConcept](https://user-images.githubusercontent.com/36323800/231401421-fbdd5cc1-5c56-4826-ad5b-3215fe13cd9f.PNG)


# 🚙 시연 화면 (추가예정)


### 🔹 로그인
<img src="./image/gif/register.gif" width="250" height="530" />





# 🚙 기술 스택

### [AI]

- Pytorch
- Tensorflow
- Anaconda
- FastAPI
- CLIP, CNN

### [FE]

- Flutter
- Firebase
- ARCore, ARKit
- Figma
- Visual Studio Code
- Android Studio
- Intellij

### [BE]

- Spring Boot
- Gradle(Kotlin)
- MySQL
- Spring Data JPA
- Spring Security
- JWT
- Jenkins
- Docker
- Docker Compose
- SSL
- NGINX
- AWS(EC2, S3)
- Intellij

### [ETC]

- Jira
- GitLab
- Notion
- Mattermost
- Webex

# 🚙 프로젝트 일정 및 산출물

## 프로젝트 일정

![일정표](./image/milestone.png)

## 프로젝트 진행

### 1. Git flow

---

### 브랜치 전략

- master
    - 배포 가능한 상태의 결과물
- develop
    - 구현한 기능을 병합하기 위한 브랜치
    - 통합 폴더의 기능
- feature
    - 개별 기능 구현 브랜치
    - 기능 개발 완료 시 삭제
    - 네이밍 규칙
        - ex. fe/feature/`function`
        - ex. be/feature/`function`

### 커밋 컨벤션

- 구조
    - 태그와 제목으로 구성
        - `태그: 제목`

- 태그
    - 영어로 쓰되 첫 문자는 대문자로 작성
    - 종류
        - `Feat` : 새로운 기능 추가
        - `Fix` : 버그 수정
        - `Docs` : 문서 수정
        - `Design` : css 등 사용자 UI 디자인 변경
        - `Style` : 코드 포맷팅, 세미콜론 수정 등 코드가 아닌 형식 수정
        - `Refactor` : 코드 리팩토링
        - `Test` : 테스트 코드 추가 및 리팩토링 테스트 등
        - `Chore` : 빌드 업무 수정, 패키지 매니저 수정 등
        - `Init` : 프로젝트 신규 생성, 라이브러리 설치 등
        - `Rename` : 파일명을 수정하거나 옮기는 작업
        - `Remove` : 파일을 삭제하는 작업

- 제목
    - 최대 50글자
    - 한글로 작성하며, 영어로 시작하는 경우 대문자로 시작
    - 마침표 및 특수기호는 사용하지 않음

### 2. Jira

---

### Epic

- 큰 파트 생성
- 기획 / 화면설계 / DB / 개발 / 배포 / 테스트 / 학습
    - [ ]  개발 세분화 내용 반영

### Story

- 회원에게 제공되는 서비스/기능 목록
- 네이밍 규칙
    - 명사로 마무리
    - ex. 회원가입, 로그인, 게시물 작성
- Story Point는 0

### Task

- 해당 스토리에 관련한 상세 구현 사항
- 네이밍 규칙
    - 파트는 대괄호에 작성
    - 명사로 마무리
    - ex. [BE] 유저 모델 작성
- Story Point는 4 이하로 작성
- SubTask 작성 대신 확인 가능한 Task로 세분화하여 작성

## 프로젝트 산출물

### 1. Figma

![MOCKUP](./image/figma.png)

### 2. ERD

![ERD](./image/erd.png)

### 3. API 문서

[SWAGGER](https://j8a401.p.ssafy.io/swagger-ui/index.html#/)

### 4. API 명세서

![API](./image/api.png)

# 🚙 프로젝트 폴더 구조

## AI

```bash
+---sentiment_analysis
|   +---app
|   |   \---model
|   |       \---ns_bert_1_epoch
|   |           +---assets
|   |           \---variables
|   \---local
|       \---__pycache__
\---shortest-path
    +---app
    \---local
        \---__pycache__
```

## BE

```bash
+---.gradle
|   +---7.6.1
|   |   +---checksums
|   |   +---dependencies-accessors
|   |   +---executionHistory
|   |   +---fileChanges
|   |   +---fileHashes
|   |   \---vcsMetadata
|   +---buildOutputCleanup
|   \---vcs-1
+---.idea
+---gradle
|   \---wrapper
\---src
    +---main
    |   +---kotlin
    |   |   \---com
    |   |       \---ssafy
    |   |           \---mmart
    |   |               +---config
    |   |               +---controller
    |   |               +---domain
    |   |               |   +---category
    |   |               |   |   \---dto
    |   |               |   +---favorite
    |   |               |   |   \---dto
    |   |               |   +---favoriteCategory
    |   |               |   +---getCart
    |   |               |   |   \---dto
    |   |               |   +---gotCart
    |   |               |   |   \---dto
    |   |               |   +---item
    |   |               |   |   \---dto
    |   |               |   +---itemCoupon
    |   |               |   |   \---dto
    |   |               |   +---itemDetail
    |   |               |   +---itemDetailImage
    |   |               |   |   \---dto
    |   |               |   +---itemItemCoupon
    |   |               |   +---payment
    |   |               |   |   \---dto
    |   |               |   +---paymentDetail
    |   |               |   |   \---dto
    |   |               |   +---review
    |   |               |   |   \---dto
    |   |               |   +---reviewImage
    |   |               |   \---user
    |   |               |       \---dto
    |   |               +---exception
    |   |               |   +---bad_request
    |   |               |   +---conflict
    |   |               |   +---forbidden
    |   |               |   +---internal_server_error
    |   |               |   +---not_found
    |   |               |   +---request_timeout
    |   |               |   \---unauthorized
    |   |               +---repository
    |   |               \---service
    |   \---resources
    |       \---static
    \---test
        \---kotlin
            \---com
                \---ssafy
                    \---mmart
```

## env

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
    - Elasticsearch 7.16.1
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

## install backend

1. install Docker 23.0.4

2. install Docker Compose 2.17.2

3. git clone
    
    ```bash
    git clone https://lab.ssafy.com/s08-ai-image-sub2/S08P22A401.git
    ```
    
4. build spring boot project in dir("backend//mmart")
    
    ```bash
    chmod +x gradlew
    ./gradlew clean build -x test
    ```
5. docker-compose up in dir("backend/mmart")
    
    ```bash
    docker-compose up -d --build
    ```
    
## properties

1. Spring Boot properties in dir("backend/mmart/src/main/resources")

    ```bash
    vim application.properties
    ```
    ```
    spring.datasource.username={MYSQL_USERNAME}
    spring.datasource.password={MYSQL_PASSWORD}
    spring.datasource.url=jdbc:mysql://{MYSQL_CONTAINER_NAME}:{MYSQL_PORT}/{MYSQL_DATABASE_NAME}?useSSL=false&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.jpa.hibernate.ddl-auto=update

    spring.jpa.generate-ddl=true
    spring.jpa.show-sql=true

    spring.mvc.pathmatch.matching-strategy=ant_path_matcher
    spring.thymeleaf.prefix=classpath:static
    spring.thymeleaf.check-template-location= true
    spring.thymeleaf.suffix= .html
    spring.thymeleaf.mode= HTML
    spring.thymeleaf.cache= false

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
    

## etc

### AWS S3

[클라우드 스토리지 | 웹 스토리지| Amazon Web Services](https://aws.amazon.com/ko/s3/?did=ap_card&trk=ap_card)



# 🚙 개발 멤버
![TEAM](./image/team.png)
## [AI]
- 김현호 : AI 모델 학습 및 배포, 데이터전처리
## [FE]
- 유지원 : FE, UI/UX
- 조은비 : FE, UI/UX
- 권택윤 : FE, Auth
## [BE]
- 안예나 : BE, REST API, DB, 디자인
- 김명호 : 팀장, BE, CI/CD, DB, FE(AR)

