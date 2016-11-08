package rpg;

import rpg.excecoes.InfInvalidoException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Nechelley Alves, Paulo
 */
public class Arquivo implements Serializable {
    
    private final String nome;
    
    /**
     * Construtor da classe Arquivo.
     * Recebe uma String com o nome do arquivo a ser trabalhado pela instância
     * 
     * @param nome Nome do arquivo
     */
    public Arquivo (String nome) {
        this.nome = nome;
    }
    
    /**
     * Carrega os dados do arquivo
     * 
     * @return Jogo carregado do arquivo.
     */
    public Jogo carregarJogo() {
        Jogo jogo = null;
        try {
            FileInputStream f_in = new FileInputStream(nome);
            ObjectInputStream obj_in = new ObjectInputStream (f_in);
            jogo = (Jogo) obj_in.readObject();
        } catch (Exception e) {
            throw new InfInvalidoException("Arquivo",nome);
        }
        
        return jogo;
    }
    
    /**
     * Salva os dados do jogo no arquivo.
     * 
     * @param jogo Jogo a ser salvo
     */
    public void salvarJogo(Jogo jogo) {
        try {
            FileOutputStream f_out = new FileOutputStream(nome);
            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
            obj_out.writeObject(jogo);
            obj_out.close();
        } catch (IOException e) {
            // Arquivo inválido.
            throw new InfInvalidoException("Arquivo",nome);
        }
    }
    
    /**
     * Testa para ver se o arquivo é possível de ser criado.
     * 
     * @throws IOException 
     */
    public void testarArquivo() throws IOException {
        try {
            FileOutputStream f_out = new FileOutputStream(nome);
            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
            obj_out.writeObject("7x1 never forget");
            obj_out.close();
        } catch (IOException e) {
            // Arquivo inválido.
            throw e;
        }
    }
    
    /**
     * Carrega uma lista de heróis de um arquivo texto
     * 
     * @return Lista de Strings com os heróis lidos. Cada posição possui dados de um herói separados por ponto e virgula ";"
     * @throws FileNotFoundException Exceção quando o arquivo não foi encontrado
     * @throws IOException Exceção com erro de leitura do arquivo
     */
    public ArrayList<String> carregarHerois() throws FileNotFoundException, IOException {
        ArrayList<String> herois = new ArrayList<String>();
        
        
        BufferedReader arq = new BufferedReader(new FileReader(nome));
        
        String linha = arq.readLine();
        
        while (linha != null) {
            herois.add(linha);
            linha = arq.readLine();
        }
        
        arq.close();
        
        return herois;
    }
    
    /**
     * Salva uma string de heróis no arquivo texto
     * 
     * @param lista String com os heróis. Cada dado de herói separado por ponto e virgula ";" e cada herói separado por quebra de linha "\n"
     * @throws IOException Exceção com erro de escrita do arquivo
     */
    public void salvarHerois(String lista) throws IOException {
        FileWriter arq = new FileWriter(nome);

        arq.write(lista);
        arq.close();
    }
    
}
