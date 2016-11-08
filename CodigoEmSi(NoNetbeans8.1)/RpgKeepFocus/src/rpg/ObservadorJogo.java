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
    
    void antesDeIniciarRodadaDeBatalhas();
    
    void apresentandoInimigos(String[][] inf);
    
    void IniciandoTurnos();
    
    void exibindoInformacoesResumidasDeTodosOsLutadores(String[][] inf);
    
    void exibindoTurnoAtual();
    
    void antesDeExibirVezDoLutador(String lutadorNome);
    
    void exibindoRelatorio(String relatorio);
    
    void encerramentoDaBatalha();
    
    void exibirFim();
    
    int antesDeSair(boolean jaSalvou);
    
    int antesDeEscolherAcao(int i);
    
    String escolhendoAlvo(int i);
    
    String escolhendoGolpe(int i);
}
