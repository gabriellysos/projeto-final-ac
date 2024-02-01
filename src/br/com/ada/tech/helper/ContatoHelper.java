package br.com.ada.tech.helper;

import br.com.ada.tech.model.Contato;
import br.com.ada.tech.model.Telefone;
import br.com.ada.tech.util.ArquivoUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ContatoHelper {
    public final static String PATH_CONTATO = "contatos.txt";
    public final static String PATH_INDEX_CONTATO = "index-contato.txt";
    private ArquivoUtil arquivoUtil;
    private TelefoneHelper telefoneHelper;
    private IndexHelper indexHelper;
    private ContatoTemTelefoneHelper contatoTemTelefoneHelper;

    public ContatoHelper() {
        this.arquivoUtil = new ArquivoUtil();
        this.telefoneHelper = new TelefoneHelper();
        this.indexHelper = new IndexHelper();
        this.contatoTemTelefoneHelper = new ContatoTemTelefoneHelper();
    }

    private Contato carregarContatoPorId(Long id) {

        if (!this.arquivoUtil.isVazio(PATH_CONTATO)) {
            Optional<String> optionalReg = this.arquivoUtil.lerArquivo(PATH_CONTATO).stream()
                    .filter(reg -> id.equals(Long.parseLong(reg.split(";")[0])))
                    .findFirst();

            if (optionalReg.isPresent()) {
                String[] regSplit = optionalReg.get().split(";");
                Contato contato = new Contato();
                contato.setId(Long.valueOf(regSplit[0]));
                contato.setNome(regSplit[1]);
                contato.setSobreNome(regSplit[2]);
                return contato;
            }

        }

        throw new RuntimeException("Contato nao encontrado");

    }

    public List<Contato> listarTodos() {

        return this.contatoTemTelefoneHelper.consultaRelacionamento()
                    .entrySet().stream()
                    .map(data -> {
                        Long idContato = Long.parseLong(data.getKey());
                        List<Telefone> telefonesBD = data.getValue().stream()
                                .map(reg -> reg.split(";")[1])
                                .map(Long::parseLong)
                                .map(idTelefone -> this.telefoneHelper.buscarPorId(idTelefone))
                                .collect(Collectors.toList());

                        Contato contato = this.carregarContatoPorId(idContato);
                        contato.setTelefones(telefonesBD);
                        return contato;
            }).collect(Collectors.toList());

    }

    public Contato buscarPorId(Long id) {
        Contato contato = this.carregarContatoPorId(id);
        List<Telefone> telefonesBD = this.contatoTemTelefoneHelper.consultaRelacionamento()
                .get(contato.getId().toString())
                .stream()
                .map(data -> {
                    Long idTelefone = Long.parseLong(data.split(";")[1]);
                    return this.telefoneHelper.buscarPorId(idTelefone);
                }).collect(Collectors.toList());
        contato.setTelefones(telefonesBD);
        return contato;
    }

    public Contato salvar(Contato contato) {

        Long indexContato = this.indexHelper.getIndex(PATH_INDEX_CONTATO);
        contato.setId(++indexContato);
        String linhaContato = this.arquivoUtil.formataLinha(contato.getId(), contato.getNome(), contato.getSobreNome());
        this.arquivoUtil.escreveLinhaArquivo(PATH_CONTATO,  linhaContato);
        this.arquivoUtil.escreveArquivo(PATH_INDEX_CONTATO, Arrays.asList(indexContato.toString()));

        List<Telefone> telefones = contato.getTelefones().stream().map(telefone -> {
            return this.telefoneHelper.salvar(telefone);
        }).collect(Collectors.toList());

        contato.setTelefones(telefones);

        this.contatoTemTelefoneHelper.salvar(contato);

        return contato;

    }

    public void remover(Long id) {

        Contato contato = this.buscarPorId(id);
        List<Contato> contatos = this.listarTodos();
        contatos.remove(contato);

        List<Telefone> telefones = this.telefoneHelper.listarTodos();
        telefones.removeAll(contato.getTelefones());

        this.telefoneHelper.salvarTodos(telefones);

        List<String> linhasContatosAtualizados = contatos.stream().map(contatoAtualizado -> {
            return this.arquivoUtil.formataLinha(contatoAtualizado.getId(), contatoAtualizado.getNome(), contatoAtualizado.getSobreNome());
        }).collect(Collectors.toList());

        this.arquivoUtil.escreveArquivo(PATH_CONTATO, linhasContatosAtualizados);

        this.contatoTemTelefoneHelper.salvarTodos(contatos);

    }

    public Contato atualizar(Contato contato) {

        List<Contato> contatos = this.listarTodos();
        contatos.set(contatos.indexOf(contato), contato);

        List<Telefone> telefones = this.telefoneHelper.listarTodos();
        contato.getTelefones().stream().forEachOrdered(telefone -> {
            telefones.set(telefones.indexOf(telefone), telefone);
        });

        this.telefoneHelper.salvarTodos(telefones);

        List<String> linhasContatosAtualizados = contatos.stream().map(contatoAtualizado -> {
            return this.arquivoUtil.formataLinha(contatoAtualizado.getId(), contatoAtualizado.getNome(), contatoAtualizado.getSobreNome());
        }).collect(Collectors.toList());

        this.arquivoUtil.escreveArquivo(PATH_CONTATO, linhasContatosAtualizados);

        this.contatoTemTelefoneHelper.salvarTodos(contatos);

        return contato;

    }
}
