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
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}
