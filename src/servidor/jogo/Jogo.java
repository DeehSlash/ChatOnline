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
    private final int passo;
    
    private Point posicaoAzul;
    private int rotacaoAzul;
    
    private Point posicaoVermelho;
    private int rotacaoVermelho;
    
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
        passo = 10;
        inicializar();
    }
    
    public int getIdGrupo() { return idGrupo; }
    public Point getTamJanela() { return tamJanela; }
    public int getTamVeiculo() { return tamVeiculo; }
    public int getPasso() { return passo; }
    public Point getPosicaoAzul() { return posicaoAzul; }
    public Point getPosicaoVermelho() { return posicaoVermelho; }
    
    public void setPosicaoAzul(Point posicao) { posicaoAzul = posicao; }
    public void setPosicaoVermelho(Point posicao) { posicaoVermelho = posicao; }
    
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
                        Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.atualizarJogo(idGrupo, posicaoAzul.x, posicaoAzul.y, time);
                    else if(time.equals("vermelho"))
                        Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.atualizarJogo(idGrupo, posicaoVermelho.x, posicaoVermelho.y, time);
                }
            }
            for (Usuario usuario : timeVermelho) {
                if(Principal.getConexaoPorIdUsuario(usuario.getId()) != null)
                    if(time.equals("azul"))
                        Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.atualizarJogo(idGrupo, posicaoAzul.x, posicaoAzul.y, time);
                    else if(time.equals("vermelho"))
                        Principal.getConexaoPorIdUsuario(usuario.getId()).comunicador.atualizarJogo(idGrupo, posicaoVermelho.x, posicaoVermelho.y, time);
            }
        } catch(RemoteException ex){
            ex.printStackTrace();
            Principal.frmPrincipal.enviarLog("Exceção ao atualizar posição de jogo com id " + idGrupo + ": " + ex.getMessage());
        }
    }
    
    public Rectangle getLimite(String time){
        if(time.equals("azul")){
            return new Rectangle(posicaoAzul.x, posicaoAzul.y, tamVeiculo, tamVeiculo);
        }else if(time.equals("vermelho"))
            return new Rectangle(posicaoVermelho.x, posicaoVermelho.y, tamVeiculo, tamVeiculo);
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
