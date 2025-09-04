package org.example;

import org.example.dao.ContatoDAO;
import org.example.model.Contato;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        inicio();
    }

    public static void inicio(){
        boolean sair = false;
        System.out.println("""
                ========================================================
                1. Cadastrar contato: Nome, Telefone, Email, Observação.
                2. Listar todos os contatos cadastrados.
                3. Buscar contato por nome.
                4. Atualizar dados de um contato (telefone, email, observação).
                5. Remover contato.
                6. Sair do sistema.
                ========================================================
                """);
        int opcao = sc.nextInt();
        sc.nextLine();

        switch(opcao){
            case 1 -> cadastrarContato();
            case 2 -> listarContato();
            case 3 -> buscarContatoPorNome();
            case 4 -> atualizarContato();
            case 5 -> removerContato();
            case 6 -> sair = true;
            default -> System.out.println("Opção inválida!");
        }

        if(!sair){
            inicio();
        }
    }

    // NOVO: usa inserirDados
    public static void cadastrarContato(){
        System.out.println("-- CADASTRAR CONTATO --");
        inserirDados(1, 0);
    }

    public static void listarContato(){
        System.out.println("===CONTATOS===");
        var dao = new ContatoDAO();
        try{
            List<Contato> contatos = dao.listarContatos();
            exibirContatos(contatos);
        }catch (SQLException e) {
            System.out.println("Erro de conexão com o banco");
        }
    }

    public static void buscarContatoPorNome(){
        System.out.println("Digite o nome do contato que deseja procurar:");
        String nome = sc.nextLine();

        var dao = new ContatoDAO();
        try{
            List<Contato> contatos = dao.buscarContatoPorNome(nome);
            exibirContatos(contatos);
        } catch(SQLException e){
            System.out.println("Erro no banco de dados");
            e.printStackTrace();
        }
    }

    // AGORA RETORNA LISTA DE IDs
    public static List<Integer> exibirContatos(List<Contato> contatos){
        List<Integer> idContatos = new ArrayList<>();
        for (Contato contato : contatos){
            System.out.println("ID: " + contato.getId());
            System.out.println("NOME: " + contato.getNome());
            System.out.println("TELEFONE: " + contato.getTelefone());
            System.out.println("EMAIL: " + contato.getEmail());
            System.out.println("OBSERVAÇÃO: "+ contato.getObservacao());
            System.out.println("-----------------------------");
            idContatos.add(contato.getId());
        }
        return idContatos;
    }

    public static void atualizarContato(){
        System.out.println("-- ATUALIZAR CONTATO --");
        List<Integer> idContatos = new ArrayList<>();
        List<Contato> contatos = new ArrayList<>();
        var dao = new ContatoDAO();

        try{
            contatos = dao.listarContatos();
            idContatos = exibirContatos(contatos);
        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("ID do contato para edição:");
        int id = sc.nextInt();
        sc.nextLine();

        if(idContatos.contains(id)){
            inserirDados(2, id);
        } else {
            System.out.println("Opção inválida!");
            atualizarContato();
        }
    }

    public static void inserirDados(int opcao, int id){
        var dao = new ContatoDAO();
        System.out.println("Nome do contato:");
        String nome = sc.nextLine();

        System.out.println("Telefone do contato:");
        String telefone = sc.nextLine();

        System.out.println("Email do contato:");
        String email = sc.nextLine();

        System.out.println("Observação:");
        String observacao = sc.nextLine();

        switch (opcao){
            case 1 -> {
                try{
                    var contato = new Contato(nome, telefone, email, observacao);
                    dao.inserirContato(contato);
                    System.out.println("Contato inserido com sucesso!");
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
            case 2 -> {
                try{
                    var contato = new Contato(id, nome, telefone, email, observacao);
                    dao.atualizarContato(contato);
                } catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void removerContato(){
        System.out.println("--REMOVER CONTATO");
        List<Integer> idContatos = new ArrayList<>();
        List<Contato> contatos = new ArrayList<>();
        var dao = new ContatoDAO();

        try{
            contatos = dao.listarContatos();
            idContatos = exibirContatos(contatos);
        } catch (SQLException e){
            e.printStackTrace();
        }

        if (contatos.isEmpty()){
            System.out.println("NENHUM CONTATO PARA REMOVER");
            return ;
        }

        System.out.println("Digite o ID do contato que deseja remover: ");
        int id = sc.nextInt();
        sc.nextLine();

        if(idContatos.contains(id)){
            try{
                dao.removerContato(id);
                System.out.println("Contato removido com sucesso!");
            }catch (SQLException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("ID inválido!");
            removerContato();
        }
    }
}
