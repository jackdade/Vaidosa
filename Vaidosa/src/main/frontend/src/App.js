import React, { useState, useEffect } from 'react';
import { IMaskInput } from 'react-imask';
import './App.css';

export default function App() {
  const [activeTab, setActiveTab] = useState('nova');
  const [simulacoes, setSimulacoes] = useState([]);
  const [produtos, setProdutos] = useState([]);
  const [produtoSelecionado, setProdutoSelecionado] = useState('');
  const [formData, setFormData] = useState({
    nomePessoa: '',
    cpf: '',
    valorSegurado: '',
    numeroContrato: '',
    fimContrato: '',
    dataNascimento: ''
  });
  const [simulationResult, setSimulationResult] = useState(null);

  const buscarSimulacoes = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/Simulacao/listar');
      if (!response.ok) throw new Error('Erro ao buscar simulações');
      const data = await response.json();
      setSimulacoes(data);
    } catch (error) {
      console.error('Erro:', error);
    }
  };

  const buscarProdutos = async () => {
    try {
      const response = await fetch('http://localhost:8080/api/Produto/listar');
      if (!response.ok) throw new Error('Erro ao buscar produtos');
      const data = await response.json();
      setProdutos(data);
    } catch (error) {
      console.error('Erro ao buscar produtos:', error);
    }
  };

  useEffect(() => {
    buscarSimulacoes();
    buscarProdutos();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const validarCPF = (cpf) => {
    cpf = cpf.replace(/\D/g, '');
    if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;

    let soma = 0;
    let resto;

    for (let i = 1; i <= 9; i++) soma += parseInt(cpf.substring(i-1, i)) * (11 - i);
    resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    if (resto !== parseInt(cpf.substring(9, 10))) return false;

    soma = 0;
    for (let i = 1; i <= 10; i++) soma += parseInt(cpf.substring(i-1, i)) * (12 - i);
    resto = (soma * 10) % 11;
    if (resto === 10 || resto === 11) resto = 0;
    return resto === parseInt(cpf.substring(10, 11));
  };

  const diferencaMeses = (dataAtual, dataFim) => {
    const anoAtual = dataAtual.getFullYear();
    const mesAtual = dataAtual.getMonth();
    const anoFim = dataFim.getFullYear();
    const mesFim = dataFim.getMonth();
    return (anoFim - anoAtual) * 12 + (mesFim - mesAtual);
  };

  const calcularIdade = (dataNascimento) => {
    const hoje = new Date();
    const nascimento = new Date(dataNascimento);
    let idade = hoje.getFullYear() - nascimento.getFullYear();
    const mesDiff = hoje.getMonth() - nascimento.getMonth();
    if (mesDiff < 0 || (mesDiff === 0 && hoje.getDate() < nascimento.getDate())) idade--;
    return idade;
  };

  const handleSimular = async () => {
    if (!produtoSelecionado) return alert('Selecione um produto antes de simular.');

    const produto = produtos.find(p => p.id === parseInt(produtoSelecionado));
    if (!produto) return alert('Produto inválido.');
    if (!validarCPF(formData.cpf)) return alert('CPF inválido!');

    const idade = calcularIdade(formData.dataNascimento);
    if (idade < produto.idadeMinima || idade > produto.idadeMaxima) {
      return alert(`Idade da pessoa (${idade} anos) fora do intervalo do produto (${produto.idadeMinima} a ${produto.idadeMaxima} anos).`);
    }

    const mesesDiferenca = diferencaMeses(new Date(), new Date(formData.fimContrato));
    if (mesesDiferenca > 120) return alert('Não é possível simular seguro: diferença maior que 120 meses.');

    const valor = parseFloat(formData.valorSegurado.replace(/[^\d,]/g, '').replace(',', '.'));
    if (isNaN(valor)) return alert('Informe um valor válido para "Valor Segurado".');

    const payload = {
      nomePessoa: formData.nomePessoa,
      cpf: formData.cpf.replace(/\D/g, ''),
      valorSegurado: valor,
      numeroContratoEmprestimo: formData.numeroContrato,
      fimContratoEmprestimo: formData.fimContrato,
      dataNascimento: formData.dataNascimento,
      produtoId: parseInt(produtoSelecionado)
    };

    try {
      const response = await fetch('http://localhost:8080/api/Simulacao/simular', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });

      if (!response.ok) throw new Error('Erro ao simular');
      const resultado = await response.json();

      setSimulationResult({
        valorTotalPrimio: `R$ ${resultado.valorTotalPremio.toFixed(2).replace('.', ',')}`,
        melhorOpcao: resultado.produtoNome
      });

      setFormData({
        nomePessoa: '',
        cpf: '',
        valorSegurado: '',
        numeroContrato: '',
        fimContrato: '',
        dataNascimento: ''
      });
      setProdutoSelecionado('');
    } catch (error) {
      console.error('Erro na simulação:', error);
      alert('Ocorreu um erro ao simular.');
    }
  };

  const formatarValorMonetario = (valor) =>
    new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(valor);

  const formatarData = (data) =>
    new Date(data + 'T00:00:00').toLocaleDateString('pt-BR');

  return (
    <div className="app-container">
      <div className="app-wrapper">
        <div className="tabs-container">
          <button
            onClick={() => setActiveTab('nova')}
            className={`tab-button tab-left ${activeTab === 'nova' ? 'tab-active' : ''}`}
          >
            ➕ Nova Simulação
          </button>
          <button
            onClick={() => setActiveTab('lista')}
            className={`tab-button tab-right ${activeTab === 'lista' ? 'tab-active' : ''}`}
          >
            📋 Simulações ({simulacoes.length})
          </button>
        </div>

        {activeTab === 'nova' && (
          <div className="form-container">
            <h2 className="form-title">Simulação Seguro Prestamista</h2>
            <form className="form-grid" onSubmit={(e) => { e.preventDefault(); handleSimular(); }}>
              <div className="form-group form-group-full">
                <label className="form-label">Escolha um Produto</label>
                <select value={produtoSelecionado} onChange={(e) => setProdutoSelecionado(e.target.value)} className="form-input" required>
                  <option value="">Selecione um produto</option>
                  {produtos.map(produto => (
                    <option key={produto.id} value={produto.id}>{produto.nome}</option>
                  ))}
                </select>
              </div>

              <div className="form-group form-group-full">
                <label className="form-label">Nome da Pessoa</label>
                <input
                  type="text"
                  name="nomePessoa"
                  value={formData.nomePessoa}
                  onChange={handleInputChange}
                  className="form-input"
                  placeholder="Digite o nome completo"
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">CPF</label>
                <IMaskInput
                  mask="000.000.000-00"
                  value={formData.cpf}
                  onAccept={(value) => setFormData(prev => ({ ...prev, cpf: value }))}
                  placeholder="000.000.000-00"
                  className="form-input"
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Valor Segurado</label>
                <IMaskInput
                  mask={Number}
                  scale={2}
                  signed={false}
                  thousands="."
                  radix=","
                  mapToRadix={['.']}
                  padFractionalZeros
                  value={formData.valorSegurado}
                  onAccept={(value) => setFormData(prev => ({ ...prev, valorSegurado: value }))}
                  placeholder="R$ 0,00"
                  className="form-input"
                  required
                />
              </div>

              <div className="form-group form-group-full">
                <label className="form-label">Número Contrato Empréstimo</label>
                <input
                  type="text"
                  name="numeroContrato"
                  value={formData.numeroContrato}
                  onChange={handleInputChange}
                  className="form-input"
                  placeholder="Digite o número do contrato"
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Fim Contrato Empréstimo</label>
                <div className="date-input-container">
                  <input
                    type="date"
                    name="fimContrato"
                    value={formData.fimContrato}
                    onChange={handleInputChange}
                    className="form-input"
                    required
                  />
                  <span className="date-icon">📅</span>
                </div>
              </div>

              <div className="form-group">
                <label className="form-label">Data Nascimento</label>
                <div className="date-input-container">
                  <input
                    type="date"
                    name="dataNascimento"
                    value={formData.dataNascimento}
                    onChange={handleInputChange}
                    className="form-input"
                    required
                  />
                  <span className="date-icon">📅</span>
                </div>
              </div>

              <div className="button-container">
                <button type="submit" className="simulate-button">✓ Simular</button>
              </div>
            </form>

            {simulationResult && (
              <div className="result-container">
                <p className="result-label">Valor Total Prêmio</p>
                <p className="result-value">{simulationResult.valorTotalPrimio}</p>
                <p className="result-label">Melhor Opção:</p>
                <p className="result-option">{simulationResult.melhorOpcao}</p>
              </div>
            )}
          </div>
        )}

        {activeTab === 'lista' && (
          <div className="table-container">
            <h2 className="table-title">Simulação Seguro Prestamista</h2>
            <div className="table-wrapper">
              {simulacoes.length === 0 ? (
                <div className="no-data"><p>Nenhuma simulação encontrada.</p></div>
              ) : (
                <table className="data-table">
                  <thead className="table-head">
                    <tr>
                      <th>Pessoa</th>
                      <th>CPF</th>
                      <th>Valor Segurado</th>
                      <th>Nº Contrato</th>
                      <th>Fim Contrato</th>
                      <th>Valor Prêmio</th>
                      <th>Produto</th>
                    </tr>
                  </thead>
                  <tbody className="table-body">
                    {simulacoes.map((simulacao, index) => (
                      <tr key={simulacao.id} className={index % 2 === 0 ? 'row-even' : 'row-odd'}>
                        <td>{simulacao.nomePessoa}</td>
                        <td>{simulacao.cpf}</td>
                        <td>{formatarValorMonetario(simulacao.valorSegurado)}</td>
                        <td>{simulacao.numeroContratoEmprestimo}</td>
                        <td>{formatarData(simulacao.fimContratoEmprestimo)}</td>
                        <td>{formatarValorMonetario(simulacao.valorTotalPremio)}</td>
                        <td>{simulacao.produtoNome}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              )}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
