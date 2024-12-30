	plugins {
		java
		war
		id("org.springframework.boot") version "3.4.1"
		id("io.spring.dependency-management") version "1.1.7"
	}

	group = "vn.sondev"
	version = "0.0.1-SNAPSHOT"

	java {
		toolchain {
			languageVersion = JavaLanguageVersion.of(17)
		}
	}

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-actuator")
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-starter-security")
		implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
		implementation("org.springframework.boot:spring-boot-starter-validation")
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
		implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
		developmentOnly("org.springframework.boot:spring-boot-devtools")
		runtimeOnly("com.mysql:mysql-connector-j")
		providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
		testImplementation("org.springframework.security:spring-security-test")
		testRuntimeOnly("org.junit.platform:junit-platform-launcher")
		compileOnly ("org.projectlombok:lombok:1.18.36")
		annotationProcessor ("org.projectlombok:lombok:1.18.36")
		testCompileOnly ("org.projectlombok:lombok:1.18.36")
		testAnnotationProcessor ("org.projectlombok:lombok:1.18.36")
		implementation ("org.mapstruct:mapstruct:1.6.3")
		annotationProcessor ("org.mapstruct:mapstruct-processor:1.6.3")
		compileOnly("org.hibernate:hibernate-jpamodelgen:6.6.4.Final")

	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
