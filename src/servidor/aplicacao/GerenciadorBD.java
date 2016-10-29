package servidor.aplicacao;

import compartilhado.modelo.*;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;

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
    
    private ByteArrayInputStream imageToBlob(Image imagem) throws IOException{
        BufferedImage bi = new BufferedImage(imagem.getWidth(null), imagem.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(imagem, 0, 0, null);
        g2d.dispose();
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
        } finally {
            try {
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ByteArrayInputStream(baos.toByteArray());
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
        Statement st = conexao().createStatement();
        String SQL = "UPDATE usuario SET usuario = '" + usuario.getUsuario() + "' , foto = '" + imageToBlob(usuario.getFoto()) + "' WHERE id = '" + Integer.toString(usuario.getId()) + "'";
        int result = st.executeUpdate(SQL);
        return result == 1;
    }
    
    public boolean criarGrupo(Grupo grupo) throws SQLException, IOException{
        Statement st = conexao().createStatement();
        int [] m = grupo.getMembros();
        String SQL = "INSERT INTO grupo (id, nomeGrupo, idMembro1, idMembro2, idMembro3, idMembro4, idMembro5, idMembro6, idMembro7, idMembro8, idMembro9, idMembro10, foto) VALUES ('" 
                + grupo.getId() + "', '" + grupo.getNome() + "', '" + m[0] + "', '" + m[1] + "', '" + m[2] + "', '" + m[3] + "', '" + m[4] + "', '" + m[5] + "', '" + m[6] + "', '" + m[7] + "', '" + m[8] + "', '" + m[9] + "', '" + imageToBlob(grupo.getFoto()) + "')";
        int result = st.executeUpdate(SQL);
        return result == 1;
    }
    
    public boolean alterarGrupo(Grupo grupo) throws SQLException, IOException{
        Statement st = conexao().createStatement();
        int [] m = grupo.getMembros();
        String SQL = "UPDATE grupo SET nomeGrupo = '" + grupo.getNome() + "', idMembro1 = '" + m[0] + "', idMembro2 = '" + m[1] + "', idMembro3 = '" + m[2] + "', idMembro4 = '" + m[3] + "', idMembro5 = '" + m[4] + "', idMembro6 = '" + m[5]
                     + "', idMembro7 = '" + m[6] + "', idMembro8 = '" + m[7] + "', idMembro9 = '" + m[8] + "', idMembro10 = '" + m[9] + "', foto = '" + imageToBlob(grupo.getFoto()) + "' WHERE id = '" + grupo.getId() + "'";
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
    
    public boolean cadastrarUsuario(UsuarioAutenticacao usuario) throws SQLException, IOException{
        Statement st = conexao().createStatement();
        String SQL = "INSERT INTO usuario (usuario, senha, foto) "
                + "VALUES ('" + usuario.getUsuario() + "', '" + usuario.getSenha() + "', ' ')";
        int result = st.executeUpdate(SQL);
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
            Image foto = ImageIO.read(is);
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
