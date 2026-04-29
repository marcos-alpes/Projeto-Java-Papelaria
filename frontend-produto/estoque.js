fetch("http://localhost:8080/produtos/estoque-baixo")
    .then(res => res.json())
    .then(data => {
        const tabela = document.getElementById("tabela-estoque");

        data.forEach(produto => {
            tabela.innerHTML += `
                <tr>
                    <td>${produto.nome}</td>
                    <td>${produto.quantidadeEstoque}</td>
                    <td>${produto.estoqueMinimo}</td>
                </tr>
            `;
        });
    });