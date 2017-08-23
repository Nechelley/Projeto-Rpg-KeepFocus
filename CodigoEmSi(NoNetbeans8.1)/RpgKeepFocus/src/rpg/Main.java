package rpg;

/**
 *
 * @author neche
 */
public class Main {
    public static void main(String[] args){
        try{
            //TelaDoJogo tela = new TelaDoJogo();
            TelaDoJogoAlternativa tela = new TelaDoJogoAlternativa();
            
            tela.exibirInicio();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
