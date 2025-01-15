#build in docker
FROM maven:3.6.3-jdk-8 as maven_build
#VOLUME "$USER_HOME_DIR/.m2"
COPY . /source
WORKDIR /source/admin
ENV TZ="Asia/Ho_Chi_Minh"
RUN mvn clean package
FROM openjdk:8
COPY --from=maven_build /source/admin/target/admin-0.0.1-SNAPSHOT.jar /usr/local/lib/myanmar-worker-admin.jar
#EXPOSE 83:8080
ENV TZ="Asia/Ho_Chi_Minh"
ENTRYPOINT ["java", "-jar", "/usr/local/lib/myanmar-worker-admin.jar"]
