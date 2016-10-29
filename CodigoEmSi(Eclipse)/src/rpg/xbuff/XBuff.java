package rpg.xbuff;

import java.io.Serializable;

/**
 *
 * @author neche
 */
public class XBuff implements Serializable {
    //nome no Xbuff
    private String nome;
    //quantos pontos sao infligidos/adicionados 
    private int pontosDeEfeito;
    //onde os pontos sao colocados/removidos
    //sendo 0 - vida, 1 - pularTurno
    private int localDeEfeito;
    //quantidade de turnos que o efeito permanece
    private int duracao;
    
    public XBuff(String nome, int pontosDeEfeito, int localDeEfeito, int duracao){
        this.nome = nome;
        this.pontosDeEfeito = pontosDeEfeito;
        this.localDeEfeito = localDeEfeito;
        this.duracao = duracao;
    }
    
    public XBuff(XBuff b){
        this.nome = b.nome;
        this.pontosDeEfeito = b.pontosDeEfeito;
        this.localDeEfeito = b.localDeEfeito;
        this.duracao = b.duracao;
    }
    
    public String getNome(){
        return nome;
    }
    
    public int getPontosDeEfeito(){
        return pontosDeEfeito;
    }
    
    public int getLocalDeEfeito(){
        return localDeEfeito;
    }
    
    public int getDuracao(){
        return duracao;
    }
    
    public void rodadaPassou(){
        duracao--;
    }
}
