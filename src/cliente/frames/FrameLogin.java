package cliente.frames;

import cliente.aplicacao.*;
import compartilhado.modelo.Usuario;
import compartilhado.modelo.UsuarioAutenticacao;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class FrameLogin extends javax.swing.JFrame {
    
    private ConexaoCliente conexao;
    
    public FrameLogin() {
        initComponents();
        addListeners();
    }

    private void addListeners(){
        btnLogin.addActionListener((ActionEvent e) -> {
            autenticarUsuario(false);
        });
        
        btnCadastrar.addActionListener((ActionEvent e) -> {
            autenticarUsuario(true);
        });
    }
    
    private void autenticarUsuario(boolean cadastro){
        if(verificarCampos()){ // se os campos estiverem preenchidos, continua
            lblStatus.setText("Criando conexão...");
            conexao = new ConexaoCliente(txtEndereco.getText(), Integer.parseInt(txtPorta.getText())); // cria a conexão
            conexao.setCliente(new Usuario(-1, txtUsuario.getText(), null)); // cliente temporário
            try {
                lblStatus.setText("Conectando ao servidor...");
                conexao.conectar(); // conecta com o servidor
                lblStatus.setText(cadastro? "Cadastrando usuário..." : "Autenticando usuário...");
                int status;
                UsuarioAutenticacao autenticacao = new UsuarioAutenticacao(txtUsuario.getText(), new String(txtSenha.getPassword()));
                if(cadastro)
                    status = conexao.comunicadorServidor.cadastrarUsuario(autenticacao); // caso cadastro, cadastra no servidor através do comunicador
                else
                    status = conexao.comunicadorServidor.autenticarUsuario(autenticacao); // caso autenticação, autentica no servidor através do comunicador
                switch(status){
                    case -1: // erro genérico
                        JOptionPane.showMessageDialog(null, "Um erro ocorreu, verifique e tente novamente", 
                                "Falha n" + (cadastro? "o cadastro" : "a autenticação"), JOptionPane.ERROR_MESSAGE);
                        break;                        
                    case 0: // cadastro: usuário já existe / autenticação: usuario não existe
                        if(cadastro)
                            JOptionPane.showMessageDialog(null, "Já existe uma pessoa cadastrada com esse nome de usuário, escolha outro nome", 
                                "Falha no cadastro", JOptionPane.ERROR_MESSAGE);
                        else
                            JOptionPane.showMessageDialog(null, "Usuário não encontrado na base de dados, verifique e tente novamente", 
                                "Falha na autenticação", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 1: // usuário já está online (autenticação)
                        if(!cadastro)
                            JOptionPane.showMessageDialog(null, "A senha está incorreta, verifique e tente novamente", 
                                    "Falha na autenticação", JOptionPane.ERROR_MESSAGE);
                        else
                            JOptionPane.showMessageDialog(null, "O cadastro foi efetuado, agora você pode entrar", 
                                    "Cadastro efetuado", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case 2: // senha incorreta
                        JOptionPane.showMessageDialog(null, "Já existe uma pessoa cadastrada com esse nome de usuário, escolha outro nome", 
                                "Falha no cadastro", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 3: // autenticação funcionou
                        if(conexao.getStatus()){ // se a conexão estiver funcionando, vai para o Frame Principal
                            conexao.setCliente(conexao.comunicadorServidor.getUsuarioPorNome(conexao.getCliente().getUsuario()));
                            Principal.frmPrincipal = new FramePrincipal(conexao);
                            Principal.frmPrincipal.setVisible(true);
                            dispose();
                        }else
                            JOptionPane.showMessageDialog(null, "Houve um erro na conexão, tente novamente", "Falha na conexão", JOptionPane.ERROR_MESSAGE);
                        break;
                }
                if(status != 3)
                    lblStatus.setText("Esperando conexão");
            } catch (IOException | NotBoundException ex){
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Exceção: " + ex.getMessage(), "Erro na conexão", JOptionPane.ERROR_MESSAGE);;
            }
        }
    }
    
    private boolean verificarCampos(){ // verifica se todos os campos foram preenchidos
        if(txtEndereco.getText().isEmpty() || txtPorta.getText().isEmpty() ||
           txtUsuario.getText().isEmpty() || new String(txtSenha.getPassword()).isEmpty()){
            JOptionPane.showMessageDialog(null, "Preencha todos os campos e tente novamente", "Campos em branco", JOptionPane.ERROR_MESSAGE);
            return false;
        }else{
            return true;
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblLogin = new javax.swing.JLabel();
        lblInfo = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        lblSenha = new javax.swing.JLabel();
        txtSenha = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JToggleButton();
        btnCadastrar = new javax.swing.JButton();
        lblEndereco = new javax.swing.JLabel();
        txtEndereco = new javax.swing.JTextField();
        lblPorta = new javax.swing.JLabel();
        txtPorta = new javax.swing.JTextField();
        lblStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login - Mensageiro");
        setMinimumSize(new java.awt.Dimension(450, 450));
        setName("frmLogin"); // NOI18N
        setPreferredSize(new java.awt.Dimension(450, 450));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        lblLogin.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblLogin.setText("Login - Mensageiro");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 5, 5);
        getContentPane().add(lblLogin, gridBagConstraints);

        lblInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfo.setText("<html>\n<center>\nDigite o endereço e porta do servidor,<br>\nalém de seu usuário e senha para acessar o mensageiro\n</center>\n</html>");
        lblInfo.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 20, 5);
        getContentPane().add(lblInfo, gridBagConstraints);

        lblUsuario.setText("Usuário");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 5, 5);
        getContentPane().add(lblUsuario, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 20);
        getContentPane().add(txtUsuario, gridBagConstraints);

        lblSenha.setText("Senha");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 20, 5);
        getContentPane().add(lblSenha, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 20, 20);
        getContentPane().add(txtSenha, gridBagConstraints);

        btnLogin.setText("Login");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(btnLogin, gridBagConstraints);

        btnCadastrar.setText("Cadastrar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 20, 5);
        getContentPane().add(btnCadastrar, gridBagConstraints);

        lblEndereco.setText("Endereço");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 5, 5);
        getContentPane().add(lblEndereco, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 150;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(txtEndereco, gridBagConstraints);

        lblPorta.setText(":");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(lblPorta, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 20);
        getContentPane().add(txtPorta, gridBagConstraints);

        lblStatus.setText("Esperando conexão");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 20, 5);
        getContentPane().add(lblStatus, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCadastrar;
    private javax.swing.JToggleButton btnLogin;
    private javax.swing.JLabel lblEndereco;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblLogin;
    private javax.swing.JLabel lblPorta;
    private javax.swing.JLabel lblSenha;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JTextField txtEndereco;
    private javax.swing.JTextField txtPorta;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

}
