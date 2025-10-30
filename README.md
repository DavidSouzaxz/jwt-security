# API de Gestão de Usuários com Autenticação JWT

![Badge de Licença](https://img.shields.io/badge/license-MIT-blue.svg)
![Badge de Status](https://img.shields.io/badge/status-concluído-brightgreen.svg)

Uma API RESTful completa para gerenciamento de usuários, implementando autenticação e autorização seguras utilizando JSON Web Tokens (JWT) e criptografia de senhas com BCrypt.

Este projeto demonstra um back-end seguro, seguindo as melhores práticas de design de API REST, e implementa um fluxo de autenticação moderno e robusto com perfis de acesso distintos (Usuário e Administrador).

## Tabela de Conteúdos

* [Features Principais](#-features-principais)
* [Tecnologias Utilizadas](#-tecnologias-utilizadas)
* [Pré-requisitos](#-pré-requisitos)
* [Instalação e Configuração](#-instalação-e-configuração)
* [Executando a Aplicação](#-executando-a-aplicação)
* [Documentação da API (Endpoints)](#-documentação-da-api-endpoints)
* [Fluxo de Autenticação (JWT)](#-fluxo-de-autenticação-jwt)
* [Perfis de Acesso (Roles)](#-perfis-de-acesso-roles)
* [Licença](#-licença)

## ✨ Features Principais

* **CRUD de Usuários:** Funcionalidades completas de Criar, Ler, Atualizar e Deletar usuários.
* **Autenticação Segura:** Processo de Login e Registro com senhas criptografadas usando **BCrypt**.
* **Autorização com JWT:** Geração e validação de JSON Web Tokens para proteger endpoints.
* **Perfis de Acesso:** Sistema de autorização baseado em *roles* (ex: `ROLE_ADMIN`, `ROLE_USER`).
* **Documentação com Swagger:** Documentação interativa da API gerada automaticamente.

## 🛠️ Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot 3+**
* **Spring Security:** Para autenticação e autorização.
* **Spring Data JPA (Hibernate):** Para persistência de dados.
* **JWT (via biblioteca `jjwt`)**
* **BCrypt (via Spring Security)**
* ** H2 Database**
* **Maven / Gradle:** Gerenciador de dependências.
* **Springdoc (Swagger):** Para documentação da API.

## 🏁 Pré-requisitos

Antes de começar, você precisará ter instalado em sua máquina:

* [Java JDK (v17+)](https://www.oracle.com/java/technologies/downloads/) OU [Node.js (v18+)](https://nodejs.org/)
* [Maven](https://maven.apache.org/) / [Gradle](https://gradle.org/) OU [npm](https://www.npmjs.com/) / [Yarn](https://yarnpkg.com/)
* Um banco de dados (ex: PostgreSQL, MySQL ou MongoDB).
* Um cliente REST (como [Postman](https://www.postman.com/) ou [Insomnia](https://insomnia.rest/)) para testar a API.

## 🚀 Instalação e Configuração

Siga os passos abaixo para configurar e executar o projeto localmente:

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/DavidSouzaxz/jwt-security
    cd jwt-security
    ```

2.  **Instale as dependências:**

    * *(Se for Spring Boot / Maven):*
        ```bash
        mvn clean install
        ```
    * *(Se for Node.js / npm):*
        ```bash
        npm install
        ```

3.  **Configure as variáveis de ambiente:**

    * 
        Renomeie `application.properties.example` para `application.properties` (ou `application.yml`) e preencha as seguintes variáveis:
        ```properties
        # Configuração do Banco de Dados
        spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
        spring.datasource.username=seu_usuario
        spring.datasource.password=sua_senha
        
        # Configuração do JWT
        jwt.secret=SUA_CHAVE_SECRETA_MUITO_FORTE_AQUI
        jwt.expiration.ms=86400000 # 24 horas

## 🏃 Executando a Aplicação

* 
    ```bash
    # Usando Maven
    mvn spring-boot:run
    # Ou executando o .jar gerado
    java -jar target/nome-do-seu-app.jar
    ```

* 

A API estará disponível em `http://localhost:8080`.

## 📄 Documentação da API (Endpoints)

A documentação completa e interativa da API, gerada com **Swagger**, pode ser acessada no seguinte endpoint após iniciar a aplicação:

➡️ **`http://localhost:8080/swagger-ui.html`** (ou a porta e path que você configurou)

---

### Resumo dos Principais Endpoints

#### Autenticação (`/auth`)

* `POST /auth/register`
    * **Descrição:** Registra um novo usuário. A senha é criptografada com BCrypt antes de salvar.
    * **Corpo (Body):** `{ "email": "user@email.com", "password": "password123" }`
    * **Resposta:** `201 Created` - Usuário criado com sucesso.

* `POST /auth/login`
    * **Descrição:** Autentica um usuário existente e retorna um token JWT.
    * **Corpo (Body):** `{ "email": "user@email.com", "password": "password123" }`
    * **Resposta:** `200 OK` - `{ "token": "seu.token.jwt.aqui" }`

#### Usuários (`/users`)

* `GET /users/me`
    * **Descrição:** Retorna os dados do usuário atualmente autenticado.
    * **Autorização:** `Bearer <token>` (Requer `ROLE_USER` ou `ROLE_ADMIN`)

* `GET /users`
    * **Descrição:** Lista todos os usuários cadastrados.
    * **Autorização:** `Bearer <token>` (**Requer `ROLE_ADMIN`**)

* `GET /users/{id}`
    * **Descrição:** Busca um usuário específico pelo seu ID.
    * **Autorização:** `Bearer <token>` (**Requer `ROLE_ADMIN`** ou ser o próprio usuário dono da conta).

* `PUT /users/{id}`
    * **Descrição:** Atualiza os dados de um usuário (ex: email, nome).
    * **Autorização:** `Bearer <token>` (**Requer `ROLE_ADMIN`** ou ser o próprio usuário dono da conta).

* `DELETE /users/{id}`
    * **Descrição:** Deleta um usuário do sistema.
    * **Autorização:** `Bearer <token>` (**Requer `ROLE_ADMIN`**).

## 🔒 Fluxo de Autenticação (JWT)

1.  O cliente envia as credenciais (email/senha) para o endpoint `POST /auth/login`.
2.  O servidor verifica as credenciais e, se válidas, gera um token JWT assinado com a `JWT_SECRET`.
3.  O servidor retorna este token para o cliente.
4.  Para acessar qualquer rota protegida (como `GET /users/me`), o cliente deve incluir o token no cabeçalho `Authorization` de todas as requisições subsequentes:
    ```
    Authorization: Bearer <seu-token-jwt-aqui>
    ```
5.  A API valida o token (assinatura e data de expiração) e o perfil de acesso (*role*) antes de permitir o acesso ao recurso.

## 👤 Perfis de Acesso (Roles)

Este projeto implementa dois níveis de autorização:

* **`ROLE_USER`:** Perfil padrão para novos usuários. Pode visualizar e editar seus próprios dados (`/users/me`).
* **`ROLE_ADMIN`:** Perfil de administrador. Possui acesso total a todos os endpoints de gerenciamento de usuários (listar, buscar por ID, atualizar e deletar qualquer usuário).

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE.md) para mais detalhes.

---
Feito com 🍵 por David
