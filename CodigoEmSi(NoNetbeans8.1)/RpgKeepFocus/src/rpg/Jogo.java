package rpg;

import rpg.batalhas.Batalha;
import rpg.excecoes.AcaoInvalidaException;
import rpg.excecoes.InfInvalidoException;
import java.io.Serializable;
import rpg.personagens.Dragao;
import rpg.personagens.Ogro;
import rpg.personagens.Inimigo;
import rpg.personagens.Zumbi;
import rpg.personagens.Esqueleto;
import rpg.personagens.Cultista;
import rpg.personagens.Personagem;
import rpg.personagens.Heroi;
import rpg.personagens.Goblin;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import rpg.golpes.Golpe;
import rpg.personagens.Arma;
import rpg.personagens.Armadura;
import rpg.personagens.Classe;
import rpg.personagens.Foco;
import rpg.personagens.SituacaoDeVida;

/**
 *
 * @author Nechelley Alves
 */
public class Jogo implements Serializable{
    //lista com os herois
    private final List<Personagem> timeDosHerois;
    //pontuacao do jogador no jogo, representa quantas partidas o time de herois venceu
    private int pontuacao;
    //Batalha Atual que esta ocorrendo
    private Batalha batalhaAtual;
    //determina a fase em que o jogo esta
    private SituacaoDoJogo situacaoDoJogo;
    
    
    //objeto que esta ouvindo o jogo
    private ObservadorJogo tela;
    //arquivo que salva e carrega o jogo
    private Arquivo arquivo;
    
    //nível de habilidade Do Time De Herois
    private int habilidadeDoTimeDeHerois;
    //nível de habilidade Do Time De Inimigos 
    private int habilidadeDoTimeDeInimigos;
    
    
    /**
     * Cria um novo jogo
     * 
     * @param tela Objeto da classe que implementar ObservadorJogo
     */
    public Jogo(ObservadorJogo tela){
        pontuacao = 0;
        habilidadeDoTimeDeHerois = 0;
        habilidadeDoTimeDeInimigos = 0;
        batalhaAtual = null;
        situacaoDoJogo = SituacaoDoJogo.CRIANDOHEROIS;
                
        timeDosHerois = new ArrayList<Personagem>();
        if(tela == null)
            throw new InfInvalidoException("Tela","NULL");
        this.tela = tela;
    }

    
    
    
    //JOGO EM SI
    
    /**
     * Metodo principal contendo a principal logica do jogo
     */
    public void iniciar(){
        //primeiramente o jogador cria o time de heroi com que ele jogara a partida
        criandoTimeDeHerois();
        
        //inicio as rodadas de batalhas
        tela.iniciandoRodadaDeBatalhas();
        situacaoDoJogo = SituacaoDoJogo.BATALHASCOMECARAM;
        
        while(situacaoDoJogo != SituacaoDoJogo.ACABOU){
            //comeca a batalha atual
            iniciarBatalha();
            
            //Apresento os inimigos
            tela.apresentandoInimigos(matrizInimigos());
            
            //Inicio o turno atual
            tela.iniciandoTurnos();

            // situaçao de todos os personagens
            tela.exibindoInformacoesResumidasDeTodosOsLutadores(matrizLutadores());
             
            //inicio do loop da batalha atual
            while(!batalhaAtual.verificaSeBatalhaAcabou()){//enquanto a batalha estiver ocorrendo, ou seja, nao acabou
                
                tela.exibindoTurnoAtual(batalhaAtual.getTurno());
                            
                //começo o loop do turno
                boolean flagDoTurno = true;
                
                //loop do Turno
                for(int i = 0; i < batalhaAtual.getNumLutadores() && flagDoTurno; i++){
                    String relatorio = new String();
                    
                    Personagem lutador = batalhaAtual.getLutadores().get(i);
                    String nomeLutador = lutador.getNome();
                    
                    tela.antesDeExibirVezDoLutador(nomeLutador);
                    
                    //verifico se o lutador esta em condicao de lutar
                    if(lutador.getSituacaoDeVida() == SituacaoDeVida.INCONSCIENTE || 
                            lutador.getSituacaoDeVida() == SituacaoDeVida.MORTO){
                        
                        relatorio = nomeLutador + " não esta em condição de executar qualquer ação.";
                        tela.exibindoRelatorio(relatorio);
                        continue;
                    }

                    //neste ponto verifico se o lutador é um Inimigo e ativo a IA se necessario
                    if(!lutador.getEhHeroi()){
                        relatorio = executarIA(lutador);
                        tela.exibindoRelatorio(relatorio);
                        
                        if(batalhaAtual.verificaSeBatalhaAcabou()){
                            //se entrou neste if quer dizer que a batlaha acabou no turno de um inimigo, portanto os herois perderam
                            flagDoTurno = false;
                            tela.encerramentoDaBatalha(getVencedores());
                            encerrarJogo();
                            tela.exibirFim(pontuacao);
                        }else
                            // situaçao de todos os personagens
                            tela.exibindoInformacoesResumidasDeTodosOsLutadores(matrizLutadores());
                        continue;
                    }
                    
                    //personagem escolhe a acao que deseja executar
                    escolherAcao(lutador);
                    
                    //escrever o que aconteceu, como se desenrolou a acao
                    if(batalhaAtual.verificaSeBatalhaAcabou()){
                        flagDoTurno = false;
                        tela.encerramentoDaBatalha(getVencedores());
                        ganharPonto();
                        tela.exibirFim(pontuacao);

                    }else{
                        // situaçao de todos os personagens
                        tela.exibindoInformacoesResumidasDeTodosOsLutadores(matrizLutadores());
                    }
                    
                }//fecha loop do Turno
                
                if (!batalhaAtual.verificaSeBatalhaAcabou())
                    passarTurno();
            }//fecha loop da batalha
        }//fecha loop do jogo
    }
    
    /**
     * Cria o time de herois, pedindo suas informacoes e guardando me timDosHerois
     */
    private void criandoTimeDeHerois(){
        int op;
        do{
            String[] heroiInf = tela.lendoHeroi();
            Personagem p = adicionarHeroi(heroiInf[0], heroiInf[1], heroiInf[2],
                    heroiInf[3], heroiInf[4]);
            
            //loop para adicionar os golpes do personagem
            int opg;
            do{
                String[] golpeInf = tela.lendoGolpe();
                int classe = Integer.parseInt(golpeInf[0]);
                String nomeGolpe = golpeInf[1];
                switch(classe){
                    case 1:
                        p.addGolpeFisico(nomeGolpe);
                        break;
                    case 2:
                        p.addGolpeMagicoBasico(nomeGolpe);
                        break;
                    case 3:
                        p.addGolpeMagicoBolaDeFogo(nomeGolpe);
                        break;
                    case 4:
                        p.addGolpeMagicoMeteoro(nomeGolpe);
                        break;
                    case 5:
                        p.addGolpeMagicoLancaDeGelo(nomeGolpe);
                        break;
                    case 6:
                        p.addGolpeMagicoNevasca(nomeGolpe);
                        break;
                }
                opg = tela.confirmarSeTemMaisGolpesDeHerois();
            }while(opg != 2);
            op = tela.confirmarSeTemMaisHerois();
        }while(op != 2);
    }
    /**
     * Cria e adiciona um heroi ao time dos herois do jogo
     * 
     * @param nome String com o nome do Heroi
     * @param classe String com qual sera a classe do Heroi
     * @param foco String com qual sera o ponto forte do Heroi
     * @param arma String dizendo qual o tipo de arma o Heroi usara
     * @param armadura String com qual o tipo de armadura o Heroi usara
     * @return Personagem que acabou de ser criado e adicionado ao time dos herois
     */
    public Personagem adicionarHeroi(String nome, String classe, String foco, String arma, String armadura){
        if(situacaoDoJogo != SituacaoDoJogo.CRIANDOHEROIS)
            throw new AcaoInvalidaException("adicionar heroi",2);
        
        Classe classee = Classe.getClassePorId(Integer.parseInt(classe));
        Foco focoo = Foco.getFocoPorId(Integer.parseInt(foco));
        Arma armaa = Arma.getArmaPorId(Integer.parseInt(arma));
        Armadura armaduraa = Armadura.getArmaduraPorId(Integer.parseInt(armadura));
        
        Personagem p = new Heroi(nome, classee, focoo, armaa, armaduraa);
        
        //adiciona a habilidadeDoTimeDeHerois do heroi no índice de habilidadeDoTimeDeHerois total
        habilidadeDoTimeDeHerois += p.getNivelDeSaude() + armaa.getValor() + armaduraa.getValor();
        
        timeDosHerois.add(p);
        
        return p;
    }
    
    
    
    /**
     * Matriz de Inimigos na ordem
     * {posicaoDoInimigo}{nome,classe,foco,nivelDeSaude,danoRecebido,danoRecebidoMaximo,situacaoDeVida,
     * Arma,Armadura,Iniciativa}
     * 
     * @return String[][] com as informacoes dos inimigos
     */
    private String[][] matrizInimigos(){
        String[][] inf = new String[batalhaAtual.getInimigos().size()][10];
        int cont = 0;
        for(Personagem p : batalhaAtual.getInimigos()){
            inf[cont][0] = p.getNome();
            inf[cont][1] = p.getClasse().getString();
            inf[cont][2] = p.getFoco().getString();
            inf[cont][3] = String.valueOf(p.getNivelDeSaude());
            inf[cont][4] = String.valueOf(p.getDanoRecebido());
            inf[cont][5] = String.valueOf(p.getDanoRecebidoMaximo());
            inf[cont][6] = p.getSituacaoDeVida().getString();
            inf[cont][7] = p.getArma().getString();
            inf[cont][8] = p.getArmadura().getString();
            inf[cont][9] = String.valueOf(p.getIniciativa());
            cont++;
        }
        return inf;
    }
    /**
     * Matriz de Lutadores ordem
     * {posicaoDoLutador}{defendendo,esquivando,nome,danoRecebido,danoRecebidoMaximo,situacaoDeVida,pontosDeAcao}
     * 
     * @return String[][] com as informacoes dos lutadores
     */
    private String[][] matrizLutadores(){
        String[][] inf = new String[batalhaAtual.getLutadores().size()][7];
        int cont = 0;
        for(Personagem p : batalhaAtual.getLutadores()){
            if(p.getEstaDefendendo())
                inf[cont][0] = " (defendendo)";
            else
                inf[cont][0] = "";
            if(p.getEstaEsquivando())
                inf[cont][1] = " (esquivando)";
            else
                inf[cont][1] = "";
            
            inf[cont][2] = p.getNome();
            inf[cont][3] = String.valueOf(p.getDanoRecebido());
            inf[cont][4] = String.valueOf(p.getDanoRecebidoMaximo());
            inf[cont][5] = p.getSituacaoDeVida().getString();
            inf[cont][6] = String.valueOf(p.getPontosDeAcao());
            cont++;
        }

        return inf;
    }
    /**
     * Vetor com as informacoes necessarias de um lutador para exibir o menu de 
     * escolher acao na ordem
     * {nome,pontosDeAcao,(estaDefendendo ou ""),(estaEsquivando ou "")}
     * 
     * @param lutador Personagem com as informacoes
     * @return String[] com as informacoes
     */
    private String[] vetorParaEscolherAcao(Personagem lutador){
        String[] inf = new String[4];
        inf[0] = lutador.getNome();
        inf[1] = String.valueOf(lutador.getPontosDeAcao());
        if(!lutador.getEstaDefendendo())
            inf[2] = "podeDefender";
        else
            inf[2] = "";
        if(lutador.getFoco() == Foco.DESTREZA && !lutador.getEstaEsquivando())
            inf[3] = "podeEsquivar";
        else
            inf[3] = "";
        
        return inf;
    }
    /**
     * Vetor com as informacoes necessarias de um lutador para exibir o menu de 
     * escolher alvo, vem com todos os nomes de alvos
     * 
     * @param lutador Personagem com as informacoes
     * @return String[] com as informacoes
     */
    private String[] vetorParaEscolherAlvo(Personagem lutador){
        List<Personagem> lista = batalhaAtual.getPossiveisAlvos(lutador);
        String[] inf = new String[lista.size()];
        int cont = 0;
        for(Personagem p : lista){
            inf[cont] = p.getNome();
            cont++;
        }
        return inf;
    }
    /**
     * Matriz com as informacoes dos golpes na ordem
     * {posicaoDoGolpe}{nome,custo}
     * 
     * @lutador Personagem para se adquirir as informacoes
     * @return String[][] com as informacoes dos golpes
     */
    private String[][] matrizParaEscolherGolpe(Personagem lutador){
        List<Golpe> lista =lutador.getGolpes();
        String[][] inf = new String[lista.size()][2];
        int cont = 0;
        for(Golpe g : lista){
            inf[cont][0] = g.getNome();
            inf[cont][1] = String.valueOf(g.getCustoDeAcao());
            cont++;
        }
        return inf;
    }
    
    
    
    
    /**
     * metodo para escolher o heroi escolher sua acao
     * @param lutador pos do lutador
     */
    public void escolherAcao(Personagem lutador){
        //loop do Personagem
        boolean flagPersonagem = true;//flag para a acao do personagem
        String relatorio = new String();
        while(flagPersonagem){
            //Personagem escolhe sua acao
            int acao = tela.escolhendoAcao(vetorParaEscolherAcao(lutador));
            
            switch(acao){
                // Ataque
                case 1:
                    try{
                        relatorio = atacar(lutador);
                    }
                    catch(AcaoInvalidaException e){
                        relatorio = e.getMessage() + " " + e.getMotivo();
                    }
                    break;
                // Defender
                case 2:
                    relatorio = defender(lutador);
                    break;
                //Esquivar
                case 3:
                    relatorio = esquivar(lutador);
                    break;
                case 5:
                    relatorio = lutador.getNome() + " encerrou sua vez.";
                    flagPersonagem = false;//sai do loop de personagens
                    break;
                case 6:
                    tela.exibindoInformacoesResumidasDeTodosOsLutadores(matrizLutadores());
                    break;
                default:
                    relatorio = " Acão inválida!"; 
            }//fecha switch(acao)
            tela.exibindoRelatorio(relatorio);
            
            if(lutador.getPontosDeAcao() == 0 || batalhaAtual.verificaSeBatalhaAcabou())
                flagPersonagem = false;//sai do loop de personagens
        }///fecha loop do Personagem
    }
    /**
     * Executa a cao de esquivar do lutador
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return relatorio de esquivar
     */
    public String esquivar(Personagem lutador){
        if(!(lutador.getFoco() == Foco.DESTREZA && !lutador.getEstaEsquivando()))
            return " Acão inválida!";
        lutador.esquivar();
        return (lutador.getNome() + " podera tentar esquivar do proximo ataque que receber.");
    }
    /**
     * Executa a cao de defender do lutador
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return relatorio de defender
     */
    public String defender(Personagem lutador){
        if(lutador.getEstaDefendendo())
            return " Acão inválida!";
        lutador.defender();
        return (lutador.getNome() + " podera se defender do proximo ataque que receber.");
    }
    /**
     * Executa a cao de atacado lutador
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return relatorio de atacar
     */
    public String atacar(Personagem lutador){
        //ler informações necessarias para executar a acao atacar
        
        if(lutador.getPontosDeAcao() == 0){
            return "Voce nao possui pontos de acao suficiente para esse problema";
        }
        
        //escolhendo alvo
        String alvoNome = tela.escolhendoAlvo(vetorParaEscolherAlvo(lutador));

        //escolhendo o golpe
        String golpeNome = tela.escolhendoGolpe(matrizParaEscolherGolpe(lutador));
        
        //agora que ja foi recebido as informações necessarias, executa-se a ação atacar
        return lutadorAtacaAlvo(lutador,alvoNome,golpeNome);
    }
    
    
    /**
     * Aciona a acao atacar do lutador na posicao posAtacante e seleciona como alvo
     * deste ataque o lutador com o nome de alvoNome
     * 
     * @param atacante Personagem que esta atacando
     * @param nomeAlvo String com o nome do alvo
     * @param golpe String com o nome do golpe sendo executado
     * @return String dizendo oque aconteceu no ataque
     */
    public String lutadorAtacaAlvo(Personagem atacante, String nomeAlvo, String golpe){
        
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(atacante);
        
        Personagem alvo = null;
        for(int i = 0; i < alvos.size(); i++){
            if(nomeAlvo.equals(alvos.get(i).getNome()))
                alvo = alvos.get(i);
        }
        
        return atacante.atacar(alvo,atacante.getGolpePeloNome(golpe));
    }
    
   
 
     
    
    /**
     * Inicia uma batalha, criando ela e escolhendo aleatoriamente os inimigos que participarao
     * da mesma.
     */
    public void iniciarBatalha(){
        //escolhendo inimigos
        List<Personagem> inimigos = new ArrayList<Personagem>();
        
        Random aleatorio = new Random();
        //Adiciona um número de inimigos na lista
        do{
            int pos = aleatorio.nextInt(Inimigo.numDeClassesDeInimigos());
            if(pos < 0 || pos > Inimigo.numDeClassesDeInimigos() -1)
                throw new IndexOutOfBoundsException("Posição " + pos + " não existe em inimigos.");
            inimigos.add(adicionarInimigo(pos));
        }while (habilidadeDoTimeDeInimigos < habilidadeDoTimeDeHerois);
        
        if (inimigos.isEmpty())
            throw new RuntimeException("Nenhum inimigo adicionado na batalha.");
        
        batalhaAtual = new Batalha(timeDosHerois, inimigos);
        batalhaAtual.iniciarBatalha();
    }
    /**
     * Define um inimigo a ser adicionado a partir de um número de índice
     * 
     * @param id Int com o índice do inimigo na lista
     * @return Inimigo a ser adicionado
     */
    private Inimigo adicionarInimigo(int id) {
        Inimigo i = null;
        Random aleatorio = new Random();
        Arma arma = null;
        Armadura armadura = null;
        Foco pontoForte = Foco.getFocoPorId(aleatorio.nextInt(4));
        switch (id) {
            case 0:
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.getArmaduraPorId(0);
                i = new Goblin(pontoForte,arma,armadura);
                i.addGolpeFisico("Facada");
                break;
            case 1:
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.getArmaduraPorId(0);
                i = new Cultista(pontoForte,arma,armadura);
                i.addGolpeFisico("Facada");
                i.addGolpeMagicoMeteoro("Invocacao");
                break;
            case 2:
                arma = Arma.getArmaPorId(aleatorio.nextInt(3));
                armadura = Armadura.getArmaduraPorId(aleatorio.nextInt(2));
                i = new Dragao(pontoForte,arma,armadura);
                i.addGolpeFisico("Garras");
                i.addGolpeMagicoBolaDeFogo("Bola de fogo");
                break;
            case 3:
                arma = Arma.getArmaPorId(aleatorio.nextInt(3));
                armadura = Armadura.getArmaduraPorId(aleatorio.nextInt(2));
                i = new Ogro(pontoForte,arma,armadura);
                i.addGolpeFisico("Machadada");
                break;
            case 4:
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.getArmaduraPorId(0);
                i = new Esqueleto(pontoForte,arma,armadura);
                i.addGolpeFisico("Espadada");
                break;
            case 5:
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.getArmaduraPorId(0);
                i = new Zumbi(pontoForte,arma,armadura);
                i.addGolpeFisico("Arranhao");
                break;
            default:
                /*
                    ARRUMAR DEPOIS PARA TER TODOS OS INIMIGOS
                
                */
                arma = Arma.getArmaPorId(aleatorio.nextInt(2));
                armadura = Armadura.getArmaduraPorId(0);
                i = new Zumbi(pontoForte,arma,armadura);
                i.addGolpeFisico("Arranhao");
                break;
        }
        
        if (i == null){
            throw new RuntimeException("Inimigo não adicionado, ID inválida");
        }
        
        habilidadeDoTimeDeInimigos += i.getNivelDeSaude() + arma.getValor() + armadura.getValor();
        return i;
        
    }
    
    /**
     * Encerra o jogo 
     */
    public void encerrarJogo(){
        situacaoDoJogo = SituacaoDoJogo.ACABOU;
    }  
    
    
    
    
    
    //QUESTAO DOS ALVOS
    
    /**
     * Executa a IA dos inimigos
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return String dizendo oque aconteceu na acao
     */
    public String executarIA(Personagem lutador){
        Classe classeLutador = lutador.getClasse();
        SituacaoDeVida situacaoLutador = lutador.getSituacaoDeVida();
        Personagem alvo = null;
        
        if (situacaoLutador == SituacaoDeVida.SAUDAVEL) {
            //Define a estratégia de luta de acordo com o lutador
            switch(classeLutador){
                case DRAGAO:
                    alvo = definirAlvoMaisForte(lutador);
                    break;
                case OGRO:
                case GOBLIN:
                    if (lutador.rodarDado() > 5) {
                        return defender(lutador);
                    } else {
                        alvo = definirAlvoMaisFraco(lutador);
                    }
                    break;
                default:
                    if (lutador.rodarDado() > 4) {
                        return defender(lutador);
                    } else {
                        alvo = definirAlvoAleatorio(lutador);
                    }
                    break;
            }
        } else if (situacaoLutador == SituacaoDeVida.ATORDOADO) {
            switch (classeLutador) {
                case DRAGAO:
                    int chance = lutador.rodarDado();
                    if (chance == 6)
                        return defender(lutador);
                    else if (chance > 2)
                        alvo = definirAlvoMaisForte(lutador);
                    else
                        alvo = definirAlvoAleatorio(lutador);
                    break;
                case OGRO:
                case GOBLIN:
                    if (lutador.rodarDado() > 3)
                        alvo = definirAlvoMaisForte(lutador);
                    else
                        alvo = definirAlvoAleatorio(lutador);
                    break;
                default:
                    if (lutador.rodarDado() > 4) {
                        return defender(lutador);
                    } else {
                        alvo = definirAlvoAleatorio(lutador);
                    }
                    break;
            }
        } else {
            switch (classeLutador) {
                case DRAGAO:
                    if (lutador.rodarDado() > 4)
                        alvo = definirAlvoMaisForte(lutador);
                    else
                        alvo = definirAlvoAleatorio(lutador);
                    break;
                case OGRO:
                    if (lutador.rodarDado() == 6)
                        alvo = definirAlvoMaisForte(lutador);
                    else
                        alvo = definirAlvoAleatorio(lutador);
                    break;
                case GOBLIN:
                    int chance = lutador.rodarDado();
                    if (chance == 6)
                        alvo = definirAlvoMaisForte(lutador);
                    else if (chance > 3)
                        return defender(lutador);
                    else
                        alvo = definirAlvoAleatorio(lutador);
                    break;
                default:
                    if (lutador.rodarDado() > 2) {
                        return defender(lutador);
                    } else {
                        alvo = definirAlvoAleatorio(lutador);
                    }
                    break;
            }
        }

       return lutadorAtacaAlvo(lutador,alvo.getNome(),definirGolpeNoAlvo(lutador).getNome());
    }
    
    /**
     * Define um alvo aleatório a ser atacado.
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return Personagem alvo
     */
    private Personagem definirAlvoAleatorio(Personagem lutador) {
        List<Personagem> lista = batalhaAtual.getPossiveisAlvos(lutador);
        Random random = new Random();
        
        return lista.get(random.nextInt(lista.size()));
    }
    /**
     * Define o alvo a ser atacado como o alvo mais fraco.
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return Personagem alvo
     */
    private Personagem definirAlvoMaisFraco(Personagem lutador) {
        //Faz uma lista de todos os alvos possíveis do atacante
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(lutador);
        
        //Percorre a lista verificando qual é o alvo com maior dano
        Personagem alvo = alvos.get(0);
        int maiorDano = alvo.getDanoRecebido();
        for(Personagem p : alvos){
            SituacaoDeVida situacaoAlvo = p.getSituacaoDeVida();
            
            //Verifica se o dano do alvo é menor e se o alvo não está incapaz
            if (maiorDano < p.getDanoRecebido() &&
                    !(situacaoAlvo == SituacaoDeVida.INCONSCIENTE) &&
                    !(situacaoAlvo == SituacaoDeVida.MORTO)) {
                maiorDano = p.getDanoRecebido();
                alvo = p;
            }
        }

        return alvo;
    }    
    /**
     * Define o alvo a ser atacado como o alvo mais fraco.
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return Personagem alvo
     */
    private Personagem definirAlvoMaisForte(Personagem lutador) {
        //Faz uma lista de todos os alvos possíveis do atacante
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(lutador);
        System.out.println("asdf");
        //Percorre a lista verificando qual é o alvo com menor dano
        Personagem alvo = alvos.get(0);
        int menorDano = alvo.getDanoRecebido();
        for (Personagem p : alvos) {
            if (menorDano > p.getDanoRecebido()) {
                menorDano = p.getDanoRecebido();
                alvo = p;
            }
        }

        return alvo;
    }
    /**
     * Define um golpe aleatorio do lutador
     * 
     * @param lutador lutador na lista de lutadores da batalha
     * @return Golpe aleatorio
     */
    private Golpe definirGolpeNoAlvo(Personagem lutador){
        List<Golpe> lista = lutador.getGolpes();
        Random random = new Random();
        return lista.get(random.nextInt(lista.size()));
    }
       

    
    
    //OUTROS
    
    /**
     * Passa o turno da batalha
     */
    public void passarTurno(){
        List<Personagem> list = batalhaAtual.getLutadores();
        //reinicio os pontos de acao para o proximo turno
        for(Personagem l : list){
            l.resetarPontosDeAcao();
        }
        batalhaAtual.passarTurno();
    }
    
    /**
     * Aumenta a pontuacao em um ponto, deve ser usado apos uma vitoria de batalha
     */
    public void ganharPonto(){
        pontuacao++;
    }
    
    /**
     * Retorna o vencedor da batalha atual
     * 
     * @return String dizendo quem venceu
     */
    public String getVencedores(){
        if(!batalhaAtual.verificaSeBatalhaAcabou())
            return "A Batalha ainda não acabou.";
        return batalhaAtual.getVencedores();
    }
}
