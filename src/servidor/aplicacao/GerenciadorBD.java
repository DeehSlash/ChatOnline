package servidor.aplicacao;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
    
    //  ALTERAR NOME DA CLASSE USUARIOAUTENTICADOR PARA O NOME DA CLASSE USUÁRIO COM SENHA
    public void cadastrarUsuario(UsuarioAutenticador usuario) throws SQLException{
        Statement st = conexao().createStatement();
        String SQL = "INSERT INTO usuario (usuario, senha, foto) "
                + "VALUES ('" + usuario.getUsuario() + "', '" + usuario.getSenha() + "', '" + u.getFoto() + "')";
        st.executeUpdate(SQL);
    }
    
    // ALTERAR O NOME DA CLASSE USUARIOLISTA PARA O NOME DA CLASSE USUARIO SEM SENHA
    public ArrayList<UsuarioLista> getListaUsuarios() throws SQLException, IOException{
        Statement st = conexao().createStatement();
        String SQL = "SELECT id, usuario, foto FROM usuarios ORDER by usuario";
        ResultSet rs = st.executeQuery(SQL);
        ArrayList<UsuarioLista> usuarios = new ArrayList<UsuarioLista>();
        while(rs.next()){
            int id = rs.getInt("id");
            String usuario = rs.getString("usuario");
            Blob blob = rs.getBlob("foto");
            InputStream is = blob.getBinaryStream();
            Image image = ImageIO.read(is);
            ImageIcon flag = new ImageIcon(image);
            usuarios.add(new UsuarioLista(id, usuario, flag));
        }
        return usuarios;
    }
    
    // ALTERAR O NOME DAS CLASSES MENSAGEM E USUARIOLISTA PARA OS NOMES DAS CLASSES DA MENSAGEM E DO USUARIO SEM SENHA
    public void enviarMensagem(Mensagem msg, UsuarioLista usuarioOrigem, UsuarioLista usuarioDestino) throws SQLException {
        Statement st = conexao().createStatement();
        switch(msg.destino){
            case grupo:
                String SQL = "INSERT INTO mensagemgrupo (idUsuarioOrigem, idUsuarioDestino, idMensagem, txtMensagem, dataMensagem, horaMensagem) "
                + "VALUES ('" + usuarioOrigem.getIdUsuario() + "', '" + usuarioDestino.getIdUsuario()() + "', '" + ????() + "', '" + msg.textoMensagem + ')";
                break;
            case usuario:
                String SQL = "INSERT INTO mensagem (usuario, senha, foto) "
                + "VALUES ('" + u.getUsuario() + "', '" + u.getSenha() + "', '" + u.getFoto() + "')";
                break;
        }
        st.executeUpdate(SQL);
    }
}
