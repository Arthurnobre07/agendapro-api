# Modelagem conceitual do AgendaPro

## Entidades principais

1. Usuario — credenciais e dados de identidade; pode possuir papéis CLIENTE, PROFISSIONAL e ADMINISTRADOR.
2. Estabelecimento — negócio cadastrado por um proprietário.
3. Profissional — perfil profissional global 1:1 com Usuario.
4. ProfissionalEstabelecimento — vínculo entre profissional e estabelecimento; controla convite/solicitação e status.
5. CategoriaServico — categoria pertencente a um estabelecimento.
6. Servico — serviço pertencente a uma categoria.
7. ProfissionalServico — oferta de um serviço por um profissional em um estabelecimento, com preço/duração opcionais personalizados.
8. Disponibilidade — horário recorrente ligado ao vínculo profissional-estabelecimento.
9. Agendamento — reserva feita por um cliente para uma oferta ProfissionalServico; registra preço e duração contratados.
10. Avaliacao — avaliação única de um agendamento concluído.

## Vínculos

- Proprietário Usuario 1:N Estabelecimento.
- Usuario 1:0..1 Profissional.
- Profissional 1:N ProfissionalEstabelecimento.
- Estabelecimento 1:N ProfissionalEstabelecimento.
- Estabelecimento 1:N CategoriaServico.
- CategoriaServico 1:N Servico.
- ProfissionalEstabelecimento 1:N ProfissionalServico.
- Servico 1:N ProfissionalServico.
- ProfissionalEstabelecimento 1:N Disponibilidade.
- Usuario/Cliente 1:N Agendamento.
- ProfissionalServico 1:N Agendamento.
- Agendamento 1:0..1 Avaliacao.

## Fluxo de vínculo

- Administrador convida profissional: vínculo PENDENTE com origem CONVITE; profissional aceita ou recusa.
- Profissional solicita vínculo: vínculo PENDENTE com origem SOLICITACAO; administrador aprova ou recusa.
- Apenas vínculo ATIVO libera acesso a agenda e dados daquele estabelecimento.
- Impor unicidade lógica para vínculo pendente/ativo por par profissional-estabelecimento.

## Regras críticas

- Não confiar em estabelecimentoId enviado pelo navegador: validar autorização no servidor.
- Profissional só oferece serviço em estabelecimento no qual tem vínculo ativo.
- Disponibilidade pertence ao vínculo, não ao perfil profissional global.
- Verificar sobreposição de horários no banco/transação antes de confirmar reserva.
- Salvar preço e duração no Agendamento para preservar histórico.
- Avaliação somente para atendimento CONCLUIDO e uma por agendamento.
