# workout-sns-api
Spring Boot REST API for Workout SNS

## 기술 스택

- Java 17
- Spring Boot 3.2.x
- MySQL 8.0
- Redis 7.2
- Docker
- JWT Authentication

## 로컬 실행

### 1. 환경변수 설정
```bash
cp .env.example .env
# .env 파일 열어서 실제 값으로 수정
```

### 2. Docker로 DB 실행
```bash
docker-compose up -d
```

### 3. Spring Boot 실행
```bash
./gradlew bootRun
```


## API 문서

추후 추가 예정

## 관련 저장소

- [프론트엔드 (React Native)](https://github.com/jayhug2/workout-sns-app)
