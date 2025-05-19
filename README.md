# 일정 관리 애플리케이션

## 프로젝트 개요
Spring Boot와 JPA를 활용한 일정 관리 애플리케이션입니다. 사용자는 회원가입 후 로그인하여 일정을 생성, 조회, 수정, 삭제할 수 있습니다.

## 기술 스택
- Java 17
- Spring Boot 3.2.x
- Spring Data JPA
- MySQL
- Lombok
- Spring Validation

## API 명세

### 1. 일정(Schedule) API

| 기능 | Method | URL | Request                                                     | Response |
|------|--------|-----|-------------------------------------------------------------|----------|
| 일정 생성 | POST | /api/schedules | { <br/> "title": "string", <br/> "content": "string" <br/>} | {<br/> "id": number,<br/> "title": "string",<br/> "content": "string",<br/> "createdAt": "datetime",<br/> "modifiedAt": "datetime",<br/> "username": "string" <br/>} |
| 전체 일정 조회 | GET | /api/schedules | -                                                           | [{<br/> "id": number,<br/> "title": "string",<br/> "content": "string",<br/> "createdAt": "datetime",<br/> "modifiedAt": "datetime",<br/> "username": "string" <br/>}] |
| 특정 일정 조회 | GET | /api/schedules/{id} | -                                                           | {<br/> "id": number,<br/> "title": "string",<br/> "content": "string",<br/> "createdAt": "datetime",<br/> "modifiedAt": "datetime",<br/> "username": "string" <br/>} |
| 일정 수정 | PUT | /api/schedules/{id} | {<br/> "title": "string",<br/> "content": "string" <br/>}                  | {<br/> "id": number,<br/> "title": "string",<br/> "content": "string",<br/> "createdAt": "datetime",<br/> "modifiedAt": "datetime",<br/> "username": "string" <br/>} |
| 일정 삭제 | DELETE | /api/schedules/{id} | -                                                           | {<br/> "msg": "일정이 삭제되었습니다." <br/>} |
| 일정 페이징 조회 | GET | /api/schedules/paging?page={page}&size={size} | -                                                           | {<br/> "content": [...],<br/> "pageable": {...},<br/> "totalElements": number,<br/> "totalPages": number,<br/> ... <br/>} |

### 2. 사용자(User) API

| 기능 | Method | URL | Request | Response |
|------|--------|-----|---------|----------|
| 회원가입 | POST | /api/users/signup | {<br/> "username": "string",<br/> "email": "string",<br/> "password": "string" <br/>} | {<br/> "id": number,<br/> "username": "string",<br/> "email": "string",<br/> "createdAt": "datetime" <br/>} |
| 로그인 | POST | /api/users/login | {<br/> "email": "string",<br/> "password": "string" <br/>} | {<br/> "msg": "로그인 성공" <br/>} |
| 전체 사용자 조회 | GET | /api/users | - | [{<br/> "id": number,<br/> "username": "string",<br/> "email": "string",<br/> "createdAt": "datetime",<br/> "modifiedAt": "datetime" <br/>}] |
| 특정 사용자 조회 | GET | /api/users/{id} | - | {<br/> "id": number,<br/> "username": "string",<br/> "email": "string",<br/> "createdAt": "datetime",<br/> "modifiedAt": "datetime" <br/>} |
| 사용자 정보 수정 | PUT | /api/users/{id} | {<br/> "username": "string",<br/> "email": "string" <br/>} | {<br/> "id": number,<br/> "username": "string",<br/> "email": "string",<br/> "createdAt": "datetime",<br/> "modifiedAt": "datetime" <br/>} |
| 사용자 삭제 | DELETE | /api/users/{id} | - | {<br/> "msg": "사용자가 삭제되었습니다." <br/>} |

### 3. 댓글(Comment) API

| 기능 | Method | URL | Request | Response |
|------|--------|-----|---------|----------|
| 댓글 생성 | POST | /api/schedules/{scheduleId}/comments | {<br/> "content": "string" <br/>} | {<br/> "id": number,<br/> "content": "string",<br/> "username": "string",<br/> "createdAt": "datetime",<br/> "modifiedAt": "datetime" <br/>} |
| 댓글 조회 | GET | /api/schedules/{scheduleId}/comments | - | [{<br/> "id": number,<br/> "content": "string",<br/> "username": "string",<br/> "createdAt": "datetime",<br/> "modifiedAt": "datetime" <br/>}] |
| 댓글 수정 | PUT | /api/schedules/{scheduleId}/comments/{commentId} | {<br/> "content": "string" <br/>} | {<br/> "id": number,<br/> "content": "string",<br/> "username": "string",<br/> "createdAt": "datetime",<br/> "modifiedAt": "datetime" <br/>} |
| 댓글 삭제 | DELETE | /api/schedules/{scheduleId}/comments/{commentId} | - | {<br/> "msg": "댓글이 삭제되었습니다." <br/>} |

## ERD (Entity Relationship Diagram)

```
[User] 1 --- * [Schedule]
  |
  |
  * 
[Comment] * --- 1 [Schedule]
```

### User 엔터티
- id (PK)
- username
- email
- password
- createdAt
- modifiedAt

### Schedule 엔터티
- id (PK)
- title
- content
- userId (FK)
- createdAt
- modifiedAt

### Comment 엔터티
- id (PK)
- content
- userId (FK)
- scheduleId (FK)
- createdAt
- modifiedAt

## 구현 기능

### 필수 기능
- [x] Lv.1: 일정 CRUD
- [x] Lv.2: 유저 CRUD 및 연관관계 설정
- [x] Lv.3: 회원가입 기능
- [x] Lv.4: Cookie/Session 기반 로그인 기능

### 도전 기능
- [x] Lv.5: 다양한 예외처리 적용
- [x] Lv.6: 비밀번호 암호화
- [x] Lv.7: 댓글 CRUD
- [x] Lv.8: 일정 페이징 조회

## 실행 방법

1. MySQL 설치 및 데이터베이스 생성
```sql
CREATE DATABASE scheduler;
```

2. application.properties 설정
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/scheduler
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
```

3. 애플리케이션 실행
```bash
./gradlew bootRun
```

4. API 테스트
    - Postman이나 API 테스트 도구를 사용하여 API 기능 테스트