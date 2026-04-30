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

