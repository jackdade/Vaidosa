import type  Pessoa  from '../types/Pessoa';

const API_BASE = 'http://localhost:8080/api/pessoas';

async function handleResponse<T>(res: Response): Promise<T> {
    if (!res.ok) {
        const text = await res.text();
        throw new Error(text || 'Erro desconhecido');
    }
    return res.json() as Promise<T>;
}

export async function listarPessoas(): Promise<Pessoa[]> {
    const res = await fetch(API_BASE);
    return handleResponse<Pessoa[]>(res);
}

export async function buscarPorCodigo(codigo: number): Promise<Pessoa> {
    const res = await fetch(`${API_BASE}/buscar?codigo=${codigo}`);
    return handleResponse<Pessoa>(res);
}

export async function buscarPorCpf(cpf: string): Promise<Pessoa> {
    const res = await fetch(`${API_BASE}/buscar?cpf=${cpf}`);
    return handleResponse<Pessoa>(res);
}
export async function buscarPorCpfECodigo(cpf: string, codigo: number): Promise<Pessoa> {
    const res = await fetch(`${API_BASE}/buscar?cpf=${cpf}&codigo=${codigo}`);
    return handleResponse<Pessoa>(res);
}

export async function inserirPessoa(pessoa: Pessoa): Promise<Pessoa> {
    const res = await fetch(`${API_BASE}/inserir`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(pessoa),
    });
    return handleResponse<Pessoa>(res);
}

export async function editarPessoa(pessoa: Pessoa): Promise<Pessoa> {
    const res = await fetch(`${API_BASE}/editar`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(pessoa),
    });
    return handleResponse<Pessoa>(res);
}

export async function deletarPessoa(codigo: number): Promise<void> {
    const res = await fetch(`${API_BASE}/deletar?codigo=${codigo}`, {
        method: 'DELETE',
    });
    await handleResponse<void>(res);
}

