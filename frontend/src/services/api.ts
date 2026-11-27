
import type {
  Estabelecimento,
  PageResponse,
  Usuario,
  LoginResponse,
  UsuarioRequest,
  EstabelecimentoRequest,
} from '../types';

const API_URL =
  import.meta.env.VITE_API_URL ?? 'http://localhost:8080/api';

const TOKEN_KEY = 'agendapro_token';

async function request<T>(
  path: string,
  init?: RequestInit
): Promise<T> {
  const token = localStorage.getItem(TOKEN_KEY);

  const response = await fetch(`${API_URL}${path}`, {
    ...init,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(init?.headers ?? {}),
    },
  });

  if (!response.ok) {
    const body = await response.json().catch(() => null);
    throw new Error(
      body?.mensagem ?? `Erro HTTP ${response.status}`
    );
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

export const authApi = {
  login: async (email: string, senha: string) => {
    const resposta = await request<LoginResponse>('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ email, senha }),
    });

    localStorage.setItem(TOKEN_KEY, resposta.token);

    return resposta;
  },

  registrarProprietario: (data: UsuarioRequest) =>
    request<Usuario>('/usuarios/proprietarios', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  logout: () => {
    localStorage.removeItem(TOKEN_KEY);
  },

  estaAutenticado: () => {
    return Boolean(localStorage.getItem(TOKEN_KEY));
  },
};

export const estabelecimentoApi = {
  listar: (nome = '') =>
    request<PageResponse<Estabelecimento>>(
      `/estabelecimentos?nome=${encodeURIComponent(nome)}&page=0&size=20&sort=nome,asc`
    ),

  buscar: (id: number) =>
    request<Estabelecimento>(`/estabelecimentos/${id}`),

  criar: (data: EstabelecimentoRequest) =>
    request<Estabelecimento>('/estabelecimentos', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  atualizar: (id: number, data: EstabelecimentoRequest) =>
    request<Estabelecimento>(`/estabelecimentos/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    }),

  desativar: (id: number) =>
    request<void>(`/estabelecimentos/${id}`, {
      method: 'DELETE',
    }),
};
