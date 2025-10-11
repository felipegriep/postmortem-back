# Postmortem Back-end 🚀

[![Java](https://img.shields.io/badge/Java-25-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue.svg)](https://gradle.org/)

Projeto Backend do sistema Postmortem desenvolvido com Spring Boot e Java 25.

## 📋 Índice

- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Como Executar](#como-executar)
- [Documentação da API](#documentação-da-api)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Banco de Dados](#banco-de-dados)
- [Testes](#testes)

## 🎯 Sobre o Projeto

Sistema backend para gerenciamento de postmortem, desenvolvido com as melhores práticas de desenvolvimento Java e Spring Boot.

## 🛠 Tecnologias Utilizadas

- **Java 25** - Linguagem de programação
- **Spring Boot 3.5.6** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Web** - API REST
- **Spring Boot Actuator** - Monitoramento e métricas
- **MySQL** - Banco de dados
- **Lombok** - Redução de código boilerplate
- **SpringDoc OpenAPI** - Documentação da API
- **JUnit 5** - Testes unitários
- **Gradle** - Gerenciador de dependências

## ✅ Pré-requisitos

Antes de começar, você precisará ter instalado em sua máquina:

- [Java JDK 25](https://openjdk.java.net/)
- [MySQL 8.0+](https://www.mysql.com/)
- [Gradle 9.x](https://gradle.org/) (opcional, o projeto inclui o Gradle Wrapper)
- [Git](https://git-scm.com/)

## 📥 Instalação e Configuração

### 1. Clone o repositório
```shell
bash git clone <url-do-repositorio> cd postmortem-back
```

### 2. Configure o banco de dados

Crie um banco de dados MySQL e execute os scripts SQL localizados em:

```
src/main/resources/scripts/01_Create_Tables.sql
```

### 3. Configure as credenciais

Edite o arquivo `src/main/resources/application.yml` com suas credenciais:

```yaml 
spring: 
  datasource: 
    url: jdbc:mysql://seu-host:3306/seu-database?useSSL=true 
    username: seu-usuario 
    password: sua-senha
```

## 🚀 Como Executar

### Usando o Gradle Wrapper (Recomendado)

**Linux/Mac:**
```shell
./gradlew bootRun
```

**Windows:**
```shell
gradlew.bat bootRun
```

### Usando Gradle instalado
```shell
gradle bootRun
```

### Compilar o projeto
```shell
./gradlew build
```

### Executar o JAR gerado
```shell
java -jar build/libs/postmortem-back-0.0.1-SNAPSHOT.jar
```
A aplicação estará disponível em: http://localhost:8080

## 📚 Documentação da API
A documentação interativa da API está disponível através do Swagger UI:
Swagger UI: http://localhost:8080/openapi-ui
OpenAPI JSON: http://localhost:8080/api-docs-custom
## 📁 Estrutura do Projeto

```
postmortem-back/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/griep/postmortem/
│   │   │       ├── api/           # Controllers REST
│   │   │       ├── config/        # Configurações
│   │   │       └── Application.java
│   │   └── resources/
│   │       ├── scripts/           # Scripts SQL
│   │       ├── static/            # Arquivos estáticos
│   │       ├── templates/         # Templates
│   │       └── application.yml    # Configurações da aplicação
│   └── test/
│       └── java/
│           └── com/griep/postmortem/  # Testes
├── build.gradle                   # Configuração do Gradle
├── gradlew                        # Gradle Wrapper (Linux/Mac)
├── gradlew.bat                    # Gradle Wrapper (Windows)
└── README.md
```

## 🗄️ Banco de Dados
O projeto utiliza MySQL como banco de dados. A configuração de conexão utiliza HikariCP para pool de conexões com as seguintes configurações:
- **Driver**: MySQL Connector/J
- **Pool de Conexões**: HikariCP
- **Porta padrão**: 3306

### Scripts SQL
Os scripts de criação de tabelas estão localizados em:
```
src/main/resources/scripts/
```

## 🧪 Testes
### Executar todos os testes
```shell
./gradlew test
```

### Executar testes com relatório detalhado
```shell
./gradlew test --info
```

Os relatórios de teste são gerados em: `build/reports/tests/test/index.html`
## 📊 Monitoramento
O Spring Boot Actuator está habilitado e fornece endpoints de monitoramento:
- **Health Check**: `http://localhost:8080/actuator/health`
- **Info**: `http://localhost:8080/actuator/info`
- **Metrics**: `http://localhost:8080/actuator/metrics`

## 🔧 Configurações Adicionais
### Níveis de Log
Os níveis de log podem ser configurados no : `application.yml`

```yaml
logging:
  level:
    root: info
    com.griep: debug
```

### Perfis de Ambiente
Você pode criar perfis diferentes (dev, prod) criando arquivos:
- `application-dev.yml`
- `application-prod.yml`

Execute com: `./gradlew bootRun --args='--spring.profiles.active=dev'`
## 👥 Autor
**Felipe Griep**
## 📄 Licença
Este projeto está sob a licença [especifique a licença].
## 📞 Suporte
Para suporte e dúvidas, entre em contato através de [adicione informações de contato].
⭐ Se este projeto foi útil para você, considere dar uma estrela!

Este README.md fornece uma documentação completa e profissional do seu projeto, incluindo instruções de instalação, execução, estrutura e recursos disponíveis. Você pode personalizá-lo conforme necessário! 🎉
