package com.fastlayout.config;



import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Arquivos {
    
    private String pasta = "layouts";
    private String[] nomesArquivos;

    public String[] lerNomes(){
        File diretorio = new File(pasta);
        if (!diretorio.exists() || !diretorio.isDirectory()) {
            System.out.println("A pasta de layouts não foi criada ou não está no caminho correto.");
            System.out.println("Por favor crie a pasta dentro do projeto no caminho /layouts");
            System.exit(1);
        }
        File[] arquivos = diretorio.listFiles();
        if(arquivos == null || arquivos.length == 0){
            System.out.println("Nenhum arquivo localizado, por favor insira os layouts dentro da pasta /layouts");
            System.exit(1);
        }

        


        
        for (File arquivo : arquivos){
            if (arquivo.isFile() && arquivo.getName().endsWith(".mly")){

                nomesArquivos = new String[arquivos.length];

                for(int i = 0; i < arquivos.length; i++){
                    nomesArquivos[i] = arquivos[i].getName();
                    
                }
            }
        }
        System.out.println("Arquivos localizados:");
        System.out.println(" ");
        for (int i = 0; i< nomesArquivos.length; i++){
            System.out.println(nomesArquivos[i]);
        }

        return nomesArquivos;

    }  
    
    public String conteudoArquivo(String nomeArquivo){
        try {
            String arq = pasta + "/" + nomeArquivo;
            String conteudo = new String(Files.readAllBytes(Paths.get(arq)), "ISO-8859-1");
            return conteudo;
        } catch (Exception e) {
            System.out.println("Não foi possível ler o arquivo:" + nomeArquivo);
            System.err.println(e);
            System.exit(1);
        }
        return null;
    }

    public String renomearArquivo(String nomeArquivo){
        try {
            int index = nomeArquivo.indexOf(".mly");
            if (index != -1){ 
                String localSufixo = nomeArquivo.substring((index-2), index);
                boolean isNumeric = localSufixo.matches("\\d{2}");
                if (isNumeric){
                    int numero = Integer.parseInt(localSufixo);
                    int novoNumero = numero + 1;
                    System.out.println("Sufixo padronizado, adicionando + 1 ao novo nome do layout");
                    return nomeArquivo.replace(localSufixo, String.valueOf(novoNumero));
                } else{
                    System.out.println("Sufixo não padronizado, será incluso um sufixo padrão _00");
                    return nomeArquivo.replace(".mly","_00.mly");
                }
            }
            else {
                System.out.println("O arquivo " + nomeArquivo + " não está no formato .mly");
                System.exit(1);
            }


        } catch(Exception e){
            System.out.println("Não foi possível renomear o arquivo: " + nomeArquivo);
            System.out.println("Erro: \n" + e);
        }
        return null;
    }

    public String salvarArquivos(String nomeArquivo,int codigoEmp ,String conteudo){
        try {
            Path caminho = Paths.get(pasta, String.valueOf(codigoEmp), nomeArquivo);
            Files.createDirectories(caminho.getParent());
            Files.write(caminho, conteudo.getBytes("ISO-8859-1"));
            
        } catch (Exception e) {
            System.out.println("Não foi possível salvar o arquivo: " + nomeArquivo);
            System.out.println("Erro: \n" + e);
        }
        return null;
    }

    public String salvarArquivos(String nomeArquivo,String novo,int codigoEmp ,String conteudo){
        try {
            Path caminho = Paths.get(pasta, String.valueOf(codigoEmp),novo, nomeArquivo);
            Files.createDirectories(caminho.getParent());
            Files.write(caminho, conteudo.getBytes("ISO-8859-1"));

            System.out.println("Arquivo salvo com sucesso: " + caminho);

            
        } catch (Exception e) {
            System.out.println("Não foi possível salvar o arquivo: " + nomeArquivo);
            System.out.println("Erro: \n" + e);
        }
        return null;
    }
}
