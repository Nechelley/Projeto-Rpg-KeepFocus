package rpg.personagens.enums;

/**
 * Representa um atributo (a classe) do personagem:
 * 0-Guerreiro ou 1-Mago ou 2-Clerigo ou 3-Zumbi ou 4-Esqueleto ou 
 * 5-Dragao ou 6-Goblin ou 7-Cultista ou 8-Ogro ou 9-Big ou 
 * 10-Marrento ou 11- Incendiario ou 12-Chefe
 * 
 * @author Nechelley Alves
 */
public enum Classe {
    GUERREIRO(0,"Guerreiro"),MAGO(1,"Mago"),CLERIGO(2,"Clérigo"),
    ZUMBI(3,"Zumbi"),ESQUELETO(4,"Esqueleto"),DRAGAO(5,"Dragão"),GOBLIN(6,"Goblin"),
    CULTISTA(7,"Cultista"),OGRO(8,"Ogro"),BIG(9,"Big"),MARRENTO(10,"Marrento"),
    INCENDIARIO(11,"Incendiário"), CHEFE(12,"Chefe");
    
    private final int id;
    private final String nome;
    
    private Classe(int id, String nome){
        this.id = id;
        this.nome = nome;
    }
    
    /**
     * Retorna a Classe passando um id
     * 
     * @param id int com o numero de identificacao da classe
     * @return Classe procurado, ou null caso nao encontre
     */
    public static Classe getClassePorId(int id){
        if(id > 12 || id < 0)
            return null;
        Classe c = null;
        switch (id) {
            case 0:
                c = Classe.GUERREIRO;
                break;
            case 1:
                c = Classe.MAGO;
                break;
            case 2:
                c = Classe.CLERIGO;
                break;
            case 3:
                c = Classe.ZUMBI;
                break;
            case 4:
                c = Classe.ESQUELETO;
                break;
            case 5:
                c = Classe.DRAGAO;
                break;
            case 6:
                c = Classe.GOBLIN;
                break;
            case 7:
                c = Classe.CULTISTA;
                break;
            case 8:
                c = Classe.OGRO;
                break;
            case 9:
                c = Classe.BIG;
                break;
            case 10:
                c = Classe.MARRENTO;
                break;
            case 11:
                c = Classe.INCENDIARIO;
                break;
            case 12:
                c = Classe.CHEFE;
                break;
            default:
                break;
        }
        return c;
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
