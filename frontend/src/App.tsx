import { useEffect, useState } from 'react';
import { estabelecimentoApi } from './services/api';
import type { Estabelecimento } from './types';
import './styles.css';

export default function App() {
  const [items, setItems] = useState<Estabelecimento[]>([]);
  const [nome, setNome] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  async function carregar(filtro = '') {
    setLoading(true); setError('');
    try { const page = await estabelecimentoApi.listar(filtro); setItems(page.content); }
    catch (e) { setError(e instanceof Error ? e.message : 'Não foi possível carregar os estabelecimentos.'); }
    finally { setLoading(false); }
  }
  useEffect(() => { void carregar(); }, []);

  return <main className="shell">
    <header className="topbar"><div className="brand"><span className="brand-mark">A</span><span>AgendaPro</span></div><span className="tagline">Seu tempo, melhor organizado.</span></header>
    <section className="hero"><p className="eyebrow">AGENDA DE SERVIÇOS</p><h1>Encontre o serviço.<br/><em>Reserve seu horário.</em></h1><p className="intro">Uma plataforma para descobrir estabelecimentos e organizar seus agendamentos.</p>
      <form className="search" onSubmit={e => { e.preventDefault(); void carregar(nome); }}><input aria-label="Buscar estabelecimento" value={nome} onChange={e => setNome(e.target.value)} placeholder="Buscar por nome do estabelecimento"/><button type="submit">Buscar</button></form>
    </section>
    <section className="results"><div className="section-heading"><div><p className="eyebrow">DESCUBRA</p><h2>Estabelecimentos</h2></div><span className="count">{items.length} encontrados</span></div>
      {loading && <p className="state">Carregando estabelecimentos...</p>}
      {error && <div className="error" role="alert">{error} <button onClick={() => void carregar(nome)}>Tentar novamente</button></div>}
      {!loading && !error && items.length === 0 && <p className="state">Nenhum estabelecimento encontrado. Tente outra busca.</p>}
      <div className="cards">{items.map(item => <article className="place-card" key={item.id}><div className="place-icon">✳</div><div><h3>{item.nome}</h3><p>{item.descricao || 'Estabelecimento cadastrado no AgendaPro.'}</p><small>{item.endereco || 'Endereço não informado'}</small></div></article>)}</div>
    </section>
    <footer>AgendaPro · Projeto acadêmico e produto em desenvolvimento</footer>
  </main>;
}
