# SGC Papelaria

Sistema de Gestao Comercial para uma papelaria, desenvolvido com Java, Spring Boot, MySQL e interface web em HTML, CSS e JavaScript.

## Funcionalidades

- Cadastro, listagem, edicao e exclusao de clientes.
- Validacao de CPF duplicado e email de cliente.
- Cadastro, listagem, edicao e exclusao de produtos.
- Controle de estoque e estoque minimo.
- Registro de vendas com itens, total automatico e baixa de estoque.
- Bloqueio de venda quando o estoque do produto for insuficiente.
- Relatorios de vendas por periodo, por cliente e grafico anual.
- Cadastro e login de usuarios com senha criptografada usando BCrypt.
- Geracao de token simples com expiracao no login.

## Arquitetura

O projeto usa arquitetura em camadas:

- `controller`: endpoints REST.
- `service`: regras de negocio.
- `model`: entidades de dominio.
- `repository`: persistencia com Spring Data JPA.
- `dto`: objetos de transferencia de dados.
- `frontend`: telas web do sistema.
- `database`: scripts SQL.

## Design Patterns

Foram usados os padroes Repository e DTO.

- Repository: aplicado nas interfaces `ClienteRepository`, `ProdutoRepository`, `VendaRepository`, `ItemVendaRepository` e `UsuarioRepository`, isolando o acesso ao banco.
- DTO: aplicado em `VendaDTO`, `ItemVendaDTO`, `LoginDTO`, `UsuarioDTO` e `RespostaDTO`, separando dados de entrada/saida das entidades.

## Banco de Dados

Banco: `sgc_papelaria`

Tabelas principais:

- `usuarios`
- `clientes`
- `produtos`
- `vendas`
- `itens_venda`

Script completo: `database/schema.sql`

## Como executar

1. Criar o banco MySQL executando `database/schema.sql`.
2. Conferir usuario, senha e porta do MySQL em `src/main/resources/application.properties`.
3. Executar o backend Spring Boot.
4. Abrir `index.html` no navegador.

## Frontend

A pagina inicial central fica em:

`index.html`

Por ela e possivel acessar:

- Clientes
- Produtos
- Estoque
- Vendas
- Relatorios

## Tecnologias

- Java 21+
- Spring Boot 3
- Spring Data JPA
- Spring Security
- MySQL
- Maven
- HTML
- CSS
- JavaScript
