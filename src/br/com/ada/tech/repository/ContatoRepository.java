package br.com.ada.tech.repository;

import br.com.ada.tech.helper.ContatoHelper;
import br.com.ada.tech.model.Contato;

import java.util.List;

public class ContatoRepository {

    private ContatoHelper contatoHelper;

    public ContatoRepository() {
        this.contatoHelper = new ContatoHelper();
    }

    public Contato salvar(Contato contato) {
        return this.contatoHelper.salvar(contato);
    }

    public Contato buscarPorId(Long id) {
        return this.contatoHelper.buscarPorId(id);
    }

    public Contato atualizar(Contato contato) {
        Contato contatoBD = this.buscarPorId(contato.getId());
        contato.setId(contatoBD.getId());
        return this.contatoHelper.atualizar(contato);
    }

    public List<Contato> todos() {
        return this.contatoHelper.listarTodos();
    }

    public void remover(Long id) {
        this.contatoHelper.remover(id);
    }

}
