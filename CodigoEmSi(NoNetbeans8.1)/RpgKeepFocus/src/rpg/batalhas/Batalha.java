package rpg.batalhas;

import rpg.excecoes.InfInvalidoException;
import java.io.Serializable;
import rpg.personagens.Personagem;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe que molda uma batalha
 * 
 * @author Nechelley Alves
 */
public class Batalha implements Serializable{
    //variavel que diz se esta ou nao ocorrendo a batalha
    private SituacaoDaBatalha situacaoDaBatalha;
    //lista com os personagens
    private List<Personagem> lutadores;
    //representa o turno que esta a batalha
    private int turno;
    //String dizendo quem venceu
    private String vencedores;
    
    /**
     * Construtor que cria uma batalha, mas nao a inicializa ainda
     * 
     * @param timeDosHerois Lista com os herois que lutarao no lado dos herois nesta partida
     * @param timeDosInimigos Lista com os timeDosInimigos que lutarao no lado dos inimigos nesta partida
     */
    public Batalha(List<Personagem> timeDosHerois, List<Personagem> timeDosInimigos){
        situacaoDaBatalha = SituacaoDaBatalha.NAOOCORRENDO;
        turno = 1;
        vencedores = null;
        lutadores = new ArrayList<Personagem>();
        if(timeDosHerois == null)
            throw new InfInvalidoException("Lista do time dos herois","NULL");
        if(timeDosInimigos == null)
            throw new InfInvalidoException("Lista dos inimigos","NULL");
        for(Personagem p : timeDosHerois)
            lutadores.add(p);
        for(Personagem p : timeDosInimigos)
            lutadores.add(p);
    }
    
    
    
    //QUESTAO DA INICIALIZACAO DA BATALHA
    
    /**
     * Inicializa a batalha
     */
    public void iniciarBatalha(){
        //defino que a batalha esta ocorrendo
        situacaoDaBatalha = SituacaoDaBatalha.OCORRENDO;
        //defino qual sera a ordem de acao dos personagens
        defineOrdemDeAcao();
    }
    /**
     * Define a ordem de ação dos lutadores com base no valor de suas iniciativas, 
     * ou seja, todos os lutadores jogam um d6 e quem tiver maior valor
     * (lembrando que quem tem foco em destreza ganha + 1 ponto no dado) comeca 
     * escolhendo a acao
     */
    private void defineOrdemDeAcao(){
        //seto iniciativa inicial
        setIniciativaInicial();
        //organizo a lista com base no valor da iniciativa de cada lutador
        Collections.sort(lutadores);
    }
    /**
     * Seta a iniciativa inicial de todos os lutadores
     */
    private void setIniciativaInicial(){
        //para cada lutador na batalha
        for(Personagem p : lutadores){
            p.setIniciativa( p.rodarDado() );
        }
    }
    
    
    
    
    
    //GETS
    
    //GETS DE INIMIGOS
    
     /**
     * Retorna o numero de inimigos em uma batalha
     * 
     * @return Int com o numero de inimigos na batalha
     */
    public int getNumInimigos(){
        int cont = 0;
        for(Personagem p : lutadores){
            if(!p.getEhHeroi())
                cont++;
        }
        return cont;
    }
    
    /**
     * Retorna a lista com todos os inimigos
     * 
     * @return Lista com os inimigos
     */
    public List<Personagem> getInimigos(){
        List<Personagem> list = new ArrayList<Personagem>();
        for(Personagem p : lutadores){
            if(!p.getEhHeroi())
                list.add(p);
        }
        return list;
    }
    
    
    
    
    
    
    //GETS DE LUTADORES
    
    /**
     * Retorna a quantidade de lutadores no total
     * 
     * @return Int com quantidade de jogadores
     */
    public int getNumLutadores(){
        return lutadores.size();
    }
    
    /**
     * Retorna a lista com todos os lutadores
     * 
     * @return Lista com os lutadores
     */
    public List<Personagem> getLutadores(){
        return lutadores;
    }
    
    
    
    

    
    //QUESTAO DOS ALVOS
    
    /**
     * Retorna uma lista com os possiveis personagens alvos de p
     * 
     * @param p Personagem que sera verificado seus possiveis alvos
     * @return List com os possiveis alvos de p
     */
    public List<Personagem> getPossiveisAlvos(Personagem p){
       List<Personagem> alvos = new ArrayList<Personagem>();
        
        //para cada lutador na batalha
        for(Personagem l : lutadores){
            //verifico se é antagonico ao personagem, e se não esta morto
            if(l.getEhHeroi() != p.getEhHeroi() && l.estaVivo()){
                alvos.add(l);
            }
        }
        
        return alvos;
    }
    
    
    
    
    
    
    //TURNOS
    
    /**
     * Passa o turno acrescentando em 1 o valor dele
     */
    public void passarTurno(){
        turno++;
    }
    
    /**
     * retorna o turno atual
     * 
     * @return Int com o turno atual
     */
    public int getTurno(){
        return turno;
    }
    
    
    
    
    
    
    
    
    //OUTROS
    
    /**
     * Retorna o numero de herois em uma batalha
     * 
     * @return Int com o numero de herois na batalha
     */
    public int getNumHerois(){
        int cont = 0;
        for(Personagem p : lutadores){
            if(p.getEhHeroi())
                cont++;
        }
        return cont;
    }
    
    /**
     * Retorna se a batalha atual ja acabou ou nao
     * 
     * @return Boolean onde (true)-a batalha acabou e alguem ganhou ou 
     * (false) caso contrario
     */
    public boolean verificaSeBatalhaAcabou(){
        //numero de herois mortos
        int mortosH = 0;
        //numero de inimigos mortos
        int mortosI = 0;
        
        //percorre a lista de lutadores
        for(Personagem p : lutadores){
            if(p.getEhHeroi()){
                //o personagem é heroi
                if(!p.estaVivo()){
                    //se nao esta vivo
                    mortosH++;
                }
            }
            else{
                //o personagem é inimigo
                if(!p.estaVivo()){
                    //se nao esta vivo
                    mortosI++;
                }
            }
        }

        if(mortosH == getNumHerois()){
            vencedores = "Inimigos";
            situacaoDaBatalha = SituacaoDaBatalha.NAOOCORRENDO;
            return true;
        }
        if(mortosI == getNumInimigos()){
            vencedores = "Herois";
            situacaoDaBatalha = SituacaoDaBatalha.NAOOCORRENDO;
            return true;
        }
        return false;
    }
    
    /**
     * Retorna uma string com os vencedores
     * 
     * @return String com os vencedores, caso ainda nao exista vencedor, retorna null
     */
    public String getVencedores(){
        return vencedores;
    }
}
