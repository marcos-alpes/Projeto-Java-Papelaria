const API = "http://localhost:8080/produtos";

const form = document.getElementById("form-produto");
const tabela = document.getElementById("tabela-produtos");

carregarProdutos();

function carregarProdutos() {
    fetch(API)
        .then(res => res.json())
        .then(data => {
            tabela.innerHTML = "";

            data.forEach(produto => {
                const linha = document.createElement("tr");

                // destaque estoque baixo
                if (produto.quantidadeEstoque < produto.estoqueMinimo) {
                    linha.classList.add("estoque-baixo");
                }

                linha.innerHTML = `
                    <td>${produto.nome}</td>
                    <td>${produto.descricao}</td>
                    <td>R$ ${produto.preco}</td>
                    <td>${produto.quantidadeEstoque}</td>
                    <td>${produto.estoqueMinimo}</td>
                    <td>
                        <button class="btn-editar" onclick="editar(${produto.id})">Editar</button>
                        <button class="btn-excluir" onclick="excluir(${produto.id})">Excluir</button>
                    </td>
                `;

                tabela.appendChild(linha);
            });
        });
}

form.addEventListener("submit", function (e) {
    e.preventDefault();

    const produto = {
        nome: document.getElementById("nome").value,
        descricao: document.getElementById("descricao").value,
        preco: parseFloat(document.getElementById("preco").value),
        quantidadeEstoque: parseInt(document.getElementById("quantidadeEstoque").value),
        estoqueMinimo: parseInt(document.getElementById("estoqueMinimo").value)
    };

    const id = document.getElementById("id").value;

    if (id) {
        // EDITAR
        fetch(`${API}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(produto)
        }).then(() => {
            form.reset();
            carregarProdutos();
        });
    } else {
        // CRIAR
        fetch(API, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(produto)
        }).then(() => {
            form.reset();
            carregarProdutos();
        });
    }
});

function editar(id) {
    fetch(`${API}/${id}`)
        .then(res => res.json())
        .then(produto => {
            document.getElementById("id").value = produto.id;
            document.getElementById("nome").value = produto.nome;
            document.getElementById("descricao").value = produto.descricao;
            document.getElementById("preco").value = produto.preco;
            document.getElementById("quantidadeEstoque").value = produto.quantidadeEstoque;
            document.getElementById("estoqueMinimo").value = produto.estoqueMinimo;
        });
}

function excluir(id) {
    if (confirm("Deseja excluir este produto?")) {
        fetch(`${API}/${id}`, {
            method: "DELETE"
        }).then(() => carregarProdutos());
    }
}