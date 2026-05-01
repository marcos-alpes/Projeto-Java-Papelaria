const API_URL = "http://localhost:8080/api";
const PRODUTOS_API_URL = "http://localhost:8080/produtos";

let itensVenda = [];
let mensagemTimer = null;

async function adicionarItem() {
    const produtoId = parseInt(document.getElementById("produtoId").value);
    const quantidade = parseInt(document.getElementById("quantidade").value);

    if (!produtoId || produtoId <= 0) {
        alert("Informe um ID de produto valido.");
        return;
    }

    if (!quantidade || quantidade <= 0) {
        alert("A quantidade deve ser maior que zero.");
        return;
    }

    const produto = await buscarProduto(produtoId);
    if (!produto) {
        return;
    }

    if (produto.quantidadeEstoque < quantidade) {
        mostrarMensagem(
            `Estoque insuficiente para ${produto.nome}. Disponivel: ${produto.quantidadeEstoque}.`,
            "erro"
        );
        return;
    }

    const preco = Number(produto.preco);
    const item = {
        produtoId: produto.id,
        nomeProduto: produto.nome,
        quantidade: quantidade,
        precoUnitario: preco,
        subtotal: preco * quantidade
    };

    itensVenda.push(item);

    document.getElementById("produtoId").value = "";
    document.getElementById("quantidade").value = "";

    renderizarItens();
    mostrarMensagem("Item adicionado.", "sucesso");
}

async function buscarProduto(produtoId) {
    try {
        const response = await fetch(`${PRODUTOS_API_URL}/${produtoId}`);

        if (!response.ok) {
            mostrarMensagem("Produto nao encontrado.", "erro");
            return null;
        }

        return await response.json();
    } catch (error) {
        mostrarMensagem("Erro de conexao ao buscar produto.", "erro");
        console.error(error);
        return null;
    }
}

function removerItem(index) {
    itensVenda.splice(index, 1);
    renderizarItens();
}

function renderizarItens() {
    const lista = document.getElementById("listaItens");
    const totalSpan = document.getElementById("totalVenda");

    lista.innerHTML = "";
    let total = 0;

    if (itensVenda.length === 0) {
        lista.innerHTML = '<p class="sem-dados">Nenhum item adicionado ainda.</p>';
    }

    itensVenda.forEach((item, index) => {
        total += item.subtotal;
        const div = document.createElement("div");
        div.className = "item-adicionado";
        div.innerHTML = `
            <span>${item.nomeProduto || "Produto"} #${item.produtoId} | Qtd: ${item.quantidade} |
            R$ ${item.precoUnitario.toFixed(2)} un. |
            Subtotal: R$ ${item.subtotal.toFixed(2)}</span>
            <button type="button" class="btn-remover" onclick="removerItem(${index})">Remover</button>
        `;
        lista.appendChild(div);
    });

    totalSpan.textContent = total.toFixed(2).replace(".", ",");
}

async function registrarVenda() {
    const clienteId = parseInt(document.getElementById("clienteId").value);
    const usuarioId = parseInt(document.getElementById("usuarioId").value);

    if (!clienteId || clienteId <= 0) {
        mostrarMensagem("Informe o ID do cliente.", "erro");
        return;
    }

    if (!usuarioId || usuarioId <= 0) {
        mostrarMensagem("Informe o ID do usuario responsavel.", "erro");
        return;
    }

    if (itensVenda.length === 0) {
        mostrarMensagem("Adicione pelo menos 1 item antes de registrar a venda.", "erro");
        return;
    }

    const venda = {
        clienteId: clienteId,
        usuarioId: usuarioId,
        itens: itensVenda.map(item => ({
            produtoId: item.produtoId,
            quantidade: item.quantidade
        }))
    };

    try {
        const response = await fetch(`${API_URL}/vendas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(venda)
        });

        if (response.ok) {
            mostrarMensagem("Venda registrada com sucesso!", "sucesso");
            itensVenda = [];
            renderizarItens();
            document.getElementById("clienteId").value = "";
            document.getElementById("usuarioId").value = "";
            carregarVendas();
        } else {
            const erro = await response.text();
            mostrarMensagem("Erro: " + erro, "erro");
        }
    } catch (error) {
        mostrarMensagem("Erro de conexao com o servidor.", "erro");
        console.error(error);
    }
}

async function carregarVendas() {
    const container = document.getElementById("tabelaVendas");
    container.innerHTML = "<p>Carregando...</p>";

    try {
        const response = await fetch(`${API_URL}/vendas`);

        if (!response.ok) {
            throw new Error("Erro ao carregar vendas.");
        }

        const vendas = await response.json();

        if (vendas.length === 0) {
            container.innerHTML = '<p class="sem-dados">Nenhuma venda registrada.</p>';
            return;
        }

        let html = `
            <div class="table-wrap">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Data</th>
                        <th>Cliente ID</th>
                        <th>Usuario ID</th>
                        <th>Qtd Itens</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
        `;

        vendas.forEach(v => {
            const data = new Date(v.data).toLocaleDateString("pt-BR");
            const valorTotal = Number(v.valorTotal || 0);
            const total = valorTotal.toFixed(2).replace(".", ",");
            html += `
                <tr>
                    <td>${v.id}</td>
                    <td>${data}</td>
                    <td>${v.clienteId}</td>
                    <td>${v.usuarioId}</td>
                    <td>${v.itens ? v.itens.length : 0}</td>
                    <td><strong>R$ ${total}</strong></td>
                </tr>
            `;
        });

        html += "</tbody></table></div>";
        container.innerHTML = html;
    } catch (error) {
        container.innerHTML = '<p class="sem-dados">Erro ao carregar vendas.</p>';
        console.error(error);
    }
}

function mostrarMensagem(texto, tipo) {
    const el = document.getElementById("mensagem");
    clearTimeout(mensagemTimer);
    el.textContent = texto;
    el.className = "mensagem " + tipo;
    mensagemTimer = setTimeout(() => {
        el.textContent = "";
        el.className = "mensagem";
    }, 4000);
}

window.onload = function () {
    renderizarItens();
    carregarVendas();
};
