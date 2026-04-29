const API = "http://localhost:8080/produtos";

function cadastrar() {
    const produto = {
        nome: document.getElementById("nome").value,
        preco: parseFloat(document.getElementById("preco").value),
        quantidadeEstoque: parseInt(document.getElementById("estoque").value),
        estoqueMinimo: 5
    };

    fetch(API, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(produto)
    }).then(() => listar());
}

function listar() {
    fetch(API)
        .then(res => res.json())
        .then(data => {
            const lista = document.getElementById("lista");
            lista.innerHTML = "";

            data.forEach(p => {
                lista.innerHTML += `<li>${p.nome} - ${p.quantidadeEstoque}</li>`;
            });
        });
}

listar();