const API_URL = "http://localhost:8080/clientes";

document.getElementById("formCliente").addEventListener("submit", salvarCliente);

function salvarCliente(event) {
    event.preventDefault();

    const id = document.getElementById("id").value;

    const cliente = {
        nome: document.getElementById("nome").value,
        cpf: document.getElementById("cpf").value,
        email: document.getElementById("email").value,
        telefone: document.getElementById("telefone").value,
        endereco: document.getElementById("endereco").value
    };

    const metodo = id ? "PUT" : "POST";
    const url = id ? `${API_URL}/${id}` : API_URL;

    fetch(url, {
        method: metodo,
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(cliente)
    })
    .then(() => {
        limparFormulario();
        listarClientes();
    });
}

function listarClientes() {
    fetch(API_URL)
        .then(response => response.json())
        .then(clientes => {
            const tabela = document.getElementById("tabelaClientes");
            tabela.innerHTML = "";

            clientes.forEach(cliente => {
                tabela.innerHTML += `
                    <tr>
                        <td>${cliente.id}</td>
                        <td>${cliente.nome}</td>
                        <td>${cliente.cpf}</td>
                        <td>${cliente.email}</td>
                        <td>${cliente.telefone}</td>
                        <td>${cliente.endereco}</td>
                        <td>
                            <button class="btn-editar" onclick="editarCliente(${cliente.id}, '${cliente.nome}', '${cliente.cpf}', '${cliente.email}', '${cliente.telefone}', '${cliente.endereco}')">Editar</button>
                            <button class="btn-excluir" onclick="excluirCliente(${cliente.id})">Excluir</button>
                        </td>
                    </tr>
                `;
            });
        });
}

function editarCliente(id, nome, cpf, email, telefone, endereco) {
    document.getElementById("id").value = id;
    document.getElementById("nome").value = nome;
    document.getElementById("cpf").value = cpf;
    document.getElementById("email").value = email;
    document.getElementById("telefone").value = telefone;
    document.getElementById("endereco").value = endereco;
}

function excluirCliente(id) {
    fetch(`${API_URL}/${id}`, {
        method: "DELETE"
    })
    .then(() => listarClientes());
}

function limparFormulario() {
    document.getElementById("formCliente").reset();
    document.getElementById("id").value = "";
}

listarClientes();