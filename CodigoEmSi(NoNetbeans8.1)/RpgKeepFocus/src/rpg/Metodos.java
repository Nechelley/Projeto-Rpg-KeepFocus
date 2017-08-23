/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rpg;

/**
 *
 * @author neche
 */
public class Metodos {
    /**
     * Verifica se um nome é valido, ou seja, composto de apenas letras(maiusculas e minusculas), numeros(0-9) e spaces
     * 
     * @param nome String com o nome a ser verificado
     * @return True se o nome for valido, false caso contrario
     */
    public static boolean verificaNome(String nome){
        if(nome.isEmpty())
            return false;
        String valoresValidos[] = {
            "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
            "0","1","2","3","4","5","6","7","8","9"," "
        };
        boolean letraValida = false;
        for(int i = 0; i < nome.length(); i++){
            letraValida = false;
            String aux = nome.substring(i, i + 1);
            for(String v : valoresValidos){
                if(aux.equals(v)){// USAR CHARAT()
                    letraValida = true;
                    break;
                }
            }
            if(!letraValida)
                return false;
        }
        return true;
    }
    
    /**
     * Verifica se o valor do dano base e valido.
     * 
     * @param danoBase valor a ser veificado
     * @return True se o nome for valido, false caso contrario
     */
    public static boolean verificaDanoBase(int danoBase) {
        return ((danoBase >= 0) && (danoBase <= 50));
    }
    
    /**
     * Verifica se o valor da chance de acerto e valido.
     * 
     * @param chanceDeAcerto valor a ser veificado
     * @return True se o nome for valido, false caso contrario
     */
    public static boolean verificaChanceDeAcerto(int chanceDeAcerto) {
        return ((chanceDeAcerto >= 2) && (chanceDeAcerto <= 12));
    }
    
    /**
     * Verifica se o valor do custo de acao e valido.
     * 
     * @param custoDeAcao valor a ser veificado
     * @return True se o nome for valido, false caso contrario
     */
    public static boolean verificaCustoDeAcao(int custoDeAcao) {
        return ((custoDeAcao == 1) || (custoDeAcao == 2));
    }
}
