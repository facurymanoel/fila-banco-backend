# 🏦 Sistema de Fila Bancária — Backend

Backend de uma aplicação de gerenciamento de filas de atendimento bancário, desenvolvido com **Java e Spring Boot**.

O sistema permite emitir senhas Normais e Preferenciais, controlar a chamada das senhas para atendimento, finalizar atendimentos e consultar a senha atualmente em atendimento.

O projeto foi desenvolvido com foco em **organização em camadas, aplicação de regras de negócio, persistência de dados, criação de uma API REST e testes unitários**, servindo também como projeto de portfólio para demonstrar conhecimentos em desenvolvimento backend com Java.

---

## 📌 Sobre o projeto

O sistema simula o funcionamento básico de uma fila de atendimento bancário.

O cliente pode solicitar uma senha:

* 🟢 **Normal**
* 🔵 **Preferencial**

Após a emissão, a senha entra no estado:

```text
AGUARDANDO
```

Quando chamada para atendimento:

```text
AGUARDANDO
      ↓
ATENDENDO
```

Após a finalização:

```text
ATENDENDO
      ↓
FINALIZADO
```

Cada senha possui um código sequencial, por exemplo:

```text
N001
N002
N003

P001
P002
P003
```

A sequência é controlada separadamente para senhas Normais e Preferenciais.

---

# 🚀 Funcionalidades

## Emissão de senha

Permite emitir uma senha informando o tipo:

* `NORMAL`
* `PREFERENCIAL`

O sistema:

1. Cria a senha;
2. Define seu tipo;
3. Gera o código sequencial;
4. Define o status como `AGUARDANDO`;
5. Registra a data e hora da criação;
6. Persiste a senha no banco;
7. Retorna os dados através de um DTO.

---

## 📢 Chamada da próxima senha

O sistema verifica se existe uma senha atualmente em atendimento.

Caso exista, uma nova senha não pode ser chamada até que o atendimento atual seja finalizado.

Quando não existe atendimento em andamento, o sistema seleciona a próxima senha seguindo uma regra de prioridade entre senhas Preferenciais e Normais.

A senha selecionada passa para:

```text
ATENDENDO
```

e recebe a data e hora de início do atendimento.

---

## 🏁 Finalização do atendimento

Permite finalizar a senha atualmente em atendimento.

O sistema altera:

```text
ATENDENDO
      ↓
FINALIZADO
```

e registra a data e hora de término do atendimento.

---

## 📺 Consulta da senha atual

O endpoint do painel permite consultar a senha que está atualmente em atendimento.

Essa funcionalidade pode ser utilizada futuramente por um frontend para exibir a senha no painel de atendimento.

---

# 🧠 Regra de prioridade

O sistema possui uma regra para evitar que as senhas Normais fiquem indefinidamente aguardando enquanto existem senhas Preferenciais.

A lógica implementada permite chamar até **duas senhas Preferenciais** antes de chamar uma senha Normal, quando houver senhas disponíveis.

Exemplo:

```text
P001
P002
N001
P003
P004
N002
```

A regra também considera a disponibilidade das senhas.

Se não houver senha Preferencial disponível, o sistema pode chamar uma Normal.

Da mesma forma, se não houver Normal disponível, uma Preferencial disponível pode ser chamada.

---

# 🏗️ Arquitetura

O backend utiliza uma arquitetura organizada em camadas:

```text
                    ┌─────────────────┐
                    │    Cliente      │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │    Controller   │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │     Service     │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │    Repository   │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    └─────────────────┘
```

### Controller

Responsável por receber as requisições HTTP e encaminhá-las para a camada de serviço.

### Service

Concentra as regras de negócio da aplicação.

### Repository

Responsável pelo acesso e persistência dos dados no banco de dados.

### Model

Representa as entidades persistidas no banco.

### DTO

Controla os dados retornados pela API, evitando a exposição direta das entidades.

### GlobalExceptionHandler

Centraliza o tratamento das exceções da aplicação.

---

# 🗂️ Estrutura do projeto

```text
src
└── main
    └── java
        └── com.devs.filabancorev
            │
            ├── controller
            │   ├── SenhaController.java
            │   └── GlobalExceptionHandler.java
            │
            ├── service
            │   └── SenhaService.java
            │
            ├── repository
            │   └── SenhaRepository.java
            │
            ├── model
            │   ├── Senha.java
            │   ├── TipoSenha.java
            │   └── StatusSenha.java
            │
            └── dto
                ├── SenhaDTO.java
                ├── ProximaSenhaDTO.java
                └── FinalizarSenhaDTO.java
```

Os testes estão organizados separadamente:

```text
src
└── test
    └── java
        └── com.devs.filabancorev
            │
            ├── service
            │   └── SenhaServiceTest.java
            │
            └── controller
                └── SenhaControllerTest.java
```

---

# 🧱 Model

A entidade principal do sistema é `Senha`.

Principais atributos:

| Campo                   | Tipo            | Descrição                   |
| ----------------------- | --------------- | --------------------------- |
| `id`                    | `Long`          | Identificador da senha      |
| `codigo`                | `String`        | Código da senha             |
| `tipo`                  | `TipoSenha`     | Tipo Normal ou Preferencial |
| `status`                | `StatusSenha`   | Estado atual da senha       |
| `dataCriacao`           | `LocalDateTime` | Data de emissão             |
| `dataInicioAtendimento` | `LocalDateTime` | Início do atendimento       |
| `dataFimAtendimento`    | `LocalDateTime` | Finalização do atendimento  |

A entidade utiliza JPA/Hibernate para realizar o mapeamento objeto-relacional.

---

# 🔢 Enums

## TipoSenha

```text
NORMAL
PREFERENCIAL
```

## StatusSenha

```text
AGUARDANDO
ATENDENDO
FINALIZADO
```

O fluxo de status é:

```text
AGUARDANDO → ATENDENDO → FINALIZADO
```

---

# 📦 DTOs

O projeto utiliza três DTOs.

### SenhaDTO

Utilizado na emissão:

```text
codigo
dataCriacao
```

### ProximaSenhaDTO

Utilizado na chamada e consulta do painel:

```text
codigo
status
dataInicioAtendimento
```

### FinalizarSenhaDTO

Utilizado na finalização:

```text
codigo
status
dataFimAtendimento
```

Essa abordagem mantém a entidade `Senha` separada dos dados expostos pela API.

---

# 🌐 API REST

Base URL:

```text
/api/senha
```

## Emitir senha

```http
POST /api/senha/{tipo}
```

### Exemplos

Senha Normal:

```http
POST /api/senha/NORMAL
```

Senha Preferencial:

```http
POST /api/senha/PREFERENCIAL
```

### Resposta

```json
{
  "codigo": "N001",
  "dataCriacao": "2026-08-21T13:30:00"
}
```

HTTP Status:

```text
201 CREATED
```

---

## Chamar próxima senha

```http
POST /api/senha/chamar
```

### Resposta

```json
{
  "codigo": "P001",
  "status": "ATENDENDO",
  "dataInicioAtendimento": "2026-08-21T13:35:00"
}
```

HTTP Status:

```text
201 CREATED
```

---

## Finalizar atendimento

```http
POST /api/senha/finalizar
```

### Resposta

```json
{
  "codigo": "P001",
  "status": "FINALIZADO",
  "dataFimAtendimento": "2026-08-21T13:45:00"
}
```

HTTP Status:

```text
201 CREATED
```

---

## Consultar painel

```http
GET /api/senha/painel
```

### Resposta

```json
{
  "codigo": "P001",
  "status": "ATENDENDO",
  "dataInicioAtendimento": "2026-08-21T13:35:00"
}
```

---

# ⚠️ Tratamento de exceções

A aplicação utiliza `@RestControllerAdvice` para centralizar o tratamento das exceções.

Exceções do tipo `RuntimeException` são convertidas para:

```text
HTTP 400 BAD REQUEST
```

Algumas situações tratadas pelo sistema:

### Atendimento já existente

```text
Finalize o atendimento atual antes de chamar uma nova senha.
```

### Nenhuma senha aguardando

```text
Não há senhas aguardando.
```

### Nenhuma senha em atendimento

```text
Não há senha em atendimento.
```

ou:

```text
Nenhuma senha em atendimento
```

---

# 🗄️ Banco de dados

O projeto utiliza:

```text
PostgreSQL
```

A entidade `Senha` é persistida na tabela:

```text
senha
```

O projeto utiliza Spring Data JPA e Hibernate para a persistência.

O Repository também possui consultas personalizadas utilizando:

* JPQL;
* SQL nativo;
* `Optional`;
* ordenação;
* filtros por status;
* filtros por tipo;
* consultas específicas para a geração dos códigos.

---

# ⚙️ Configuração

O projeto utiliza as seguintes propriedades para conexão com o PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fila_banco_rev
spring.datasource.username=postgres
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.datasource.platform=postgres
```

> Para ambientes reais, recomenda-se não manter credenciais diretamente no arquivo de configuração. Variáveis de ambiente ou mecanismos de gerenciamento de secrets devem ser utilizados.

---

# 🛠️ Tecnologias utilizadas

### Backend

* Java 21
* Spring Boot 3.5.16
* Spring Web
* Spring Data JPA
* Hibernate

### Banco de dados

* PostgreSQL

### Testes

* JUnit 5
* Mockito
* Spring Boot Test

### Ferramentas

* Maven
* Git
* GitHub
* Postman
* Spring Tool Suite

### Bibliotecas

* Lombok
* PostgreSQL JDBC Driver

---

# 🧪 Testes

O projeto possui testes unitários para as principais camadas da aplicação.

## SenhaServiceTest

Os testes validam as principais regras de negócio do sistema, incluindo:

* emissão de senha;
* geração dos códigos;
* tipos Normal e Preferencial;
* status inicial;
* chamada de senha;
* prioridade das senhas;
* bloqueio de chamada quando existe atendimento em andamento;
* finalização de senha;
* consulta da senha atual;
* tratamento de situações sem senhas disponíveis.

O `SenhaRepository` é simulado utilizando Mockito.

---

## SenhaControllerTest

Os testes validam o comportamento dos endpoints do Controller utilizando Mockito para simular o `SenhaService`.

São testadas operações relacionadas a:

* emissão;
* chamada;
* finalização;
* consulta do painel.

---

# 🔬 Testes de integração

Os testes de integração com o banco de dados não fazem parte da primeira etapa do projeto.

Eles poderão ser adicionados posteriormente para validar a integração entre:

```text
SenhaRepository
       ↓
JPA / Hibernate
       ↓
PostgreSQL
```

Essa etapa permitirá validar não apenas a regra de negócio, mas também o comportamento das consultas diretamente com um banco de teste.

---

# 📚 Documentação JavaDoc

As principais classes do backend possuem documentação JavaDoc explicando suas responsabilidades e comportamentos.

Entre elas:

* `SenhaController`
* `SenhaService`
* `SenhaRepository`
* `Senha`
* `SenhaDTO`
* `ProximaSenhaDTO`
* `FinalizarSenhaDTO`
* `TipoSenha`
* `StatusSenha`
* `GlobalExceptionHandler`
* classes de testes

A documentação foi criada com foco em explicar principalmente **responsabilidades e regras de negócio**, evitando comentários desnecessários sobre operações que já são evidentes no código.

---

# 🔮 Próximos passos

Algumas evoluções planejadas para o projeto:

* [ ] Criar testes de integração para o Repository;
* [ ] Utilizar banco de dados específico para testes;
* [ ] Criar frontend utilizando Angular;
* [ ] Criar painel visual para exibição das senhas;
* [ ] Melhorar o tratamento global de exceções;
* [ ] Criar documentação da API com Swagger/OpenAPI;
* [ ] Melhorar configurações para diferentes ambientes;
* [ ] Adicionar Docker para facilitar a execução do projeto;
* [ ] Evoluir o sistema de controle da fila.

---

# 🎯 Objetivo do projeto

Este projeto foi desenvolvido com apoio de Inteligência Artificial 
como ferramenta de aprendizado e produtividade. A IA foi utilizada para 
auxiliar na discussão de soluções, esclarecer dúvidas, revisar código, 
sugerir melhorias na organização do projeto e apoiar a documentação. 
Todo o desenvolvimento, implementação, testes, adaptações e decisões 
finais foram realizados e validados pelo autor. 
Foram usados conhecimentos em desenvolvimento backend 
com Java e Spring Boot.

Os principais conceitos aplicados incluem:

* Programação Orientada a Objetos;
* arquitetura em camadas;
* API REST;
* Spring Boot;
* Spring Data JPA;
* Hibernate;
* PostgreSQL;
* DTOs;
* Enums;
* regras de negócio;
* tratamento de exceções;
* JUnit 5;
* Mockito;
* testes unitários;
* persistência de dados;
* JavaDoc;
* Git e GitHub.

---

## 👨‍💻 Desenvolvedor

**Manoel Dalmo Facuri Filho**

Desenvolvedor Java com foco em desenvolvimento backend e interesse em oportunidades Full Stack.

GitHub

https://github.com/facurymanoel

LinkedIn

https://www.linkedin.com/in/manoel-facuri

Tecnologias de interesse:

```text
Java
Spring Boot
Spring Data JPA
REST API
Angular
PostgreSQL
SQL Server
AWS
Git/GitHub
```
 
 
 
 

 
 

 

 
 
 
 

 

   

 
 

 

 
  
