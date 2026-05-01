const API = "http://localhost:8080/produtos";

const tabela = document.getElementById("tabela-estoque");

fetch(API)
    .then(res => res.json())
    .then(data => {
        tabela.innerHTML = "";

        if (data.length === 0) {
            tabela.innerHTML = '<tr><td colspan="6" class="sem-dados">Nenhum produto cadastrado.</td></tr>';
            return;
        }

        data.forEach(produto => {
            const linha = document.createElement("tr");
            const estoqueBaixo = produto.quantidadeEstoque < produto.estoqueMinimo;

            if (estoqueBaixo) {
                linha.classList.add("estoque-baixo");
            }

            linha.innerHTML = `
                <td>${produto.id}</td>
                <td>${produto.nome}</td>
                <td>${produto.descricao}</td>
                <td>${produto.quantidadeEstoque}</td>
                <td>${produto.estoqueMinimo}</td>
                <td><span class="badge ${estoqueBaixo ? "badge-danger" : "badge-success"}">${estoqueBaixo ? "Baixo" : "OK"}</span></td>
            `;

            tabela.appendChild(linha);
        });
    })
    .catch(err => {
        console.error("Erro ao carregar estoque:", err);
        tabela.innerHTML = '<tr><td colspan="6" class="sem-dados">Erro ao carregar estoque.</td></tr>';
    });
