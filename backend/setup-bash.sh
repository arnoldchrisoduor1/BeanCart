#!/bin/bash

# Create the project directory structure
mkdir -p src/main/java/com/yourcompany/app/{application,domain,infrastructure,interfaces,common}
mkdir -p src/main/java/com/yourcompany/app/application/{dto,service}
mkdir -p src/main/java/com/yourcompany/app/domain/{model,repository,service}
mkdir -p src/main/java/com/yourcompany/app/infrastructure/{config,persistence,security,feature,payment}
mkdir -p src/main/java/com/yourcompany/app/interfaces/rest
mkdir -p src/main/java/com/yourcompany/app/common/{exception,util}
mkdir -p src/main/resources
mkdir -p src/test/java/com/yourcompany/app/{application,domain,infrastructure,interfaces}
mkdir -p src/test/resources

# Create the main application class
cat > src/main/java/com/yourcompany/app/Application.java << 'EOF'
package com.yourcompany.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
EOF

# Create basic application.properties
cat > src/main/resources/application.properties << 'EOF'
# Server configuration
server.port=8080

# Database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/yourdb
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Redis configuration
spring.redis.host=localhost
spring.redis.port=6379

# JWT configuration
jwt.secret=your_jwt_secret_key_here
jwt.expiration=86400000

# Feature flags
feature.payment-processing=true
feature.new-user-onboarding=false

# Logging
logging.level.org.springframework=INFO
logging.level.com.yourcompany=DEBUG
EOF

# Create pom.xml with necessary dependencies
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version>
        <relativePath/>
    </parent>
    
    <groupId>com.yourcompany</groupId>
    <artifactId>app</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>app</name>
    <description>Spring Boot Monolith Application with DDD</description>
    
    <properties>
        <java.version>17</java.version>
        <jwt.version>0.11.5</jwt.version>
        <stripe.version>24.5.0</stripe.version>
        <ff4j.version>2.0.0</ff4j.version>
    </properties>
    
    <dependencies>
        <!-- Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        
        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>${jwt.version}</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>${jwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>${jwt.version}</version>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Database -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Stripe -->
        <dependency>
            <groupId>com.stripe</groupId>
            <artifactId>stripe-java</artifactId>
            <version>${stripe.version}</version>
        </dependency>
        
        <!-- Feature Flags using FF4J -->
        <dependency>
            <groupId>org.ff4j</groupId>
            <artifactId>ff4j-spring-boot-starter</artifactId>
            <version>${ff4j.version}</version>
        </dependency>
        <dependency>
            <groupId>org.ff4j</groupId>
            <artifactId>ff4j-web</artifactId>
            <version>${ff4j.version}</version>
        </dependency>
        
        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>postgresql</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

# Create a hello world controller
cat > src/main/java/com/yourcompany/app/interfaces/rest/HelloWorldController.java << 'EOF'
package com.yourcompany.app.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api")
public class HelloWorldController {

    private final FF4j ff4j;

    @Autowired
    public HelloWorldController(FF4j ff4j) {
        this.ff4j = ff4j;
        
        // Create a hello world feature flag if it doesn't exist
        if (!ff4j.exist("hello-world-feature")) {
            ff4j.createFeature(new Feature("hello-world-feature", true));
        }
    }
    
    @GetMapping("/hello")
    public ResponseEntity<String> helloWorld() {
        if (ff4j.check("hello-world-feature")) {
            return ResponseEntity.ok("Hello, World! Your Spring Boot application is running successfully!");
        } else {
            return ResponseEntity.ok("Feature is currently disabled.");
        }
    }
}
EOF

# Create feature flag configuration
cat > src/main/java/com/yourcompany/app/infrastructure/feature/FeatureFlagConfig.java << 'EOF'
package com.yourcompany.app.infrastructure.feature;

import org.ff4j.FF4j;
import org.ff4j.web.FF4jDispatcherServlet;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeatureFlagConfig {

    @Bean
    public FF4j getFF4j() {
        FF4j ff4j = new FF4j();
        
        // Define default features
        ff4j.createFeature("hello-world-feature", true);
        ff4j.createFeature("payment-processing", true);
        ff4j.createFeature("new-user-onboarding", false);
        
        return ff4j;
    }
    
    @Bean
    public ServletRegistrationBean<FF4jDispatcherServlet> ff4jDispatcherServletRegistrationBean(FF4j ff4j) {
        FF4jDispatcherServlet ff4jConsoleServlet = new FF4jDispatcherServlet();
        ff4jConsoleServlet.setFf4j(ff4j);
        return new ServletRegistrationBean<>(ff4jConsoleServlet, "/ff4j-console/*");
    }
}
EOF

# Create a simple test for the hello endpoint
cat > src/test/java/com/yourcompany/app/interfaces/HelloWorldControllerTest.java << 'EOF'
package com.yourcompany.app.interfaces;

import com.yourcompany.app.interfaces.rest.HelloWorldController;
import org.ff4j.FF4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloWorldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FF4j ff4j;

    @BeforeEach
    public void setup() {
        // Default mock behavior
        when(ff4j.check(anyString())).thenReturn(true);
    }

    @Test
    public void testHelloEndpoint() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World! Your Spring Boot application is running successfully!"));
    }

    @Test
    public void testHelloEndpointWithFeatureDisabled() throws Exception {
        // Override mock behavior for this test
        when(ff4j.check(anyString())).thenReturn(false);
        
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Feature is currently disabled."));
    }
}
EOF

# Create a Dockerfile
cat > Dockerfile << 'EOF'
FROM eclipse-temurin:17-jdk-focal as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jre-focal
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.yourcompany.app.Application"]
EOF

# Create docker-compose.yml
cat > docker-compose.yml << 'EOF'
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/yourdb
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=postgres
      - SPRING_REDIS_HOST=redis
    depends_on:
      - db
      - redis

  db:
    image: postgres:15-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=yourdb
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
    volumes:
      - postgres_data:/var/lib/postgresql/data

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data

volumes:
  postgres_data:
  redis_data:
EOF

# Create .gitignore
cat > .gitignore << 'EOF'
HELP.md
target/
!.mvn/wrapper/maven-wrapper.jar
!**/src/main/**/target/
!**/src/test/**/target/

### STS ###
.apt_generated
.classpath
.factorypath
.project
.settings
.springBeans
.sts4-cache

### IntelliJ IDEA ###
.idea
*.iws
*.iml
*.ipr

### NetBeans ###
/nbproject/private/
/nbbuild/
/dist/
/nbdist/
/.nb-gradle/
build/
!**/src/main/**/build/
!**/src/test/**/build/

### VS Code ###
.vscode/

### Docker ###
docker-compose.override.yml

### Maven ###
.mvn/
mvnw
mvnw.cmd
EOF