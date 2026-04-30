DOCUMENTAÇÃO - Módulo Clientes 

Requisitos Funcionais
- Cadastrar clientes
- Listar clientes
- Editar clientes
- Excluir clientes

Regras de Negócio
- CPF deve ser único no sistema (não pode ser duplicado)
- Email deve estar em formato válido
- Nome, CPF e email são obrigatórios

Requisitos Não Funcionais
- Sistema desenvolvido em Java com Spring Boot
- Banco de dados MySQL
- Interface web para o usuário
- Arquitetura em camadas (Controller, Service, Repository, Model)

Endpoints da API
- POST /clientes → cadastrar cliente
- GET /clientes → listar clientes
- PUT /clientes/{id} → editar cliente
- DELETE /clientes/{id} → excluir cliente

Caso de Uso: Cadastrar Cliente
Ator: Usuário
Fluxo:
1. Usuário preenche os dados
2. Sistema valida CPF e email
3. Sistema verifica duplicidade de CPF
4. Sistema salva no banco
5. Sistema retorna confirmação

Caso de Uso: Listar Clientes

Ator: Usuário

Fluxo:
1. Sistema consulta o banco
2. Sistema retorna a lista

Caso de Uso: Editar Cliente

Ator: Usuário

Fluxo:
1. Usuário seleciona cliente
2. Altera os dados
3. Sistema atualiza no banco
4. Sistema retorna confirmação

Caso de Uso: Excluir Cliente

Ator: Usuário

Fluxo:
1. Usuário seleciona cliente
2. Sistema remove do banco
3. Sistema atualiza a lista

Diagrama de Classes (Descrição)
- Cliente: entidade de dados
- ClienteRepository: acesso ao banco
- ClienteService: regras de negócio
- ClienteController: endpoints da API