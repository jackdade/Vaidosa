import { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import '../styles/PessoaForm.css';
import type Pessoa from '../types/Pessoa';
import { inserirPessoa, editarPessoa } from '../api/PessoaApi';

export default function PessoaForm() {
  const navigate = useNavigate();
  const location = useLocation();
  const pessoaEdicao = location.state?.pessoa;
  const temporarioEdicao = location.state?.temporario ?? false;
  const [mensagem, setMensagem] = useState<{ texto: string; tipo: 'erro' | 'sucesso' } | null>(null);
  const [loading, setLoading] = useState(false);

  const [pessoa, setPessoa] = useState<Pessoa>({
    codigo: pessoaEdicao?.codigo,
    nome: pessoaEdicao?.nome || '',
    cpf: pessoaEdicao?.cpf || '',
    email: pessoaEdicao?.email || '',
    telefone: pessoaEdicao?.telefone || '',
    dataNascimento: pessoaEdicao?.dataNascimento || '',
    endereco: pessoaEdicao?.endereco || '',
    cep: pessoaEdicao?.cep || '',
    cidade: pessoaEdicao?.cidade || '',
    estado: pessoaEdicao?.estado || '',
    ativo: pessoaEdicao?.ativo ?? true,
  });


  useEffect(() => {
    if (mensagem) {
      const timer = setTimeout(() => setMensagem(null), 3000);
      return () => clearTimeout(timer);
    }
  }, [mensagem]);

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    const { name, value, type, checked } = e.target;
    let novoValor = value;

    if (name === 'cpf') {
      novoValor = value.replace(/\D/g, '')
        .replace(/^(\d{3})(\d)/, '$1.$2')
        .replace(/^(\d{3})\.(\d{3})(\d)/, '$1.$2.$3')
        .replace(/\.(\d{3})(\d{1,2})$/, '.$1-$2')
        .substring(0, 14);
    }

    if (name === 'cep') {
      novoValor = value.replace(/\D/g, '')
        .replace(/^(\d{5})(\d)/, '$1-$2')
        .substring(0, 9);
    }

    if (name === 'telefone') {
      novoValor = value.replace(/\D/g, '');
      if (novoValor.length <= 10) {
        novoValor = novoValor.replace(/^(\d{2})(\d{4})(\d)/, '($1) $2-$3');
      } else {
        novoValor = novoValor.replace(/^(\d{2})(\d{5})(\d)/, '($1) $2-$3');
      }
    }

    setPessoa(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : novoValor,
    }));
  }

  function salvarTemporario() {
    if (!pessoa.nome.trim() || !pessoa.cpf.trim()) {
      setMensagem({ texto: 'Nome e CPF são obrigatórios!', tipo: 'erro' });
      return;
    }

    const registrosTemp = JSON.parse(localStorage.getItem('registrosTemp') || '[]');
    registrosTemp.push(pessoa);
    localStorage.setItem('registrosTemp', JSON.stringify(registrosTemp));
    setMensagem({ texto: 'Registro salvo temporariamente!', tipo: 'sucesso' });

    setTimeout(() => navigate(-1), 1500);
  }

  async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();

    if (!pessoa.nome.trim() || !pessoa.cpf.trim()) {
      setMensagem({ texto: 'Nome e CPF são obrigatórios!', tipo: 'erro' });
      return;
    }

    setLoading(true);

    try {
      if (temporarioEdicao) {
        const resultado = await inserirPessoa(pessoa);

        const registrosTemp: Pessoa[] = JSON.parse(localStorage.getItem('registrosTemp') || '[]');
        const novosTemp = registrosTemp.filter(p => p.cpf !== pessoa.cpf);
        localStorage.setItem('registrosTemp', JSON.stringify(novosTemp));

        setMensagem({ texto: resultado?.message || 'Pessoa sincronizada com sucesso!', tipo: 'sucesso' });
      } else {
        const resultado = pessoaEdicao
          ? await editarPessoa(pessoa)
          : await inserirPessoa(pessoa);

        const mensagemSucesso = resultado?.message ||
          (pessoaEdicao ? 'Pessoa editada com sucesso!' : 'Pessoa cadastrada com sucesso!');
        setMensagem({ texto: mensagemSucesso, tipo: 'sucesso' });
      }

      setTimeout(() => navigate(-1), 1500);
    } catch (error: any) {
      setMensagem({ texto: error?.message || 'Erro ao salvar pessoa', tipo: 'erro' });
    } finally {
      setLoading(false);
    }
  }

  function handleCancel() {
    navigate(-1);
  }

  return (
    <div className="pessoa-form-page">
      {mensagem && (
        <div className={`toast ${mensagem.tipo}`}>
          {mensagem.texto}
        </div>
      )}

      <div className="form-header">
        <h1 className="form-title">{pessoaEdicao ? 'Editar Pessoa' : 'Cadastro de Pessoa'}</h1>
        <p className="form-subtitle">
          {pessoaEdicao ? 'Atualize os dados abaixo' : 'Preencha os dados abaixo para cadastrar uma nova pessoa'}
        </p>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="form-content">
          <div className="form-grid">
            <div className="form-field-full nome-ativo-row">
              <div className="form-field">
                <label className="form-label">Nome Completo *</label>
                <input
                  name="nome"
                  type="text"
                  placeholder="Digite o nome completo"
                  value={pessoa.nome}
                  onChange={handleChange}
                  required
                  disabled={loading || !!pessoaEdicao}
                  className="form-input"
                />
              </div>

              <div className="checkbox-wrapper">
                <input
                  name="ativo"
                  type="checkbox"
                  checked={pessoa.ativo}
                  onChange={handleChange}
                  disabled={loading}
                  className="form-checkbox"
                />
                <span className="checkbox-label">Pessoa está ativa</span>
              </div>
            </div>

            <div className="form-field">
              <label className="form-label">CPF *</label>
              <input
                name="cpf"
                type="text"
                placeholder="000.000.000-00"
                value={pessoa.cpf}
                onChange={handleChange}
                required
                disabled={loading}
                className="form-input"
              />
            </div>

            <div className="form-field">
              <label className="form-label">Data de Nascimento</label>
              <input
                name="dataNascimento"
                type="date"
                value={pessoa.dataNascimento}
                onChange={handleChange}
                disabled={loading}
                className="form-input"
              />
            </div>

            <div className="form-field">
              <label className="form-label">Email</label>
              <input
                name="email"
                type="email"
                placeholder="exemplo@email.com"
                value={pessoa.email}
                onChange={handleChange}
                disabled={loading}
                className="form-input"
              />
            </div>

            <div className="form-field">
              <label className="form-label">Telefone</label>
              <input
                name="telefone"
                type="tel"
                placeholder="(48) 99999-9999"
                value={pessoa.telefone}
                onChange={handleChange}
                disabled={loading}
                className="form-input"
              />
            </div>
          </div>

          <div className="endereco-section">
            <div className="endereco-header">
              <svg className="endereco-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
              <h3 className="endereco-title">Endereço</h3>
            </div>

            <div className="endereco-grid">
              <div className="form-field">
                <label className="form-label">CEP</label>
                <input
                  name="cep"
                  type="text"
                  placeholder="00000-000"
                  value={pessoa.cep}
                  onChange={handleChange}
                  disabled={loading}
                  className="form-input"
                />
              </div>

              <div className="form-field">
                <label className="form-label">Cidade</label>
                <input
                  name="cidade"
                  type="text"
                  placeholder="Cidade"
                  value={pessoa.cidade}
                  onChange={handleChange}
                  disabled={loading}
                  className="form-input"
                />
              </div>

              <div className="form-field">
                <label className="form-label">Estado</label>
                <input
                  name="estado"
                  type="text"
                  placeholder="UF"
                  value={pessoa.estado}
                  onChange={handleChange}
                  disabled={loading}
                  className="form-input"
                />
              </div>

              <div className="form-field form-field-full">
                <label className="form-label">Endereço Completo</label>
                <input
                  name="endereco"
                  type="text"
                  placeholder="Rua, número, bairro"
                  value={pessoa.endereco}
                  onChange={handleChange}
                  disabled={loading}
                  className="form-input"
                />
              </div>
            </div>
          </div>

          <div className="form-buttons">
            <button
              type="button"
              onClick={handleCancel}
              disabled={loading}
              className="btn-cancelar"
            >
              Cancelar
            </button>

            {!pessoa.codigo && (
              <button
                type="button"
                onClick={salvarTemporario}
                disabled={loading}
                className="btn-temporario"
              >
                Salvar Temporariamente
              </button>
            )}

            <button
              type="submit"
              disabled={loading}
              className="btn-salvar"
            >
              {loading ? 'Salvando...' : 'Salvar Pessoa'}
            </button>
          </div>
        </div>
      </form>
    </div>
  );
}
