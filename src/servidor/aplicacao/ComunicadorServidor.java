package servidor.aplicacao;

import compartilhado.aplicacao.IComunicadorCliente;
import compartilhado.aplicacao.IComunicadorServidor;
import compartilhado.modelo.Grupo;
import compartilhado.modelo.Mensagem;
import compartilhado.modelo.Usuario;
import compartilhado.modelo.UsuarioAutenticacao;
import java.awt.Image;
import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

public class ComunicadorServidor extends UnicastRemoteObject implements IComunicadorServidor {

    public int idConexao;
        
    public ComunicadorServidor(int id) throws RemoteException{
        idConexao = id;
    }
    
    @Override
    public boolean registrarCliente(IComunicadorCliente comunicador) throws RemoteException {
        Principal.getConexao(idConexao).setComunicador(comunicador);
        return true;
    }
    
    @Override
    public int autenticarUsuario(UsuarioAutenticacao autenticacao) throws RemoteException {
        /*  CÓDIGO DE RETORNO (STATUS)
            -1 - Erro genérico
            0 - Usuário não existe
            1 - Usuário já está online
            2 - Senha incorreta
            3 - Autenticou
        */
        boolean existe = false; // controle se usuário já existe
        int status = -1, id = 0; // controle de status da autenticação
        for (Usuario u : Principal.usuarios) { // verifica se o usuário já existe na lista de usuários
            if(u.getUsuario().equals(autenticacao.getUsuario())){
                existe = true;
                id = u.getId();
                if(u.isOnline())
                    return 1; // se já está online, então retorna 1
            }
        }
        if(!existe)
            return 0; // se usuário não existe, então retorna 0
        try {
            status = Principal.gerenciador.autenticarUsuario(autenticacao); // tenta autenticar na db
        } catch (SQLException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao autenticar usuário " + autenticacao.getUsuario() + ": " + ex.getMessage());
            return status;
        }
        if(status == 2){
            return status; // se a senha está incorreta, retorna
        }
        Principal.frmPrincipal.enviarLog("Usuário " + autenticacao.getUsuario() + " (" + id + ") se conectou"); // envia log de autenticação
        Principal.getConexao(idConexao).setIdCliente(id);
        Principal.usuarios.get(id - 1).setOnline(true);
        Principal.frmPrincipal.alterarUsuarios(true); // incrementa número de usuários online
        Principal.getConexao(idConexao).atualizarLista();
        return status; // retorna status de autenticação
    }

    @Override
    public int cadastrarUsuario(UsuarioAutenticacao autenticacao) throws RemoteException {
        /*  CÓDIGO DE RETORNO (STATUS)
            -1 - Erro genérico
            0 - Usuário já existe
            1 - Cadastrou
        */
        int status = -1; // controle de status do cadastro
        boolean cadastro; // controle para retorno da db
        for (Usuario u : Principal.usuarios) { // verifica se o usuário já existe na lista de usuários
            if(u.getUsuario().equals(autenticacao.getUsuario())){
                return 0; // se usuário já existe, então retorna 0
            }
        }
        try {
            cadastro = Principal.gerenciador.cadastrarUsuario(autenticacao); // tenta cadastrar na db
        } catch (SQLException | IOException | URISyntaxException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao cadastrar usuário " + autenticacao.getUsuario() + ": " + ex.getMessage());
            return status;
        }
        if(!cadastro){
            return status; // se a db retornou falso, retorna o status
        }else{
            status = 1; // se não, status é 1 (cadastrou)
        }
        ImageIcon imagem = new ImageIcon(getClass().getResource("/compartilhado/imagens/usuario.png")); // cria uma ImageIcon com a foto padrão de usuário
        Image foto = compartilhado.aplicacao.Imagem.redimensionarImagem(imagem.getImage(), 50, false); // redimensiona a imagem
        Principal.usuarios.add(new Usuario(Principal.usuarios.size() + 1, autenticacao.getUsuario(), new ImageIcon(foto))); // adiciona na lista de usuários
        Principal.frmPrincipal.enviarLog("Usuário " + autenticacao.getUsuario() + " se cadastrou"); // envia log de cadastro
        return status; // retorna o status
    }

    @Override
    public boolean alterarUsuario(Usuario usuario) throws RemoteException {
        try {
            Principal.gerenciador.alterarUsuario(usuario);
            Principal.usuarios.set(usuario.getId() - 1, usuario);
            Principal.getConexao(idConexao).atualizarLista();
        } catch (SQLException | IOException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao alterar usuário " + usuario.getUsuario() + ": " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Usuario getUsuarioPorNome(String nome) throws RemoteException {
        for (Usuario usuario : Principal.usuarios) {
            if(usuario.getUsuario().equals(nome))
                return usuario;
        }
        return null;
    }
    
    @Override
    public boolean criarGrupo(Grupo grupo) throws RemoteException {
        try {
            Principal.gerenciador.criarGrupo(grupo);
        } catch (SQLException | IOException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao criar grupo " + grupo.getNome() + ": " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        Principal.grupos.add(grupo);
        Principal.getConexao(idConexao).atualizarLista();
        Principal.frmPrincipal.enviarLog("Grupo " + grupo.getNome() + " foi criado");
        return true;
    }

    @Override
    public boolean alterarGrupo(Grupo grupo) throws RemoteException {
        try {
            Principal.gerenciador.alterarGrupo(grupo);
            int i = 0;
            for (Grupo g : Principal.grupos) {
                if(g.getId() == grupo.getId())
                    break;
                i++;
            }
            Principal.grupos.set(i, grupo);
            Principal.getConexao(idConexao).atualizarLista();
        } catch (SQLException | IOException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao alterar grupo " + grupo.getNome() + ": " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public boolean deletarGrupo(Grupo grupo) throws RemoteException {
        try {
            Principal.gerenciador.deletarGrupo(grupo.getId());
        } catch (SQLException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao deletar grupo " + grupo.getNome() + ": " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
        int i = 0;
        for (Grupo g : Principal.grupos) {
            if(g.getId() == grupo.getId())
                break;
            i++;
        }
        Principal.grupos.remove(i);
        Principal.getConexao(idConexao).atualizarLista();
        return true;
    }
    
    @Override
    public int recuperarIdDisponivelGrupo() throws RemoteException {
        try {
            return Principal.gerenciador.receberIdGrupoDisponivel();
        } catch (SQLException ex) {
            Principal.frmPrincipal.enviarLog("Exceção SQL ao recuperar id disponível para grupo: " + ex.getMessage());
            ex.printStackTrace();
        }
        return 0;
    }
    
    @Override
    public boolean verificarNomeGrupo(String nome) throws RemoteException{
        for (Grupo grupo : Principal.grupos) {
            if(grupo.getNome().equals(nome))
                return true;
        }
        return false;
    }
    
    @Override
    public ArrayList<Usuario> recuperarListaUsuarios() throws RemoteException {
        return Principal.usuarios; // retorna a lista de usuários
    }

    @Override
    public ArrayList<Grupo> recuperarListaGrupos() throws RemoteException {
        return Principal.getConexao(idConexao).getGrupos();
    }

    @Override
    public boolean enviarMensagem(Mensagem mensagem) throws RemoteException {
        try {
            Principal.gerenciador.enviarMensagem(mensagem);
        } catch (SQLException | IOException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao enviar mensagem com origem " + mensagem.getIdOrigem() + " e destino " + mensagem.getIdDestino() + ": " + ex.getMessage());
            return false;
        }
        boolean teste = false;
        for (Conexao conexao : Principal.conexoes) {
            if(conexao.getIdCliente() != mensagem.getIdOrigem()){
                if(conexao.getIdCliente() == mensagem.getIdDestino() && mensagem.getDestinoTipo() == 'U') // se a mensagem for individual e é o destinatário
                    teste = true; // então a mensagem é para essa conexão
                else if(mensagem.getDestinoTipo() == 'G'){ // ou se for a mensagem para grupo
                    if(conexao.pertenceAoGrupo(mensagem.getIdDestino())) // e essa conexão faz parte do grupo
                        teste = true; // então a mensagem é para essa conexão
                }
                if(teste){
                    if(conexao.comunicador == null) // verifica se o comunicador não é nulo
                        return false;
                    conexao.comunicador.receberMensagem(mensagem); // envia a mensagem
                    if(mensagem.getDestinoTipo() == 'U') // se for conversa individual, não há nada mais para fazer, então retorna
                        return true;
                }
            }
        }
        return true;
    }

    @Override
    public ArrayList<Mensagem> recuperarListaMensagens(int idOrigem, int idDestino, char tipoDestino) throws RemoteException {
        ArrayList<Mensagem> mensagens = null;
        try {
            mensagens = Principal.gerenciador.getListaMensagens(idOrigem, idDestino, tipoDestino);
        } catch (SQLException | IOException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao recuperar lista de mensagens com origem " + idOrigem + " e destino " + idDestino + ": " + ex.getMessage());
        }
        return mensagens;
    }

    @Override
    public boolean desconectar() throws RemoteException {
        try {
            Principal.getConexao(idConexao).desconectar();
        } catch (IOException ex) {
            Principal.frmPrincipal.enviarLog("Exceção ao desconectar cliente " + Principal.getConexao(idConexao).getIdCliente() + ": " + ex.getMessage());
            return false;
        }
        return true;
    }
}
