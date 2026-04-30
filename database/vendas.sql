-- ============================================================
-- Script SQL - Módulo de Vendas e Relatórios
-- Responsável: Érica
-- Sistema: SGC Papelaria
-- ============================================================

-- ATENÇÃO: Execute APÓS os scripts da Andressa (clientes) e da Júlia (produtos),
-- pois as tabelas vendas e itens_venda dependem de clientes, produtos e usuarios.

-- ============================================================
-- Tabela: vendas
-- ============================================================
CREATE TABLE IF NOT EXISTS vendas (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    data          DATE         NOT NULL,
    cliente_id    BIGINT       NOT NULL,
    usuario_id    BIGINT       NOT NULL,
    valor_total   DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    -- Chave estrangeira para clientes (módulo da Andressa)
    CONSTRAINT fk_venda_cliente  FOREIGN KEY (cliente_id)  REFERENCES clientes(id),
    -- Chave estrangeira para usuarios (módulo do Marcos)
    CONSTRAINT fk_venda_usuario  FOREIGN KEY (usuario_id)  REFERENCES usuarios(id)
);

-- ============================================================
-- Tabela: itens_venda
-- ============================================================
CREATE TABLE IF NOT EXISTS itens_venda (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    venda_id       BIGINT        NOT NULL,
    produto_id     BIGINT        NOT NULL,
    quantidade     INT           NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    subtotal       DECIMAL(10,2) NOT NULL,

    -- Chave estrangeira para vendas
    CONSTRAINT fk_item_venda    FOREIGN KEY (venda_id)   REFERENCES vendas(id),
    -- Chave estrangeira para produtos (módulo da Júlia)
    CONSTRAINT fk_item_produto  FOREIGN KEY (produto_id) REFERENCES produtos(id)
);

-- ============================================================
-- Dados de exemplo para teste (só rodar depois de ter clientes/produtos/usuarios)
-- ============================================================

-- Venda 1: cliente 1, usuário 1
INSERT INTO vendas (data, cliente_id, usuario_id, valor_total)
VALUES ('2025-06-01', 1, 1, 45.50);

-- Itens da venda 1
INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES (1, 1, 2, 12.50, 25.00);

INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES (1, 2, 1, 20.50, 20.50);

-- Venda 2: cliente 2, usuário 1
INSERT INTO vendas (data, cliente_id, usuario_id, valor_total)
VALUES ('2025-07-15', 2, 1, 80.00);

-- Itens da venda 2
INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario, subtotal)
VALUES (2, 3, 4, 20.00, 80.00);
