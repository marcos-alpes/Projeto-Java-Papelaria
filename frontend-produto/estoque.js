const API = "http://localhost:8080/produtos/estoque-baixo";

const tabela = document.getElementById("tabela-estoque");

fetch(API)
    .then(res => res.json())
    .then(data => {
        tabela.innerHTML = "";

        data.forEach(produto => {
            const linha = document.createElement("tr");

            linha.classList.add("estoque-baixo");

            linha.innerHTML = `
                <td>${produto.nome}</td>
                <td>${produto.descricao}</td>
                <td>${produto.quantidadeEstoque}</td>
                <td>${produto.estoqueMinimo}</td>
            `;

            tabela.appendChild(linha);
        });
    });