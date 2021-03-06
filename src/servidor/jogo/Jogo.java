package servidor.jogo;

import compartilhado.modelo.Usuario;
import java.awt.Point;
import java.awt.Rectangle;
import java.rmi.RemoteException;
import java.util.ArrayList;
import servidor.aplicacao.Principal;

public class Jogo {

    private final int idGrupo;
    
    private final ArrayList<Usuario> timeAzul;
    private final ArrayList<Usuario> timeVermelho;
    
    private final Point tamJanela;
    private final int tamVeiculo;
    private final Point tamTiro;
    private final int passo;
    
    private Point posicaoAzul;
    private int rotacaoAzul;
    
    private Point posicaoVermelho;
    private int rotacaoVermelho;
    
    private boolean tiroAzul;
    private Point posicaoTiroAzul;
    private boolean tiroVermelho;
    private Point posicaoTiroVermelho;
    
    private int vidaAzul;
    private int vidaVermelho;
    
    private ComandoInvoker comandoInvoker;
    
    public Jogo(int idGrupo, ArrayList<Usuario> timeAzul, ArrayList<Usuario> timeVermelho){
        this.idGrupo = idGrupo;
        this.timeAzul = timeAzul;
        this.timeVermelho = timeVermelho;
        tamJanela = new Point(500, 500);
        tamVeiculo = 50;
        posicaoAzul = new Point();
        posicaoVermelho = new Point();
        posicaoTiroAzul = new Point();
        posicaoTiroVermelho = new Point();
        tiroAzul = false;
        tiroVermelho = false;
        tamTiro = new Point(10, 63);
        passo = 10;
        inicializar();
    }
    
    public int getIdGrupo() { return idGrupo; }
    public Point getTamJanela() { return tamJanela; }
    public int getTamVeiculo() { return tamVeiculo; }
    public int getPasso() { return passo; }
    public Point getPosicaoAzul() { return posicaoAzul; }
    public Point getPosicaoVermelho() { return posicaoVermelho; }
    public boolean getTiroAzul() { return tiroAzul; }
    public boolean getTiroVermelho() { return tiroVermelho; }
    public Point getPosicaoTiroAzul() { return posicaoTiroAzul; }
    public Point getPosicaoTiroVermelho() { return posicaoTiroVermelho; }
    public int getVidaAzul() { return vidaAzul; }
    public int getVidaVermelho() { return vidaVermelho; }
    
    public void setPosicaoAzul(Point posicao) { posicaoAzul = posicao; }
    public void setPosicaoVermelho(Point posicao) { posicaoVermelho = posicao; }
    public void setTiroAzul(boolean tiro) { tiroAzul = tiro; }
    public void setTiroVermelho(boolean tiro) { tiroVermelho = tiro; }
    public void setPosicaoTiroAzul(Point posicao) { posicaoTiroAzul = posicao; }
    public void setPosicaoTiroVermelho(Point posicao) { posicaoTiroVermelho = posicao; }
    public void setVidaAzul(int vida) { vidaAzul = vida; }
    public void setVidaVermelho(int vida) { vidaVermelho = vida; }
    
    public void inicializar(){
        // inicialização das variáveis com valores iniciais
        posicaoAzul.x = (tamJanela.x / 2) - (tamVeiculo / 2); // coordenada inicial para x e y
        posicaoAzul.y = tamJanela.y - tamVeiculo;
        rotacaoAzul = 0;
        posicaoVermelho.x = (tamJanela.x / 2) - (tamVeiculo / 2);
        posicaoVermelho.y = 0;
        rotacaoVermelho = 180;
        vidaAzul = 5;
        vidaVermelho = 5;
        // cria invoker para comandos
        comandoInvoker = new ComandoInvoker(idGrupo);
        // envia informações para os jogadores
        try {
            enviarInformacao("Iniciando jogo...");
            String azul = "Time azul: ";
            for (Usuario usuario : timeAzul) {
                azul += usuario.getUsuario() + ", ";
            }
            azul = azul.substring(0, azul.lastIndexOf(","));
            String vermelho = "Time vermelho: ";
            for (Usuario usuario : timeVermelho) {
                vermelho += usuario.getUsuario() + ", ";
            }
            vermelho = vermelho.substring(0, vermelho.lastIndexOf(","));
            enviarInformacao(azul);
            enviarInformacao(vermelho);
            enviarInformacao("Comandos:\n.cima\n.baixo\n.esquerda\n.direita\n.atirar");
            enviarInformacao("O jogo foi iniciado");
            // abre o frame do jogo para todos os jogadores
            abrirFrames();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            Principal.frmPrincipal.enviarLog("Falha ao enviar informação para grupo " + idGrupo + ":" + ex.getMessage());
        }
    }
    
    public void abrirFrames() throws RemoteException{
        for (Usuario usuario : timeAzul) {
            if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null)
                Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.iniciarJogo(idGrupo);
        }
        for (Usuario usuario : timeVermelho) {
            if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null)
                Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.iniciarJogo(idGrupo);
        }
    }
    
    public void enviarInformacao(String informacao) throws RemoteException{
        for (Usuario usuario : timeAzul) {
            if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null)
                Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.receberInformacao(idGrupo, informacao);
        }
        for (Usuario usuario : timeVermelho) {
            if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null)
                Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.receberInformacao(idGrupo, informacao);
        }
    }
    
    public void atualizarPosicao(String time){
        try {
            for (Usuario usuario : timeAzul) {
                if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null){
                    if(time.equals("azul"))
                        Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.atualizarPosicaoJogo(idGrupo, posicaoAzul.x, posicaoAzul.y, time);
                    else if(time.equals("vermelho"))
                        Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.atualizarPosicaoJogo(idGrupo, posicaoVermelho.x, posicaoVermelho.y, time);
                }
            }
            for (Usuario usuario : timeVermelho) {
                if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null)
                    if(time.equals("azul"))
                        Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.atualizarPosicaoJogo(idGrupo, posicaoAzul.x, posicaoAzul.y, time);
                    else if(time.equals("vermelho"))
                        Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.atualizarPosicaoJogo(idGrupo, posicaoVermelho.x, posicaoVermelho.y, time);
            }
        } catch(RemoteException ex){
            ex.printStackTrace();
            Principal.frmPrincipal.enviarLog("Exceção ao atualizar posição de jogo com id " + idGrupo + ": " + ex.getMessage());
        }
    }
    
    public void atualizarVida(int vida, String time){
        try {
            for (Usuario usuario : timeAzul) {
                if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null){
                    Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.atualizarVidaJogo(idGrupo, vida, time);
                }
            }
            for (Usuario usuario : timeVermelho) {
                if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null)
                    Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.atualizarVidaJogo(idGrupo, vida, time);
            }
        } catch(RemoteException ex){
            ex.printStackTrace();
            Principal.frmPrincipal.enviarLog("Exceção ao atualizar vida de jogo com id " + idGrupo + ": " + ex.getMessage());
        }
    }
    
    public void criarTiro(String time){
        if(time.equals("azul")){
            posicaoTiroAzul.x = posicaoAzul.x + (tamVeiculo / 2) - (tamTiro.x / 2);
            posicaoTiroAzul.y = posicaoAzul.y - tamVeiculo;
            setTiroAzul(true);
        }else{
            posicaoTiroVermelho.x = posicaoVermelho.x + (tamVeiculo / 2) - (tamTiro.x / 2);
            posicaoTiroVermelho.y = posicaoVermelho.y + tamVeiculo;
            setTiroVermelho(true);
        }
        try {
            for (Usuario usuario : timeAzul) {
                if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null){
                    Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.criarTiroJogo(idGrupo, time);
                }
            }
            for (Usuario usuario : timeVermelho) {
                if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null){
                    Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.criarTiroJogo(idGrupo, time);
                }
            }
        if(time.equals("azul"))
            setTiroAzul(false);
        else
            setTiroVermelho(false);
        } catch(RemoteException ex){
            ex.printStackTrace();
            Principal.frmPrincipal.enviarLog("Exceção ao criar tiro de jogo com id " + idGrupo + ": " + ex.getMessage());
        }
    }
    
    public Rectangle getLimiteTiro(String time){
        if(time.equals("azul")){
            return new Rectangle(posicaoTiroAzul.x, posicaoTiroAzul.y, tamTiro.x, tamTiro.y);
        }else if(time.equals("vermelho"))
            return new Rectangle(posicaoTiroVermelho.x, posicaoTiroVermelho.y, tamTiro.x, tamTiro.y);
        return null;
    }
    
    public Rectangle getLimiteVeiculo(String time, int x, int y){ // x e y são incrementos para verificar se uma futura posição vai colidir
        if(time.equals("azul")){
            return new Rectangle(posicaoAzul.x + x, posicaoAzul.y + y, tamVeiculo, tamVeiculo);
        }else if(time.equals("vermelho"))
            return new Rectangle(posicaoVermelho.x + x, posicaoVermelho.y + y, tamVeiculo, tamVeiculo);
        return null;
    }
    
    public void receberComando(int id, String nome){
        String time = "";
        for (Usuario usuario : timeAzul) {
            if(usuario.getId() == id)
                time = "azul";
        }
        if(time.isEmpty())
            time = "vermelho";
        if(comandoInvoker.setComando(nome, time))
            comandoInvoker.executar();
    }
    
}
