package com.fastlayout.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Querys {
    StringBuilder resultado = new StringBuilder();
    ResultSet rs = null;
    Statement stmt = null;
    String [] layouts;

    public String query(Connection conn){
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT CODIGO FROM CADIMO");

            while(rs.next()){
                String codigo = rs.getString("CODIGO");
                System.out.println("CÓDIGO DO IMÓVEL: " + codigo);

                resultado.append(codigo).append(",");
            }
        } catch (Exception e){
            System.out.println("Erro ao montar consulta:");
            e.printStackTrace();
        }

        return resultado.toString();
    }

    // Puxa todos os layouts e coloca dentro de 'resultado'
    public String[] getLayouts(Connection conn){
        

        try{
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet countRs = stmt.executeQuery("SELECT COUNT(*) AS total FROM CNFGLY;");
            countRs.next();
            int total = countRs.getInt("total");
            System.out.println("--------------------");
            System.out.println("Quantidade de layouts é: " + total);
            System.out.println("--------------------");
            layouts = new String[total];

             rs = stmt.executeQuery("SELECT MLY_DESCRICAO FROM CNFGLY;");

            while(rs.next()){
                
                String layout = rs.getString("MLY_DESCRICAO");
                    
                layouts[i] = layout;
                i++;
                resultado.append(layout).append(",");
            }

            /*for(int e = 0; e < layouts.length; e++){
                System.out.println(layouts[e]);
            }*/

        } catch (Exception e){
            System.out.println("Erro ao montar consulta:");
            e.printStackTrace();
        
        }

        return layouts;

    }

    // Puxa todos os imóveis ativos e coloca dentro de 'resultado'
    public String getImoveisAtivos(Connection conn){
        try{
            Statement stmt = conn.createStatement();
            ResultSet status = stmt.executeQuery("SELECT GROUP_CONCAT("
             + "    IFNULL(NULLIF(CONCAT(\"'\", REPLACE(IMOVEIS_ATIVOS1_VALUES, CHAR(13,10), \"','\"), \"'\"), \"''\"), ''),"
             + "    IFNULL(NULLIF(CONCAT(\"'\", REPLACE(IMOVEIS_ATIVOS2_VALUES, CHAR(13,10), \"','\"), \"'\"), \"''\"), ''),"
             + "    \",\","
             + "    IFNULL(NULLIF(CONCAT(\"'\", REPLACE(IMOVEIS_ATIVOS3_VALUES, CHAR(13,10), \"','\"), \"'\"), \"''\"), '')"
             + ") AS CAMPO FROM CNFGSYS;");

             
            ResultSet rs = stmt.executeQuery("SELECT CODIGO FROM CADIMO WHERE STATUS IN(" + status.toString() + ")");

            while(rs.next()){
                String codigo = rs.getString("CODIGO");
                System.out.println("CÓDIGO DO IMÓVEL: " + codigo);

                resultado.append(codigo).append(",");
            }
        } catch (Exception e){
            System.out.println("Erro ao montar consulta:");
            e.printStackTrace();
        }

        return resultado.toString();
    }

    public void updateLayouts(Connection conn, String layout, String conteudo, String novoNome){
        String sql = "UPDATE CNFGLY SET MLY_MLY = ?, MLY_DESCRICAO = ? WHERE MLY_DESCRICAO = ?;";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, conteudo);
            pstmt.setString(2, novoNome);
            pstmt.setString(3, layout);
            int linhasAfetadas = pstmt.executeUpdate();
            System.out.println("Linhas afetadas: " + linhasAfetadas);
            System.out.println("Layout " + layout + " atualizado com sucesso!");  
        } catch(Exception e) {
            System.out.println("Erro ao atualizar layout" + layout + ":");
            e.printStackTrace();
        }
    }

    public String getConteudoLayout(Connection conn, String layout){
        String conteudo = "";
        String sql = "SELECT MLY_MLY FROM CNFGLY WHERE MLY_DESCRICAO = ? LIMIT 1;";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, layout);
           try(ResultSet rs1 = pstmt.executeQuery()){
            if (rs1.next()){
                conteudo = rs1.getString("MLY_MLY");
                } else {
                    System.out.println("Não foi localizado o layout no banco de dados. Verifique o nome do arquivo!");
                    System.exit(1);
                }
           }
           } catch (Exception e){
            System.out.println("Erro ao buscar conteúdo do layout " + layout + " Verifique o nome do arquivo!");
            e.printStackTrace();
        }
        return conteudo;
    }

    public int getCodigoEmp(Connection conn, String banco){
        String sqlCount = "SELECT COUNT(*) AS Total FROM CNFGSYS";
        String sql = "SELECT CODIGO_EM FROM CNFGSYS;";
        int count = 0;
        int[] codigoEmp;
        try(PreparedStatement pstmt = conn.prepareStatement(sqlCount)){
            ResultSet rsCount = pstmt.executeQuery();
            rsCount.next();
            count = rsCount.getInt("Total");
            codigoEmp = new int[count];
            if(codigoEmp.length > 1){
                System.out.println("-------- Atenção! --------\n Essa é uma base de rede. Informe o código da base que deseja:");
                int escolha = showEmpresas(conn, count);
                if(escolha != 0){
                    return escolha;
                }
            }
            try(PreparedStatement pstmt1 = conn.prepareStatement(sql)){
                ResultSet rsEmp = pstmt1.executeQuery();
                rsEmp.next();
                codigoEmp = new int[count];
                codigoEmp[0] = rsEmp.getInt("CODIGO_EM");
                return codigoEmp[0];
            }
            
        } catch (Exception e){
            System.out.println("Erro ao buscar quantidade de empresas:");
            e.printStackTrace();
        }
        codigoEmp = new int[count];
        return codigoEmp[0];
    }

    public int showEmpresas(Connection conn, int qtdEmp){
        String sql = "SELECT NOME,CODIGO_EM FROM CNFGSYS GROUP BY CODIGO_EM;";
        String[] empresas = new String[qtdEmp];
        int[] codigoEmp = new int[qtdEmp];
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            ResultSet rsEmp = pstmt.executeQuery();
            int i = 0;
            while(rsEmp.next()){
                empresas[i] = rsEmp.getString("NOME");
                codigoEmp[i] = rsEmp.getInt("CODIGO_EM");

                empresas[i] = empresas[i] + " - " + codigoEmp[i];
                System.out.println(empresas[i]);
            }
            Scanner scanner = new Scanner(System.in);
            String escolha = scanner.nextLine();
            int escolhaInt = Integer.parseInt(escolha);
            for(int e = 0; e < codigoEmp.length; e++){
                if(codigoEmp[e] == escolhaInt){
                    for(int j = 0; j < codigoEmp.length; j++){
                        if(codigoEmp[j] == escolhaInt){
                            System.out.println("Escolhido: " + empresas[j]);
                            return codigoEmp[j];
                        }else{
                            System.out.println("Não foi possível encontrar a empresa escolhida! \n Digite novamente:");
                            showEmpresas(conn, qtdEmp);

                        }
                    }
                }
            }
        } catch(SQLException e){
            System.out.println("Erro ao buscar empresas:");
            e.printStackTrace();
        
        }
        return 0;
    }



    public ResultSet getRs() {
        return rs;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    
}
