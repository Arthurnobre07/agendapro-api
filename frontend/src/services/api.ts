import type { Estabelecimento, PageResponse } from '../types';
const API_URL = import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api';

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_URL}${path}`, {
    ...init,
    headers: { 'Content-Type': 'application/json', ...(init?.headers ?? {}) },
  });
  if (!response.ok) {
    const body = await response.json().catch(() => null);
    throw new Error(body?.mensagem ?? `Erro HTTP ${response.status}`);
  }
  if (response.status === 204) return undefined as T;
  return response.json() as Promise<T>;
}
export const estabelecimentoApi = {
  listar: (nome = '') => request<PageResponse<Estabelecimento>>(`/estabelecimentos?nome=${encodeURIComponent(nome)}&page=0&size=20&sort=nome,asc`),
  criar: (data: Omit<Estabelecimento, 'id' | 'ativo' | 'criadoEm'>) => request<Estabelecimento>('/estabelecimentos', { method: 'POST', body: JSON.stringify(data) }),
};
