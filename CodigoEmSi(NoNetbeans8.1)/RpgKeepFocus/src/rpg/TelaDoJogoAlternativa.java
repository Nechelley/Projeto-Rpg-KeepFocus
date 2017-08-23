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
        String[] heroi = {"Nech",
            String.valueOf(1),//mago
            String.valueOf(3),//constituicao
            String.valueOf(0),//pequena
            String.valueOf(0)};//nada
        
        return heroi;
    }
    
    /**
     * Pede a informacao ao usuario se deseja adicionar mais herois ao time de herois
     * 
     * @return retorna 1-sim 2-nao
     */
    @Override
    public int confirmarSeTemMaisHerois(){
        return 2;
    }    
    
    /**
     * Le as informacoes informaçoes do golpe do heroi na ordem
     * {nome,classe}
     * 
     * @return Vetor de Strings com as informaçoes do golpe do heroi
     */
    @Override
    public String[] lendoGolpe(String classe){
        String[] inf = {"3", "magia arcana"};
        return inf;
    }
    
    /**
     * Pede a informacao ao usuario se deseja adicionar mais golpes ao heroi
     * 
     * @return retorna 1-sim 2-nao
     */
    @Override
    public int confirmarSeTemMaisGolpesDeHerois(){
        return 2;
    } 
    
    /**
     * Exibe menu para escolha de acao
     * 
     * @param inf Vetor de strings com que acoes o lutador pode fazer
     * @return Int com a escolha do jogador
     */
    @Override
    public int escolhendoAcao(String[] inf){
        return 1;//so ataca
    }
    
    /**
     * Exibe o menu para escolher o alvo quando a acao atacar for selecionada
     * 
     * @param inf Vetor com os nomes dos alvos
     * @return Nome do alvo
     */
    @Override
    public String escolhendoAlvo(String[] inf){
        return inf[0];
    }
    
    /**
     * Metodo para se adquirir um nome de golpe valido
     * 
     * @param inf matriz com as informacoes necessarias
     * @return String com o nome do golpe
     */
    @Override
    public String escolhendoGolpe(String[][] inf){
        return inf[0][0];
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
        return true;
    }
}