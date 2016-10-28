package rpg;
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
    private boolean estaOcorrendo;
    //lista com os personagens
    private ArrayList<Personagem> lutadores;
    //representa o turno que esta a batalha
    private int turno;
    //String dizendo quem venceu
    private String vencedores;
    
    
    
     //QUESTAO DA INICIALIZACAO DA BATALHA
    
    /**
     * Construtor que cria uma batalha, mas nao a inicializa ainda
     * 
     * @param timeDosHerois Lista com os herois que lutarao no lado dos herois nesta partida
     * @param mobs Lista com os mobs que lutarao no lado dos inimigos nesta partida
     */
    public Batalha(List<Personagem> timeDosHerois, List<Personagem> mobs){
        estaOcorrendo = false;
        turno = 1;
        lutadores = new ArrayList<Personagem>();
        if(timeDosHerois == null)
            throw new InfInvalidoException("Lista do time dos herois","");
        if(mobs == null)
            throw new InfInvalidoException("Lista dos inimigos","");
        for(int i = 0; i < timeDosHerois.size(); i++){
            lutadores.add(timeDosHerois.get(i));
        }
        for(int i = 0; i < mobs.size(); i++){
            lutadores.add(mobs.get(i));
        }
    }
    
    /**
     * Inicializa a batalha
     */
    public void comecarBatalha(){
        //defino que a batalha esta ocorrendo
        estaOcorrendo = true;
        //defino qual sera a ordem de acao dos personagens
        defineOrdemDeAcao();
    }
    /**
     * Define a ordem de ação dos lutadores com base no valor de suas iniciativas
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
     * @return int com o numero de inimigos na batalha
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
     * @return lsita com os inimigos
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
     * @return int com quantidade de jogadores
     */
    public int getNumLutadores(){
        return lutadores.size();
    }
    
    /**
     * Retorna a lista com todos os lutadores
     * 
     * @return lsita com os lutadores
     */
    public List<Personagem> getLutadores(){
        return lutadores;
    }
    
    
    
    

    
    //QUESTAO DOS ALVOS
    
    /**
     * Retorna a quantidade de alvos do Personagem p
     * 
     * @param p Personagem ao qual deseja-se saber a quantidade de alvos
     * @return int com a quantidade de possiveis alvo do personagem
     */
    public int getNumPossiveisAlvos(Personagem p){
        int cont = 0;
        
        //para cada lutador na batalha
        for(Personagem l : lutadores){
            //verifico se é antagonico ao personagem, e se não esta morto
            if(l.getEhHeroi() != p.getEhHeroi() && l.estaVivo()){
                cont++;
            }
        }
        
        return cont;
    }
    
    /**
     * Retorna uma lista com os possiveis personagens alvos de p
     * 
     * @param p Personagem que sera verificado seus possiveis alvos
     * @return ArrayList com os possiveis alvos de p
     */
    public ArrayList<Personagem> getPossiveisAlvos(Personagem p){
        ArrayList<Personagem> alvos = new ArrayList<Personagem>();
        
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
     * @return int com o turno atual
     */
    public int getTurno(){
        return turno;
    }
    
    
    
    
    
    
    
    
    //OUTROS
    
    /**
     * Retorna o numero de herois em uma batalha
     * 
     * @return int com o numero de herois na batalha
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
     * @return boolean onde (true)-a batalha acabou e alguem ganhou ou (false)-caso nao
     */
    public boolean getAcabou(){
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
            estaOcorrendo = false;
            return true;
        }
        else if(mortosI == getNumInimigos()){
            vencedores = "Herois";
            estaOcorrendo = false;
            return true;
        }
        else{
            return false;
        }
    }
    
    /**
     * Retorna uma string com os vencedores
     * @return string com os vencedores
     */
    public String getVencedores(){
        return vencedores;
    }
}
