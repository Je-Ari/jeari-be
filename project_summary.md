# jeari-be 프로젝트 분석 보고서

## 1. 프로젝트 개요

이 프로젝트는 **jeari-be**라는 이름의 Spring Boot 기반 백엔드 애플리케이션입니다. 파일 구조와 클래스명을 통해 유추해 볼 때, 대학교 동아리 및 소모임의 회원을 모집하고 관리하는 시스템으로 보입니다.

- **언어 및 프레임워크**: Java, Spring Boot
- **빌드 도구**: Gradle
- **주요 기술**:
    - **API**: Spring Web
    - **데이터베이스**: Spring Data JPA
    - **인증**: Spring Security with JWT (JSON Web Token)
    - **API 문서**: Swagger

## 2. 주요 기능

`controller`, `service`, `entity` 패키지의 클래스들을 기반으로 다음과 같은 핵심 기능들을 추정할 수 있습니다.

### 가. 사용자 인증 (Authentication)
- `AuthController`, `AuthService`
- JWT 토큰 기반의 사용자 로그인 (`LoginRequest`)
- 신규 사용자 회원가입 (`SignUpRequest`)

### 나. 사용자 관리 (User Management)
- `UserController`, `UserService`
- 사용자 정보 조회 및 관리

### 다. 동아리 관리 (Club Management)
- `ClubController`, `ClubService`
- 동아리 생성 (`CreateClubRequest`)
- 동아리 정보 조회 (`ClubInfoResponse`)
- 동아리 목록 요약 조회 (`ClubSummaryResponse`)

### 라. 모집 공고 관리 (Recruitment Management)
- `RecruitmentController`, `RecruitmentService`
- 모집 공고 생성 및 수정 (`RecruitmentRequest`)
- 모집 공고 목록 및 상세 정보 조회 (`RecruitmentListResponse`, `RecruitmentResponse`)
- 동아리 지원서 제출 (`ApplicationRequest`)

## 3. 프로젝트 구조

전형적인 Spring Boot의 계층형 아키텍처를 따릅니다.

- `src/main/java/com/jeari`
    - `config`: Spring Security, JWT, Swagger 등 애플리케이션의 주요 설정 클래스 위치.
    - `controller`: HTTP 요청을 받아 처리하는 API 엔드포인트.
    - `dto`: 데이터 전송 객체 (Data Transfer Objects)로, API 요청/응답에 사용.
    - `entity`: 데이터베이스 테이블과 매핑되는 JPA 엔티티.
    - `repository`: Spring Data JPA를 사용한 데이터베이스 접근 인터페이스.
    - `service`: 비즈니스 로직을 처리하는 서비스 계층.
- `src/test`: 단위 테스트 및 통합 테스트 코드 위치.

## 4. 빌드 및 테스트

- **빌드**: `build.gradle` 파일에 정의된 의존성을 바탕으로 Gradle을 통해 프로젝트를 빌드합니다. (`./gradlew build`)
- **테스트**: JUnit을 기반으로 서비스 및 컨트롤러에 대한 테스트 코드가 작성되어 있습니다. (`RecruitmentControllerTest`, `AuthServiceTest`)

## 5. 결론 및 요약

**jeari-be** 프로젝트는 동아리 관리 및 모집을 위한 핵심 기능(인증, 동아리 관리, 모집 공고)을 갖춘 잘 구조화된 Spring Boot 애플리케이션입니다. Spring Security, JPA, JWT 등 현대적인 자바 백엔드 개발 기술 스택을 사용하고 있으며, Swagger를 통해 API 문서화를 자동화하고 있습니다. 향후 게시글/댓글(`Post`, `Comment` 엔티티) 기능을 추가하여 동아리 내 커뮤니티 기능을 확장할 가능성이 있습니다.

## 6. 개선 제안 (Improvement Suggestions)

현재 코드 구조와 파일 목록을 기반으로 다음과 같은 개선점을 제안합니다. 코드의 상세 내용을 확인하면 더 정확한 분석이 가능합니다.

### 가. 설정 정보 관리 (`application.properties.copy`)
- **문제점**: `application.properties.copy` 파일은 실제 설정 파일이 아니며, 중요한 정보(DB 접속 정보, JWT 시크릿 키 등)가 포함된 `application.properties` 파일이 `.gitignore`에 의해 버전 관리에서 제외되었을 가능성이 높습니다. 이는 팀원 간의 설정 공유를 어렵게 만듭니다.
- **개선 방안**: Spring Profiles (`application-dev.properties`, `application-prod.properties` 등)를 도입하여 개발, 운영 등 환경별로 설정을 분리하는 것을 권장합니다. 민감한 정보는 환경 변수나 외부 설정 파일 서버(Spring Cloud Config)를 통해 주입하는 것이 안전합니다.

### 나. 패키지 구조 일관성
- **문제점**: `entity` 패키지 내에 `RecruitmentQuestionRequest.java` 파일이 존재합니다. 파일명으로 보아 이는 클라이언트의 요청을 받는 DTO(Data Transfer Object)일 가능성이 높습니다.
- **개선 방안**: 해당 파일의 역할을 명확히 확인하고, DTO가 맞다면 일관성을 위해 `dto` 패키지로 이동시키는 것이 좋습니다. 이는 코드의 가독성과 유지보수성을 높입니다.

### 다. 전역 예외 처리 부재
- **문제점**: 별도의 전역 예외 처리 클래스(`@RestControllerAdvice`)가 보이지 않습니다. 각 서비스나 컨트롤러에서 개별적으로 예외를 처리하고 있을 가능성이 있으며, 이는 중복 코드를 유발하고 에러 응답의 일관성을 해칠 수 있습니다.
- **개선 방안**: `exception` 패키지를 생성하고 `@RestControllerAdvice`와 `@ExceptionHandler`를 사용하여 애플리케이션 전반의 예외를 한 곳에서 처리하는 전역 예외 핸들러를 구현하는 것을 강력히 권장합니다.

### 라. 테스트 커버리지 부족
- **문제점**: `RecruitmentControllerTest`, `AuthServiceTest` 등 일부 테스트만 존재합니다. 안정적인 애플리케이션 운영을 위해서는 더 높은 테스트 커버리지가 필요합니다.
- **개선 방안**: `ClubService`, `UserService` 등 다른 비즈니스 로직과 `Repository` 계층에 대한 단위/통합 테스트를 추가하여 코드의 신뢰성을 높여야 합니다.

### 마. 데이터베이스 스키마 관리
- **문제점**: 별도의 데이터베이스 마이그레이션 도구(Flyway, Liquibase 등)가 보이지 않습니다. `spring.jpa.hibernate.ddl-auto` 옵션에만 의존하여 스키마를 관리할 경우, 운영 환경에서 데이터 손실이나 스키마 불일치 문제가 발생할 수 있습니다.
- **개선 방안**: Flyway 또는 Liquibase를 도입하여 데이터베이스 스키마 변경을 체계적으로 버전 관리하는 것을 권장합니다.