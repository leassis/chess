#build
FROM bellsoft/liberica-openjdk-alpine-musl:17 as builder
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN ./mvnw clean install

#running
FROM bellsoft/liberica-openjdk-alpine-musl:17 as running
WORKDIR /app/

COPY --from=builder /usr/src/app/target/chess-0.0.1-SNAPSHOT.jar  /app/app.jar

RUN addgroup -S appgroup && \
    adduser -S appuser -G appgroup && \
    chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080
CMD [ "java", "-jar", "app.jar"]