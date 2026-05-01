const API_URL = "http://localhost:8080/api";

let itensVenda = [];

function adicionarItem() {
    const produtoId = parseInt(document.getElementById("produtoId").value);
    const quantidade = parseInt(document.getElementById("quantidade").value);

    if (!produtoId || produtoId <= 0) {
        alert("Informe um ID de produto válido.");
        return;
    }
    if (!quantidade || quantidade <= 0) {
        alert("A quantidade deve ser maior que zero.");
        return;
    }

    // Verifica se o produto já foi adicionado
    const jaExiste = itensVenda.find(i => i.produtoId === produtoId);
    if (jaExiste) {
        alert("Esse produto já foi adicionado. Remova e adicione novamente se quiser alterar a quantidade.");
        return;
    }

    const item = {
        produtoId: produtoId,
        quantidade: quantidade
    };
    itensVenda.push(item);

    document.getElementById("produtoId").value = "";
    document.getElementById("quantidade").value = "";

    renderizarItens();
}

function removerItem(index) {
    itensVenda.splice(index, 1);
    renderizarItens();
}

function renderizarItens() {
    const lista = document.getElementById("listaItens");

    lista.innerHTML = "";

    if (itensVenda.length === 0) {
        lista.innerHTML = '<p class="sem-dados">Nenhum item adicionado ainda.</p>';
        return;
    }

    itensVenda.forEach((item, index) => {
        const div = document.createElement("div");
        div.className = "item-adicionado";
        div.innerHTML = `
            <span>Produto #${item.produtoId} | Qtd: ${item.quantidade}</span>
            <button class="btn-remover" onclick="removerItem(${index})">🗑️ Remover</button>
        `;
        lista.appendChild(div);
    });
}

async function registrarVenda() {
    const clienteId = parseInt(document.getElementById("clienteId").value);
    const usuarioId = parseInt(document.getElementById("usuarioId").value);

    if (!clienteId || clienteId <= 0) {
        mostrarMensagem("Informe o ID do cliente.", "erro");
        return;
    }
    if (!usuarioId || usuarioId <= 0) {
        mostrarMensagem("Informe o ID do usuário responsável.", "erro");
        return;
    }
    if (itensVenda.length === 0) {
        mostrarMensagem("Adicione pelo menos 1 item antes de registrar a venda.", "erro");
        return;
    }

    const venda = {
        clienteId: clienteId,
        usuarioId: usuarioId,
        itens: itensVenda
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
        mostrarMensagem("Erro de conexão com o servidor.", "erro");
        console.error(error);
    }
}

async function carregarVendas() {
    const container = document.getElementById("tabelaVendas");
    container.innerHTML = "<p>Carregando...</p>";

    try {
        const response = await fetch(`${API_URL}/vendas`);
        const vendas = await response.json();

        if (vendas.length === 0) {
            container.innerHTML = '<p class="sem-dados">Nenhuma venda registrada.</p>';
            return;
        }

        let html = `
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Data</th>
                        <th>Cliente ID</th>
                        <th>Usuário ID</th>
                        <th>Qtd Itens</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody>
        `;

        vendas.forEach(v => {
            const data = new Date(v.data).toLocaleDateString("pt-BR");
            const total = v.valorTotal.toFixed(2).replace(".", ",");
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

        html += "</tbody></table>";
        container.innerHTML = html;

    } catch (error) {
        container.innerHTML = '<p class="sem-dados">Erro ao carregar vendas.</p>';
        console.error(error);
    }
}

function mostrarMensagem(texto, tipo) {
    const el = document.getElementById("mensagem");
    el.textContent = texto;
    el.className = "mensagem " + tipo;
    setTimeout(() => { el.className = "mensagem"; }, 4000);
}

window.onload = function () {
    renderizarItens();
    carregarVendas();
};