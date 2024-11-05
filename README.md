# KidsPing-server

- LG 유플러스 유레카 2024 - KidsPing
- 개발 기간 : 2024.10 ~ 2024.11

2024 LG 유플러스 유레카 캠프에서 진행한 아이들 나라 클론 프로젝트 KidsPing입니다!
5명의 백엔드 개발자와 한명의 안드로이드 개발자가 참여했습니다.
자세한 내용은 각 [서버](#역활-분담)와 [Docs](./docs/)를 참고해주세요.

## 로고

<img src="./resources/harmony.png" width="250px">

## 팀 소개

| **박종혁** | **서민수**| **안재진** | **이지윤** | **정시은** | **최준하** |
| :------: |  :------: | :------: | :------: | :------: | :------: |
| [<img src="https://avatars.githubusercontent.com/u/30859374?v=4" height=130 width=130> <br/> @pardessuccess](https://github.com/pardessuccess) | [<img src="https://avatars.githubusercontent.com/u/89891511?v=4" height=130 width=130> <br/> @Minsu17](https://github.com/hellokorea) | [<img src="https://avatars.githubusercontent.com/u/69111959?v=4" height=130 width=130> <br/> @acs0209](https://github.com/acs0209) | [<img src="https://avatars.githubusercontent.com/u/51826219?v=4" height=130 width=130> <br/> @howecofe](https://github.com/howecofe) | [<img src="https://avatars.githubusercontent.com/u/80161733?v=4" height=130 width=130> <br/> @Sieun53](https://github.com/Sieun53) |[<img src="https://avatars.githubusercontent.com/u/128604591?v=4" height=130 width=130> <br/> @choijh0309](https://github.com/choijh0309) |

## 기술 스택

### Frontend

- React, Javascript
- Zustand
- styled-components
- Stomp.js

### Backend

- Language
  - node.js, Nest.js
  - Java 17
  - Spring Boot 3.2.1
  - Spring MVC
  - C#
  - ASP.NET Core 16
- Library
  - WebSocket, STOMP, SockJS
  - mediasoup
  - socket.io
- Database
  - MSSQL
  - MySQL
  - Mongo DB
  - Redis
- Common
  - Jenkins(CI/CD)
  - Google Cloud Platform
  - Docker
  - NGINX
  - EFK
  - kafka

## 전체 아키텍처

![image](./resources/전체%20아키텍처.png)

## 역할 분담

### 김영현

- [커뮤니티 서버](./docs/서버-소개/커뮤니티-서버)
- [로그 서버](./docs/서버-소개/로그-서버)
- Jenkins
- NGINX
- GCP

### 최성민

- [미디어 서버](./src/backend/media-service/server)
- STUN + TURN 서버

### 안재진

- [채팅 서버](./src/backend/chat-service)
- [상태 관리 서버](./src/backend/state-service)
- [kafka](./docs/서버-소개/카프카)

### 홍지현

- [Smile Gateway](./src/backend/api-gateway/)
- [유저 서버](./src/backend/user-service/)
- NGINX

### 프론트

- 모두가 함께 만듬.
