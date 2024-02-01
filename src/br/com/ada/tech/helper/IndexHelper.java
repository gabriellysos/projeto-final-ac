package br.com.ada.tech.helper;

import br.com.ada.tech.util.ArquivoUtil;

import java.util.Optional;

public class IndexHelper {

    private ArquivoUtil arquivoUtil;

    public IndexHelper() {
        this.arquivoUtil = new ArquivoUtil();
    }

    public Long getIndex(String path) {

        Optional<String> indexOptional = this.arquivoUtil.lerArquivo(path)
                .stream().findFirst();

        if (indexOptional.isPresent()) {
            return Long.parseLong(indexOptional.get());
        }

        return 0L;

    }


}
