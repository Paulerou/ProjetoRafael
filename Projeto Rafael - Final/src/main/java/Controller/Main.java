package Controller;

import DAO.AluguelDAO;
import DAO.ClienteDAO;
import DAO.MotoDAO;


import Model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;

public class Main {
    private static Date dataInicio;
    private static Date dataFim;
    private static double valor;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        });
    }

    static void criarEExibirGUI() throws SQLException {
        JFrame frame = new JFrame("MotoSync");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10)); // Ajustado para incluir o novo botão
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton listClientsButton = criarBotao("Listar Clientes", "Visualizar lista de clientes", e -> ClienteDAO.listarClientes());
        JButton addClientButton = criarBotao("Cadastrar Cliente", "Cadastrar um novo cliente", e -> new ClienteDAO.CadastroClientePanel().addClientFrame.setVisible(true));
        JButton updateClientButton = criarBotao("Atualizar Cliente", "Atualizar um cliente existente", e -> {
            Cliente clienteSelecionado = AluguelDAO.selectClient(ClienteDAO.listClient());
            if (clienteSelecionado != null) {
                new ClienteDAO.CadastroClientePanel().atualizarCliente(
                        clienteSelecionado.getIdCliente(),
                        "Novo Nome",
                        "Novo CPF",
                        "Novo Endereço",
                        "Novo Telefone"
                );
            }
        });
        JButton deleteClientButton = criarBotao("Excluir Cliente", "Excluir um cliente existente", e -> {
            Cliente clienteSelecionado = AluguelDAO.selectClient(ClienteDAO.listClient());
            if (clienteSelecionado != null) {
                ClienteDAO.excluirCliente(clienteSelecionado.getIdCliente());
            }
        });

        JButton listAvailableMotosButton = criarBotao("Listar Motos Disponíveis", "Visualizar motos disponíveis", e -> MotoDAO.listarMotosDisponiveis());
        JButton rentMotoButton = criarBotao("Alugar Moto", "Alugar uma moto", e -> AluguelDAO.alugarMoto());
        JButton viewRentalsButton = criarBotao("Visualizar Aluguéis", "Visualizar lista de aluguéis", e -> AluguelDAO.visualizarAlugueis());
        JButton addMotoButton = criarBotao("Adicionar Moto", "Adicionar uma nova moto", e -> MotoDAO.adicionarMoto());
        JButton removeMotoButton = criarBotao("Remover Moto", "Remover uma moto existente", e -> MotoDAO.removerMotoPorId( /* parâmetros necessários */ ));
        JButton cancelRentButton = criarBotao("Cancelar Aluguel", "Cancelar um aluguel existente", e -> AluguelDAO.cancelarAluguel( /* parâmetros necessários */ ));


        adicionarIcones(listClientsButton, addClientButton, updateClientButton, deleteClientButton, listAvailableMotosButton, rentMotoButton, viewRentalsButton);

        panel.add(listClientsButton);
        panel.add(addClientButton);
        panel.add(addMotoButton);
        panel.add(updateClientButton);
        panel.add(deleteClientButton);
        panel.add(listAvailableMotosButton);
        panel.add(rentMotoButton);
        panel.add(viewRentalsButton);
        panel.add(removeMotoButton);
        panel.add(cancelRentButton);

        JScrollPane scrollPane = new JScrollPane(panel);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    private static void adicionarIcones(JButton... ignoredButtons) {
        // Adicione ícones aos botões aqui, se necessário
    }
    private static JButton criarBotao(String texto, String tooltip, ActionListener listener) {
        JButton botao = new JButton(texto);
        botao.setToolTipText(tooltip);
        botao.addActionListener(listener);
        botao.setFont(new Font("Arial", Font.BOLD, 18));
        botao.setFocusPainted(false);
        return botao;
    }


    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, "Erro: " + message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}



