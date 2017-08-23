package rpg.golpes;

import java.io.Serializable;
import rpg.Metodos;

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
    Golpe(String nome, int danoBase, int chanceDeAcerto, int custoDeAcao){
        if(!Metodos.verificaNome(nome))
            throw new RuntimeException("Nome do golpe inválido (" + nome + ")");
        this.nome = nome;
        
        if(!Metodos.verificaDanoBase(danoBase))
            throw new RuntimeException("Dano base do golpe inválido (" + danoBase + ")");
        this.danoBase = danoBase;
        
        if(!Metodos.verificaChanceDeAcerto(chanceDeAcerto))
            throw new RuntimeException("Chance de acerto do golpe inválido (" + chanceDeAcerto + ")");
        this.chanceDeAcerto = chanceDeAcerto;
        
        if(!Metodos.verificaCustoDeAcao(custoDeAcao))
            throw new RuntimeException("Custo de ação do golpe inválido (" + custoDeAcao + ")");
        this.custoDeAcao = custoDeAcao;
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
     * Retorna o dano base do golpe
     * 
     * @return Int com o dano base do golpe
     */
    protected int getDanoBase(){
        return danoBase;
    }       
    
    /**
     * Metodo para definir o valor do dano de um golpe durante a batalha
     * 
     * @param valorDoDado Valor do dado jogado
     */
    public abstract int getDanoEmBatalha(int valorDoDado);
    
    /**
     * Retorna o custo do golpe
     * 
     * @return int com o custo do golpe
     */
    public int getCustoDeAcao(){
        return custoDeAcao;
    }  
    
    
    
    /**
     * Altera a chance de acerto do golpe
     * 
     * @param valor Int com o novo valor de chance de acerto
     */
    protected void setChanceDeAcerto(int valor){
        if(!Metodos.verificaChanceDeAcerto(valor))
            throw new RuntimeException("Chance de acerto inválida ( " + valor + " )");
        chanceDeAcerto = valor;
    }
    
    /**
     * Altero o valor do dano base
     * 
     * @param valor int com novo valor do dano base
     */
    protected void setDanoBase(int valor){
        if(!Metodos.verificaDanoBase(valor))// o mais 50 e so para via de duvidas
            throw new RuntimeException("Dano base inválido ( " + valor + " )");
        danoBase = valor;
    }
    
    /**
     * Altero o valor do custo
     * 
     * @param valor int com novo valor do custo
     */
    protected void setCustoDeAcao(int valor){
        if(!Metodos.verificaCustoDeAcao(valor))
            throw new RuntimeException("Custo de ação inválido ( " + valor + " )");
        custoDeAcao = valor;
    }
    
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        // se o objeto passado não é do mesmo tipo, eles não são iguais
        else if (!(o instanceof Golpe)) {
            return false;
        }
        else {
            Golpe outro = (Golpe) o; // Por que podemos fazer isso aqui?
            return nome.equals(outro.nome) && chanceDeAcerto == outro.chanceDeAcerto && danoBase == outro.danoBase && custoDeAcao == outro.custoDeAcao;
        }
    }
}