# 📚 BiblioTech - Sistema de Gestão de Biblioteca

[![CI/CD Pipeline](https://github.com/DCF-2/BiblioTech/actions/workflows/main.yml/badge.svg)](https://github.com/DCF-2/BiblioTech/actions)
[![Java 21](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.2-brightgreen.svg)](https://spring.io/projects/spring-boot)

Projeto desenvolvido como requisito prático para a disciplina de **Gerência de Configuração e Testes de Software** do **IFPE**. 

O **BiblioTech** é um "mini sistema" focado no núcleo de regras de negócio (Backend) de uma biblioteca acadêmica. O principal objetivo deste repositório é demonstrar a aplicação prática de **Test Driven Development (TDD)** e **Integração/Entrega Contínua (CI/CD)** através do GitHub Actions e Docker.

---

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.4.2
* **Banco de Dados:** H2 Database (Em memória, para facilitar a execução dos testes)
* **Testes:** JUnit 5 e Mockito
* **DevOps:** Docker, DockerHub e GitHub Actions (CI/CD)
* **Gerenciamento de Dependências:** Maven

---

## 🎯 Cobertura de Testes (Matriz de Rastreabilidade)

Toda a lógica da aplicação foi construída para satisfazer **15 Casos de Teste** pré-definidos nos requisitos do projeto. A camada de `Service` concentra as validações:

| Módulo | Casos de Teste | Descrição Resumida | Status |
| :--- | :--- | :--- | :---: |
| **Autenticação** | `TC001`, `TC002`, `TC003`, `TC010`, `TC015` | Validação de credenciais, campos vazios, níveis de acesso e logout. | ✅ |
| **Catálogo** | `TC004`, `TC013` | Busca exata por título e cadastro de novos exemplares inicializando estoque. | ✅ |
| **Empréstimos** | `TC005`, `TC006`, `TC007` | Verificação de disponibilidade, baixa de estoque e cálculo de devolução (+7 dias). | ✅ |
| **Devoluções e Multas** | `TC011`, `TC012` | Reposição de estoque e cálculo automático de R$ 2,00/dia em caso de atraso. | ✅ |
| **Renovação e Histórico** | `TC008`, `TC009`, `TC014` | Extensão de prazo (+7 dias), bloqueio por reservas ativas e consulta cronológica. | ✅ |

---

## ⚙️ Pipeline de CI/CD

O projeto conta com um workflow automatizado configurado em `.github/workflows/main.yml`. 
A cada `push` ou `pull_request` para a branch `main`, o GitHub Actions executa os seguintes passos:

1.  **Continuous Integration (CI):** Levanta um contêiner Ubuntu, instala o Java 21 e roda a suíte completa de testes unitários (`mvn test`).
2.  **Continuous Deployment (CD):** Se (e somente se) todos os testes passarem, o projeto é empacotado (`mvn package`), uma imagem Docker baseada no `eclipse-temurin:21-jdk-alpine` é construída e enviada automaticamente para o **Docker Hub**.

---

## 🛠️ Como Executar o Projeto Localmente

### Opção 1: Rodando os Testes Unitários (Recomendado para avaliação)
Para verificar a integridade das regras de negócio e a aprovação dos 15 Casos de Teste, execute na raiz do projeto:
```bash
./mvnw clean test
```
### Opção 2: Rodando a Aplicação Completa via Maven:
```bash
./mvnw spring-boot:run
```
### Opção 3: Via Docker (Container)
Como o pipeline gera a imagem automaticamente, você pode baixar a versão mais recente diretamente do Docker Hub e rodar em qualquer máquina que tenha o Docker instalado:
```bash
# Baixa e executa a imagem na porta 8080
docker run -p 8080:8080 seu-usuario-dockerhub/bibliotech:latest
```

> (Nota: Substitua seu-usuario-dockerhub pelo namespace configurado no CI)

---

## 👨‍💻 Autor
Desenvolvido por **Davi Freitas** [(@DCF-2)](https://github.com/DCF-2)  
_Curso de Análise e Desenvolvimento de Sistemas – IFPE_
