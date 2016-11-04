package rpg.personagens;

/**
 * Representa um atributo (a classe) do personagem:
 *  Guerreiro ou Mago ou Clerigo ou Zumbi ou Esqueleto ou Dragao ou Goblin ou Cultista ou Ogro
 * 
 * @author Nechelley Alves
 */
public enum Classe {
    GUERREIRO(0,"Guerreiro"),MAGO(1,"Mago"),CLERIGO(2,"Clérigo"),ZUMBI(3,"Zumbi"),
    ESQUELETO(4,"Esqueleto"),DRAGAO(5,"Dragão"),GOBLIN(6,"Goblin"),CULTISTA(7,"Cultista"),
    OGRO(8,"Ogro");
    
    private final int id;
    private final String nome;
    
    private Classe(int id, String nome){
        this.id = id;
        this.nome = nome;
    }
    
    /**
     * @return String com o id da classe
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
