package com.fastlayout.auth;

import java.io.*;

public class GetRdsToken {
    static String tokenGerado;
    
    
        public static String getRdsToken(String endPoint, String user){
            try{
                String[] endPoints = new String[]{"office-db",
                "sp-mysql-01",
                "us-mysql-01",
                "us-mysql-01-restricted",
                "us-mysql-02",
                "us-mysql-03",
                "us-mysql-03-pb",
                "us-mysql-04",
                "us-mysql-05",
                "us-mysql-09",
                "us-mysql-12-pb",
                "us-mysql-13",
                "us-mysql-15",
                "vistaimobi"};
    
                for (String e : endPoints) {
                    if (endPoint.equals(e)) {
                    String script = "resources\\get_rds_token_v2.1.ps1";
                    

        
                    ProcessBuilder processBuilder = new ProcessBuilder("powershell.exe","-ExecutionPolicy","Bypass","-File", script,"-DNS_PREFIX", endPoint, "-USERNAME", user);
                    processBuilder.redirectErrorStream(true);
                    Process process = processBuilder.start();
        
                    // Capturar a saida
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
        
                    StringBuilder output = new StringBuilder();
                    while((line = reader.readLine())!= null){
                        output.append(line).append("\n");
                    }
        
                    process.waitFor();
        
                    System.out.println("Token RDS gerado com sucesso!");
                    tokenGerado = output.toString();
                    System.out.println(tokenGerado);
                    break;
            }                
        }
        if (tokenGerado == null){
            System.out.println("Não foi possível gerar o token RDS, confira o endpoint informado");
            Thread.sleep(3000);
            System.exit(1);
        }    
            
        } catch (Exception e){
            e.printStackTrace();
        }
        return tokenGerado;
    }

    
}