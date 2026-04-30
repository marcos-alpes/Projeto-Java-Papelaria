// ==========================
// TESTE INICIAL
// ==========================
console.log("JS carregou");

// ==========================
// CONFIG
// ==========================
const API = "http://localhost:8080/produtos";

const form = document.getElementById("form-produto");
const tabela = document.getElementById("tabela-produtos");

// ==========================
// GARANTE QUE O DOM CARREGOU
// ==========================
document.addEventListener("DOMContentLoaded", () => {
    console.log("DOM carregado");
    carregarProdutos();
});

// ==========================
// LISTAR PRODUTOS
// ==========================
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
        })
        .catch(err => {
            console.error("Erro ao carregar produtos:", err);
        });
}

// ==========================
// SALVAR / EDITAR
// ==========================
form.addEventListener("submit", function (e) {
    e.preventDefault();

    console.log("Botão salvar clicado");

    const produto = {
        nome: document.getElementById("nome").value,
        descricao: document.getElementById("descricao").value,
        preco: parseFloat(document.getElementById("preco").value),
        quantidadeEstoque: parseInt(document.getElementById("quantidadeEstoque").value),
        estoqueMinimo: parseInt(document.getElementById("estoqueMinimo").value)
    };

    console.log("Produto enviado:", produto);

    const id = document.getElementById("id").value;

    // ======================
    // EDITAR
    // ======================
    if (id) {
        fetch(`${API}/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(produto)
        })
        .then(res => {
            if (!res.ok) {
                throw new Error("Erro ao editar produto");
            }
            return res.json();
        })
        .then(() => {
            alert("Produto atualizado com sucesso!");
            form.reset();
            carregarProdutos();
        })
        .catch(err => {
            console.error(err);
            alert("Erro ao atualizar produto");
        });
    } 
    
    // ======================
    // CRIAR
    // ======================
    else {
        fetch(API, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(produto)
        })
        .then(res => {
            console.log("Status resposta:", res.status);

            if (!res.ok) {
                throw new Error("Erro ao salvar produto");
            }

            return res.json();
        })
        .then(() => {
            alert("Produto salvo com sucesso!");
            form.reset();
            carregarProdutos();
        })
        .catch(err => {
            console.error("Erro no POST:", err);
            alert("Erro ao salvar produto");
        });
    }
});

// ==========================
// EDITAR
// ==========================
function editar(id) {
    console.log("Editar ID:", id);

    fetch(`${API}/${id}`)
        .then(res => res.json())
        .then(produto => {
            document.getElementById("id").value = produto.id;
            document.getElementById("nome").value = produto.nome;
            document.getElementById("descricao").value = produto.descricao;
            document.getElementById("preco").value = produto.preco;
            document.getElementById("quantidadeEstoque").value = produto.quantidadeEstoque;
            document.getElementById("estoqueMinimo").value = produto.estoqueMinimo;
        })
        .catch(err => {
            console.error("Erro ao buscar produto:", err);
        });
}

// ==========================
// EXCLUIR
// ==========================
function excluir(id) {
    console.log("Excluir ID:", id);

    if (confirm("Deseja excluir este produto?")) {
        fetch(`${API}/${id}`, {
            method: "DELETE"
        })
        .then(res => {
            if (!res.ok) {
                throw new Error("Erro ao excluir produto");
            }

            alert("Produto excluído com sucesso!");
            carregarProdutos();
        })
        .catch(err => {
            console.error("Erro ao excluir:", err);
            alert("Erro ao excluir produto");
        });
    }
}