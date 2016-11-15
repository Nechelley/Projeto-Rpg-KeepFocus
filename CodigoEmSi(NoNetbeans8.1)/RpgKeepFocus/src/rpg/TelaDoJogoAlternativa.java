package rpg;

import java.io.Serializable;
import java.util.Scanner;

/**
 *
 * @author Nechelley Alves
 */
public class TelaDoJogoAlternativa implements ObservadorJogo, Serializable{
    private static Jogo rpg;
    public TelaDoJogoAlternativa(){
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
            nome = "Nechelley";
        }while(!verificaNome(nome));
        do{
            System.out.println("Insira a classe do personagem:\n"
                    + "1-Guerreiro 2-Mago 3-Clerigo");
            classe = "2";
        }while(!verificaNumero(classe,3));
        do{
            System.out.println("Insira o foco do personagem:\n"
                    + "1-Força 2-Destreza 3-Constituição 4-Carisma");
            foco = "1";
        }while(!verificaNumero(foco,4));
        do{
            System.out.println("Insira qual é a arma do personagem:\n"
                    + "1-Pequena 2-Média 3-Grande");
            arma = "2";
        }while(!verificaNumero(arma,3));
        do{
            System.out.println("Insira qual é o tipo de armadura do personagem:\n"
                    + "1-Nada 2-Leve 3-Pesada");
            armadura = "2";
        }while(!verificaNumero(armadura,3));
        
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
            num = 2; 
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
    public String[] lendoGolpe(){
        Scanner scan = new Scanner (System.in);
        String classe = "";
        String golpeNome = "";
                
        do{
            System.out.println("Qual a classe de golpe:\n"
                    + "1-Fisico 2-Magico 3-Bola De Fogo 4-Meteoro 5-Lanca De Gelo 6-Nevasca");
            classe = "2";
        }while(!verificaNumero(classe,6));
        do{
            System.out.println("Qual o nome do golpe:");
            golpeNome = "mag";
        }while(!verificaNome(golpeNome));
        
        String[] inf = {classe, golpeNome};
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
            num = 2;
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
            escolha = 1;
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
            alvoNumero = 1;
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
            golpeNumero = 1;
        }while(!verificaNumero(String.valueOf(golpeNumero), cont));
        return inf[golpeNumero-1][0];
    }
    
    
    
    //verificacoes
    private boolean verificaNome(String nome){
        if(nome.isEmpty()){
            System.out.println("Nome digitado invalido.");
            System.out.println();
            return false;
        }
        String valoresValidos[] = {
            "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
            "0","1","2","3","4","5","6","7","8","9"," "
        };
        boolean letraValida = false;
        for(int i = 0; i < nome.length(); i++){
            letraValida = false;
            for(String v : valoresValidos){
                if(nome.substring(i, i + 1).equals(v)){
                    letraValida = true;
                    break;
                }
            }
            if(!letraValida){
                System.out.println("Nome digitado errado.");
                System.out.println();
                return false;
            }
        }
        return true;
    }
    private boolean verificaNumero(String num, int limite){
        if(Integer.valueOf(num) > limite || Integer.valueOf(num) < 1){
            System.out.println("Numero digitado invalido, digite outro.");
            System.out.println();
            return false;
        }
        return true;
    }
    private boolean verificaAcao(String aux, int escolha){
        if((!aux.contains("2-Defender") && escolha == 2) || 
                (!aux.contains("3-Esquivar") && escolha == 3) ||
                escolha < 1 || escolha > 5){
            System.out.println("Numero digitado invalido, digite outro.");
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
            a = 1;
        }while(!verificaNumero(String.valueOf(a), 2));
        
        if(a == 1)
            return true;
        return false;
    }
}