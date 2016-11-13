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
    
    void exibindoTurnoAtual(int turno);
    
    void antesDeExibirVezDoLutador(String lutadorNome);
    
    void exibindoRelatorio(String relatorio);
    
    void encerramentoDaBatalha(String vencedores);
    
    void exibirFim(int pontuacao);
    
    int escolhendoAcao(String[] inf);
    
    String escolhendoAlvo(String[] inf);
    
    String escolhendoGolpe(String[][] inf);
}
