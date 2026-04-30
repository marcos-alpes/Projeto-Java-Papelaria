const API_URL = "http://localhost:8080/api";

let graficoInstance = null;

const MESES = ["Jan", "Fev", "Mar", "Abr", "Mai", "Jun",
               "Jul", "Ago", "Set", "Out", "Nov", "Dez"];

async function buscarPorPeriodo() {
    const inicio = document.getElementById("dataInicio").value;
    const fim = document.getElementById("dataFim").value;
    const container = document.getElementById("resultadoPeriodo");

    if (!inicio || !fim) {
        alert("Selecione as datas de início e fim.");
        return;
    }
    if (inicio > fim) {
        alert("A data de início deve ser anterior à data final.");
        return;
    }

    container.innerHTML = "<p>Carregando...</p>";

    try {
        const response = await fetch(`${API_URL}/relatorios/periodo?inicio=${inicio}&fim=${fim}`);
        const vendas = await response.json();
        renderizarTabelaVendas(vendas, container);
    } catch (error) {
        container.innerHTML = '<p class="sem-dados">Erro ao buscar dados.</p>';
        console.error(error);
    }
}

async function buscarPorCliente() {
    const clienteId = document.getElementById("clienteIdRelatorio").value;
    const container = document.getElementById("resultadoCliente");

    if (!clienteId || clienteId <= 0) {
        alert("Informe um ID de cliente válido.");
        return;
    }

    container.innerHTML = "<p>Carregando...</p>";

    try {
        const response = await fetch(`${API_URL}/relatorios/cliente/${clienteId}`);
        const vendas = await response.json();
        renderizarTabelaVendas(vendas, container);
    } catch (error) {
        container.innerHTML = '<p class="sem-dados">Erro ao buscar dados.</p>';
        console.error(error);
    }
}

async function carregarGrafico() {
    const ano = document.getElementById("anoGrafico").value;

    if (!ano) {
        alert("Informe o ano.");
        return;
    }

    try {
        const response = await fetch(`${API_URL}/relatorios/grafico-anual?ano=${ano}`);
        const dados = await response.json();

        // Se já existe um gráfico, destrói antes de criar outro
        if (graficoInstance) {
            graficoInstance.destroy();
        }

        const ctx = document.getElementById("graficoVendas").getContext("2d");

        graficoInstance = new Chart(ctx, {
            type: "bar", // Gráfico de barras
            data: {
                labels: MESES,
                datasets: [{
                    label: `Vendas em ${ano} (R$)`,
                    data: dados.totais,
                    backgroundColor: "rgba(26, 115, 232, 0.7)",
                    borderColor: "rgba(26, 115, 232, 1)",
                    borderWidth: 1,
                    borderRadius: 4
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: true,
                        position: "top"
                    },
                    tooltip: {
                        callbacks: {
                            // Formata o valor como moeda brasileira
                            label: function(context) {
                                const val = context.raw.toFixed(2).replace(".", ",");
                                return ` R$ ${val}`;
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return "R$ " + value.toFixed(0);
                            }
                        }
                    }
                }
            }
        });
    } catch (error) {
        alert("Erro ao carregar dados do gráfico.");
        console.error(error);
    }
}

function renderizarTabelaVendas(vendas, container) {
    if (!vendas || vendas.length === 0) {
        container.innerHTML = '<p class="sem-dados">Nenhuma venda encontrada.</p>';
        return;
    }

    let totalGeral = 0;
    let html = `
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Data</th>
                    <th>Cliente ID</th>
                    <th>Qtd Itens</th>
                    <th>Total</th>
                </tr>
            </thead>
            <tbody>
    `;

    vendas.forEach(v => {
        const data = new Date(v.data).toLocaleDateString("pt-BR");
        const total = v.valorTotal;
        totalGeral += total;
        html += `
            <tr>
                <td>${v.id}</td>
                <td>${data}</td>
                <td>${v.clienteId}</td>
                <td>${v.itens ? v.itens.length : 0}</td>
                <td>R$ ${total.toFixed(2).replace(".", ",")}</td>
            </tr>
        `;
    });

    html += `</tbody></table>`;
    html += `<div class="total-relatorio">Total Geral: R$ ${totalGeral.toFixed(2).replace(".", ",")}</div>`;

    container.innerHTML = html;
}

window.onload = function () {
    const anoAtual = new Date().getFullYear();
    document.getElementById("anoGrafico").value = anoAtual;
    carregarGrafico();
};

window.onload = function () {
    const anoAtual = new Date().getFullYear();
    document.getElementById("anoGrafico").value = anoAtual;
    carregarGrafico();
};