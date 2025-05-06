To run locally on docker: 
        `docker build -t spring-app .`
        `docker run -p 8000:8000 spring-app`

        `docker-compose up --build`


docker-compose up --build -d db redis

# Wait for them to be healthy
Then run `docker-compose up --build spring-boot-backend`