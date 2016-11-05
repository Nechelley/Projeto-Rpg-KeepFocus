package rpg.personagens;

/**
 * Representa um atributo (a classe) do personagem:
 * 0-Guerreiro ou 1-Mago ou 2-Clerigo ou 3-Zumbi ou 4-Esqueleto ou 
 * 5-Dragao ou 6-Goblin ou 7-Cultista ou 8-Ogro
 * 
 * @author Nechelley Alves
 */
public enum Classe {
    GUERREIRO(0,"Guerreiro"),MAGO(1,"Mago"),CLERIGO(2,"Clérigo"),ZUMBI(3,"Zumbi"),
    ESQUELETO(4,"Esqueleto"),DRAGAO(5,"Dragão"),GOBLIN(6,"Goblin"),CULTISTA(7,"Cultista"),
    OGRO(8,"Ogro"),BIG(9,"Big"),MARRENTO(10,"Marrento"),INCENDIARIO(11,"Incendiário"),
    CHEFE(12,"Chefe");
    
    private final int id;
    private final String nome;
    
    private Classe(int id, String nome){
        this.id = id;
        this.nome = nome;
    }
    
    /**
     * @return Int com o id da classe
     */
    public int getId(){
        return id;
    }
    
    /**
     * @return String com o nome da classe
     */
    public String getString(){
        return nome;
    }
}
