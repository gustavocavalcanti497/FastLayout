package com.fastlayout.auth;

import java.io.*;
import java.io.IOException;

public class GetRdsToken {
    static String tokenGerado;
    
        public static String getRdsToken(String endPoint, String user){
            try{
                String script = "src\\main\\java\\com\\fastlayout\\auth\\get_rds_token_v2.1.ps1";
    
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

            
            
        } catch (Exception e){
            e.printStackTrace();
        }
        return tokenGerado;
    }

    
}