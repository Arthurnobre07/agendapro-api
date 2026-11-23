# Plano de implementação e checklist

Este plano transforma o pacote inicial em entrega final. Marque cada item somente depois de implementar e testar.

## Etapa 1 — Ambiente e estrutura
- [x] Estrutura monorepo backend/frontend/docs.
- [x] PostgreSQL com Docker Compose.
- [x] Spring Boot, JPA, validação, Security, Actuator e OpenAPI no Maven.
- [x] React + TypeScript + Vite.
- [ ] Executar `docker compose up -d`.
- [ ] Executar backend e confirmar `/actuator/health`.
- [ ] Executar frontend e confirmar busca na API.

## Etapa 2 — Domínio e persistência
- [ ] Implementar Usuario e papéis múltiplos.
- [ ] Implementar Profissional 1:1 com Usuario.
- [ ] Relacionar Estabelecimento ao Usuario proprietário.
- [ ] Implementar ProfissionalEstabelecimento com origem, status, criador, datas e expiração.
- [ ] Implementar CategoriaServico e Servico.
- [ ] Implementar ProfissionalServico.
- [ ] Implementar Disponibilidade.
- [ ] Implementar Agendamento e Avaliacao.
- [ ] Criar migrações Flyway e constraints/índices.

## Etapa 3 — Segurança
- [ ] Cadastro e login.
- [ ] BCrypt.
- [ ] JWT Bearer com expiração e validação.
- [ ] Rotas públicas e protegidas.
- [ ] Autorização por papel e por estabelecimento (tenant).
- [ ] Testar tentativa de acessar estabelecimento alheio.

## Etapa 4 — API e regras de negócio
- [ ] CRUD de estabelecimentos.
- [ ] Convite e aceite/recusa de profissional.
- [ ] Solicitação e aprovação/recusa de vínculo.
- [ ] Gestão de categorias e serviços.
- [ ] Associação de serviços a profissionais.
- [ ] Gestão de disponibilidade.
- [ ] Consulta de horários livres.
- [ ] Criar/confirmar/cancelar/concluir agendamento.
- [ ] Impedir conflito de agenda com transação.
- [ ] Avaliar atendimento concluído.
- [ ] Filtros, ordenação, paginação e relatórios.

## Etapa 5 — Front-end
- [ ] Rotas públicas, autenticadas e 404.
- [ ] Cadastro, login e logout.
- [ ] Context + useReducer para sessão.
- [ ] Área do cliente: busca, detalhes, agendamento, meus agendamentos e avaliações.
- [ ] Área profissional: vínculos, agenda e atendimentos.
- [ ] Área administrador: estabelecimentos, equipe, serviços, horários e reservas.
- [ ] Loading, error, empty state e retry em chamadas.
- [ ] Formulários controlados e validação.
- [ ] Layout responsivo e acessível.

## Etapa 6 — Qualidade e entrega
- [ ] Testes JUnit/Mockito de Services e Controllers.
- [ ] Testes Vitest/React Testing Library.
- [ ] OpenAPI/Swagger revisado.
- [ ] Dockerfile da API e compose com API + banco.
- [ ] README com participantes, setup, env vars, dados de teste e endpoints.
- [ ] Postman collection.
- [ ] Deploy e demonstração.
- [ ] Cada integrante apresenta sua contribuição real no GitHub.

## Consultas específicas que o projeto deve demonstrar
1. Estabelecimentos por nome (`ContainingIgnoreCase`) — já iniciada.
2. Estabelecimentos por proprietário (relacionamento/consulta específica; substituir e-mail temporário por FK Usuario).
3. Serviços por categoria e estabelecimento.
4. Profissionais ativos por estabelecimento.
5. Agendamentos por período (`Between`).
6. Agendamentos por status e profissional (filtros combinados).
7. Agendamentos paginados e ordenados por data.
8. Receita/quantidade de atendimentos concluídos por período (`@Query` agregada, evolução).

## Distribuição sugerida para três integrantes

- Integrante A: autenticação, Usuario, Estabelecimento e autorização multi-tenant.
- Integrante B: Profissional, vínculos, categorias, serviços e disponibilidade.
- Integrante C: agendamentos, avaliações, conflitos, relatórios e testes de regras.

Todos devem revisar integração, executar o sistema, conhecer o domínio e fazer commits próprios e verificáveis.
