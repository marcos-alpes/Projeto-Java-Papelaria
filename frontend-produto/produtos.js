const API = "http://localhost:8080/produtos";

const form = document.getElementById("form-produto");
const tabela = document.getElementById("tabela-produtos");

document.addEventListener("DOMContentLoaded", () => {
    carregarProdutos();
});

function carregarProdutos() {
    fetch(API)
        .then(res => res.json())
        .then(data => {
            tabela.innerHTML = "";

            if (data.length === 0) {
                tabela.innerHTML = '<tr><td colspan="7" class="sem-dados">Nenhum produto cadastrado.</td></tr>';
                return;
            }

            data.forEach(produto => {
                const linha = document.createElement("tr");

                if (produto.quantidadeEstoque < produto.estoqueMinimo) {
                    linha.classList.add("estoque-baixo");
                }

                linha.innerHTML = `
                    <td>${produto.id}</td>
                    <td>${produto.nome}</td>
                    <td>${produto.descricao}</td>
                    <td>R$ ${Number(produto.preco).toFixed(2).replace(".", ",")}</td>
                    <td>${produto.quantidadeEstoque}</td>
                    <td>${produto.estoqueMinimo}</td>
                    <td>
                        <div class="actions">
                            <button class="btn-editar" onclick="editar(${produto.id})">Editar</button>
                            <button class="btn-excluir" onclick="excluir(${produto.id})">Excluir</button>
                        </div>
                    </td>
                `;

                tabela.appendChild(linha);
            });
        })
        .catch(err => {
            console.error("Erro ao carregar produtos:", err);
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
    const method = id ? "PUT" : "POST";
    const url = id ? `${API}/${id}` : API;

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(produto)
    })
        .then(res => {
            if (!res.ok) {
                throw new Error(id ? "Erro ao editar produto" : "Erro ao salvar produto");
            }
            return res.json();
        })
        .then(() => {
            alert(id ? "Produto atualizado com sucesso!" : "Produto salvo com sucesso!");
            form.reset();
            document.getElementById("id").value = "";
            carregarProdutos();
        })
        .catch(err => {
            console.error(err);
            alert(id ? "Erro ao atualizar produto" : "Erro ao salvar produto");
        });
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
        })
        .catch(err => {
            console.error("Erro ao buscar produto:", err);
        });
}

function excluir(id) {
    if (confirm("Deseja excluir este produto?")) {
        fetch(`${API}/${id}`, {
            method: "DELETE"
        })
            .then(res => {
                if (!res.ok) {
                    throw new Error("Erro ao excluir produto");
                }

                alert("Produto excluido com sucesso!");
                carregarProdutos();
            })
            .catch(err => {
                console.error("Erro ao excluir:", err);
                alert("Erro ao excluir produto");
            });
    }
}
