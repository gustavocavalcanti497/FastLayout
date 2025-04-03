// Pacote onde a classe está localizada
package com.fastlayout.database;

// Importa biblioteca para carregar variáveis de ambiente a partir de um arquivo `.env`
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

// Classe responsável por armazenar informações de conexão com o banco de dados
public class ConexaoDados {
    // Declaração de variáveis privadas da classe
    private String endPoint;   // Nome do endpoint do banco de dados
    private String banco;      // Nome do banco de dados
    private String usuario;    // Nome do usuário do banco de dados
    Dotenv dotenv = Dotenv.load(); // Carrega variáveis de ambiente do arquivo `.env`
    private Scanner scanner = new Scanner(System.in);
    private String envFilePath = ".env";


   
    public ConexaoDados(String endPoint, String banco) throws IOException {
        this.endPoint = endPoint; 
        this.banco = banco;

        if (dotenv.get("DB_USER") != null) {
            setUsuario();setUsuario();
            System.out.println("Usuário carregado: " + usuario);
        } else{
            System.out.println("Não há usuário configurado, digite seu usuário:");
            setUsuario(scanner.nextLine());
            List<String> lines = Files.readAllLines(Paths.get(envFilePath));
            lines.add("DB_USER=" + "" + usuario + "");
            Files.write(Paths.get(envFilePath), lines, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            System.out.println("Usuário configurado");
        }
            
    }       
    // Getters e Setters
    public String getEndPoint() {
        return endPoint;
    }



    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }



    public String getBanco() {
        return banco;
    }



    public void setBanco(String banco) {
        this.banco = banco;
    }



    public String getUsuario() {
        return usuario;
    }

    public void setUsuario() {
        this.usuario = dotenv.get("DB_USER"); // Obtém o nome do usuário do banco a partir do `.env`
    }
    
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }     

}