package rpg.personagens;

/**
 * Representa um atributo (a armadura) do personagem:
 *  Nada ou Leve ou Pesada
 * 
 * @author Nechelley Alves
 */
public enum Armadura {
    NADA(0,"Nada"),LEVE(1,"Leve"),PESADA(2,"Pesada");
    
    private final int id;
    private final String nome;
    
    private Armadura(int id, String nome){
        this.id = id;
        this.nome = nome;
    }
    
    /**
     * @return String com o id da armadura
     */
    public int getId(){
        return id;
    }
    
    /**
     * @return String com o nome da armadura
     */
    public String getString(){
        return nome;
    }
}
