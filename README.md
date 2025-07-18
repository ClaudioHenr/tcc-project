# Instruções de execução do projeto
Faça o clone do projeto em sua máquina

# Frontend

## Tecnologias Utilizadas

- Node.js (v18+ recomendado)
- Angular CLI (v17+)

## Executar o projeto

Instale as dependências
```
npm install
```

Inicie o servidor 
```
ng serve
```

# Backend

## Tecnologias Utilizadas

- Java 17+
- Spring Boot 3.x
- Maven ou Gradle
- Spring Web
- Spring Data JPA
- Spring Security (se aplicável)
- PostgreSQL / MySQL / H2
- Lombok

## Configure o application.properties
Atualize as variáveis no arquivo application.properties localizado em src/main/resources/, com os dados do seu banco de dados
```
spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
```

## Executar o projeto

Compile o projeto
```
./mvnw clean install
```

Rode a aplicação
```
./mvnw spring-boot:run
```
