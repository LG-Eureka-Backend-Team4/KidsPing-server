#  👶🏻키즈핑

- lg 유플러스 유레카 2024 - kidsping
- 개발 기간 : 2024.10.15 ~ 2024.11.03

<br>

2024 LG 유플러스 유레카 캠프에서 진행한 아이들 나라 클론 프로젝트 **KidsPing** 입니다!    
5명의 백엔드 개발자와 한명의 안드로이드 개발자가 참여했습니다.
자세한 내용은 각 [서버](#역활-분담)와 [Docs](./docs/)를 참고해주세요.
<br>

## 🙌 프로젝트 소개
키즈핑은 자녀의 성향(MBTI)을 분석하여 맞춤형 도서를 추천하는 **자녀 도서 추천** 플랫폼입니다. 
### 주요 기능
- 자녀 MBTI 진단
- 자녀 MBTI 기반 도서 추천 기능
- 자녀 MBTI 논리적 삭제 시 관련 데이터 논리적 삭제 ( 한달 후 물리적 삭제 ) 
- 좋아요 선호도에 따른 도서 추천
- 자녀 짝꿍이 좋아하는 도서 추천
- 장르별 도서 큐레이션
- 100명 한정 선착순 응모 시스템
<br>
<br>

## 🎞️ 시연 영상


- 키즈핑 어플 : https://drive.google.com/file/d/1ERhZH2DrIU9uxsoB-fTySdTALqdwaIgf/view 
- 관리자 페이지 : https://drive.google.com/file/d/1N2nyPiZZkwJvc8amnouBlsVoxMdYktd0/view

<br>


## 💁‍♂️ 팀 소개

| **박종혁** | **서민수**| **안재진** | **이지윤** | **정시은** | **최준하** |
| :------: |  :------: | :------: | :------: | :------: | :------: |
| [<img src="https://avatars.githubusercontent.com/u/30859374?v=4" height=130 width=130> <br/> @pardessuccess](https://github.com/pardessuccess) | [<img src="https://avatars.githubusercontent.com/u/89891511?v=4" height=130 width=130> <br/> @Minsu17](https://github.com/Minsu17) | [<img src="https://avatars.githubusercontent.com/u/69111959?v=4" height=130 width=130> <br/> @acs0209](https://github.com/acs0209) | [<img src="https://avatars.githubusercontent.com/u/51826219?v=4" height=130 width=130> <br/> @howecofe](https://github.com/howecofe) | [<img src="https://avatars.githubusercontent.com/u/80161733?v=4" height=130 width=130> <br/> @Sieun53](https://github.com/Sieun53) |[<img src="https://avatars.githubusercontent.com/u/128604591?v=4" height=130 width=130> <br/> @choijh0309](https://github.com/choijh0309) |

<br>

## 🛠 기술 스택

### Frontend
- Kotlin
- HTML/CSS/JavaScript


### Backend
- Java 17, Spring Boot 3.3.3
- Spring Batch
- Spring Data JPA
- Spring Security, JWT, Oauth
- JUnit 5, Mockito, K6, Jmeter

### Database
- MySQL
- Redis

### 협업툴
- Notion, Jira, Slack

### 클라우드
- AWS
- Amazon S3

### 인프라
- Docker

<br>
<br>



## 📚 전체 아키텍처
![image](https://github.com/user-attachments/assets/10b2981b-f725-4538-abc0-cd0b7dbc8b9f)


<br>
<br>

## ⚙️ 역할 분담

### 박종혁
- 안드로이드 프론트엔드 구현

### 서민수
- 도서 데이터 웹 스크래핑 및 AI활용
- 아이 관심사 도서 추천 서비스
- 도서 피드백 서비스
  - (피드백 -> 관심사 - 좋아요, 싫어요) 
- 아이 뱃지 기능 구현

### 안재진
- 아이 성향 진단 서비스
- 삭제 배치 처리 구현
- 도서 피드백 서비스
  - (피드백 -> 아이 성향 - 좋아요, 싫어요)
- 이벤트 응모 시스템
- Docker 기반 배포
- Amazon S3 연동하여 이미지 업로드 기능 구현

### 이지윤
- 이벤트 CRUD
- 이벤트 응모 시스템
- 삭제 배치 처리 구현
- 인증/인가 (Spring Security)

### 정시은
- 아이 프로필 CRUD
- 자녀 성향 및 히스토리 조회 구현
- 인증/인가 (Spring Security 서포트, 카카오 Oauth)
- Amazon S3 연동하여 이미지 업로드 기능 구현

### 최준하
- 도서 CRUD
- 아이 관심사 도서 추천 서비스
- 아이 짝꿍 도서 추천 서비스

<br>


## 🚨 Trouble Shooting
ex) 문제: 아이 성향 재진단시 아이 성향 초기화하며 삭제되는 쿼리가 N개의 DELETE쿼리 발생으로 인한 성능 저하
- 원인: JPA 기본 메서드를 사용, 각 엔티티를 개별적으로 조회한 후 해당 엔티티를 삭제하는 쿼리를 각각 실행.
- 해결: @Modifying을 사용하여 모든 데이터를 한번의 DELETE문으로 벌크연산으로 처리하도록 수정





