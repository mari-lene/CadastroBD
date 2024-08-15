package cadastrobd;

import java.util.ArrayList;
import java.util.Scanner;

import cadastrobd.model.PessoaFisica;
import cadastrobd.model.PessoaFisicaDAO;
import cadastrobd.model.PessoaJuridica;
import cadastrobd.model.PessoaJuridicaDAO;

import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class CadastroBD {
    
    private static final Logger LOGGER = Logger.getLogger(CadastroBD.class.getName());
    private Scanner in;
    private PessoaFisicaDAO pfDao;
    private PessoaJuridicaDAO pjDao;
    
    public CadastroBD() {
        in = new Scanner(System.in);
        pfDao = new PessoaFisicaDAO();
        pjDao = new PessoaJuridicaDAO();
    }
    
    private String strAnswerQuestion(String question) {
        System.out.print(question);
        return in.nextLine();
    }
    
    private Integer intAnswerQuestion(String question) {
        System.out.print(question);
        String strValue = in.nextLine();
        Integer intValue = 0;
        try {
            intValue = Integer.valueOf(strValue);
        }
        catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return intValue;
    }
    
    private void printMenu() {
        System.out.println("\n==============================");
        System.out.println("1 - Incluir");
        System.out.println("2 - Alterar");
        System.out.println("3 - Excluir");
        System.out.println("4 - Buscar pelo ID");
        System.out.println("5 - Exibir todos");
        System.out.println("0 - Sair");
        System.out.println("==============================");
    }
    
    public void run() {
        int opcao = -1;
        while (opcao != 0) {
            printMenu();
            opcao = intAnswerQuestion("ESCOLHA: ");
            switch (opcao) {
                case 1: {
                    System.out.println("F - Pessoa Fisica | J - Pessoa Juridica");
                    String escolhaIncluir = strAnswerQuestion("TIPO DE PESSOA: ").toUpperCase();
                    if (escolhaIncluir.equals("F")) {
                        String nome = strAnswerQuestion("Informe o nome: ");
                        String cpf = strAnswerQuestion("Informe o CPF: ");
                        String endereco = strAnswerQuestion("Informe o endereco: ");
                        String cidade = strAnswerQuestion("Informe a cidade: ");
                        String estado = strAnswerQuestion("Informe o estado: ");
                        String telefone = strAnswerQuestion("Informe o telefone: ");
                        String email = strAnswerQuestion("Informe o email: ");
                        try {
                            pfDao.incluir(new PessoaFisica(null, nome, endereco, cidade, estado, telefone, email, cpf));
                        }
                        catch (SQLException e) {
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                        }
                    }
                    else if (escolhaIncluir.equals("J")) {
                        String nome = strAnswerQuestion("Informe o nome: ");
                        String cnpj = strAnswerQuestion("Informe o CNPJ: ");
                        String endereco = strAnswerQuestion("Informe o endereco: ");
                        String cidade = strAnswerQuestion("Informe a cidade: ");
                        String estado = strAnswerQuestion("Informe o estado: ");
                        String telefone = strAnswerQuestion("Informe o telefone: ");
                        String email = strAnswerQuestion("Informe o email: ");
                        try {
                            pjDao.incluir(new PessoaJuridica(null, nome, endereco, cidade, estado, telefone, email, cnpj));
                        }
                        catch (SQLException e) {
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                        }
                    }
                    else {
                        System.out.println("Erro: Escolha Invalida!");
                    }
                }; break;
                case 2: {
                    System.out.println("F - Pessoa Fisica | J - Pessoa Juridica");
                    String escolhaAlterar = strAnswerQuestion("TIPO DE PESSOA: ").toUpperCase();
                    if (escolhaAlterar.equals("F")) {
                        try {
                            Integer id = intAnswerQuestion("Informe o ID da Pessoa Fisica: ");
                            PessoaFisica pf = pfDao.getPessoa(id);
                            if (pf != null) {
                                pf.setNome(strAnswerQuestion("Informe o nome: "));
                                pf.setCpf(strAnswerQuestion("Informe o CPF: "));
                                pf.setEndereco(strAnswerQuestion("Informe o endereco: "));
                                pf.setCidade(strAnswerQuestion("Informe a cidade: "));
                                pf.setEstado(strAnswerQuestion("Informe o estado: "));
                                pf.setTelefone(strAnswerQuestion("Informe o telefone: "));
                                pf.setEmail(strAnswerQuestion("Informe o email: "));
                                pfDao.alterar(pf);
                            }
                            else {
                                System.out.println("ID nao encontrado!");
                            }
                        }
                        catch (NullPointerException | SQLException e) {
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                        }
                        
                    }
                    else if (escolhaAlterar.equals("J")) {
                        try {
                            Integer id = intAnswerQuestion("Informe o ID da Pessoa Juridica: ");
                            PessoaJuridica pj = pjDao.getPessoa(id);
                            if (pj != null) {
                                pj.setNome(strAnswerQuestion("Informe o nome: "));
                                pj.setCnpj(strAnswerQuestion("Informe o CNPJ: "));
                                pj.setEndereco(strAnswerQuestion("Informe o endereco: "));
                                pj.setCidade(strAnswerQuestion("Informe a cidade: "));
                                pj.setEstado(strAnswerQuestion("Informe o estado: "));
                                pj.setTelefone(strAnswerQuestion("Informe o telefone: "));
                                pj.setEmail(strAnswerQuestion("Informe o email: "));
                                pjDao.alterar(pj);
                            }
                            else {
                                System.out.println("ID nao encontrado!");
                            }
                        }
                        catch (NullPointerException | SQLException e) {
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                        }
                    }
                    else {
                        System.out.println("Erro: Escolha Invalida!");
                    }
                }; break;
                case 3: {
                    System.out.println("F - Pessoa Fisica | J - Pessoa Juridica");
                    String escolhaExcluir = strAnswerQuestion("TIPO DE PESSOA: ").toUpperCase();
                    if (escolhaExcluir.equals("F")) {
                        try {
                            Integer id = intAnswerQuestion("Informe o ID da Pessoa Fisica: ");
                            PessoaFisica pf = pfDao.getPessoa(id);
                            if (pf != null) {
                                pfDao.excluir(pf);
                                System.out.println("Excluido com sucesso.");
                            }
                            else {
                                System.out.println("ID nao encontrado.");
                            }
                        }
                        catch (NullPointerException | SQLException e) {
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                        }
                    }
                    else if (escolhaExcluir.equals("J")) {
                        try {
                            Integer id = intAnswerQuestion("Informe o ID da Pessoa Juridica: ");
                            PessoaJuridica pj = pjDao.getPessoa(id);
                            if (pj != null) {
                                pjDao.excluir(pj);
                                System.out.println("Excluido com sucesso.");
                            }
                            else {
                                System.out.println("ID nao encontrado.");
                            }
                        }
                        catch (NullPointerException | SQLException e) {
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                        }
                    }
                    else {
                        System.out.println("Erro: Escolha Invalida!");
                    }
                }; break;
                case 4: {
                    System.out.println("F - Pessoa Fisica | J - Pessoa Juridica");
                    String escolhaExibir = strAnswerQuestion("TIPO DE PESSOA: ").toUpperCase();
                    if (escolhaExibir.equals("F")) {
                        try {
                            PessoaFisica pf = pfDao.getPessoa(intAnswerQuestion("Informe o ID da Pessoa Fisica: "));
                            if (pf != null) {
                                pf.exibir();
                            }
                        }
                        catch (SQLException e){
                            System.err.println("Pessoa nao encontrada!");
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                        }
                    }
                    else if (escolhaExibir.equals("J")) {
                        try {
                            PessoaJuridica pj = pjDao.getPessoa(intAnswerQuestion("Informe o ID da Pessoa Juridica: "));
                            if (pj != null) {
                                pj.exibir();
                            }
                        }
                        catch (SQLException e){
                            System.err.println("Pessoa nao encontrada!");
                            LOGGER.log(Level.SEVERE, e.toString(), e);
                        }
                    }
                    else {
                        System.out.println("Erro: Escolha Invalida!");
                    }
                }; break;
                case 5: {
                    try {
                        ArrayList<PessoaFisica> listaPf = pfDao.getPessoas();
                        for (PessoaFisica pessoa : listaPf) {
                            System.out.println("---------------------------------");
                            pessoa.exibir();
                        }
                        System.out.println("---------------------------------");
                        ArrayList<PessoaJuridica> listaPj = pjDao.getPessoas();
                        for (PessoaJuridica pessoa : listaPj) {
                            pessoa.exibir();
                            System.out.println("---------------------------------");
                        }
                    }
                    catch (SQLException e) {
                        LOGGER.log(Level.SEVERE, e.toString(), e);
                    }
                }; break;
                default: System.out.println("Escolha invalida!");
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new CadastroBD().run();
    }
    
}
