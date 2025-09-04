import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import type { Pessoa } from '../types/Pessoa';
import { listarPessoas, buscarPorCodigo, buscarPorCpf, deletarPessoa,buscarPorCpfECodigo} from '../api/pessoaApi';
import '../styles/PessoaList.css';
import { FaPencilAlt, FaTrashAlt } from 'react-icons/fa';
import Confirm from '../components/Confirm';

export default function PessoaList() {
  const [pessoas, setPessoas] = useState<(Pessoa & { sincronizado: boolean })[]>([]);
  const [loading, setLoading] = useState(true);
  const [erro, setErro] = useState<string | null>(null);
  const [filtroNome, setFiltroNome] = useState('');
  const [codigoBusca, setCodigoBusca] = useState('');
  const [cpfBusca, setCpfBusca] = useState('');
  const navigate = useNavigate();
  const [confirmExcluir, setConfirmExcluir] = useState<{ ativo: boolean; codigo?: number }>({ ativo: false });
  const [mensagem, setMensagem] = useState<{ texto: string; tipo: 'erro' | 'sucesso' } | null>(null);


  useEffect(() => {
    async function carregar() {
      try {
        const dadosApi = await listarPessoas();
        const registrosTemp: Pessoa[] = JSON.parse(localStorage.getItem('registrosTemp') || '[]');

        const pessoasComStatus = [
          ...registrosTemp.map(r => ({ ...r, sincronizado: false })),
          ...dadosApi.map(r => ({ ...r, sincronizado: true })),
        ];

        setPessoas(pessoasComStatus);
      } catch (e: any) {
        setErro(e.message);
      } finally {
        setLoading(false);
      }
    }

    carregar();
  }, []);

  const pessoasFiltradas = pessoas.filter(p =>
    p.nome.toLowerCase().includes(filtroNome.toLowerCase())
  );

  async function handleBuscar() {
    try {
      setErro(null);
      setLoading(true);

      if (codigoBusca.trim() && cpfBusca.trim()) {
        const pessoa = await buscarPorCpfECodigo(cpfBusca, Number(codigoBusca));
        setPessoas([{ ...pessoa, sincronizado: true }]);
      } else if (codigoBusca.trim()) {

        const pessoa = await buscarPorCodigo(Number(codigoBusca));
        setPessoas([{ ...pessoa, sincronizado: true }]);
      } else if (cpfBusca.trim()) {

        const pessoa = await buscarPorCpf(cpfBusca);
        setPessoas([{ ...pessoa, sincronizado: true }]);
      } else {

        const dadosApi = await listarPessoas();
        const registrosTemp: Pessoa[] = JSON.parse(localStorage.getItem('registrosTemp') || '[]');

        const pessoasComStatus = [
          ...registrosTemp.map(r => ({ ...r, sincronizado: false })),
          ...dadosApi.map(r => ({ ...r, sincronizado: true })),
        ];

        setPessoas(pessoasComStatus);
      }
    } catch (e: any) {
      setErro(e.message);
      setPessoas([]);
    } finally {
      setLoading(false);
    }
  }


  async function handleExcluirConfirmado() {
    const pessoaParaExcluir = pessoas.find(p => p.codigo === confirmExcluir.codigo);
    if (!pessoaParaExcluir) return;

    if (confirmExcluir.temporario) {
      const registrosTemp: Pessoa[] = JSON.parse(localStorage.getItem('registrosTemp') || '[]');
      const novosTemp = registrosTemp.filter(p => p !== pessoaParaExcluir);
      localStorage.setItem('registrosTemp', JSON.stringify(novosTemp));

      setPessoas(prev => prev.filter(p => p !== pessoaParaExcluir));
      setConfirmExcluir({ ativo: false });
      setMensagem({ texto: 'Registro temporário excluído!', tipo: 'sucesso' });
    } else {
      try {
        setLoading(true);
        await deletarPessoa(pessoaParaExcluir.codigo!);

        setPessoas(prev => prev.filter(p => p.codigo !== pessoaParaExcluir.codigo));
        setMensagem({ texto: 'Pessoa excluída com sucesso!', tipo: 'sucesso' });
      } catch (e: any) {
        setMensagem({ texto: e.message || 'Erro ao excluir a pessoa', tipo: 'erro' });
      } finally {
        setLoading(false);
        setConfirmExcluir({ ativo: false });
      }
    }
  }


  useEffect(() => {
    if (mensagem) {
      const timer = setTimeout(() => setMensagem(null), 3000);
      return () => clearTimeout(timer);
    }
  }, [mensagem]);

  if (loading) return <p className="loading">Carregando...</p>;

  return (
    <div className="pessoa-list-container">
      <div className="header">
        <h2>Lista de Pessoas</h2>
      </div>

      {erro && <div className="mensagem-erro">⚠️ Erro ao carregar os dados: {erro}</div>}

      <div className="filtros">
        <div className="filtro-item">
          <label>Filtrar por nome</label>
          <input
            type="text"
            placeholder="Digite o nome..."
            value={filtroNome}
            onChange={e => setFiltroNome(e.target.value)}
          />
        </div>

        <div className="filtro-item">
          <label>Buscar por código</label>
          <input
            type="number"
            placeholder="Ex: 123"
            value={codigoBusca}
            onChange={e => setCodigoBusca(e.target.value)}
          />
        </div>

        <div className="filtro-item" style={{ flex: 1 }}>
          <label>Buscar por CPF</label>
          <input
            type="text"
            placeholder="000.000.000-00"
            value={cpfBusca}
            onChange={(e) => {
              let v = e.target.value.replace(/\D/g, '');
              if (v.length > 11) v = v.slice(0, 11);
              v = v.replace(/(\d{3})(\d)/, '$1.$2');
              v = v.replace(/(\d{3})(\d)/, '$1.$2');
              v = v.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
              setCpfBusca(v);
            }}
          />
        </div>

        <div className="botoes-filtros">
          <button className="buscar-button" onClick={handleBuscar}>Buscar</button>
          <button className="add-button" onClick={() => navigate('/pessoas/adicionar')}>+ Adicionar</button>
        </div>
      </div>

      <div className="tabela-container">
        <table className="tabela">
          <thead>
            <tr>
              <th className="editar-coluna"></th>
              <th>Nome</th>
              <th>CPF</th>
              <th>Email</th>
              <th>Telefone</th>
              <th>Cidade</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {pessoasFiltradas.map((p, idx) => (
              <tr key={p.codigo ?? idx} className={!p.sincronizado ? 'nao-sincronizado' : ''}>
                <td>
                  <div className="acoes-botoes">
                    <button
                      className="editar-button"
                      title="Editar"
                      onClick={() =>
                        navigate('/pessoas/adicionar', { state: { pessoa: p, temporario: !p.sincronizado } })
                      }
                    >
                      <FaPencilAlt />
                    </button>

                    <button
                      className="excluir-button"
                      onClick={() => setConfirmExcluir({ ativo: true, codigo: p.codigo, temporario: !p.sincronizado })}
                    >
                      <FaTrashAlt />
                    </button>
                  </div>
                </td>

                <td>{p.codigo ? `${p.codigo} - ${p.nome}` : p.nome}</td>
                <td>{p.cpf}</td>
                <td>{p.email || '-'}</td>
                <td>{p.telefone || '-'}</td>
                <td>{p.cidade || '-'}</td>
                <td>{p.sincronizado ? 'Sincronizado' : 'Não sincronizado'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <div className="rodape">
        <button className="back-button" onClick={() => navigate('/')}>Voltar</button>
      </div>

      {confirmExcluir.ativo && (
        <Confirm
          texto="Tem certeza que deseja excluir esta pessoa?"
          onConfirm={handleExcluirConfirmado}
          onCancel={() => setConfirmExcluir({ ativo: false })}
        />
      )}

      {mensagem && (
        <div className={`toast ${mensagem.tipo}`}>
          {mensagem.texto}
        </div>
      )}
    </div>
  );
}
