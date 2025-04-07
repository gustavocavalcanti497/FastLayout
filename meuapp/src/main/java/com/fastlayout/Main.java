package com.fastlayout;

import com.fastlayout.database.DatabaseConnection;
import com.fastlayout.sql.Querys;
import com.fastlayout.config.Arquivos;
import com.fastlayout.auth.GetRdsToken;
import com.fastlayout.database.ConexaoDados;


import java.io.IOException;
import java.sql.Connection;


public class Main 
{

    public static void main(String[] args) throws IOException, ClassNotFoundException{
        Class.forName("com.mysql.cj.jdbc.Driver");


        Menu menu = new Menu();
        ConexaoDados dados = menu.exibir();
        

        System.out.println(dados);

        GetRdsToken token = new GetRdsToken();
        System.out.println(dados.getEndPoint());
        String tokenGerado = token.getRdsToken(dados.getEndPoint(), dados.getUsuario());

        DatabaseConnection db = new DatabaseConnection(dados.getEndPoint(), dados.getBanco(), 3306, dados.getUsuario(), tokenGerado);
      
        try (Connection conn =  db.getConnection();) {
           
            if (conn != null) {
                System.out.println("Conexão realizada");
                
                Querys querys = new Querys();
                String[] layoutsBanco = querys.getLayouts(conn);

                Arquivos arquivos = new Arquivos();
                String[] layoutsPasta = arquivos.lerNomes();

                // Compara o nome dos arquivos da pasta com o nome dos arquivos no banco
                String[] resultadoComparacao = new String[layoutsPasta.length];

                for (int i = 0; i < layoutsPasta.length; i++) {
                    for (int j = 0; j < layoutsBanco.length; j++){
                        if (layoutsPasta[i].equals(layoutsBanco[j])){
                            resultadoComparacao[i] = layoutsPasta[i];
                        }
                    }
                }


                System.out.println("Layouts que serão upados:");
                for (int i = 0; i < resultadoComparacao.length; i++){
                    if (resultadoComparacao[i] != null){
                        System.out.println(resultadoComparacao[i]);
                    }                    
                }
                boolean todosNulos = true;

                for(String item : resultadoComparacao){
                    if(item !=null){
                        todosNulos = false;
                    }
                }
                
                if (todosNulos){
                    System.out.println("Não há layouts na pasta para serem upados");
                    System.exit(1);
                }

                int codigoEmp = querys.getCodigoEmp(conn,dados.getBanco());

                // Loop para salvar arquivo original
                for (int i = 0; i < resultadoComparacao.length; i++){
                    if(resultadoComparacao[i] != null){
                        String conteudo = querys.getConteudoLayout(conn, resultadoComparacao[i]);
                        arquivos.salvarArquivos(resultadoComparacao[i], codigoEmp, conteudo);

                    }
                }

                //Pega o conteúdo a ser upado no layout e faz update no banco e salva.
                for (int i = 0; i < resultadoComparacao.length; i++){
                    if (resultadoComparacao[i] != null){
                        String conteudo = arquivos.conteudoArquivo(resultadoComparacao[i]);
                        String novoNome = arquivos.renomearArquivo(resultadoComparacao[i]);
                        querys.updateLayouts(conn, resultadoComparacao[i], conteudo, novoNome);
                        arquivos.salvarArquivos(novoNome, "editado", codigoEmp,conteudo);
                        
                    }
                }



                
                
                

            }else {
                System.out.println("Erro de conexão");
            }
        } catch (Exception e) {
            System.err.println("Erro ao conectar ao banco de dados: ");
            e.printStackTrace();
        }
            





// 3eimobi3

    }
}

