package cliente.frames;

import cliente.aplicacao.Principal;
import compartilhado.aplicacao.Foto;
import compartilhado.modelo.Grupo;
import compartilhado.modelo.Usuario;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;

public class FrameCriarGrupo extends javax.swing.JFrame {

    private ArrayList<JCheckBox> lista;
    
    public FrameCriarGrupo() {
        initComponents();
        //lblFoto.setIcon(Foto.redimensionarFoto(imagem, 50, false));
        carregarUsuarios();
    }
    
    private void addListeners(){
        btnOk.addActionListener((ActionEvent e) -> {
            if(txtNome.getText().isEmpty())
                JOptionPane.showMessageDialog(null, "Campo nome do grupo não pode ser vazio!", "Campo vazio", JOptionPane.ERROR_MESSAGE);
            int id;
            try {
                id = Principal.frmPrincipal.conexao.receberIdGrupoDisponivel();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Houve uma falha na comunicação com o servidor, tente novamente!!", "Falha na comunicação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ImageIcon foto = null;
            Grupo grupo = new Grupo(id, txtNome.getText(), identificarMembros(), foto);
            try {
                Principal.frmPrincipal.conexao.criarGrupo(grupo);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Houve uma falha na comunicação com o servidor, tente novamente!!", "Falha na comunicação", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Principal.grupos.add(grupo);
            Principal.frmPrincipal.carregarLista();
            dispose();
        });
        
        btnCancelar.addActionListener((ActionEvent e) -> {
            dispose();
        });
    }
    
    
    private int[] identificarMembros(){
        int[] membros = new int[10];
        int i = 0;
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
            if(usuario.getId() != Principal.frmPrincipal.conexao.getIdCliente()){
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected(false);
                checkBox.setText(usuario.getUsuario());
                pnlUsuarios.add(checkBox, layout);
                lista.add(checkBox);
                layout.gridy++;
            }
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

        lblPessoasGrupo.setText("Pessoas no grupo:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlUsuarios.add(lblPessoasGrupo, gridBagConstraints);

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
    private javax.swing.JPanel pnlUsuarios;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

}
