# open jdk 17 버전의 환경 구성
FROM openjdk:17-alpine

# tzdata 패키지 설치 및 타임존 설정
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo Asia/Seoul > /etc/timezone

# build가 되는 시점에 JAR_FILE 경로에 jar파일 생성
# build/libs - gradle로 빌드했을 때 jar 파일이 생성되는 경로
ARG JAR_FILE=/build/libs/sansanmulmul-0.0.1-SNAPSHOT.jar
#ARG JAR_FILE=build/libs/*.jar

# JAR_FILE을 sanmulproject.jar로 복사
COPY ${JAR_FILE} /sanmulproject.jar

# 운영 및 개발에서 사용되는 환경 설정을 분리
# -Duser.timezone=Asia/Seoul JVM 옵션을 사용하여 애플리케이션 수준에서도 타임존을 설정
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "/sanmulproject.jar"]
# -Dspring.profiles.active=prod / docker / dev
# jar 파일 실행 명령 (여기서 -Dspring.profiles.active=prod 옵션은 application.yml을 개발 환경에서 따로 분리한 것)
