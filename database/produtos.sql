CREATE DATABASE sgc_papelaria;
USE sgc_papelaria;

CREATE TABLE produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    descricao VARCHAR(255),
    preco DECIMAL(10,2) NOT NULL,
    quantidade_estoque INT NOT NULL,
    estoque_minimo INT NOT NULL,

    -- VALIDAÇÕES
    CHECK (preco >= 0),
    CHECK (quantidade_estoque >= 0),
    CHECK (estoque_minimo >= 0)
);

-- Produtos
INSERT INTO produtos (nome, descricao, preco, quantidade_estoque, estoque_minimo) VALUES
('Caneta Azul', 'Caneta esferográfica azul', 2.50, 100, 10),
('Caderno', 'Caderno 100 folhas', 15.00, 50, 5),
('Borracha', 'Borracha branca', 1.50, 5, 10), -- estoque baixo
('Lápis', 'Lápis preto HB', 1.00, 200, 20);

-- ================================
-- CONSULTAS ÚTEIS
-- ================================

-- Listar produtos
SELECT * FROM produtos;

-- Produtos com estoque baixo
SELECT * 
FROM produtos
WHERE quantidade_estoque < estoque_minimo;

-- Buscar produto por nome
SELECT * 
FROM produtos
WHERE nome LIKE '%caneta%';

-- Atualizar estoque
UPDATE produtos
SET quantidade_estoque = quantidade_estoque - 1
WHERE id = 1;

-- Deletar produto
DELETE FROM produtos WHERE id = 1;