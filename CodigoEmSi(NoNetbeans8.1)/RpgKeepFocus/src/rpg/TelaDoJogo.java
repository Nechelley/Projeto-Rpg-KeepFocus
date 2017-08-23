package rpg;

import java.io.Serializable;
import java.util.Scanner;

/**
 *
 * @author Nechelley Alves
 */
public class TelaDoJogo implements ObservadorJogo, Serializable{
    private static Jogo rpg;
    public TelaDoJogo(){
        rpg = new Jogo(this);
    }
    
    public void exibirInicio(){
        System.out.println("\n- Rpg The Game -");
        rpg.iniciar();
    }
    
    /**
     * Le as informacoes informaçoes do heroi na ordem
     * {nome,classe,foco,arma,armadura}
     * 
     * @return Vetor de Strings com as informaçoes do heroi
     */
    @Override
    public String[] lendoHeroi(){
        Scanner scan = new Scanner (System.in);
        String nome = "";
        String classe = "";
        String foco = "";
        String arma = "";
        String armadura = "";
        do{
            System.out.println("Insira o nome do personagem:");
            nome = scan.nextLine();
        }while(!verificaNome(nome));
        do{
            System.out.println("Insira a classe do personagem:\n"
                    + "1-Guerreiro 2-Mago 3-Clerigo");
            classe = scan.next();
        }while(!verificaNumero(classe,3));
        do{
            System.out.println("Insira o foco do personagem:\n"
                    + "1-Força 2-Destreza 3-Constituição 4-Carisma");
            foco = scan.next();
        }while(!verificaNumero(foco,4));
        if(classe.equals("2")){//MAGO
            do{
                System.out.println("Insira qual é a arma do personagem:\n"
                        + "1-Pequena");
                arma = scan.next();
            }while(!verificaNumero(arma,1));
            do{
                System.out.println("Insira qual é o tipo de armadura do personagem:\n"
                        + "1-Nada");
                armadura = scan.next();
            }while(!verificaNumero(armadura,1));
        }else if(classe.equals("3")){//CLERIGO
            do{
                System.out.println("Insira qual é a arma do personagem:\n"
                        + "1-Pequena");
                arma = scan.next();
            }while(!verificaNumero(arma,1));
            do{
                System.out.println("Insira qual é o tipo de armadura do personagem:\n"
                        + "1-Nada 2-Leve");
                armadura = scan.next();
            }while(!verificaNumero(armadura,2));
        }else{//GUERReIRO
            do{
                System.out.println("Insira qual é a arma do personagem:\n"
                        + "1-Pequena 2-Média 3-Grande");
                arma = scan.next();
            }while(!verificaNumero(arma,3));
            do{
                System.out.println("Insira qual é o tipo de armadura do personagem:\n"
                        + "1-Nada 2-Leve 3-Pesada");
                armadura = scan.next();
            }while(!verificaNumero(armadura,3));
        }
        
        
        String[] heroi = {nome,
            String.valueOf(Integer.parseInt(classe) - 1),
            String.valueOf(Integer.parseInt(foco) - 1),
            String.valueOf(Integer.parseInt(arma) - 1),
            String.valueOf(Integer.parseInt(armadura) - 1)};
        
        return heroi;
    }
    
    /**
     * Pede a informacao ao usuario se deseja adicionar mais herois ao time de herois
     * 
     * @return retorna 1-sim 2-nao
     */
    @Override
    public int confirmarSeTemMaisHerois(){
        Scanner scan = new Scanner (System.in);
        int num = 0;
        do{
            System.out.println("Adicao do personagem no time dos herois encerrada, deseja adicionar mais personagens ao time de herois:\n"
                    + "1-Sim 2-Nao");
            num = scan.nextInt(); 
        }while(!verificaNumero(String.valueOf(num),2));
        return num;
    }    
    
    /**
     * Le as informacoes informaçoes do golpe do heroi na ordem
     * {nome,classe}
     * 
     * @return Vetor de Strings com as informaçoes do golpe do heroi
     */
    @Override
    public String[] lendoGolpe(String classe){//ACRESCENTAR O PARAMETRO DE GOLPES JA LIDOS, PARA Q N AJA REPETICOES DE NOME
        Scanner scan = new Scanner (System.in);
        String classeG = "";
        String golpeNome = "";
        if(classe.equals("Guerreiro")){
            do{
                System.out.println("Qual a classe de golpe:\n"
                        + "1-Fisico");
                classeG = scan.next();
            }while(!verificaNumero(classeG,1));
            scan.next();
            do{
                System.out.println("Qual o nome do golpe:");
                golpeNome = scan.nextLine();
            }while(!verificaNome(golpeNome));
        }else if(classe.equals("Clérigo")){
            do{
                System.out.println("Qual a classe de golpe:\n"
                        + "1-Fisico 2-Arcano nv1 3-Fogo nv1 ");
                classeG = scan.next();
            }while(!verificaNumero(classeG,3));
            scan.next();
            if(classeG.equals("3"))//GAMBIARRA
                classeG = "4";
            do{
                System.out.println("Qual o nome do golpe:");
                golpeNome = scan.nextLine();
            }while(!verificaNome(golpeNome));
        }else{
            do{
                System.out.println("Qual a classe de golpe:\n"
                        + "1-Fisico 2-Arcano nv1 3-Arcano nv2 4-Fogo nv1 5-Fogo nv2");
                classeG = scan.next();
            }while(!verificaNumero(classeG,5));
            scan.next();
            do{
                System.out.println("Qual o nome do golpe:");
                golpeNome = scan.nextLine();
            }while(!verificaNome(golpeNome));
        }
        
        
        String[] inf = {classeG, golpeNome};
        return inf;
    }
    
    /**
     * Pede a informacao ao usuario se deseja adicionar mais golpes ao heroi
     * 
     * @return retorna 1-sim 2-nao
     */
    @Override
    public int confirmarSeTemMaisGolpesDeHerois(){
        Scanner scan = new Scanner (System.in);
        int num = 0;
        do{
            System.out.println("Deseja adicionar mais golpes:\n"
                        + "1-Sim 2-Nao");
            num = scan.nextInt();
        }while(!verificaNumero(String.valueOf(num),2));
        return num;
    } 
    
    /**
     * Exibe menu para escolha de acao
     * 
     * @param inf Vetor de strings com que acoes o lutador pode fazer
     * @return Int com a escolha do jogador
     */
    @Override
    public int escolhendoAcao(String[] inf){
        int escolha = 0;
        String aux = "Escolha sua ação " + inf[0] + ", voce tem " + inf[1] + " pontos de acao restantes: 1-Atacar ";
        if(!inf[2].isEmpty())
            aux += "2-Defender ";
        if(!inf[3].isEmpty())
            aux += "3-Esquivar ";
        aux += "4-Passar vez 5-Pedir para exibir status dos personagens";
        do{
            System.out.println(aux);
            Scanner scan = new Scanner (System.in);  
            escolha = scan.nextInt();
        }while(!verificaAcao(aux, escolha));
        
        return escolha;
    }
    
    /**
     * Exibe o menu para escolher o alvo quando a acao atacar for selecionada
     * 
     * @param inf Vetor com os nomes dos alvos
     * @return Nome do alvo
     */
    @Override
    public String escolhendoAlvo(String[] inf){
        System.out.println();
        System.out.println("Escolha quem atacar, escolha o numero do seu alvo:");
        System.out.println("A seguir estão suas possíveis escolhas:");

        int cont = 0;
        for(String s : inf){
            cont++;
            System.out.println(cont + ")" + s);
        }
        
        int alvoNumero = 0;
        do{
            System.out.println ();
            System.out.print("Numero: ");
            Scanner scan = new Scanner (System.in);
            alvoNumero = scan.nextInt();
        }while(!verificaNumero(String.valueOf(alvoNumero), cont));
        
        return inf[alvoNumero-1];
    }
    
    /**
     * Metodo para se adquirir um nome de golpe valido
     * 
     * @param inf matriz com as informacoes necessarias
     * @return String com o nome do golpe
     */
    @Override
    public String escolhendoGolpe(String[][] inf){
        System.out.println();
        System.out.println("Informe o golpe(o numero do golpe) que sera executado:");
        System.out.println("A seguir estão suas possíveis escolhas:");
        
        int cont = 0;
        for(String[] s : inf){
            cont++;
            System.out.println(cont + ")" + s[0] + " (custa " + s[1] + " pontos de acao)");
        }
        
        int golpeNumero = 0;
        do{
            System.out.println ();
            System.out.print("Numero do golpe: ");
            Scanner scan = new Scanner (System.in);
            golpeNumero = scan.nextInt();
        }while(!verificaNumero(String.valueOf(golpeNumero), cont));
        return inf[golpeNumero-1][0];
    }
    
    
    
    //verificacoes
    private boolean verificaNome(String nome){
        if(Metodos.verificaNome(nome))
            return true;
        else{
            System.out.println("Nome digitado inválido, tente novamente.");
            System.out.println();
            return false;
        }
    }
    private boolean verificaNumero(String num, int limite){
        if(Integer.valueOf(num) > limite || Integer.valueOf(num) < 1){
            System.out.println("Numero digitado inválido, tente novamente.");
            System.out.println();
            return false;
        }
        return true;
    }
    private boolean verificaAcao(String aux, int escolha){
        if((!aux.contains("2-Defender") && escolha == 2) || 
                (!aux.contains("3-Esquivar") && escolha == 3) ||
                escolha < 1 || escolha > 5){
            System.out.println("Numero digitado invalido, tente novamente.");
            System.out.println();
            return false;
        }
        
        return true;
    }
    
    
    /**
     * Apresenta que as batalhas iniciaram
     */
    @Override
    public void iniciandoRodadaDeBatalhas(){
        System.out.println("Inicia-se o jogo:");
    }
    
    /**
     * Apresenta os inimigos da batalha atual
     * 
     * @param inf Matriz de string com as informacoes necessarias
     */
    @Override
    public void apresentandoInimigos(String[][] inf){
        System.out.println("\nInimigos:");
        for(String[] x : inf){
            System.out.println();
            System.out.println(
                    "Nome: " + x[0] +
                    "\nClasse: " + x[1] +
                    "\nFoco: " + x[2] +
                    "\nNivel de Saúde: " + x[3] +
                    "\nDano Recebido/Dano Recebido Máximo: " + x[4] +
                    "/" + x[5] +
                    "\nSituação de Vida: " + x[6] +
                    "\nArma: " + x[7] +
                    "\nArmadura: " + x[8] +
                    "\nIniciativa: " + x[9]
            );
        }
    }
    
    /**
     * Exibe que comecou os turnos
     */
    @Override
    public void iniciandoTurnos(){
        System.out.println("\n- Inicio dos turnos -\n");
    }
    
    /**
     * Exibe informacoes resumidas de todos os lutadores da batlaha atual
     * 
     * @param inf Matriz de strings com as informacoes necessarias
     */
    @Override
    public void exibindoSituacaoDosPersonagens(String[][] inf){
        System.out.println("\nStatus:");
        int cont = 1;
        for(String[] x : inf){
            System.out.println(); 
            System.out.println(cont + ")");
            System.out.println(
                    "Nome: " + x[2] + x[0] + x[1] +
                    "\nDano Recebido/Dano Recebido Máximo: " + x[3] +
                    "/" + x[4] +
                    "\nSituação de Vida: " + x[5] +
                    "\nPontos de Ação: " + x[6]
            );
            cont++;
        }
        System.out.println();
    }
    
    /**
     * Exibe qual turno esta
     * 
     * @param turno Int com o turno atual
     */
    @Override
    public void exibindoTurnoAtual(int turno){
        System.out.println("[turno " + turno + "]");
    }
    
    /**
     * Exibe de quem e a vez de executar acao
     * 
     * @param lutadorNome String com o nome do lutador
     */
    @Override
    public void exibindoVezDoLutador(String lutadorNome){
        System.out.println("////////////////////////// "+lutadorNome+" //////////////////////////");
    }
    
    /**
     * Exibe em forma de relatorio oq for passado em relatorio
     * 
     * @param relatorio String com o relatorio a ser escrito
     */
    @Override
    public void exibindoRelatorio(String relatorio){
        System.out.println("Relatório: " + relatorio);
        System.out.println();
    }
    
    /**
     * Exibe o encerramento de uma batalha, dizendo quem venceu e a pontuacao atual
     * 
     * @param vencedores Quem venceu a batalha
     * @param pontuacao 
     */
    @Override
    public void encerramentoDaBatalha(String vencedores, int pontuacao){
        System.out.println("A batalha acabou!");
        System.out.println("Vencedores: " + vencedores);
        System.out.println("Pontuacao Final: " + pontuacao);
        System.out.println();
    }
    
    
    
    /**
     * Pergunta se o jogador quer jogar outra partida ou nao
     * 
     * @return True quer, false caso nao queira
     */
    @Override
    public boolean querJogarOutraPartida(){
        int a = 0;
        do{
            System.out.println("Quer jogar outra partida:\n"
                    + "1-Sim 2-Nao");
            Scanner scan = new Scanner (System.in);
            a = scan.nextInt();
        }while(!verificaNumero(String.valueOf(a), 2));
        
        if(a == 1)
            return true;
        return false;
    }
}