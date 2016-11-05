package rpg;

/**
 *
 * @author neche
 */
public class AcaoInvalidaException extends RuntimeException{
    //motivo do erro
    private int motivo;
    
    /**
     * Construtor da excecao
     * 
     * @param acao String com acao que gerou a excecao
     * @param motivo int dizendo qual o motivo do erro, 
     * sendo 0 para pontos insuficientes,
     * 1 para quando durante um ataque o golpe usado é invalido
     * 2 para quando a acao nao pode ser executada devido ao fato do turno de batalhas ja ter comecado
     * -1 para quando nao ha motivos
     */
    public AcaoInvalidaException(String acao, int motivo){
        super("Ação " + acao + " inválida.");
        this.motivo = motivo;
    }
    
    public String getMotivo(){
        switch(motivo){
            case -1:
                return "Sem motivos aparentes.";
            case 0:
                return "Pontos de ação insuficientes.";
            case 1:
                return "Golpe usado durante o ataque é invalido.";
            case 2:
               return "A acao nao pode ser executada devido ao fato do turno de batalhas ja ter começado.";
            case 3:
               return "Personagem já esta em estado de esquiva.";
            case 4:
               return "Personagem já esta em estado de defesa.";
            default:
                return "Sem motivo fornecido.";
        }
    }
}
