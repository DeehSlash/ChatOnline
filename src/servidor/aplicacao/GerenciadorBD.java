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
    
    public void cadastrarUsuario(Usuario u) throws SQLException{
        Statement st = conexao().createStatement();
        String SQL = "INSERT INTO usuario (usuario, senha, foto) "
                + "VALUES ('" + u.getUsuario() + "', '" + u.getSenha() + "', '" + u.getFoto() + "')";
        st.executeUpdate(SQL);

    }
    
    public ArrayList getUsuarios() throws SQLException, IOException{
        Statement st = conexao().createStatement();
        String SQL = "SELECT * FROM usuarios ORDER by usuario";
        ResultSet rs = st.executeQuery(SQL);
        ArrayList<Usuario> usuarios = new ArrayList<>();
        while(rs.next()){
            int id = rs.getInt("id");
            String usuario = rs.getString("usuario");
            String senha = rs.getString("senha");
            Blob blob = rs.getBlob("foto");
            InputStream is = blob.getBinaryStream();
            Image image = ImageIO.read(is);
            ImageIcon flag = new ImageIcon(image);
            usuarios.add(new Usuario(id, usuario, senha, flag));
        }
        return usuarios;
    }
}
