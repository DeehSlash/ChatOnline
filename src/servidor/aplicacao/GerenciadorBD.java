package servidor.aplicacao;

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/*

--> String SELECT
Statement st = conexao().createStatement();
String SQL = "SELECT * FROM pais ORDER by nome";

--> String UPDATE:
Statement st = conexao().createStatement();
    String SQL = "INSERT INTO telefone VALUES ("
            + t.getId() + ", '" + t.getNome() + "', '" + t.getCodPais() + "', '" + t.getNumero() + "')";
    st.executeUpdate(SQL);
*/


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
        Connection c = DriverManager.getConnection(url, usuario, senha);
        return c;
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
            }
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return bais;
    }
    
    
    //  ALTERAR NOME DA CLASSE USUARIOAUTENTICADOR PARA O NOME DA CLASSE USUÁRIO COM SENHA
    public Usuario autenticar(UsuarioAutenticacao userAuth) throws SQLException{
        Usuario user = null;
        Statement st = conexao().createStatement();
        String SQL = "SELECT * FROM usuario WHERE usuario = '"+userAuth.getUsuario() +"' AND senha = '"+userAuth.getSenha()+"'";
        ResultSet rs = st.executeQuery(SQL);
        if(rs.next()){
            user = new Usuario(rs.getInt("id"),rs.getString("usuario"),rs.getBlob("foto"));
        }
        return user;
    }
    
    public void alterarContato(Usuario user)throws SQLException{
        Statement st = conexao().createStatement();
        String SQL = "UPDATE usuario SET usuario = '"+ user.getUsuario +"' , foto = '"+imageToBlob(user.getFoto)+"' WHERE id = "+Integer.toString(user.getId());
        st.executeUpdate(SQL);
    }
    
    public void criarGrupo(Grupo grupo)throws SQLException{
        Statement st = conexao().createStatement();
        int [] m = grupo.getMembros();
        String SQL = "INSERT INTO grupo (id,nomeGrupo,idMembro1,idMembro2,idMembro3,idMembro4,idMembro5,idMembro6,idMembro7,idMembro8,idMembro9,idMembro10,foto) VALUES (" 
                +grupo.getID()+", '" + grupo.getNome() + "',"+m[0]+ ","+m[1] + ","+m[2] + ","+m[3] + ","+m[4] + ","+m[5] + ","+m[6] + ","+m[7] + ","+m[8] + ","+m[9] +" ,'"+imageToBlob(grupo.getFoto)+"')";
        st.executeUpdate(SQL);
    }
    
    public void alterarGrupo(Grupo grupo) throws SQLException{
        Statement st = conexao().createStatement();
        int [] m = grupo.getMembros();
        String SQL = "UPDATE grupo SET id = "+grupo.getId()+",nomeGrupo = "+grupo.getNome()+",idMembro1 = "+m[0]+",idMembro2 = "+m[1]+",idMembro3 = "+m[2]+",idMembro4 = "+m[3]+",idMembro5 = "+m[4]+",idMembro6 = "+m[5]+
                     ",idMembro7 = "+m[6]+",idMembro8 = "+m[7]+",idMembro9 = "+m[8]+",idMembro10 = "+m[9]+",foto = "+imageToBlob(grupo.getFoto())+" WHERE id = " + grupo.getId(); 
        st.executeUpdate(SQL);
    }
    
    public void deleteGrupo(int id) throws SQLException{
        Statement st = conexao().createStatement();
        String SQL = "DELETE FROM grupo WHERE id = " + id; 
        st.executeUpdate(SQL);
    }
   
    public void alterarSenha(int id, String novaSenha)throws SQLException{
        Statement st = conexao().createStatement();
        String SQL = "UPDATE usuario SET senha = '"+ novaSenha +"' WHERE id = "+Integer.toString(id);
        st.executeUpdate(SQL);
    }
    
    public void deletarContato(int id)throws SQLException{
        Statement st = conexao().createStatement();
        String SQL = "DELETE FROM usuario WHERE id = "+Integer.toString(id);
        st.executeUpdate(SQL);
    }
    
    public void cadastrarUsuario(Usuario usuario, String senha) throws SQLException{
        Statement st = conexao().createStatement();
        String SQL = "INSERT INTO usuario (usuario, senha, foto) "
                + "VALUES ('" + usuario.getUsuario() + "', '" + senha + "', '" + imageToBlob(usuario.getFoto()) + "')";
        st.executeUpdate(SQL);
    }
    
    // ALTERAR O NOME DA CLASSE USUARIOLISTA PARA O NOME DA CLASSE USUARIO SEM SENHA
    public ArrayList<Usuario> getListaUsuarios() throws SQLException, IOException{
        Statement st = conexao().createStatement();
        String SQL = "SELECT id, usuario, foto FROM usuarios ORDER by usuario";
        ResultSet rs = st.executeQuery(SQL);
        ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
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
    
  
    
    // ALTERAR O NOME DAS CLASSES MENSAGEM E USUARIOLISTA PARA OS NOMES DAS CLASSES DA MENSAGEM E DO USUARIO SEM SENHA
    public void enviarMensagem(Mensagem msg, int idOrigem, int idDestino, char tipoDestino) throws SQLException {
        Statement st = conexao().createStatement();
            String SQL = "INSERT INTO mensagem (idUsuarioOrigem, idDestino, destinoTipo, txtMensagem, timeMensagem, arquivo, tipoMens) "
            + "VALUES ('" + idOrigem + "', '" + idDestino + "', '" + msg.getTexto() + "', '" +convData(msg.getData) + "', '"+ msg.getArquivo +"', '"+msg.getTipo() +"')";
        st.executeUpdate(SQL);
    }
}
