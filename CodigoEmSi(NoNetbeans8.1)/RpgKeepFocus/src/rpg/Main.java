package rpg;

import rpg.excecoes.AcaoInvalidaException;

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
        catch(AcaoInvalidaException e){
            System.out.println(e.getMessage());
            System.out.println(e.getMotivo());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
