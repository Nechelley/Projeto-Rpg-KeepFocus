package rpg.golpes;

import rpg.xbuff.XBuff;
import java.io.Serializable;
import rpg.InfInvalidoException;

/**
 *  Representa um golpe dentro do jogo, ele sera executado por um Personagem.
 * 
 * @author Nechelley Alves
 */
public abstract class Golpe implements Serializable {
    //nome do golpe
    private final String nome;
    //diz quanto dano o golpe inflinge
    private int dano;
    //chance do golpe acertar o alvo vai de [2,12]
    //onde 2 chance minima de acerto, quase 0%, e 12 a chance maxima de acerto, 100%
    //esse valor representa quanto o valor dos dados tem que ser maior comparado a este valor para acertar
    //ex: se chanceDeAcerto for 6, entao para o golpe acertar o valor dos dados tem que ser maior que 6
    private int chanceDeAcerto;
    //pontos de acao gastos para executar o golpe
    private int custoDeAcao;
    
    /**
     * Construtor onde defino como o golpe sera executado.
     */
    Golpe(String nome){
        if(nome.isEmpty())
            throw new InfInvalidoException("Nome do golpe",nome);
        this.nome = nome;
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
     * @return int com a chance de acerto do golpe
     */
    public int getChanceDeAcerto(){
        return chanceDeAcerto;
    }  
    
    /**
     * Altera a chance de acerto do golpe
     * 
     * @param valor int com o novo valor de chance de acerto
     */
    protected void setChanceDeAcerto(int valor){
        if(valor < 2 || valor > 12)
            throw new InfInvalidoException("Chance de acerto",String.valueOf(valor));
        chanceDeAcerto = valor;
    }  
    
    
    
    /**
     * Retorna o dano do golpe
     * 
     * @return int com o dano do golpe
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
            throw new InfInvalidoException("Chance de acerto",String.valueOf(valor));
        dano = valor;
    }
    
    /**
     * Metodo para deifinir o valor do dano de um golpe durante a batalha
     * 
     * @param valorDoDado valor do dado jogado
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
        if(custoDeAcao < 0 || custoDeAcao > 2)// se acao com zero de custo for impossivel, arrumar AQUI
            throw new InfInvalidoException("Custo de ação",String.valueOf(valor));
        custoDeAcao = valor;
    }
    
    
    
    public abstract XBuff getXBuff();
    
    public abstract String getTipo();
}