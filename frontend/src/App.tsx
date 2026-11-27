
import { useEffect, useState } from 'react';
import { authApi, estabelecimentoApi } from './services/api';
import type { Estabelecimento, EstabelecimentoRequest } from './types';
import './styles.css';

type Tela = 'login' | 'cadastro' | 'painel';

const formularioVazio: EstabelecimentoRequest = {
  nome: '',
  descricao: '',
  telefone: '',
  endereco: '',
};

export default function App() {
  const [tela, setTela] = useState<Tela>(
    authApi.estaAutenticado() ? 'painel' : 'login'
  );

  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  const [nome, setNome] = useState('');
  const [telefone, setTelefone] = useState('');

  const [estabelecimentos, setEstabelecimentos] =
    useState<Estabelecimento[]>([]);
  const [formulario, setFormulario] =
    useState<EstabelecimentoRequest>(formularioVazio);
  const [editando, setEditando] = useState<number | null>(null);

  const [loading, setLoading] = useState(false);
  const [erro, setErro] = useState('');
  const [sucesso, setSucesso] = useState('');

  async function carregarEstabelecimentos() {
    setLoading(true);
    setErro('');

    try {
      const resposta = await estabelecimentoApi.listar();
      setEstabelecimentos(resposta.content);
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Erro ao carregar estabelecimentos.');
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    if (tela === 'painel') {
      void carregarEstabelecimentos();
    }
  }, [tela]);

  async function fazerLogin(e: React.FormEvent) {
    e.preventDefault();
    setErro('');
    setLoading(true);

    try {
      await authApi.login(email, senha);
      setSenha('');
      setTela('painel');
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Não foi possível entrar.');
    } finally {
      setLoading(false);
    }
  }

  async function cadastrarProprietario(e: React.FormEvent) {
    e.preventDefault();
    setErro('');
    setLoading(true);

    try {
      await authApi.registrarProprietario({
        nome,
        email,
        senha,
        telefone,
      });

      setSucesso('Cadastro realizado! Entre com seu e-mail e senha.');
      setSenha('');
      setTela('login');
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Não foi possível realizar o cadastro.');
    } finally {
      setLoading(false);
    }
  }

  async function salvarEstabelecimento(e: React.FormEvent) {
    e.preventDefault();
    setErro('');
    setSucesso('');
    setLoading(true);

    try {
      if (editando !== null) {
        await estabelecimentoApi.atualizar(editando, formulario);
        setSucesso('Estabelecimento atualizado com sucesso!');
      } else {
        await estabelecimentoApi.criar(formulario);
        setSucesso('Estabelecimento cadastrado com sucesso!');
      }

      setFormulario(formularioVazio);
      setEditando(null);
      await carregarEstabelecimentos();
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Não foi possível salvar.');
    } finally {
      setLoading(false);
    }
  }

  function prepararEdicao(item: Estabelecimento) {
    setEditando(item.id);
    setFormulario({
      nome: item.nome,
      descricao: item.descricao ?? '',
      telefone: item.telefone ?? '',
      endereco: item.endereco ?? '',
    });

    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  async function desativarEstabelecimento(id: number) {
    if (!window.confirm('Deseja realmente desativar este estabelecimento?')) {
      return;
    }

    setErro('');
    setSucesso('');

    try {
      await estabelecimentoApi.desativar(id);
      setSucesso('Estabelecimento desativado com sucesso!');
      await carregarEstabelecimentos();
    } catch (e) {
      setErro(e instanceof Error ? e.message : 'Não foi possível desativar.');
    }
  }

  function sair() {
    authApi.logout();
    setEstabelecimentos([]);
    setTela('login');
    setErro('');
    setSucesso('');
  }

  return (
    <main className="shell">
      <header className="topbar">
        <div className="brand">
          <span className="brand-mark">A</span>
          <span>AgendaPro</span>
        </div>

        {tela === 'painel' ? (
          <button className="secondary-button" onClick={sair}>
            Sair da conta
          </button>
        ) : (
          <span className="tagline">Seu tempo, melhor organizado.</span>
        )}
      </header>

      {tela === 'login' && (
        <section className="auth-section">
          <p className="eyebrow">BEM-VINDO AO AGENDAPRO</p>
          <h1>Acesse sua <em>conta.</em></h1>
          <p className="intro">
            Entre para administrar seus estabelecimentos e organizar seus serviços.
          </p>

          <form className="auth-form" onSubmit={fazerLogin}>
            <label>E-mail</label>
            <input
              type="email"
              required
              value={email}
              onChange={e => setEmail(e.target.value)}
              placeholder="seu@email.com"
            />

            <label>Senha</label>
            <input
              type="password"
              required
              value={senha}
              onChange={e => setSenha(e.target.value)}
              placeholder="Sua senha"
            />

            {erro && <p className="error" role="alert">{erro}</p>}
            {sucesso && <p className="success">{sucesso}</p>}

            <button type="submit" disabled={loading}>
              {loading ? 'Entrando...' : 'Entrar'}
            </button>
          </form>

          <p className="auth-switch">
            Ainda não tem conta?{' '}
            <button onClick={() => {
              setErro('');
              setSucesso('');
              setTela('cadastro');
            }}>
              Criar conta
            </button>
          </p>
        </section>
      )}

      {tela === 'cadastro' && (
        <section className="auth-section">
          <p className="eyebrow">COMECE AGORA</p>
          <h1>Crie sua <em>conta.</em></h1>
          <p className="intro">
            Cadastre-se como proprietário para administrar seus estabelecimentos.
          </p>

          <form className="auth-form" onSubmit={cadastrarProprietario}>
            <label>Nome completo</label>
            <input
              required
              maxLength={120}
              value={nome}
              onChange={e => setNome(e.target.value)}
              placeholder="Seu nome"
            />

            <label>E-mail</label>
            <input
              type="email"
              required
              maxLength={160}
              value={email}
              onChange={e => setEmail(e.target.value)}
              placeholder="seu@email.com"
            />

            <label>Telefone</label>
            <input
              maxLength={30}
              value={telefone}
              onChange={e => setTelefone(e.target.value)}
              placeholder="(82) 99999-9999"
            />

            <label>Senha</label>
            <input
              type="password"
              required
              minLength={8}
              maxLength={100}
              value={senha}
              onChange={e => setSenha(e.target.value)}
              placeholder="Mínimo de 8 caracteres"
            />

            {erro && <p className="error" role="alert">{erro}</p>}

            <button type="submit" disabled={loading}>
              {loading ? 'Cadastrando...' : 'Criar minha conta'}
            </button>
          </form>

          <p className="auth-switch">
            Já possui conta?{' '}
            <button onClick={() => {
              setErro('');
              setTela('login');
            }}>
              Fazer login
            </button>
          </p>
        </section>
      )}

      {tela === 'painel' && (
        <>
          <section className="dashboard-hero">
            <p className="eyebrow">ÁREA DO PROPRIETÁRIO</p>
            <h1>Seus negócios.<br /><em>Seu controle.</em></h1>
            <p className="intro">
              Gerencie seus estabelecimentos em um só lugar.
            </p>
          </section>

          <section className="management">
            <div className="section-heading">
              <div>
                <p className="eyebrow">GERENCIAMENTO</p>
                <h2>{editando !== null ? 'Editar estabelecimento' : 'Novo estabelecimento'}</h2>
              </div>
            </div>

            <form className="management-form" onSubmit={salvarEstabelecimento}>
              <label>Nome do estabelecimento</label>
              <input
                required
                maxLength={120}
                value={formulario.nome}
                onChange={e => setFormulario({ ...formulario, nome: e.target.value })}
                placeholder="Ex.: Barbearia Central"
              />

              <label>Descrição</label>
              <textarea
                maxLength={500}
                value={formulario.descricao}
                onChange={e => setFormulario({ ...formulario, descricao: e.target.value })}
                placeholder="Conte um pouco sobre seu estabelecimento"
              />

              <label>Telefone</label>
              <input
                maxLength={30}
                value={formulario.telefone}
                onChange={e => setFormulario({ ...formulario, telefone: e.target.value })}
                placeholder="Telefone para contato"
              />

              <label>Endereço</label>
              <input
                maxLength={220}
                value={formulario.endereco}
                onChange={e => setFormulario({ ...formulario, endereco: e.target.value })}
                placeholder="Endereço do estabelecimento"
              />

              {erro && <p className="error" role="alert">{erro}</p>}
              {sucesso && <p className="success">{sucesso}</p>}

              <div className="form-actions">
                <button type="submit" disabled={loading}>
                  {loading ? 'Salvando...' : editando !== null ? 'Salvar alterações' : 'Cadastrar estabelecimento'}
                </button>

                {editando !== null && (
                  <button
                    type="button"
                    className="secondary-button"
                    onClick={() => {
                      setEditando(null);
                      setFormulario(formularioVazio);
                    }}
                  >
                    Cancelar
                  </button>
                )}
              </div>
            </form>
          </section>

          <section className="results">
            <div className="section-heading">
              <div>
                <p className="eyebrow">SUA EMPRESA</p>
                <h2>Meus estabelecimentos</h2>
              </div>
              <span className="count">
                {estabelecimentos.length} encontrados
              </span>
            </div>

            {loading && <p className="state">Carregando...</p>}

            {!loading && estabelecimentos.length === 0 && !erro && (
              <p className="state">
                Você ainda não possui estabelecimentos cadastrados.
              </p>
            )}

            <div className="cards">
              {estabelecimentos.map(item => (
                <article className="place-card" key={item.id}>
                  <div className="place-icon">✳</div>
                  <div className="place-info">
                    <h3>{item.nome}</h3>
                    <p>{item.descricao || 'Sem descrição cadastrada.'}</p>
                    <small>{item.endereco || 'Endereço não informado'}</small>

                    <div className="card-actions">
                      <button onClick={() => prepararEdicao(item)}>
                        Editar
                      </button>
                      <button
                        className="danger-button"
                        onClick={() => void desativarEstabelecimento(item.id)}
                      >
                        Desativar
                      </button>
                    </div>
                  </div>
                </article>
              ))}
            </div>
          </section>
        </>
      )}

      <footer>AgendaPro · Projeto acadêmico e produto em desenvolvimento</footer>
    </main>
  );
}
