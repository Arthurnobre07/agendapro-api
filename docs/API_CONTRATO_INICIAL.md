# Contrato inicial da API AgendaPro

Prefixo: `/api`. Os endpoints abaixo são o alvo de implementação; somente os endpoints de Estabelecimentos listados como implementados já existem neste pacote.

## Autenticação
- `POST /auth/cadastro` — público; cria usuário e senha BCrypt.
- `POST /auth/login` — público; retorna JWT e papéis.
- `GET /auth/me` — autenticado; dados do usuário atual.
- `PATCH /auth/me` — autenticado; atualiza perfil próprio.

## Estabelecimentos
- `POST /estabelecimentos` — criar estabelecimento.
- `GET /estabelecimentos` — público; busca por nome com paginação.
- `GET /estabelecimentos/{id}` — público; detalhes.
- `PUT /estabelecimentos/{id}` — administrador autorizado.
- `DELETE /estabelecimentos/{id}` — administrador autorizado; desativação lógica.
- `GET /estabelecimentos/proprietario` — administrador; listar estabelecimentos próprios. No pacote inicial usa e-mail provisório; substituir pela identidade JWT.

## Vínculos profissionais
- `POST /estabelecimentos/{id}/convites` — administrador envia convite.
- `GET /profissionais/me/convites` — profissional lista convites pendentes.
- `PATCH /vinculos/{id}/aceitar` — profissional aceita convite.
- `PATCH /vinculos/{id}/recusar` — profissional recusa convite.
- `POST /estabelecimentos/{id}/solicitacoes` — profissional solicita vínculo.
- `GET /estabelecimentos/{id}/solicitacoes` — administrador lista solicitações.
- `PATCH /vinculos/{id}/aprovar` — administrador aprova solicitação.
- `PATCH /vinculos/{id}/recusar-solicitacao` — administrador recusa solicitação.
- `PATCH /vinculos/{id}/encerrar` — administrador ou profissional encerra vínculo, conforme regra.

## Serviços e equipe
- `POST /estabelecimentos/{id}/categorias`
- `GET /estabelecimentos/{id}/categorias`
- `PUT /categorias/{id}`
- `DELETE /categorias/{id}`
- `POST /estabelecimentos/{id}/servicos`
- `GET /estabelecimentos/{id}/servicos`
- `GET /servicos/{id}`
- `PUT /servicos/{id}`
- `DELETE /servicos/{id}`
- `POST /vinculos/{id}/servicos` — associa serviço ao profissional.
- `GET /vinculos/{id}/servicos`
- `PATCH /profissional-servicos/{id}`

## Disponibilidade
- `POST /vinculos/{id}/disponibilidades`
- `GET /vinculos/{id}/disponibilidades`
- `PUT /disponibilidades/{id}`
- `DELETE /disponibilidades/{id}`
- `GET /profissional-servicos/{id}/horarios-livres?data=YYYY-MM-DD`

## Agendamentos e avaliações
- `POST /agendamentos`
- `GET /agendamentos/me`
- `GET /estabelecimentos/{id}/agendamentos` — administrador autorizado, filtros e paginação.
- `GET /profissionais/me/agendamentos` — profissional, filtros e paginação.
- `GET /agendamentos/{id}`
- `PATCH /agendamentos/{id}/confirmar`
- `PATCH /agendamentos/{id}/cancelar`
- `PATCH /agendamentos/{id}/concluir`
- `POST /agendamentos/{id}/avaliacoes`
- `GET /estabelecimentos/{id}/avaliacoes`

## Relatórios
- `GET /estabelecimentos/{id}/relatorios/atendimentos?inicio=&fim=`
- `GET /estabelecimentos/{id}/relatorios/receita?inicio=&fim=`

## Respostas esperadas
- `200 OK`: consulta/atualização bem-sucedida.
- `201 Created`: criação, com `Location` quando aplicável.
- `204 No Content`: exclusão/desativação sem corpo.
- `400 Bad Request`: validação ou transição inválida.
- `401 Unauthorized`: token ausente/inválido.
- `403 Forbidden`: usuário autenticado sem permissão.
- `404 Not Found`: recurso inexistente ou fora do escopo visível.
- `409 Conflict`: conflito de horário, duplicidade ou estado incompatível.
