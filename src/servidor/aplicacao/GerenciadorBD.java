package servidor.aplicacao;

import compartilhado.modelo.*;
import compartilhado.aplicacao.*;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GerenciadorBD {
        
    private String url = "jdbc:mysql://";
    private String usuario;
    private String senha;
    
    public GerenciadorBD(String url, String usuario, String senha){
        this.url += url;
        this.usuario = usuario;
        this.senha = senha;
    }
    
    public Connection conexao() throws SQLException{
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        return DriverManager.getConnection(url, usuario, senha);
    }
    
    private String convData(Date dt){
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS").format(dt);
    }    
    
    public int autenticarUsuario(UsuarioAutenticacao userAuth) throws SQLException{
        Statement st = conexao().createStatement();
        String SQL = "SELECT * FROM usuario WHERE usuario = '" + userAuth.getUsuario() + "'";
        ResultSet rs = st.executeQuery(SQL);
        if(!rs.next())
            return 1; // retorna 1 caso não achou registro desse usuário
        if(rs.getString("senha").equals(userAuth.getSenha()))
            return 3; // retorna 3 caso as senhas batem
        else
            return 0; // ou então retorna 0 caso os dados estejam incorretos
    }
     
    public boolean alterarUsuario(Usuario usuario) throws SQLException, IOException{
        PreparedStatement ps = conexao().prepareStatement("UPDATE usuario SET usuario = ?, foto = ? WHERE id = ?");
        ps.setString(1, usuario.getUsuario());
        ps.setBlob(2, compartilhado.aplicacao.Foto.imageToBlob(usuario.getFoto().getImage()));
        ps.setInt(3, usuario.getId());
        int result = ps.executeUpdate();
        return result == 1;
    }
    
    public boolean criarGrupo(Grupo grupo) throws SQLException, IOException{
        Statement st = conexao().createStatement();
        int [] m = grupo.getMembros();
        String SQL = "INSERT INTO grupo (id, nomeGrupo, idMembro1, idMembro2, idMembro3, idMembro4, idMembro5, idMembro6, idMembro7, idMembro8, idMembro9, idMembro10, foto) VALUES ('" 
                + grupo.getId() + "', '" + grupo.getNome() + "', '" + m[0] + "', '" + m[1] + "', '" + m[2] + "', '" + m[3] + "', '" + m[4] + "', '" + m[5] + "', '" + m[6] + "', '" + m[7] + "', '" + m[8] + "', '" + m[9] + "', '" + compartilhado.aplicacao.Foto.imageToBlob(grupo.getFoto()) + "')";
        int result = st.executeUpdate(SQL);
        return result == 1;
    }
    
    public boolean alterarGrupo(Grupo grupo) throws SQLException, IOException{
        Statement st = conexao().createStatement();
        int [] m = grupo.getMembros();
        String SQL = "UPDATE grupo SET nomeGrupo = '" + grupo.getNome() + "', idMembro1 = '" + m[0] + "', idMembro2 = '" + m[1] + "', idMembro3 = '" + m[2] + "', idMembro4 = '" + m[3] + "', idMembro5 = '" + m[4] + "', idMembro6 = '" + m[5]
                     + "', idMembro7 = '" + m[6] + "', idMembro8 = '" + m[7] + "', idMembro9 = '" + m[8] + "', idMembro10 = '" + m[9] + "', foto = '" + compartilhado.aplicacao.Foto.imageToBlob(grupo.getFoto()) + "' WHERE id = '" + grupo.getId() + "'";
        int result = st.executeUpdate(SQL);
        return result == 1;
    }
    
    public boolean deletarGrupo(int id) throws SQLException{
        Statement st = conexao().createStatement();
        String SQL = "DELETE FROM grupo WHERE id = '" + id + "'"; 
        int result1 = st.executeUpdate(SQL);
        
        SQL = "DELETE FROM mensagem WHERE destinoTipo = 'G' AND idGrupoDestino = '" + id + "'"; 
        int result2 = st.executeUpdate(SQL);
        
        return (result1 == 1) && (result2 >= 1);
    }
   
    public boolean alterarSenha(int id, String novaSenha) throws SQLException{
        Statement st = conexao().createStatement();
        String SQL = "UPDATE usuario SET senha = '" + novaSenha + "' WHERE id = '" + Integer.toString(id) + "'";
        int result = st.executeUpdate(SQL);
        return result == 1;
    }
    
    public boolean deletarUsuario(int id) throws SQLException{
        Statement st = conexao().createStatement();
        String SQL = "DELETE FROM usuario WHERE id = '" + Integer.toString(id) + "'";
        int result = st.executeUpdate(SQL);
        return result == 1;
    }
    
    public boolean cadastrarUsuario(UsuarioAutenticacao usuario) throws SQLException, IOException, URISyntaxException{
        ImageIcon imagem = new ImageIcon(getClass().getResource("/compartilhado/imagens/usuario.png"));
        Image foto = compartilhado.aplicacao.Foto.redimensionarFoto(imagem.getImage(), 50);
        PreparedStatement ps = conexao().prepareStatement("INSERT INTO usuario (usuario, senha, foto) VALUES (?, ?, ?)");
        ps.setString(1, usuario.getUsuario());
        ps.setString(2, usuario.getSenha());
        ps.setBlob(3, compartilhado.aplicacao.Foto.imageToBlob(foto));
        int result = ps.executeUpdate();
        return result == 1;
    }
    
    public ArrayList<Usuario> getListaUsuarios() throws SQLException, IOException{
        Statement st = conexao().createStatement();
        String SQL = "SELECT * FROM usuario ORDER by id";
        ResultSet rs = st.executeQuery(SQL);
        ArrayList<Usuario> usuarios = new ArrayList<>();
        while(rs.next()){
            int id = rs.getInt("id");
            String usuario = rs.getString("usuario");
            Blob blob = rs.getBlob("foto");
            InputStream is = blob.getBinaryStream();
            Image imagem = ImageIO.read(is);
            ImageIcon foto = new ImageIcon(imagem);
            usuarios.add(new Usuario(id, usuario, foto));
        }
        return usuarios;
    }
        
    public boolean enviarMensagem(Mensagem mensagem) throws SQLException {
        String SQL;
        Statement st = conexao().createStatement();
        if(mensagem.getTipoMensagem() == 'T'){
            SQL = "INSERT INTO mensagem (idUsuarioOrigem, idUsuarioDestino, destinoTipo, txtMensagem, timeMensagem, tipoMens, idMensagem, idGrupoDestino) "
        + "VALUES ('" + mensagem.getIdOrigem() + "', '" + mensagem.getIdDestino() + "', '" + mensagem.getDestinoTipo() + "', " + mensagem.getMensagem() + "', '" + convData(mensagem.getDataMensagem()) + "', '" + mensagem.getTipoMensagem() + "', '" + mensagem.getIdMensagem() + "', " + mensagem.getIdDestino()+ " ')";
        } else {
            SQL = "INSERT INTO mensagem (idUsuarioOrigem, idUsuarioDestino, destinoTipo, txtMensagem, arquivo, timeMensagem, tipoMens, idMensagem, idGrupoDestino) "
        + "VALUES ('" + mensagem.getIdOrigem() + "', '" + mensagem.getIdDestino() + "', '" + mensagem.getDestinoTipo() + "', ' ', '" + mensagem.getMensagem() + "', '" + convData(mensagem.getDataMensagem()) + "', '" + mensagem.getTipoMensagem() + "', '" + mensagem.getIdMensagem() + "', " + mensagem.getIdDestino()+ " ')";
        }
        int result = st.executeUpdate(SQL);
        return result == 1;
    }
}
