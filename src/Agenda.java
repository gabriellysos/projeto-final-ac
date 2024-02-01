import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Agenda {
    /* MANIPULAÇÃO DE ARQUIVOS */
    private static final String AGENDA_TXT = "agenda.txt";
    private static Long id = 0L;
    private List<Contato> contatos;

    public Agenda() {
        contatos = new ArrayList<>();
    }

    public void carregarDados() {
        try {
            FileReader reader = new FileReader(AGENDA_TXT);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String linha;
            while ((linha = bufferedReader.readLine()) != null) {
                String[] dados = linha.split(";");

                Contato contato = new Contato();
                contato.setId(Long.parseLong(dados[0]));
                contato.setNome(dados[1]);
                contato.setSobrenome(dados[2]);

                if (contato.getTelefones() == null) {
                    contato.setTelefones(new ArrayList<>());
                }

                Telefone telefone = new Telefone();
                telefone.setDdd(dados[3]);
                telefone.setNumero(Long.parseLong(dados[4]));

                contato.getTelefones().add(telefone);

                contatos.add(contato);

                if (contato.getId() > id) {
                    id = contato.getId();
                }
            }

            validarIds();

            reader.close();
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado: " + AGENDA_TXT);
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }


    private void validarIds() {
        for (int i = 0; i < contatos.size(); i++) {
            for (int j = i + 1; j < contatos.size(); j++) {
                if (contatos.get(i).getId().equals(contatos.get(j).getId())) {
                    Contato contato = contatos.get(i);

                    contato.setId(++id);

                    contatos.remove(i);
                    contatos.add(contato);

                    for (int k = i; k < contatos.size(); k++) {
                        contatos.get(k).setId((long) (k + 1));
                    }

                    break;
                }
            }
        }
    }


    public void salvarDados() {
        try {
            FileWriter writer = new FileWriter(AGENDA_TXT);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            for (Contato contato : contatos) {
                bufferedWriter.write(contato.getId() + ";" + contato.getNome() + ";" + contato.getSobrenome());

                for (Telefone telefone : contato.getTelefones()) {
                    if (telefone != null) {
                        bufferedWriter.write(";" + telefone.getDdd() + ";" + telefone.getNumero());
                    } else {
                        bufferedWriter.write(";;");
                    }
                }
                bufferedWriter.newLine();
            }

            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }

    /* MENU */
    public void exibirMenu() {
        System.out.println("\n##################");
        System.out.println("##### AGENDA #####");
        System.out.println("##################\n");

        System.out.println(">>>> Contatos <<<<");
        System.out.println("Id | Nome");
        for (Contato contato : contatos) {
            System.out.printf("%d | %s %s\n", contato.getId(), contato.getNome(), contato.getSobrenome());
        }

        System.out.println("\n>>>> Menu <<<<");
        System.out.println("1 - Adicionar Contato");
        System.out.println("2 - Remover Contato");
        System.out.println("3 - Editar Contato");
        System.out.println("4 - Sair\n");

        selecionarMenu();
    }


    public void selecionarMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Informe a opção: ");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                adicionarContato();;
            case 2:
                removerContato();
            case 3:
                editarContato();
            case 4:
                sair();
            default:
                System.out.println("Opção inválida.");
                selecionarMenu();
        }
    }


    // OPÇÕES
    public void adicionarContato() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Informe o nome do contato: ");
        String nome = scanner.nextLine();

        System.out.print("Informe o sobrenome do contato: ");
        String sobrenome = scanner.nextLine();

        System.out.print("Informe o ddd: ");
        String ddd = scanner.nextLine();

        System.out.print("Informe o número do telefone: ");
        String numero = scanner.nextLine();

        for (Contato c : contatos) {
            for (Telefone t : c.getTelefones()) {
                if (t.getDdd().equals(ddd) && t.getNumero().equals(Long.parseLong(numero))) {
                    System.out.println("Já existe um contato com esse telefone.");
                    selecionarMenu();
                }
            }
        }

        Contato contato = new Contato();

        if (contatos.isEmpty()) {
            contato.setId(1L);
        } else {
            contato.setId(contatos.get(contatos.size() - 1).getId() + 1);
        }
        contato.setNome(nome);
        contato.setSobrenome(sobrenome);

        Telefone telefone = new Telefone();
        telefone.setDdd(ddd);
        telefone.setNumero(Long.parseLong(numero));

        if (contato.getTelefones() == null) {
            contato.setTelefones(new ArrayList<>());
        }

        contato.getTelefones().add(telefone);

        contatos.add(contato);
        System.out.println("Contato adicionado com sucesso.");

        salvarDados();
        exibirMenu();
    }






    public void removerContato() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Informe o id do contato a ser removido: ");
        Long id = Long.parseLong(scanner.nextLine());

        Contato contato = null;
        for (Contato c : contatos) {
            if (c.getId().equals(id)) {
                contato = c;
                break;
            }
        }

        if (contato == null) {
            System.out.println("Contato não encontrado.");
            return;
        }

        System.out.println("\nCONTATO ENCONTRADO");
        System.out.printf("Nome: %s %s\n", contato.getNome(), contato.getSobrenome());
        System.out.printf("Telefone: (%s) %d\n", contato.getTelefones().get(0).getDdd(), contato.getTelefones().get(0).getNumero());

        System.out.println("1 - Confirmar remoção");
        System.out.println("2 - Selecionar outro contato");
        System.out.println("3 - Voltar ao menu");

        System.out.print("Digite a opção desejada: ");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                contatos.remove(contato);
                System.out.println("Contato removido com sucesso.");

                for (int i = 0; i < contatos.size(); i++) {
                    contatos.get(i).setId((long) (i + 1));
                }

                break;
            case 2:
                removerContato();
            case 3:
                exibirMenu();
            default:
                System.out.println("Opção inválida.");
                break;
        }

        salvarDados();
        exibirMenu();
    }


    public void editarContato() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Informe o id do contato a ser editado: ");
        Long id = Long.parseLong(scanner.nextLine());

        Contato contato = null;
        for (Contato c : contatos) {
            if (c.getId().equals(id)) {
                contato = c;
                break;
            }
        }

        if (contato == null) {
            System.out.println("Contato não encontrado.");
            return;
        }

        System.out.println("\nCONTATO ENCONTRADO");
        System.out.printf("Nome: %s %s\n", contato.getNome(), contato.getSobrenome());
        System.out.printf("Telefone: (%s) %d\n", contato.getTelefones().get(0).getDdd(), contato.getTelefones().get(0).getNumero());

        System.out.println("1 - Nome");
        System.out.println("2 - Sobrenome");
        System.out.println("3 - Telefone");
        System.out.println("4 - Voltar ao menu");

        System.out.print("Selecione o atributo a ser editado: ");
        int opcao = scanner.nextInt();

        switch (opcao) {
            case 1:
                System.out.print("Informe o novo nome: ");
                scanner.nextLine();
                String nome = scanner.nextLine();
                contato.setNome(nome);
                break;
            case 2:
                System.out.print("Informe o novo sobrenome: ");
                scanner.nextLine();
                String sobrenome = scanner.nextLine();
                contato.setSobrenome(sobrenome);
                break;
            case 3:
                System.out.print("Informe o novo ddd: ");
                scanner.nextLine();
                String ddd = scanner.nextLine();
                System.out.print("Informe o novo número do telefone: ");
                scanner.nextLine();
                String numero = scanner.nextLine();

                Telefone telefone = new Telefone();
                telefone.setDdd(ddd);
                telefone.setNumero(Long.parseLong(numero));
                contato.getTelefones().clear();
                contato.getTelefones().add(telefone);
                break;
            case 4:
                exibirMenu();
            default:
                System.out.println("Opção inválida.");
                break;
        }

        System.out.println("Contato editado com sucesso.");

        salvarDados();
        exibirMenu();
    }


    public void sair() {
        System.out.println("Até logo!");
        System.exit(0);
    }

}