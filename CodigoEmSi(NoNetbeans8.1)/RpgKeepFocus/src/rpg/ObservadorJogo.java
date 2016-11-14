package rpg;

/**
 *
 * @author neche
 */
public interface ObservadorJogo {
    
    String[] lendoHeroi();
    int confirmarSeTemMaisHerois();
    
    String[] lendoGolpe();
    public int confirmarSeTemMaisGolpesDeHerois();
    
    void iniciandoRodadaDeBatalhas();
    void apresentandoInimigos(String[][] inf);
    void iniciandoTurnos();
    void exibindoTurnoAtual(int turno);
    
    void exibindoVezDoLutador(String lutadorNome);
    
    void exibindoSituacaoDosPersonagens(String[][] inf);
    
    void exibindoRelatorio(String relatorio);
    
    void encerramentoDaBatalha(String vencedores, int pontuacao);
    
    int escolhendoAcao(String[] inf);
    String escolhendoAlvo(String[] inf);
    String escolhendoGolpe(String[][] inf);
    
    boolean querJogarOutraPartida();
}
