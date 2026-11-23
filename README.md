# AgendaPro

Plataforma de agendamento de serviços para múltiplos estabelecimentos.

> Repositório-base para desenvolvimento acadêmico. Antes da entrega, cada integrante deve implementar, testar e compreender sua contribuição. Não publique senhas, tokens ou dados pessoais reais.

## Estrutura

- `backend/`: API Java 21 + Spring Boot + Spring Data JPA + PostgreSQL.
- `frontend/`: React + TypeScript + Vite.
- `docs/`: decisões de domínio, contrato inicial da API e roteiro GitHub.
- `compose.yml`: PostgreSQL local.

## Pré-requisitos

- Java 21
- Maven 3.9+ (ou Maven Wrapper, se gerado pelo Spring Initializr)
- Node.js 20+
- Docker Desktop (opcional, recomendado para PostgreSQL)

## 1. Banco de dados

Na raiz do projeto:

```bash
docker compose up -d
```

Banco local: `agendapro`, usuário `agendapro`, senha `agendapro_dev`. Esses valores são somente para desenvolvimento local.

## 2. Back-end

```bash
cd backend
mvn spring-boot:run
```

API: http://localhost:8080

Health: http://localhost:8080/actuator/health

O backend lê as configurações de `backend/src/main/resources/application.yml`. Para outro ambiente, configure `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` e `JWT_SECRET` por variáveis de ambiente.

## 3. Front-end

```bash
cd frontend
npm install
npm run dev
```

Aplicação: http://localhost:5173

## 4. GitHub

1. Crie um repositório vazio chamado `agendapro` na sua conta ou na organização do grupo.
2. Na pasta raiz, execute `git init`.
3. Faça commits reais e pequenos, conforme cada funcionalidade for implementada.
4. Cada integrante deve configurar o Git com seu próprio nome/e-mail e fazer seus próprios commits.
5. Use branches como `feat/autenticacao`, `feat/estabelecimentos` e `feat/agendamentos`.
6. Não invente autoria nem faça commits em nome de outro integrante.

Exemplo:

```bash
git init
git add .
git commit -m "chore: cria estrutura inicial do AgendaPro"
git branch -M main
git remote add origin https://github.com/SEU-USUARIO/agendapro.git
git push -u origin main
```

## Estado deste pacote

Este pacote entrega a estrutura inicial executável, a conexão com PostgreSQL, health check, uma API inicial de estabelecimentos e uma interface inicial para evoluir. Ainda é necessário completar os módulos de autenticação JWT, vínculos, serviços, disponibilidade, agendamentos, avaliações, permissões por estabelecimento e testes antes de considerar o trabalho finalizado.

Consulte `docs/PLANO_DE_IMPLEMENTACAO.md` para a sequência recomendada.
