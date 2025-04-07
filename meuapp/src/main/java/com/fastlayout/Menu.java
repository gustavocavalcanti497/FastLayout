package com.fastlayout;

import java.io.IOException;
import java.util.Scanner;

import com.fastlayout.database.ConexaoDados;

public class Menu {
    public ConexaoDados exibir() throws IOException {
        int opcao = 0;
        boolean ativo = true;
        Scanner scanner = new Scanner(System.in);
        

        while (ativo) {

            
            
            // Solicitando qual servidor o cliente está alocado
            System.out.println("Digite onde o banco está alocado:");
            System.out.println("us-mysql-01, us-mysql-02, etc...");

            String endPoint = scanner.nextLine();

            System.out.println("Digite o nome do banco(schema) do cliente:");
            String banco = scanner.nextLine();

            System.out.println("Confirme os dados:");
            System.out.println("Servidor: " + endPoint);
            System.out.println("Banco: " + banco);
            System.out.println("1 - Confirmar");
            System.out.println("2 - Reiniciar");
            System.out.println("3 - Sair");
            
            while (true) {
                try {
                    opcao = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida, digite novamente um número de 1 a 3");
                }
            }



            switch (opcao) {
            case 1:
                System.out.println("Conectando ao banco...");
                ativo = false;
                return new ConexaoDados(endPoint,banco);
                
            case 2:
                System.out.println("Reiniciando...");
                break;
            case 3:
                System.out.println("Saindo...");
                ativo = false;
                break;

            default:
                System.out.println("Opcao incorreta, o programa será encerrado.");
                System.exit(1);
                break;
            }
        }
        scanner.close();
                return null;
            

    }
}
