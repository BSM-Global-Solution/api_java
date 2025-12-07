# 🌐 Evolvere API — Java / RESTful

![Java](https://img.shields.io/badge/Java-21&17-blue)
![Quarkus](https://img.shields.io/badge/Framework-Quarkus-red)
![Database](https://img.shields.io/badge/DB-Oracle-yellow)
![Status](https://img.shields.io/badge/Build-Running-success)

API Restful desenvolvida para o projeto **Evolvere**, uma plataforma inteligente focada em evolução profissional, conectando usuários a jornadas personalizadas com uso de Inteligência Artificial, mentores, recrutadores e oportunidades reais do mercado.


---

## 🚀 Objetivo do Projeto

A API é responsável por gerenciar todo o fluxo de autenticação, cadastro, confirmação de acesso, recuperação de senha e gerenciamento de usuários.

Ela implementa conceitos de arquitetura em camadas inspirada em **DDD**, usando **Model, DAO, BO e Resource**, garantindo separação de responsabilidades e manutenibilidade.


---

## ✨ Funcionalidades

- Cadastro de usuário
- Envio e validação de código por e-mail
- Confirmação de conta
- Recuperação de senha
- Reenvio de código
- Consulta de dados do usuário


---

## 📌 Endpoints

### 🔐 Autenticação – `/auth`

| Método | Rota | Descrição | Códigos |
|---|---|---|---|
| POST | `/auth/registro` | Criar usuário | 200, 400 |
| POST | `/auth/confirmar` | Validar código de confirmação | 200, 400, 404 |
| POST | `/auth/reenviar` | Reenvia código por e-mail | 200, 404 |


### 👤 Usuário – `/usuario`

| Método | Rota | Descrição | Códigos |
|---|---|---|---|
| GET | `/usuario/{email}` | Retorna dados do usuário | 200, 404 |


### 🔄 Recuperação de Senha – `/recuperacao`

| Método | Rota | Descrição | Códigos |
|---|---|---|---|
| POST | `/recuperacao/enviar` | Envia código de recuperação | 200, 404 |
| POST | `/recuperacao/redefinir` | Redefine senha | 200, 400, 404 |

---

## 🧩 Tecnologias

| Tipo       | Tecnologia |
|-----------|------------|
| Linguagem | Java       |
| Framework | Quarkus    |
| Banco     | MySQL      |
| Conexão   | JDBC       |
| Deploy    | Render     |

---

## 🌍 Deploy Produção

https://api-java-evolvere.onrender.com/

---

## 📎 Links

- **GitHub API:** https://github.com/BSM-Global-Solution/api_java
- **Deploy API:** https://api-java-evolvere.onrender.com
- **GitHub Projeto Evolvere:** https://github.com/BSM-Global-Solution/evolvere
- **Projeto Evolvere:** https://evolvere-web.vercel.app/

