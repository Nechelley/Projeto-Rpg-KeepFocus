package rpg.personagens;

/**
 * Representa um atributo (o foco) do personagem:
 *  Força ou Destreza ou Constituição ou Carisma
 * 
 * @author Nechelley Alves
 */
public enum Foco {
    FORCA(0,"Força"), DESTREZA(1,"Destreza"), CONSTITUICAO(2,"Constituição"), CARISMA(3,"Carisma");
    
    private final int id;
    private final String nome;
    
    private Foco(int id, String nome){
        this.id = id;
        this.nome = nome;
    }
    
    /**
     * Retorna o Foco passando um id
     * 
     * @param id int com o numero de identificacao do Foco
     * @return Foco procurado, ou null caso nao encontre
     */
    public static Foco getPontoFortePorId(int id){
        if(id > 4 || id < 0)
            return null;
        Foco pf = Foco.CARISMA;
        switch (id) {
            case 0:
                pf = Foco.FORCA;
                break;
            case 1:
                pf = Foco.DESTREZA;
                break;
            case 2:
                pf = Foco.CONSTITUICAO;
                break;
            case 3:
                pf = Foco.CARISMA;
                break;
            default:
                break;
        }
        return pf;
    }
    
    /**
     * @return String com o id do foco
     */
    public int getId(){
        return id;
    }
    
    /**
     * @return String com o nome do foco
     */
    public String getString(){
        return nome;
    }
}
