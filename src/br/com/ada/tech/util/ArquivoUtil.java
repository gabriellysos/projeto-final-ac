package br.com.ada.tech.util;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ArquivoUtil {

    public boolean isVazio(String path) {
        return carregarArquivo(path).length() == 0;
    }

    public List<String> lerArquivo(String path) {

        List<String> linhas = null;

        try {
            File file = carregarArquivo(path);
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String linha = br.readLine();
            linhas = new ArrayList<>();
            while(linha != null) {
                linhas.add(linha);
                linha = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return linhas;

    }

    public void escreveLinhaArquivo(String path, String linha) {
        try {
            File file = carregarArquivo(path);
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(linha);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void escreveArquivo(String path, List<String> linhas) {
        try {
            File file = carregarArquivo(path);
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            for(String linha : linhas) {
                bw.write(linha);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String formataLinha(Object ...parametros) {
        StringBuilder sb = new StringBuilder();
        int contador = 0;
        int quantidadeParametros = parametros.length-1;
        for (Object obj : parametros) {
            if (contador < quantidadeParametros) {
                sb.append(String.format("%s", obj)).append(";");
            } else {
                sb.append(String.format("%s", obj));
            }
            contador++;
        }
        return sb.toString();
    }

    private static File carregarArquivo(String path) {
        Path resource = Paths.get("src", "resources");
        File file = new File(resource.toString().concat(File.separator.concat(path)));
        return file;
    }

}
