CREATE DATABASE IF NOT EXISTS sgc_papelaria;
USE sgc_papelaria;

CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    perfil VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    cpf VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL,
    telefone VARCHAR(30),
    endereco VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    preco DECIMAL(10,2) NOT NULL,
    quantidade_estoque INT NOT NULL,
    estoque_minimo INT NOT NULL,
    CHECK (preco >= 0),
    CHECK (quantidade_estoque >= 0),
    CHECK (estoque_minimo >= 0)
);

CREATE TABLE IF NOT EXISTS vendas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data DATE NOT NULL,
    cliente_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    CONSTRAINT fk_venda_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_venda_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS itens_venda (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venda_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_item_venda FOREIGN KEY (venda_id) REFERENCES vendas(id),
    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id) REFERENCES produtos(id),
    CHECK (quantidade > 0),
    CHECK (preco_unitario >= 0),
    CHECK (subtotal >= 0)
);

INSERT INTO usuarios (username, senha, perfil)
VALUES ('admin', '$2a$10$7Qd0v5rQV2tR82WBV3bnru4dlrJix09V6I9h9G97ptdL1kkWBsmhW', 'ADMIN')
ON DUPLICATE KEY UPDATE username = username;

INSERT INTO clientes (nome, cpf, email, telefone, endereco)
VALUES ('Cliente Teste', '123.456.789-00', 'cliente@email.com', '(63) 99999-0000', 'Rua Teste, 123')
ON DUPLICATE KEY UPDATE cpf = cpf;

INSERT INTO produtos (nome, descricao, preco, quantidade_estoque, estoque_minimo)
VALUES
('Caneta Azul', 'Caneta esferografica azul', 2.50, 100, 10),
('Caderno', 'Caderno 100 folhas', 15.00, 50, 5),
('Borracha', 'Borracha branca', 1.50, 5, 10),
('Lapis', 'Lapis preto HB', 1.00, 200, 20)
ON DUPLICATE KEY UPDATE nome = nome;
