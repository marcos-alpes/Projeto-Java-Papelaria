**Sistema de Gestão de Papelaria (SGC)**

**Descrição: Sistema desenvolvido para gerenciamento de produtos e controle de estoque de uma papelaria.**
________________________________________
**Funcionalidades**

**Usuários**

•	Cadastro de usuário

•	Login de usuário

**Produtos**

•	Cadastrar produto

•	Listar produtos

•	Editar produto

•	Excluir produto

**Estoque**

•	Controle de quantidade em estoque

•	Definição de estoque mínimo

•	Listagem de produtos com estoque baixo

•	Bloqueio de venda com estoque insuficiente
________________________________________
**Tecnologias Utilizadas**

**Backend**

•	Java

•	Spring Boot

•	Maven

**Frontend**

•	HTML

•	CSS

•	JavaScript

**Banco de Dados**

•	MySQL
________________________________________
**Estrutura do Banco de Dados**

**Tabela: produtos**

Campo	Tipo

id	Long

nome	String

descricao	String

preco	Double

quantidade_estoque	Integer

estoque_minimo	Integer
________________________________________
**Regras de Negócio**

•	O preço não pode ser negativo

•	A quantidade em estoque não pode ser negativa

•	O estoque mínimo deve ser definido

•	Produtos com estoque abaixo do mínimo devem ser identificados

•	Não é permitido realizar venda com estoque insuficiente
________________________________________
**Casos de Uso**

**Cadastrar Produto**
1.	Usuário informa os dados
2.	Sistema valida os dados
3.	Produto é salvo

**Listar Produto**

1.	Sistema recupera os produtos cadastrados

2.	Exibe na interface

**Editar Produto**

1.	Usuário altera os dados do produto
2.	Sistema atualiza o registro

**Excluir Produto**

1.	Usuário seleciona o produto
2.	Sistema remove o registro

**Consultar Estoque Baixo**


1.	Sistema identifica produtos com estoque abaixo do mínimo

2.	Exibe os resultados

________________________________________
**Diagrama de Classes (Resumo)**

**Produto**
•	id
•	nome
•	descricao
•	preco
•	quantidadeEstoque
•	estoqueMinimo
________________________________________

**Execução do Projeto**

**Backend**
1.	Clonar o repositório
2.	Importar como projeto Maven
3.	Executar a aplicação Spring Boot

**Frontend**

1.	Abrir o arquivo produtos.html em um navegador

________________________________________
Autor
Júlia

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
