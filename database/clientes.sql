USE sgc_papelaria;

-- Inserindo cliente de teste
INSERT INTO clientes (nome, cpf, email, telefone, endereco)
VALUES 
('Andressa Teste', '123.456.789-00', 'andressa@email.com', '(63) 99999-0000', 'Rua Teste, 123');

-- Listando clientes
SELECT * FROM clientes;