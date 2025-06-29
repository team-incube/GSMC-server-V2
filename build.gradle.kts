plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "team.incude"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven("https://jitpack.io")
}

dependencies {
	// spring-boot-starter
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-cache")

	// cache
	implementation("com.github.ben-manes.caffeine:caffeine:3.2.0")

	// docker
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")

	// lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// test
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// DB
	runtimeOnly("com.mysql:mysql-connector-j")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-redis")
	implementation("com.h2database:h2")

	// jwt
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
	implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")

	// queryDSL
	implementation("com.querydsl:querydsl-jpa:5.1.0:jakarta")
	annotationProcessor("com.querydsl:querydsl-apt:5.1.0:jakarta")

	// apache poi
	implementation("org.apache.poi:poi:5.4.0")
	implementation("org.apache.poi:poi-ooxml:5.4.0")

	// aws
	implementation("software.amazon.awssdk:s3:2.29.49")

	// jakarta
	annotationProcessor("jakarta.annotation:jakarta.annotation-api")
	annotationProcessor("jakarta.persistence:jakarta.persistence-api")

	// aop
	implementation("org.springframework.boot:spring-boot-starter-aop")

	// thymeleaf
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	// prometheus
	implementation("io.micrometer:micrometer-registry-prometheus")

	// retryable
	implementation("org.springframework.retry:spring-retry")
}

tasks.withType<Test> {
	useJUnitPlatform()
}