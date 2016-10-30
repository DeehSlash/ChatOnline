package compartilhado.modelo;

public class UsuarioAutenticacao implements java.io.Serializable{
    
    private String usuario;
    private String senha;
    
    public UsuarioAutenticacao(String usuario, String senha){
        this.usuario = usuario;
        this.senha = senha;
    }
    
    public String getUsuario(){ return usuario; }
    public String getSenha(){ return senha; }
}
