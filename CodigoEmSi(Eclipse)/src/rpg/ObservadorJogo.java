package rpg;

/**
 *
 * @author neche
 */
public interface ObservadorJogo {
    
    void antesDeCriarTimeDeHerois();
    
    void antesDeIniciarRodadaDeBatalhas();
    
    void antesDeApresentarInimigos();
    
    void antesDoInicioDoTurno();
    
    void antesDeExibirInformacoesResumidasDeTodosOsLutadores();
    
    void antesDeExibirTurnoAtual();
    
    void antesDeExibirVezDoLutador(String lutadorNome);
    
    void antesDeExibirRelatorio(String relatorio);
    
    void antesDoEncerramentoDaBatalha();
    
    void antesDaDerrota();
    
    void antesDaVitoria();
    
    int antesDeSair(boolean jaSalvou);
    
    int antesDeEscolherAcao(int i);
    
    String escolhendoAlvo(int i);
    
    String escolhendoGolpe(int i);
    
    String lerNomeArquivo(String operacao);
    
    void arquivoInvalido(String mensagem,boolean criar);
    
    boolean decideCarregarHerois();
    
    public void personagemArquivoInvalido(int linha);
    
    boolean decideSalvarHerois();
}
