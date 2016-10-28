package rpg;

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

/**
 *
 * @author Nechelley Alves
 */
public class Jogo implements Serializable{
    //lista com os herois
    private final List<Personagem> timeDosHerois;
    //Batalha Atual que esta ocorrendo
    private Batalha batalhaAtual;
    //determina se a sequencia de batalhas ja comecou
    private boolean asBatalhasJaComecaram;
    //pontuacao do jogador no jogo, representa quantas partidas o time de herois venceu
    private int pontuacao;
    //número de tipos de inimigos disponíveis no jogo
    private final int numInimigos = 6;
    //objeto que esta ouvindo o jogo
    private ObservadorJogo tela;
    //arquivo que salva e carrega o jogo
    private Arquivo arquivo;
    //nível de habilidade do time dos herois
    private int habilidade;
    //nível de dificuldade dos vilões
    private int dificuldade;
    
    
    /**
     * Cria um novo jogo
     * 
     * @param tela objeto da classe que implementar ObservadorJogo
     */
    public Jogo(ObservadorJogo tela){
        asBatalhasJaComecaram = false;
        pontuacao = 0;
        habilidade = 0;
        timeDosHerois = new ArrayList<Personagem>();
        if(tela == null)
            throw new InfInvalidoException("Tela","");
        this.tela = tela;
    }

    
    
    
    //JOGO EM SI
    /**
     * Metodo principal contendo a pricinpal logica do jogo
     * @param novoJogo Serve para saber se e um novo jogo ou nao
     */
    public void comecar(boolean novoJogo){

        //Primeiro verifica se o jogador quer começar um jogo novo
        if (novoJogo) {
            
            boolean valido = false;
            
            while (!valido) {
                
                //pegar o nome do savefile
                arquivo = new Arquivo(tela.lerNomeArquivo("salvo"));

                //testa se o arquivo pode ser salvo
                String mensagem = testarArquivo();
                if (mensagem == null) {
                    valido = true;
                } else {
                    tela.arquivoInvalido(mensagem,true);
                }
            } 

            //Verifica se o jogador quer criar um time novo de heróis ou carregar
            boolean loop = true;
            ArrayList<String> newHerois = null;
            while (loop) {
                if (tela.decideCarregarHerois()) {
                    //carrego o grupo de um arquivo
                    Arquivo arqHerois = new Arquivo(tela.lerNomeArquivo("carregado"));

                    try {
                        newHerois = arqHerois.carregarHerois();
                        loop = false;
                    } catch (Exception e) {
                        tela.arquivoInvalido(e.getMessage(),true);
                    }
                } else {
                    //crio o grupo de herois
                    tela.antesDeCriarTimeDeHerois();
                    loop = false;
                    salvarHeroisNoArquivo();
                }
            }

            if (newHerois != null){
                if (!newHerois.isEmpty()) {
                    adicionarHeroisDoArquivo(newHerois);
                }
            }
            
            //se o time dos herois estiver vazio no final das operações
            if (timeDosHerois.isEmpty()) {
                //crio o grupo de herois
                tela.antesDeCriarTimeDeHerois();
                salvarHeroisNoArquivo();
            }
            
        }
        
        //inicio as rodadas de batalhas
        tela.antesDeIniciarRodadaDeBatalhas();
        asBatalhasJaComecaram = true;
        
        while(!jogoJaTerminou()){
            //comeca a batalha atual
            comecarBatalha();
            
            //Apresento os inimigos
            tela.antesDeApresentarInimigos();
            
            //Inicio o turno atual
            tela.antesDoInicioDoTurno();

            // situaçao de todos os personagens
            tela.antesDeExibirInformacoesResumidasDeTodosOsLutadores();
             
            //inicio do loop da batalha atual
            while(getBatalhaEstaOcorrendo()){//enquanto a batalha estiver ocorrendo, ou seja, nao acabou
                
                tela.antesDeExibirTurnoAtual();
                            
                //começo o loop do turno
                boolean flagDoTurno = true;
                
                //loop do Turno
                for(int i = 0; i < getNumLutadores() && flagDoTurno; i++){
                    String relatorio = new String();
                    
                    tela.antesDeExibirVezDoLutador(lutadorGetNome(i));
                    
                    //verifico se o lutador esta em condicao de lutar
                    if(!lutadorEstaEmCondicao(i)){
                        relatorio = lutadorGetNome(i) + " não esta em condição de executar qualquer ação.";
                        tela.antesDeExibirRelatorio(relatorio);
                        continue;
                    }
                    
                    //neste ponto verifico se o lutador é um Inimigo e ativo a IA se necessario
                    if(!lutadorGetEhHeroi(i)){
                        relatorio = executarIA(i);
                        tela.antesDeExibirRelatorio(relatorio);
                        
                        if(!getBatalhaEstaOcorrendo()){
                            //se entrou neste if quer dizer que a batlaha acabou no turno de um inimigo, portanto os herois perderam
                            flagDoTurno = false;
                            tela.antesDoEncerramentoDaBatalha();
                            encerrarJogo();
                            tela.antesDaDerrota();
                        }
                        continue;
                    }
                    
                    //personagem escolhe a acao que deseja executar
                    escolherAcao(i);
                    
                    //escrever o que aconteceu, como se desenrolou a acao
                    if(!getBatalhaEstaOcorrendo()){
                        flagDoTurno = false;
                        tela.antesDoEncerramentoDaBatalha();
                        ganharPonto();
                        tela.antesDaVitoria();
                        int s = tela.antesDeSair(false);
                        if(s == 0)
                            encerrarJogo();
                        else if (s == 2){
                            salvarJogo();
                            s = tela.antesDeSair(true);
                            if(s == 0)
                                encerrarJogo();
                        }
                    }else{
                        tela.antesDeExibirInformacoesResumidasDeTodosOsLutadores();
                    }
                    
                }//fecha loop do Turno
                
                if (getBatalhaEstaOcorrendo())
                    tela.antesDeExibirRelatorio(passarTurno());
            }//fecha loop da batalha
        }//fecha loop do jogo
    }
    
    /**
     * metodo para escolher o heroi escolher sua acao
     * @param i pos do lutador
     */
    public void escolherAcao(int i){
        //loop do Personagem
        boolean flagPersonagem = true;//flag para a acao do personagem
        String relatorio = new String();
        while(flagPersonagem){
            //Personagem escolhe sua acao
            int acao = tela.antesDeEscolherAcao(i);
            
            switch(acao){
                // Ataque
                case 1:
                    try{
                        relatorio = atacar(i);
                    }
                    catch(AcaoInvalidaException e){
                        relatorio = e.getMessage() + " " + e.getMotivo();
                    }
                    break;
                // Defender
                case 2:
                    if(lutadorGetEstaDefendendo(i))
                        relatorio = " Acão inválida!";
                    else
                        relatorio = defender(i);
                    break;
                //Esquivar
                case 3:
                    if(lutadorGetPontoForte(i).equals("Destreza") && !lutadorGetEstaEsquivando(i))
                        relatorio = esquivar(i);
                    else
                        relatorio = " Acão inválida!";
                    break;
                case 5:
                    relatorio = lutadorGetNome(i) + " encerrou sua vez.";
                    flagPersonagem = false;//sai do loop de personagens
                    break;
                case 6:
                    tela.antesDeExibirInformacoesResumidasDeTodosOsLutadores();
                    break;
                default:
                    relatorio = " Acão inválida!"; 
            }//fecha switch(acao)
            tela.antesDeExibirRelatorio(relatorio);
            
            if(lutadorGetPontosDeAcao(i) == 0 || !getBatalhaEstaOcorrendo())
                flagPersonagem = false;//sai do loop de personagens
        }///fecha loop do Personagem
    }
    
    /**
     * esquiva
     * @param i pos do lutador
     * @return relatorio de esquivar
     */
    public String esquivar(int i){
        getLutadorNaPosicao(i).esquivar();
        return (lutadorGetNome(i) + " podera tentar esquivar do proximo ataque que receber.");
    }
    
    /**
     * defendender
     * @param i pos dolutador
     * @return 
     */
    public String defender(int i){
        getLutadorNaPosicao(i).defender();
        return (lutadorGetNome(i) + " podera se defender do proximo ataque que receber.");
    }
    
    public String atacar(int i){
        //ler informações necessarias para executar a acao atacar
        
        if(lutadorGetPontosDeAcao(i) == 0){
            return "Voce nao possui pontos de acao suficiente para esse problema";
        }
        
        //escolhendo alvo
        String alvoNome = tela.escolhendoAlvo(i);

        //escolhendo o golpe
        String golpeNome = tela.escolhendoGolpe(i);
        
        //agora que ja foi recebido as informações necessarias, executa-se a ação atacar
        return lutadorAtacaAlvo(i,alvoNome,golpeNome);
    }
    
    

    
    
    //PONTUACAO
    /**
     * Aumenta a pontuacao em um ponto, deve ser usado apos uma vitoria de batalha
     */
    public void ganharPonto(){
        pontuacao++;
    }
    
    /**
     * Retorna o valor da pontuacao atual
     * 
     * @return int com a pontuacao atual
     */
    public int getPontuacao(){
        return pontuacao;
    }
    
    
    
    
    
    
    //COISAS DE HEROI
    
    /**
     * Cria e adiciona um heroi ao time dos herois do jogo
     * 
     * @param nome String com o nome do Heroi
     * @param classe String com qual sera a classe do Heroi
     * @param pontoForte Int com qual sera o ponto forte do Heroi
     * @param arma Int dizendo qual o tipo de arma o Heroi usara
     * @param armadura Int com qual o tipo de armadura o Heroi usara
     */
    public void adicionarHeroi(String nome, String classe, int pontoForte, int arma, int armadura){
        if(asBatalhasJaComecaram)
            throw new AcaoInvalidaException("adicionar heroi",2);
        Personagem p = new Heroi(nome,classe,pontoForte,arma,armadura);
        
        //adiciona a habilidade do heroi no índice de habilidade total
        habilidade += p.getPontoSaude() + arma + armadura;
        
        timeDosHerois.add(p);
    }
    /**
     * Adiciona no time os heróis lidos do arquivo
     * @param newHerois Lista de Strings com os heróis lidos do arquivo
     */
    private void adicionarHeroisDoArquivo(ArrayList<String> newHerois) {
        //se newHerois existir e possuir algum herói, adiciona no jogo
        for (int i = 0;i < newHerois.size();i++) {
            //divide as informações do herói atual em um vetor de String
            String[] heroiInf = newHerois.get(i).split(";");
            //verifica se todas as posições obrigatórias do vetor são válidas
            //0 a 6 que são informações obrigatórias para um personagem existir
            boolean invalido = false;
            for (int j = 0;j <= 6;j++) {
                try {
                    //tenta acessar vetor na posição
                    if (heroiInf[j] == null);

                    //tenta verificar se as posições 3 e 4 são numeros
                    if (j == 3 || j == 4)
                        Integer.parseInt(heroiInf[j]);


                } catch (Exception e) {
                    //se der erro é porque a posição é inválida
                    tela.personagemArquivoInvalido(i);
                    invalido = true;
                    break;
                }
            }

            if (invalido)
                continue;



            //verifica se o primeiro golpe adicionado possui tipo valido
            if (!checarValidadeGolpe(heroiInf[5])) {
                tela.personagemArquivoInvalido(i);
                continue;
            }


            String nome = heroiInf[0];
            String classe = heroiInf[1];
            int pontoForte = 0;
            
            switch (heroiInf[2]) {
                case "Força":
                    pontoForte = 0;
                    break;
                case "Destreza":
                    pontoForte = 1;
                    break;
                case "Constituição":
                    pontoForte = 2;
                    break;
                case "Carisma":
                    pontoForte = 3;
                    break;
                default:
                    break;
            }
            
            int arma = Integer.parseInt(heroiInf[3]);
            int armadura = Integer.parseInt(heroiInf[4]);

            adicionarHeroi(nome,classe,pontoForte,arma,armadura);
            int j = 5;

            try {
                while (checarValidadeGolpe(heroiInf[j])) {
                    //faz a adição de golpes até dar erro
                    if (heroiInf[j].equals("fisico"))
                        addGolpeFisicoNoHeroi(nome,heroiInf[++j]);
                    else if (heroiInf[j].equals("magico"))
                        addGolpeMagicoBasicoNoHeroi(nome,heroiInf[++j]);
                    else if (heroiInf[j].equals("meteoro"))
                        addGolpeMagicoMeteoroNoHeroi(nome,heroiInf[++j]);
                    else if (heroiInf[j].equals("gelo"))
                        addGolpeMagicoLancaDeGeloNoHeroi(nome,heroiInf[++j]);
                    else if (heroiInf[j].equals("nevasca"))
                        addGolpeMagicoNevascaNoHeroi(nome,heroiInf[++j]);
                    else if (heroiInf[j].equals("fogo"))
                        addGolpeMagicoBolaDeFogoNoHeroi(nome,heroiInf[++j]);
                    ++j;
                }
            } catch (Exception e) { }

        }
    }
    
    /**
     * Checa pra ver se o tipo de golpe lido do arquivo é válido
     * @param tipo String com o tipo
     * @return boolean, true se o tipo é válido, false se não é válido
     */
    private boolean checarValidadeGolpe (String tipo) {
        return (tipo.equals("fisico") ||
                    tipo.equals("magico") ||
                    tipo.equals("fogo") ||
                    tipo.equals("gelo") ||
                    tipo.equals("meteoro") ||
                    tipo.equals("nevasca"));
    }
    
    /**
     * Salva no arquivo os heróis inseridos pelo usuário em tempo de execução.
     */
    private void salvarHeroisNoArquivo() {
        if (!timeDosHerois.isEmpty() && tela.decideSalvarHerois()) {
            try {
                String lista = "";
                Arquivo arq = new Arquivo(tela.lerNomeArquivo("salvo"));
                for (int i = 0;i < timeDosHerois.size();i++) {
                    Personagem h = timeDosHerois.get(i);
                    lista += h.getNome()+";"
                            +h.getClasse()+";"
                            +h.getPontoForte()+";"
                            +h.getArma()+";"
                            +h.getArmadura();

                    for (int j = 0;j < timeDosHerois.get(i).getNumGolpes();j++) {
                        lista += ";"+h.getGolpeTipo(j)+";"
                                +h.getGolpe(j);
                    }

                    lista += "\n";

                    arq.salvarHerois(lista);
                }
            } catch (Exception e) {
                tela.arquivoInvalido(e.getMessage(), true);
            }
        }
    }
    
    /**
     * Adiciona um golpe fisico a lista de possiveis golpes de um heroi
     * 
     * @param nomeDoHeroi String com o nome do heroi que o golpe devera ser adc
     * @param nomeDoGolpe String com o nome do golpe
     */
    public void addGolpeFisicoNoHeroi(String nomeDoHeroi, String nomeDoGolpe){
        int pos = getPosicaoDoHeroiComNome(nomeDoHeroi);
        if(pos < 0 || pos > timeDosHerois.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + pos + " não existe em time dos herois.");
        timeDosHerois.get(pos).addGolpeFisico(nomeDoGolpe);
    }
    
    /**
     * Adiciona um golpe magico a lista de possiveis golpes de um heroi
     * 
     * @param nomeDoHeroi String com o nome do heroi que o golpe devera ser adc
     * @param nomeDoGolpe String com o nome do golpe
     */
    public void addGolpeMagicoBasicoNoHeroi(String nomeDoHeroi, String nomeDoGolpe){
        int pos = getPosicaoDoHeroiComNome(nomeDoHeroi);
        if(pos < 0 || pos > timeDosHerois.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + pos + " não existe em time dos herois.");
        timeDosHerois.get(pos).addGolpeMagicoBasico(nomeDoGolpe);
    }
    
    /**
     * Adiciona um golpe meteoro a lista de possiveis golpes de um heroi
     * 
     * @param nomeDoHeroi String com o nome do heroi que o golpe devera ser adc
     * @param nomeDoGolpe String com o nome do golpe
     */
    public void addGolpeMagicoMeteoroNoHeroi(String nomeDoHeroi, String nomeDoGolpe){
        int pos = getPosicaoDoHeroiComNome(nomeDoHeroi);
        if(pos < 0 || pos > timeDosHerois.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + pos + " não existe em time dos herois.");
        timeDosHerois.get(pos).addGolpeMagicoMeteoro(nomeDoGolpe);
    }
    
    /**
     * Adiciona um golpe lanca de gelo a lista de possiveis golpes de um heroi
     * 
     * @param nomeDoHeroi String com o nome do heroi que o golpe devera ser adc
     * @param nomeDoGolpe String com o nome do golpe
     */
    public void addGolpeMagicoLancaDeGeloNoHeroi(String nomeDoHeroi, String nomeDoGolpe){
        int pos = getPosicaoDoHeroiComNome(nomeDoHeroi);
        if(pos < 0 || pos > timeDosHerois.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + pos + " não existe em time dos herois.");
        timeDosHerois.get(pos).addGolpeMagicoLancaDeGelo(nomeDoGolpe);
    }
    
    /**
     * Adiciona um golpe nevasca a lista de possiveis golpes de um heroi
     * 
     * @param nomeDoHeroi String com o nome do heroi que o golpe devera ser adc
     * @param nomeDoGolpe String com o nome do golpe
     */
    public void addGolpeMagicoNevascaNoHeroi(String nomeDoHeroi, String nomeDoGolpe){
        int pos = getPosicaoDoHeroiComNome(nomeDoHeroi);
        if(pos < 0 || pos > timeDosHerois.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + pos + " não existe em time dos herois.");
        timeDosHerois.get(pos).addGolpeMagicoNevasca(nomeDoGolpe);
    }
    
    /**
     * Adiciona um golpe bola de fogo a lista de possiveis golpes de um heroi
     * 
     * @param nomeDoHeroi String com o nome do heroi que o golpe devera ser adc
     * @param nomeDoGolpe String com o nome do golpe
     */
    public void addGolpeMagicoBolaDeFogoNoHeroi(String nomeDoHeroi, String nomeDoGolpe){
        int pos = getPosicaoDoHeroiComNome(nomeDoHeroi);
        if(pos < 0 || pos > timeDosHerois.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + pos + " não existe em time dos herois.");
        timeDosHerois.get(pos).addGolpeMagicoBolaDeFogo(nomeDoGolpe);
    }
    
    /**
     * Procura um heroi pelo nome na lista do TimeDeHerois
     * 
     * @param nome String com o nome do heroi a ser buscado
     * @return int com a posicao na lista do heroi, ou -1 caso nao encontre
     */
    private int getPosicaoDoHeroiComNome(String nome){
        int pos = 0;
        for(Personagem h : timeDosHerois){
            if(h.getNome().equals(nome))
                return pos;
            pos++;
        }
        return -1;
    }
    
    
    
    
    
    
    //QUESTAO DE SABER SE ESTA OCORRENDO ALGO OU SE TERMINOU ALGO
    /**
     * Retorna se o jogo ja acabou ou não
     * 
     * @return boolean onde (true)-jogo acabou ou (false)-jogo ainda nao acabou
     */
    public boolean jogoJaTerminou(){
        return !asBatalhasJaComecaram;
    }
    
    /**
     * Retorna se a batalha atual esta ocorrendo, ou seja ainda nao acabou ou nem comecou
     * 
     * @return boolean onde (true)-a batalha esta ocorrendo ou (false)-a batalha ja acabou
     */
    public boolean getBatalhaEstaOcorrendo(){
        return !batalhaAtual.getAcabou();
    }
    
    /**
     * Retorna o vencedor da batalha atual
     * 
     * @return String dizendo quem venceu
     */
    public String getVencedores(){
        if(getBatalhaEstaOcorrendo())
            return "A Batalha ainda não acabou.";
        return batalhaAtual.getVencedores();
    }
     
    
    /**
     * Inicia uma batalha, criando ela e escolhendo aleatoriamente os inimigos que participarao
     * da mesma.
     */
    public void comecarBatalha(){
        //escolhendo inimigos
        List<Personagem> mobs = new ArrayList<Personagem>();
        dificuldade = 0;
        
        Random aleatorio = new Random();
        //Adiciona um número de inimigos na lista
        while (dificuldade < habilidade) {
            int pos = aleatorio.nextInt(numInimigos);
            if(pos < 0 || pos > numInimigos -1)
                throw new IndexOutOfBoundsException("Posição " + pos + " não existe em inimigos.");
            mobs.add(adicionarInimigo(pos));
        }
        
        if (mobs.isEmpty())
            throw new RuntimeException("Nenhum inimigo adicionado na batalha.");
        
        batalhaAtual = new Batalha(timeDosHerois, mobs);
        batalhaAtual.comecarBatalha();
    }
    
    /**
     * Define um inimigo a ser adicionado a partir de um número de índice
     * @param id int com o índice do inimigo na lista
     * @return Inimigo a ser adicionado
     */
    private Inimigo adicionarInimigo(int id) {
        Inimigo i = null;
        Random aleatorio = new Random();
        int arma = 0,armadura = 0;
        int pontoForte = aleatorio.nextInt(4);
        switch (id) {
            case 0:
                arma = aleatorio.nextInt(2);
                armadura = 0;
                i = new Goblin(pontoForte,arma,armadura);
                i.addGolpeFisico("faca");
                break;
            case 1:
                arma = aleatorio.nextInt(2);
                armadura = 0;
                i = new Cultista(pontoForte,arma,armadura);
                i.addGolpeFisico("faca");
                i.addGolpeMagicoMeteoro("invocação");
                break;
            case 2:
                arma = aleatorio.nextInt(3);
                armadura = aleatorio.nextInt(2);
                i = new Dragao(pontoForte,arma,armadura);
                i.addGolpeFisico("garras");
                i.addGolpeMagicoBolaDeFogo("bola de fogo");
                break;
            case 3:
                arma = aleatorio.nextInt(3);
                armadura = aleatorio.nextInt(2);
                i = new Ogro(pontoForte,arma,armadura);
                i.addGolpeFisico("machado");
                break;
            case 4:
                arma = aleatorio.nextInt(2);
                armadura = 0;
                i = new Esqueleto(pontoForte,arma,armadura);
                i.addGolpeFisico("espada");
                break;
            case 5:
                arma = aleatorio.nextInt(2);
                armadura = 0;
                i = new Zumbi(pontoForte,arma,armadura);
                i.addGolpeFisico("arranhão");
                break;
            default:
                break;
        }
        
        if (i != null){
            
            
            dificuldade += i.getPontoSaude() + arma + armadura;
            
            return i;
        } else {
            throw new RuntimeException("Inimigo não adicionado, ID inválida");
        }
        
    }
    
    /**
     * Encerra o jogo 
     */
    public void encerrarJogo(){
        asBatalhasJaComecaram = false;
    }  
    
    
    
    //QUESTAO DOS ALVOS
    
    /**
     * Executa a IA dos inimigos
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return String dizendo oque aconteceu na acao
     */
    public String executarIA(int pos){
        Personagem lutador = getLutadorNaPosicao(pos);
        String classeLutador = lutador.getClasse();
        String situacao = lutador.getSituacaoDeVida();
        int alvoPos = 0;
        
        if (situacao.equals("Saudável")) {
            //Define a estratégia de luta de acordo com o lutador
            switch (classeLutador) {
                case "Dragao":
                    alvoPos = definirAlvoMaisForte(pos);
                    break;
                case "Ogro":
                case "Goblin":
                    if (lutador.rodarDado() > 5) {
                        return defender(pos);
                    } else {
                        alvoPos = definirAlvoMaisFraco(pos);
                    }
                    break;
                default:
                    if (lutador.rodarDado() > 4) {
                        return defender(pos);
                    } else {
                        alvoPos = definirAlvoAleatorio(pos);
                    }
                    break;
            }
        } else if (situacao.equals("Atordoado")) {
            switch (classeLutador) {
                case "Dragao":
                    int chance = lutador.rodarDado();
                    if (chance == 6)
                        return defender(pos);
                    else if (chance > 2)
                        alvoPos = definirAlvoMaisForte(pos);
                    else
                        alvoPos = definirAlvoAleatorio(pos);
                    break;
                case "Ogro":
                case "Goblin":
                    if (lutador.rodarDado() > 3)
                        alvoPos = definirAlvoMaisForte(pos);
                    else
                        alvoPos = definirAlvoAleatorio(pos);
                    break;
                default:
                    if (lutador.rodarDado() > 4) {
                        return defender(pos);
                    } else {
                        alvoPos = definirAlvoAleatorio(pos);
                    }
                    break;
            }
        } else {
            switch (classeLutador) {
                case "Dragao":
                    if (lutador.rodarDado() > 4)
                        alvoPos = definirAlvoMaisForte(pos);
                    else
                        alvoPos = definirAlvoAleatorio(pos);
                    break;
                case "Ogro":
                    if (lutador.rodarDado() == 6)
                        alvoPos = definirAlvoMaisForte(pos);
                    else
                        alvoPos = definirAlvoAleatorio(pos);
                    break;
                case "Goblin":
                    int chance = lutador.rodarDado();
                    if (chance == 6)
                        alvoPos = definirAlvoMaisForte(pos);
                    else if (chance > 3)
                        return defender(pos);
                    else
                        alvoPos = definirAlvoAleatorio(pos);
                    break;
                default:
                    if (lutador.rodarDado() > 2) {
                        return defender(pos);
                    } else {
                        alvoPos = definirAlvoAleatorio(pos);
                    }
                    break;
            }
        }
        
        Random aleatorio = new Random();
        int golpe = aleatorio.nextInt(lutador.getNumGolpes());
        String alvoNome = possivelAlvoLutadorGetNome(pos,alvoPos);
        return lutadorAtacaAlvo(pos,alvoNome,lutador.getGolpe(golpe));
    }
    
    /**
     * Define um alvo aleatório a ser atacado.
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com a posicao do alvo a ser atacado
     */
    private int definirAlvoAleatorio(int pos) {
        //meu valores para j variam de 0 ate getNumPossiveisAlvos(i)
        Random random = new Random();
        return random.nextInt(getNumPossiveisAlvos(pos));
    }
    /**
     * Define o alvo a ser atacado como o alvo mais fraco.
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com a posicao do alvo a ser atacado
     */
    private int definirAlvoMaisFraco(int pos) {
        Personagem atacante = getLutadorNaPosicao(pos);
        
        //Faz uma lista de todos os alvos possíveis do atacante
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(atacante);
        
        //Percorre a lista verificando qual é o alvo com maior dano
        int maiorDano = alvos.get(0).getDanoRecebido();
        int posAlvo = 0;
        for (int i = 1;i < alvos.size();i++) {
            String situacaoAlvo = alvos.get(i).getSituacaoDeVida();
            
            //Verifica se o dano do alvo é menor e se o alvo não está incapaz
            if (maiorDano < alvos.get(i).getDanoRecebido() &&
                    !situacaoAlvo.equals("Inconsciente") &&
                    !situacaoAlvo.equals("Morto")) {
                maiorDano = alvos.get(i).getDanoRecebido();
                posAlvo = i;
            }
        }
        
        return posAlvo;
    }    
    /**
     * Define o alvo a ser atacado como o alvo mais fraco.
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com a posicao do alvo a ser atacado
     */
    private int definirAlvoMaisForte(int pos) {
        Personagem atacante = getLutadorNaPosicao(pos);
        
        //Faz uma lista de todos os alvos possíveis do atacante
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(atacante);
        
        //Percorre a lista verificando qual é o alvo com menor dano
        int menorDano = alvos.get(0).getDanoRecebido();
        int posAlvo = 0;
        for (int i = 1;i < alvos.size();i++) {
            if (menorDano > alvos.get(i).getDanoRecebido()) {
                menorDano = alvos.get(i).getDanoRecebido();
                posAlvo = i;
            }
        }
        
        return posAlvo;
    }
    
    
    
    /**
     * Retorna o nome do possivel alvo de um atacante
     * 
     * @param posDoAtacante int com a posicao do lutador atacante na lista de todos os lutadores
     * @param posDoAlvo int com a posaicao do alvo na lista de alvos
     * @return String com o nome do possivel alvo
     */
    public String possivelAlvoLutadorGetNome(int posDoAtacante, int posDoAlvo){
        Personagem atacante = getLutadorNaPosicao(posDoAtacante);
        
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(atacante);
        if(posDoAlvo < 0 || posDoAlvo > alvos.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + posDoAlvo + " não existe em alvos.");
        return alvos.get(posDoAlvo).getNome();
    }
    
    /**
     * Aciona a acao atacar do lutador na posicao posAtacante e seleciona como alvo
     * deste ataque o lutador com o nome de alvoNome
     * 
     * @param posAtacante int com a posicao do lutador atacante na lista de todos os lutadores
     * @param nomeAlvo String com o nome do alvo
     * @param golpe String com o nome do golpe sendo executado
     * @return String dizendo oque aconteceu no ataque
     */
    public String lutadorAtacaAlvo(int posAtacante, String nomeAlvo, String golpe){
        Personagem atacante = getLutadorNaPosicao(posAtacante);
        
        List<Personagem> alvos = batalhaAtual.getPossiveisAlvos(atacante);
        
        Personagem alvo = null;
        for(int i = 0; i < alvos.size(); i++){
            if(nomeAlvo.equals(alvos.get(i).getNome()))
                alvo = alvos.get(i);
        }
        
        return atacante.atacar(alvo,golpe);
    }
    
    
    
    
    
    
    
    //GETS DE QUANTIDADE
    
    /**
     * Retorna a quantidade de alvos do lutador na posição i
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com a quantidade de possiveis alvo do lutador
     */
    public int getNumPossiveisAlvos(int pos){
        return batalhaAtual.getNumPossiveisAlvos(getLutadorNaPosicao(pos));
    }
    
    /**
     * Retorna o numero total de lutadores na batalha
     * 
     * @return int com o numero total de lutadores na batalha
     */
    public int getNumLutadores(){
        return batalhaAtual.getNumLutadores();
    }
    
    /**
     * Retorna o numero de inimigos em uma batalha
     * 
     * @return int com o numero de inimigos na batalha
     */
    public int getNumInimigosNaBatalha(){
        return batalhaAtual.getNumInimigos();
    }
    
    
    
    
    //GETS DAS INFORMACOES DOS LUTADORES
    
    /**
     * Metodo auxiliar que dada uma posicao ele retorna o lutador daquela posicao 
     * na lista de todos os lutadores
     * 
     * @param pos int com a posicao do lutador
     * @return Personagem que é o lutador
     */
    private Personagem getLutadorNaPosicao(int pos){
        List<Personagem> list = batalhaAtual.getLutadores();
        if(pos < 0 || pos > list.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + pos + " não existe nos lutadores.");
        return list.get(pos);
    }
    
    /**
     * Retorna o nome do lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return String com o nome do lutador
     */
    public String lutadorGetNome(int pos){
        return getLutadorNaPosicao(pos).getNome();
    }
    
    /**
     * Retorna o dano recebido do lutador
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com o dano recebido
     */
    public int lutadorGetDanoRecebido(int pos){
       return getLutadorNaPosicao(pos).getDanoRecebido();
    }
    
    /**
     * Retorna o dano recebido maximo do lutador
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com o dano recebido maximo
     */
    public int lutadorGetDanoRecebidoMaximo(int pos){
        return getLutadorNaPosicao(pos).getDanoRecebidoMaximo();
    }
    
    /**
     * Retorna a situacao de vida do lutador
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return String com a situacao de vida 
     */
    public String lutadorGetSituacaoDeVida(int pos){
        return getLutadorNaPosicao(pos).getSituacaoDeVida();
    }
    
    /**
     * Retorna se o jogador esta ou nao em condicao de executar ações
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return boolean onde (true)-esta em condicao ou (false)-nao esta e condicao
     */
    public boolean lutadorEstaEmCondicao(int pos){
        Personagem p = getLutadorNaPosicao(pos);
        if(p.getSituacaoDeVida().equals("Inconsciente") || p.getSituacaoDeVida().equals("Morto") || p.getEstaCongelado()){
            return false;
        }
        return true;
    }
    
    /**
     * Retorna se um lutador na posicao pos é ou nao heroi
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return boolean onde (true)-se é heroi ou (false)-caso seja inimigo
     */
    public boolean lutadorGetEhHeroi(int pos){
        return getLutadorNaPosicao(pos).getEhHeroi();
    }
    
    /**
     * Retorna a quantidade de pontos de acao de um lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com a quantidade de pontos de acao
     */
    public int lutadorGetPontosDeAcao(int pos){
        return getLutadorNaPosicao(pos).getPontosDeAcao();
    }
    
    /**
     * Retorna quantos golpes tem um lutador na posicao pos
     * 
     * @param pos int com a posicao
     * @return int com num de golpes
     */
    public int lutadorGetNumGolpes(int pos){
        return getLutadorNaPosicao(pos).getNumGolpes();
    }
    
    /**
     * Retorna um golpe de um personagem
     * 
     * @param pos int com a posicao do personagem na lista
     * @param posG int com a posicao do golpes procurado
     * @return String com o nome do golpe
     */
    public String lutadorGetGolpe(int pos, int posG){
        return getLutadorNaPosicao(pos).getGolpe(posG);
    }
    
    /**
     * Retorna o custo do golpe de um personagem
     * 
     * @param pos int com a posicao do personagem na lista
     * @param posG int com a posicao do golpes procurado
     * @return int com o custo do golpe
     */
    public int lutadorGetGolpeCusto(int pos, int posG){
        return getLutadorNaPosicao(pos).getGolpeCusto(posG);
    }
    
    /**
     * Retorna se o lutador esta esquivando
     * 
     * @param pos int com a posicao do personagem na lista
     * @return Retorna se o lutador esta esquivando
     */
    public boolean lutadorGetEstaEsquivando(int pos){
        return getLutadorNaPosicao(pos).getEstaEsquivando();
    }
    
    /**
     * Retorna se o lutador esta defendendo
     * 
     * @param pos int com a posicao do personagem na lista
     * @return Retorna se o lutador esta defendendo
     */
    public boolean lutadorGetEstaDefendendo(int pos){
        return getLutadorNaPosicao(pos).getEstaDefendendo();
    }
    
    /**
     * Retorna se o lutador esta congelado
     * 
     * @param pos int com a posicao do personagem na lista
     * @return Retorna se o lutador esta congelado
     */
    public boolean lutadorGetEstaCongelado(int pos){
        return getLutadorNaPosicao(pos).getEstaCongelado();
    }
    
    /**
     * Retorna o ponto forte do lutador
     * 
     * @param pos int com a posicao do personagem na lista
     * @return Retorna o ponto forte do lutador
     */
    public String lutadorGetPontoForte(int pos){
        return getLutadorNaPosicao(pos).getPontoForte();
    }
    
    
    
    
    //GETS DAS INFORMACOES DOS INIMIGOS
    
    /**
     * Metodo auxiliar que dada uma posicao ele retorna o inimigo daquela posicao na lista de inimigos
     * 
     * @param pos int com a posicao do inimigo
     * @return Personagem que é o Inimigo
     */
    private Personagem getInimigoNaPosicao(int pos){
        List<Personagem> list = batalhaAtual.getInimigos();
        if(pos < 0 || pos > list.size() - 1)
            throw new IndexOutOfBoundsException("Posição " + pos + " não existe nos inimigos.");
        return list.get(pos);
    }
    
    /**
     * Retorna o nome do lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return String com o nome do lutador
     */
    public String inimigoGetNome(int pos){
        return getInimigoNaPosicao(pos).getNome();
    }
    
    /**
     * Retorna a classe do lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return String com a classe do lutador
     */
    public String inimigoGetClasse(int pos){
        return getInimigoNaPosicao(pos).getClasse();
    }
    
    /**
     * Retorna o ponto forte do lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return String com o ponto forte do lutador
     */
    public String inimigoGetPontoForte(int pos){
        return getInimigoNaPosicao(pos).getPontoForte();
    }
    
    /**
     * Retorna a saude do lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com a saude do lutador
     */
    public int inimigoGetSaude(int pos){
        return getInimigoNaPosicao(pos).getPontoSaude();
    }
    
    /**
     * Retorna o dano recebido pelo lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com o dano recebido pelo lutador
     */
    public int inimigoGetDanoRecebido(int pos){
        return getInimigoNaPosicao(pos).getDanoRecebido();
    }
    
    /**
     * Retorna o dano recebido maximo do lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com o dano recebido maximo do lutador
     */
    public int inimigoGetDanoRecebidoMaximo(int pos){
        return getInimigoNaPosicao(pos).getDanoRecebidoMaximo();
    }
    
    /**
     * Retorna a situacao de vida do lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return String com a situacao de vida do lutador
     */
    public String inimigoGetSituacaoDeVida(int pos){
        return getInimigoNaPosicao(pos).getSituacaoDeVida();
    }
    
    /**
     * Retorna a arma do lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com a arma do lutador
     */
    public int inimigoGetArma(int pos){
        return getInimigoNaPosicao(pos).getArma();
    }
    
    /**
     * Retorna a armadurado lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com a armadura do lutador
     */
    public int inimigoGetArmadura(int pos){
       return getInimigoNaPosicao(pos).getArmadura();
    }
    
    /**
     * Retorna a iniciativa do lutador
     * 
     * @param pos int com a posicao do lutador na lista de lutadores da batalha
     * @return int com a iniciativa do lutador
     */
    public int inimigoGetIniciativa(int pos){
        return getInimigoNaPosicao(pos).getIniciativa();
    }
   
    
    
    
    
    //TURNOS
    
    /**
     * Passa o turno da batalha
     * @return String dizendo oque ocorreu
     */
    public String passarTurno(){
        List<Personagem> list = batalhaAtual.getLutadores();
        //reinicio os pontos de acao para o proximo turno //ativo os xbuffs
        String relatorio = "Aconteceu na passagem de turnos:\n";
        for(Personagem l : list){
            l.setPontosDeAcao(2);
            relatorio += l.receberXBuffs();
        }
        batalhaAtual.passarTurno();
        return relatorio;
    }
    
    /**
     * Retorna o turno atual
     * 
     * @return int com o turno atual
     */
    public int getTurno(){
        return batalhaAtual.getTurno();
    }
    
    
    
    
    //QUESTAO DE SALVAMENTO
    /**
     * Carrega o jogo.
     * @return String com a mensagem de sucesso ou fracasso
     */
    public Jogo carregarJogo() {
        Jogo j = null;
        try {
            arquivo = new Arquivo(tela.lerNomeArquivo("carregado"));
            j = arquivo.carregar();
        } catch (Exception e) {
            tela.arquivoInvalido(e.getMessage(), false);
        }
        return j;
    }
    
    /**
     * Salva o jogo.
     * @return String com a mensagem de sucesso ou fracasso
     */
    public String salvarJogo() {
        try {
            arquivo.salvar(this);
        } catch (Exception e) {
            tela.arquivoInvalido(e.getMessage(), true);
        }
        return ("Jogo salvo com sucesso");
    }
    
    /**
     * Testa se o arquivo pode ser escrito
     * @return String com a mensagem de erro, null se não houver erros
     */
    public String testarArquivo() {
        try {
            arquivo.testarArquivo();
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }
    
}
