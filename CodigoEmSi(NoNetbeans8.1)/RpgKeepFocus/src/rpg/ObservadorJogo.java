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
    
    void exibindoInformacoesResumidasDeTodosOsLutadores(String[][] inf);
    
    void exibindoTurnoAtual();
    
    void antesDeExibirVezDoLutador(String lutadorNome);
    
    void exibindoRelatorio(String relatorio);
    
    void encerramentoDaBatalha();
    
    void exibirFim();
    
    int escolhendoAcao(String[] inf);
    
    String escolhendoAlvo(String[] inf);
    
    String escolhendoGolpe(String[][] inf);
}
