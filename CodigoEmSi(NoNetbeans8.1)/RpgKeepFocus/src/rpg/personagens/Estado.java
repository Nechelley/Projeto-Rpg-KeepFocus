package rpg.personagens;

/**
 * Representa o estado de um personagem que pode estar em estado de:
 * Esquiva ou Defendendo ou Congelado
 * 
 * @author Nechelley Alves
 */
public enum Estado {
    ESQUIVANDO(0,"Esquivando"),DEFENDENDO(1,"Defendendo"),CONGELADO(2,"Congelando");
    
    private final int id;
    private final String nome;
    
    private Estado(int id, String nome){
        this.id = id;
        this.nome = nome;
    }
    
    /**
     * @return String com o id do estado
     */
    public int getId(){
        return id;
    }
    
    /**
     * @return String com o nome do estado
     */
    public String getString(){
        return nome;
    }
}
