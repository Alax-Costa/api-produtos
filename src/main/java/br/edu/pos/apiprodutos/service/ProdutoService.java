package br.edu.pos.apiprodutos.service;

import br.edu.pos.apiprodutos.model.Produto;
import br.edu.pos.apiprodutos.repository.ProdutoRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto cadastrar(String nome, double preco,int estoque) {
        validarProduto(nome, preco);
        if (estoque < 0) {
            throw new IllegalArgumentException("O estoque não pode receber valor negativo.");
        }
        Produto produto = new Produto(null, nome, preco, estoque);
        repository.salvar(produto);
        return produto;
    }

    public List<Produto> listar() {
        return repository.listarTodos();
    }

    public Produto buscarPorId(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Produto não encontrado: " + id));
    }

    public Produto atualizar(Long id, String nome, double preco, int estoque) {
        validarProduto(nome, preco);
        if (estoque < 0) {
            throw new IllegalArgumentException("O estoque não pode receber valor negativo.");
        }
        Produto produto = buscarPorId(id);

        produto.setNome(nome);
        produto.setPreco(preco);
        produto.setEstoque(estoque);

        repository.salvar(produto);
        return produto;
    }

    public Produto adicionarEstoque(Long id, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de entrada deve ser maior que zero.");
        }

        Produto produto = buscarPorId(id);
        produto.setEstoque(produto.getEstoque() + quantidade);

        repository.salvar(produto);
        return produto;
    }

    public Produto subtrairEstoque(Long id, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade de saída deve ser maior que zero.");
        }

        Produto produto = buscarPorId(id);

        if (produto.getEstoque() - quantidade < 0) {
            throw new IllegalArgumentException("Estoque insuficiente. Estoque atual: " + produto.getEstoque());
        }

        produto.setEstoque(produto.getEstoque() - quantidade);

        repository.salvar(produto);
        return produto;
    }

    public List<Produto> listarEstoqueBaixo(int limite) {
        return repository.listarEstoqueBaixo(limite);
    }

    public void excluir(Long id) {
        buscarPorId(id);
        repository.excluir(id);
    }

    private void validarProduto(String nome, double preco) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (!Double.isFinite(preco) || preco <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
    }
}