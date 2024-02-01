package br.com.ada.tech.helper;

import br.com.ada.tech.model.Telefone;
import br.com.ada.tech.util.ArquivoUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TelefoneHelper {
    public final static String PATH_TELEFONE = "telefones.txt";
    public final static String PATH_INDEX_TELEFONE = "index-telefone.txt";
    private ArquivoUtil arquivoUtil;
    private IndexHelper indexHelper;

    public TelefoneHelper() {
        this.arquivoUtil = new ArquivoUtil();
        this.indexHelper = new IndexHelper();
    }

    public List<Telefone> listarTodos() {

        if (!this.arquivoUtil.isVazio(PATH_TELEFONE)) {
            return this.arquivoUtil.lerArquivo(PATH_TELEFONE).stream()
                    .map(reg -> reg.split(";"))
                    .map(regSplit -> {
                        Telefone telefone = new Telefone();
                        telefone.setId(Long.valueOf(regSplit[0]));
                        telefone.setDdd(regSplit[1]);
                        telefone.setNumero(Long.valueOf(regSplit[2]));
                        return telefone;
                    }).collect(Collectors.toList());
             }

            throw new RuntimeException("Nao existem telefones cadastrados");
        }

        public Telefone buscarPorId (Long id){

            if (!this.arquivoUtil.isVazio(PATH_TELEFONE)) {
                Optional<String> optionalReg = this.arquivoUtil.lerArquivo(PATH_TELEFONE).stream()
                        .filter(reg -> id.equals(Long.parseLong(reg.split(";")[0])))
                        .findFirst();

                if (optionalReg.isPresent()) {
                    String[] regSplit = optionalReg.get().split(";");
                    Telefone telefone = new Telefone();
                    telefone.setId(Long.valueOf(regSplit[0]));
                    telefone.setDdd(regSplit[1]);
                    telefone.setNumero(Long.valueOf(regSplit[2]));
                    return telefone;
                }

            }

            throw new RuntimeException("Telefone nao encontrado");

        }

        public Telefone salvar(Telefone telefone){
            Long indexTelefone = this.indexHelper.getIndex(PATH_INDEX_TELEFONE);
            telefone.setId(++indexTelefone);
            String linhaTelefone = this.arquivoUtil.formataLinha(telefone.getId(), telefone.getDdd(), telefone.getNumero());
            this.arquivoUtil.escreveLinhaArquivo(PATH_TELEFONE, linhaTelefone);
            this.arquivoUtil.escreveArquivo(PATH_INDEX_TELEFONE, Arrays.asList(indexTelefone.toString()));
            return telefone;
        }

        public void salvarTodos(List<Telefone> telefones) {
            List<String> linhasTelefonesAtualizados = telefones.stream().map(telefoneAtualizado -> {
                return this.arquivoUtil.formataLinha(telefoneAtualizado.getId(), telefoneAtualizado.getDdd(), telefoneAtualizado.getNumero());
            }).collect(Collectors.toList());
            this.arquivoUtil.escreveArquivo(PATH_TELEFONE, linhasTelefonesAtualizados);
        }

    }
