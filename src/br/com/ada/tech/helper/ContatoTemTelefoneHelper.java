package br.com.ada.tech.helper;

import br.com.ada.tech.model.Contato;
import br.com.ada.tech.util.ArquivoUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContatoTemTelefoneHelper {

    private ArquivoUtil arquivoUtil;
    public final static String PATH_CONTATO_TELEFONES = "contato-tem-telefones.txt";

    public ContatoTemTelefoneHelper() {
        this.arquivoUtil = new ArquivoUtil();
    }

    public void salvar(Contato contato) {

        contato.getTelefones().stream().forEachOrdered(telefone -> {
            String linha = this.arquivoUtil.formataLinha(contato.getId(), telefone.getId());
            this.arquivoUtil.escreveLinhaArquivo(PATH_CONTATO_TELEFONES, linha);
        });

    }

    public Map<String, List<String>> consultaRelacionamento() {
        if (!this.arquivoUtil.isVazio(PATH_CONTATO_TELEFONES)) {
            return this.arquivoUtil.lerArquivo(PATH_CONTATO_TELEFONES).stream()
                    .collect(Collectors.groupingBy(reg -> reg.split(";")[0]));
        } else {
            return new HashMap<>();
        }
    }

    public void salvarTodos(List<Contato> contatos) {

        List<String> contatosTemTelefonesAtualizados = new ArrayList<>();
        contatos.stream().forEachOrdered(contato -> {
            contato.getTelefones().forEach(telefone -> {
                contatosTemTelefonesAtualizados.add(this.arquivoUtil.formataLinha(contato.getId(), telefone.getId()));
            });
        });

        this.arquivoUtil.escreveArquivo(PATH_CONTATO_TELEFONES, contatosTemTelefonesAtualizados);

    }
}
