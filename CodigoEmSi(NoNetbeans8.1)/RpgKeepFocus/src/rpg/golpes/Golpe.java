package rpg.golpes;

import java.io.Serializable;
import rpg.excecoes.InfInvalidoException;

/**
 * Representa um golpe dentro do jogo, ele sera executado por um Personagem durante um ataque
 * 
 * @author Nechelley Alves
 */
public abstract class Golpe implements Serializable {
    //nome do golpe
    private final String nome;
    //dano base do golpe, sem o dado
    private int danoBase;
    //diz quanto dano o golpe inflinge
    private int dano;
    //chance do golpe acertar o alvo vai de [2,12]
    //onde 2 chance minima de acerto, quase 0%, e 12 a chance maxima de acerto, 100%
    //esse valor representa quanto o valor dos dados tem que ser maior comparado a este valor para acertar
    //ex: se chanceDeAcerto for 8, entao para o golpe acertar o valor dos dados tem que ser menor ou igual a 8
    //ou seja, [2,3,4,5,6,7,8] para acertar
    private int chanceDeAcerto;
    //pontos de acao gastos para executar o golpe
    private int custoDeAcao;
    
    /**
     * Construtor onde defino as caracteristicas do golpe
     */
    Golpe(String nome){
        if(!verificaNome(nome))
            throw new InfInvalidoException("Nome do golpe",nome);
        this.nome = nome;
    }
    /**
     * Verifica se um nome é valido, ou seja, composto de apenas letras(maiusculas e minusculas), numeros(0-9) e spaces
     * 
     * @param nome String com o nome a ser verificado
     * @return True se o nome for valido, false caso contrario
     */
    private static boolean verificaNome(String nome){
        if(nome.isEmpty())
            return false;
        String valoresValidos[] = {
            "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
            "0","1","2","3","4","5","6","7","8","9"," "
        };
        boolean letraValida = false;
        for(int i = 0; i < nome.length(); i++){
            letraValida = false;
            for(String v : valoresValidos){
                if(nome.substring(i, i + 1).equals(v)){
                    letraValida = true;
                    break;
                }
            }
            if(!letraValida)
                return false;
        }
        return true;
    }
    
    /**
     * Retorna o nome do golpe
     * 
     * @return String com o nome do Golpe 
     */
    public String getNome(){
        return nome;
    }
    
    
    /**
     * Retorna a chance de acerto do golpe
     * 
     * @return Int com a chance de acerto do golpe
     */
    public int getChanceDeAcerto(){
        return chanceDeAcerto;
    }  
    
    /**
     * Altera a chance de acerto do golpe
     * 
     * @param valor Int com o novo valor de chance de acerto
     */
    protected void setChanceDeAcerto(int valor){
        if(valor < 2 || valor > 12)
            throw new InfInvalidoException("Chance de acerto",String.valueOf(valor));
        chanceDeAcerto = valor;
    }
    
    
    /**
     * Retorna o dano base do golpe
     * 
     * @return Int com o dano base do golpe
     */
    protected int getDanoBase(){
        return danoBase;
    }    
    
    /**
     * Altero o valor do dano base
     * 
     * @param valor int com novo valor do dano base
     */
    protected void setDanoBase(int valor){
        if(dano < 0 || dano > 50)// o mais 50 e so para via de duvidas
            throw new InfInvalidoException("Dano Base",String.valueOf(valor));
        dano = valor;
    }
    
    
    /**
     * Retorna o dano do golpe
     * 
     * @return Int com o dano do golpe
     */
    public int getDano(){
        return dano;
    }    
    
    /**
     * Altero o valor do dano
     * 
     * @param valor int com novo valor do dano
     */
    protected void setDano(int valor){
        if(dano < 0 || dano > 50)// o mais 50 e so para via de duvidas
            throw new InfInvalidoException("Dano",String.valueOf(valor));
        dano = valor;
    }
    
    
    /**
     * Metodo para definir o valor do dano de um golpe durante a batalha
     * 
     * @param valorDoDado Valor do dado jogado
     */
    public abstract void setDanoEmBatalha(int valorDoDado);
    
    
    /**
     * Retorna o custo do golpe
     * 
     * @return int com o custo do golpe
     */
    public int getCustoDeAcao(){
        return custoDeAcao;
    }  
    
    /**
     * Altero o valor do custo
     * 
     * @param valor int com novo valor do custo
     */
    protected void setCustoDeAcao(int valor){
        if(custoDeAcao < 0 || custoDeAcao > 2)
            throw new InfInvalidoException("Custo de ação",String.valueOf(valor));
        custoDeAcao = valor;
    }
}