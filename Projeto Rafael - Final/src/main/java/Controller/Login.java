package Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class Login {

    private static JFrame loginFrame;
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    private static JLabel feedbackLabel;
    private static JCheckBox showPasswordCheckBox;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::exibirTelaLogin);
    }

    private static void exibirTelaLogin() {
        loginFrame = new JFrame("Login - MotoSync");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 250);
        loginFrame.setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(0, 0, 10, 0);

        JLabel titleLabel = new JLabel("Bem-vindo ao MotoSync");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        loginPanel.add(titleLabel, constraints);

        constraints.gridy++;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.WEST;

        JLabel usernameLabel = new JLabel("Usuário:");
        loginPanel.add(usernameLabel, constraints);

        constraints.gridx++;
        usernameField = new JTextField(15);
        loginPanel.add(usernameField, constraints);

        constraints.gridy++;
        constraints.gridx = 0;

        JLabel passwordLabel = new JLabel("Senha:");
        loginPanel.add(passwordLabel, constraints);

        constraints.gridx++;
        passwordField = new JPasswordField(15);
        loginPanel.add(passwordField, constraints);

        constraints.gridy++;
        constraints.gridx = 0;

        showPasswordCheckBox = new JCheckBox("Mostrar Senha");
        showPasswordCheckBox.addActionListener(e -> toggleShowPassword());
        loginPanel.add(showPasswordCheckBox, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 2;

        feedbackLabel = new JLabel();
        feedbackLabel.setForeground(Color.RED);
        loginPanel.add(feedbackLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(10, 0, 0, 0);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> realizarLogin());
        loginPanel.add(loginButton, constraints);

        // Adiciona ação de teclado para o botão de login (pressionar Enter)
        loginButton.registerKeyboardAction(
                e -> realizarLogin(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        loginFrame.getContentPane().add(loginPanel, BorderLayout.CENTER);
        loginFrame.setVisible(true);
    }

    private static void toggleShowPassword() {
        passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? '\0' : '•');
    }

    private static int tentativasLogin = 0;
    private static final int MAX_TENTATIVAS = 3; // Número máximo de tentativas permitidas

    private static boolean isLoggedIn = false;

    private static void realizarLogin() {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();

        if (verificarCredenciais(username, password)) {
            isLoggedIn = true;
            loginFrame.dispose(); // Fecha tela de login

            SwingUtilities.invokeLater(() -> {
                try {
                    Main.criarEExibirGUI();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            tentativasLogin++;

            if (tentativasLogin >= MAX_TENTATIVAS) {
                // Número máximo de tentativas excedido
                feedbackLabel.setText("Número máximo de tentativas excedido. O aplicativo será fechado.");
                loginFrame.setEnabled(false); // Desabilita a tela de login

                // Pode adicionar uma lógica adicional aqui, como esperar por algum tempo antes de fechar o aplicativo.
                // Por exemplo, usando um Timer.

                Timer timer = new Timer(3000, e -> System.exit(0)); // Fecha o aplicativo após 3 segundos
                timer.setRepeats(false);
                timer.start();
            } else {
                feedbackLabel.setText("Credenciais inválidas. Tentativas restantes: " + (MAX_TENTATIVAS - tentativasLogin));
            }
        }
    }

    private static boolean verificarCredenciais(String username, char[] password) {
        return "teste".equals(username) && "123456".equals(String.valueOf(password));
    }
}
