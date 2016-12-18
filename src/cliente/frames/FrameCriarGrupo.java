package cliente.frames;

// IMPORTAÇÕES DO PROJETO
import cliente.aplicacao.Principal;
import compartilhado.modelo.Grupo;
import compartilhado.modelo.Usuario;
// IMPORTAÇÕES JAVA
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FrameCriarGrupo extends javax.swing.JFrame {

    private ArrayList<JCheckBox> lista;
    private int n;
    private ImageIcon foto;
    
    public FrameCriarGrupo() {
        initComponents();
        addListeners();
        carregarUsuarios();
        n = 0;
        foto = new ImageIcon(getClass().getResource("/compartilhado/imagens/grupo.png")); // cria uma ImageIcon com a foto padrão de usuário
        Image fotoRedimensionada = compartilhado.aplicacao.Foto.redimensionarFoto(foto.getImage(), 50, false); // redimensiona a imagem
        lblFoto.setIcon(new ImageIcon(fotoRedimensionada));
    }
    
    private void addListeners(){
        btnOk.addActionListener((ActionEvent e) -> {
            if(txtNome.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Campo nome do grupo não pode ser vazio!", "Campo vazio", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(quantidadeMembros() == 0){
                JOptionPane.showMessageDialog(null, "Selecione pelo menos 1 membro", "Poucos membros", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                if(Principal.frmPrincipal.conexao.comunicador.verificarNomeGrupo(txtNome.getText())){
                    JOptionPane.showMessageDialog(null, "Já existe um grupo com este nome, verifique e tente novamente!", "Nome já existente", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (RemoteException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Houve uma falha na comunicação com o servidor, tente novamente!", "Falha na comunicação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id;
            try {
                id = Principal.frmPrincipal.conexao.comunicador.recuperarIdDisponivelGrupo();
            } catch (RemoteException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Houve uma falha na comunicação com o servidor, tente novamente!", "Falha na comunicação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Grupo grupo = new Grupo(id, txtNome.getText(), identificarMembros(), foto);
            try {
                Principal.frmPrincipal.conexao.comunicador.criarGrupo(grupo);
            } catch (RemoteException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Houve uma falha na comunicação com o servidor, tente novamente!", "Falha na comunicação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Principal.grupos.add(grupo);
            Principal.frmPrincipal.carregarLista(true);
            JOptionPane.showMessageDialog(null, "O grupo foi criado, agora ele está disponível na sua lista!", "Grupo criado", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
        
        btnCancelar.addActionListener((ActionEvent e) -> {
            dispose();
        });
        
        btnAlterarFoto.addActionListener((ActionEvent e) -> {
            JFileChooser fs = new JFileChooser();
            fs.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int val = fs.showOpenDialog(this);
            if(val == JFileChooser.APPROVE_OPTION){
                File caminhoFoto = fs.getSelectedFile();
                foto = new ImageIcon(caminhoFoto.getPath());
                Image imagemRedimensionada = compartilhado.aplicacao.Foto.redimensionarFoto(foto.getImage(), 50, false);
                lblFoto.setIcon(new ImageIcon(imagemRedimensionada));
            }
        });
    }
    
    private int quantidadeMembros(){
        int n = 0;
        for (JCheckBox checkBox : lista) {
            if(checkBox.isSelected())
                n++;
        }
        return n;
    }
    
    private int[] identificarMembros(){
        int[] membros = new int[10];
        Arrays.fill(membros, 0);
        membros[0] = Principal.frmPrincipal.conexao.getCliente().getId();
        int i = 1;
        for (JCheckBox checkBox : lista) {
            if(checkBox.isSelected()){
                membros[i] = Principal.frmPrincipal.idPorNome(checkBox.getText());
                i++;
            }
        }
        return membros;
    }
    
    private void carregarUsuarios(){
        lista = new ArrayList<>();
        GridBagConstraints layout = new GridBagConstraints();
        layout.gridx = 0;
        layout.gridy = 1;
        layout.insets = new Insets(0, 5, 5, 0);
        layout.anchor = GridBagConstraints.NORTHWEST;
        for (Usuario usuario : Principal.usuarios) {
            if(usuario.getId() != Principal.frmPrincipal.conexao.getCliente().getId()){
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected(false);
                checkBox.setText(usuario.getUsuario());
                pnlMembros.add(checkBox, layout);
                lista.add(checkBox);
                layout.gridy++;
            }
        }
        adicionarListenersCheckBox();
    }
    
    private void adicionarListenersCheckBox(){
        for (JCheckBox checkBox : lista) {
            checkBox.addActionListener((ActionEvent e) -> {
                if(checkBox.isSelected()){
                    if(n < 9){
                        checkBox.setSelected(true);
                        n++;
                    }else
                        checkBox.setSelected(false);
                }else{
                    checkBox.setSelected(false);
                    n--;
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlHeader = new javax.swing.JPanel();
        txtNome = new javax.swing.JTextField();
        lblFoto = new javax.swing.JLabel();
        btnAlterarFoto = new javax.swing.JButton();
        lblNome = new javax.swing.JLabel();
        pnlUsuarios = new javax.swing.JPanel();
        lblPessoasGrupo = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        pnlMembros = new javax.swing.JPanel();
        pnlBottom = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Criar grupo");
        setMaximumSize(null);
        setMinimumSize(new java.awt.Dimension(375, 500));
        setName("frmCriarGrupo"); // NOI18N
        setPreferredSize(new java.awt.Dimension(375, 500));
        java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
        layout.rowWeights = new double[] {2.0, 8.0, 1.0};
        getContentPane().setLayout(layout);

        pnlHeader.setBorder(javax.swing.BorderFactory.createTitledBorder("Informações"));
        pnlHeader.setLayout(new java.awt.GridBagLayout());

        txtNome.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 15, 15);
        pnlHeader.add(txtNome, gridBagConstraints);

        lblFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblFoto.setMaximumSize(new java.awt.Dimension(50, 50));
        lblFoto.setMinimumSize(new java.awt.Dimension(50, 50));
        lblFoto.setPreferredSize(new java.awt.Dimension(50, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(15, 15, 5, 5);
        pnlHeader.add(lblFoto, gridBagConstraints);

        btnAlterarFoto.setText("Alterar foto...");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 15, 15, 5);
        pnlHeader.add(btnAlterarFoto, gridBagConstraints);

        lblNome.setText("Nome do grupo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 15);
        pnlHeader.add(lblNome, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(pnlHeader, gridBagConstraints);

        pnlUsuarios.setBorder(javax.swing.BorderFactory.createTitledBorder("Membros"));
        pnlUsuarios.setLayout(new java.awt.GridBagLayout());

        lblPessoasGrupo.setText("Selecione até 9 membros:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlUsuarios.add(lblPessoasGrupo, gridBagConstraints);

        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBar(null);

        pnlMembros.setLayout(new java.awt.GridBagLayout());
        scroll.setViewportView(pnlMembros);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlUsuarios.add(scroll, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(pnlUsuarios, gridBagConstraints);

        pnlBottom.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        pnlBottom.setLayout(new java.awt.GridBagLayout());

        btnOk.setText("OK");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlBottom.add(btnOk, gridBagConstraints);

        btnCancelar.setText("Cancelar");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlBottom.add(btnCancelar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(pnlBottom, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterarFoto;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblNome;
    private javax.swing.JLabel lblPessoasGrupo;
    private javax.swing.JPanel pnlBottom;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlMembros;
    private javax.swing.JPanel pnlUsuarios;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

}
