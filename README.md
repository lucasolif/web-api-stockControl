# 🚀 AutoFlex API – Sistema de Gestão de Estoque e Produção

API REST desenvolvida em **Java Spring Boot** para gerenciamento de **matérias-primas, produtos e sugestão de produção**, com autenticação segura via **JWT** e integração com front-end Angular.

O sistema permite controlar insumos, cadastrar produtos, associar matérias-primas aos produtos e analisar o que pode ser produzido com base no estoque disponível.

---

## 📌 Tecnologias Utilizadas

### Backend

* Java 17+ / Spring Boot
* Spring Security + JWT (autenticação stateless)
* Spring Data JPA / Hibernate
* Maven
* Banco relacional (MySQL)

### Frontend

* Angular
* Angular Material
  
---

## 🔐 Segurança

A API utiliza:

* Autenticação via JWT (Bearer Token)
* Controle de acesso por roles
* Proteção de endpoints com Spring Security
* Configuração de CORS para integração com front-end

Fluxo:

1. Usuário realiza login
2. API retorna token JWT
3. Frontend envia token em cada requisição:

   ```
   Authorization: Bearer <token>
   ```

---

## ⚙️ Funcionalidades Principais

### 🧱 Matéria-prima

* Cadastro
* Atualização
* Exclusão
* Consulta e controle de estoque

### 📦 Produtos

* Cadastro de produtos
* Associação com matérias-primas (Bill of Materials)
* Atualização e exclusão
* Consulta geral

### 🏭 Sugestão de Produção

* Análise do estoque disponível
* Cálculo de quantidades possíveis de produção
* Apoio à tomada de decisão produtiva

---

## 🌐 Endpoints Principais

### Autenticação

```
POST /api/auth/login
```

### Matéria-prima

```
GET    /api/raw-material
POST   /api/raw-material
PUT    /api/raw-material/{id}
DELETE /api/raw-material/{id}
```

### Produtos

```
GET    /api/product
POST   /api/product
PUT    /api/product/{id}
DELETE /api/product/{id}
```

### Sugestão de Produção

```
GET /api/production/suggestion
```

---

## ▶️ Como Executar o Projeto

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/autoflex-api.git
```

### 3. Execute o backend

```bash
mvn spring-boot:run
```

API disponível em:

```
http://localhost:8080/autoflex
```

---

## 🧪 Testes da API

Pode usar:

* Postman
* Insomnia
* Frontend Angular integrado

Fluxo básico:

1. Login para obter token
2. Usar Bearer Token nas requisições protegidas

---

## 📂 Estrutura do Projeto

```
src/
 ├── main/
 │   ├── java/
 │   │   └── br.com.autoflex
 │   └── resources/
 │       ├── application.properties
```

---

## 🧠 Boas Práticas Aplicadas

* Arquitetura RESTful
* Separação front/back
* Externalização de secrets
* Segurança stateless
* Clean code e organização modular

---

## 👨‍💻 Autor

**Lucas Oliviera de Farias**

Projeto desenvolvido para estudo, prática profissional e avaliação técnica em desenvolvimento full-stack com Java + Angular.

---

## 📜 Licença

Uso educacional e demonstrativo.
