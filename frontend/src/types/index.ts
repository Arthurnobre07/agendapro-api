
export interface Estabelecimento {
  id: number;
  nome: string;
  descricao: string | null;
  telefone: string | null;
  endereco: string | null;
  ativo: boolean;
  proprietarioEmail: string;
  criadoEm: string;
}

export interface EstabelecimentoRequest {
  nome: string;
  descricao: string;
  telefone: string;
  endereco: string;
}

export interface Usuario {
  id: number;
  nome: string;
  email: string;
  telefone: string | null;
  ativo: boolean;
  papeis: string[];
  criadoEm: string;
}

export interface UsuarioRequest {
  nome: string;
  email: string;
  senha: string;
  telefone: string;
}

export interface LoginResponse {
  token: string;
  tipo: string;
  expiraEm: number;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
