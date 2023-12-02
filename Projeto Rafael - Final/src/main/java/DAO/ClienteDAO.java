package DAO;

import Controller.Main;
import Model.Cliente;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String password = "123456";



        public static List<Cliente> listClient() {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String query = "SELECT idcliente, nome, cpf, endereco, telefone FROM cliente";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                List<Cliente> lCLiente = new ArrayList<>();
                while (resultSet.next()) {
                    int idCliente = resultSet.getInt("idcliente");
                    String nome = resultSet.getString("nome");
                    String cpf = resultSet.getString("cpf");
                    String endereco = resultSet.getString("endereco");
                    String telefone = resultSet.getString("telefone");

                    Cliente cliente = new Cliente(idCliente, nome, cpf, endereco, telefone);
                    lCLiente.add(cliente);
                }
                return lCLiente;
            } catch (SQLException e) {
                System.out.println("error: " + e.getMessage());
                return null;
            }
        }

    public static void listarClientes() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT idcliente, nome, cpf, endereco, telefone FROM cliente";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                StringBuilder clientesStr = new StringBuilder("Clientes:\n");
                while (resultSet.next()) {
                    int idCliente = resultSet.getInt("idcliente");
                    String nome = resultSet.getString("nome");
                    String cpf = resultSet.getString("cpf");
                    String endereco = resultSet.getString("endereco");
                    String telefone = resultSet.getString("telefone");

                    Cliente cliente = new Cliente(idCliente, nome, cpf, endereco, telefone);
                    clientesStr.append("ID: ").append(cliente.getIdCliente())
                            .append(", Nome: ").append(cliente.getNome())
                            .append(", CPF: ").append(cliente.getCpf())
                            .append(", Endereço: ").append(cliente.getEndereco())
                            .append(", Telefone: ").append(cliente.getTelefone())
                            .append("\n");
                }
                JOptionPane.showMessageDialog(null, clientesStr.toString());
            }
        } catch (SQLException e) {
            Main.showError("Erro ao listar clientes.");
        }
    }

    public static void excluirCliente(int idCliente) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String deleteAluguelQuery = "DELETE FROM aluguel WHERE idCliente = ?";
            try (PreparedStatement deleteAluguelStatement = connection.prepareStatement(deleteAluguelQuery)) {
                deleteAluguelStatement.setInt(1, idCliente);
                deleteAluguelStatement.executeUpdate();
            }

            String deleteClienteQuery = "DELETE FROM cliente WHERE idcliente = ?";
            try (PreparedStatement deleteClienteStatement = connection.prepareStatement(deleteClienteQuery)) {
                deleteClienteStatement.setInt(1, idCliente);

                int rowsAffected = deleteClienteStatement.executeUpdate();

                if (rowsAffected > 0) {
                    Main.showMessage("Cliente excluído com sucesso!");
                } else {
                    Main.showError("Erro ao excluir o cliente. O cliente pode não existir ou estar vinculado a outros registros.");
                }
            }
        } catch (SQLException e) {
            Main.showError("Erro ao excluir o cliente: " + e.getMessage());
        }
    }

    public static class CadastroClientePanel {

            public final JFrame addClientFrame;
            private final JTextField nomeField;
            private final JTextField cpfField;
            private final JTextField enderecoField;
            private final JTextField telefoneField;

            public CadastroClientePanel() {
                addClientFrame = new JFrame("Cadastrar Cliente");
                addClientFrame.setSize(400, 300);
                addClientFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                addClientFrame.setLocationRelativeTo(null);

                JPanel addClientPanel = new JPanel(new GridLayout(5, 2, 10, 10));
                addClientPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                nomeField = new JTextField();
                cpfField = new JTextField();
                enderecoField = new JTextField();
                telefoneField = new JTextField();

                addClientPanel.add(new JLabel("Nome:"));
                addClientPanel.add(nomeField);
                addClientPanel.add(new JLabel("CPF:"));
                addClientPanel.add(cpfField);
                addClientPanel.add(new JLabel("Endereço:"));
                addClientPanel.add(enderecoField);
                addClientPanel.add(new JLabel("Telefone:"));
                addClientPanel.add(telefoneField);

                JButton cadastrarButton = new JButton("Cadastrar");
                cadastrarButton.addActionListener(e -> cadastrarClienteAcao());

                addClientPanel.add(cadastrarButton);

                addClientFrame.getContentPane().add(addClientPanel, BorderLayout.CENTER);
            }

        private void cadastrarClienteAcao() {
            String nome = nomeField.getText();
            String cpf = cpfField.getText();
            String endereco = enderecoField.getText();
            String telefone = telefoneField.getText();

            // Verifica se todos os campos estão preenchidos
            if (!nome.isEmpty() && !cpf.isEmpty() && !endereco.isEmpty() && !telefone.isEmpty()) {
                // Verifica se o telefone possui exatamente 11 dígitos numéricos
                if (telefone.matches("\\d{11}")) {
                    inserirCliente(nome, cpf, endereco, telefone);
                    addClientFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "O telefone deve conter exatamente 11 dígitos numéricos.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos para cadastrar o cliente.");
            }
        }

            private static void inserirCliente(String nome, String cpf, String endereco, String telefone) {
                try (Connection connection = DriverManager.getConnection(url, user, password)) {
                    String insertQuery = "INSERT INTO cliente (nome, cpf, endereco, telefone) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, nome);
                        preparedStatement.setString(2, cpf);
                        preparedStatement.setString(3, endereco);
                        preparedStatement.setString(4, telefone);

                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            Main.showMessage("Cliente cadastrado com sucesso!");
                        } else {
                            Main. showError("Erro ao cadastrar o cliente. Tente novamente.");
                        }
                    }
                } catch (SQLException e) {
                    Main.showError("Erro ao cadastrar o cliente.");
                }
            }

            public static void atualizarCliente(int idCliente, String nome, String cpf, String endereco, String telefone) {
                try (Connection connection = DriverManager.getConnection(url, user, password)) {
                    String updateQuery = "UPDATE cliente SET nome = ?, cpf = ?, endereco = ?, telefone = ? WHERE idcliente = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                        preparedStatement.setString(1, nome);
                        preparedStatement.setString(2, cpf);
                        preparedStatement.setString(3, endereco);
                        preparedStatement.setString(4, telefone);
                        preparedStatement.setInt(5, idCliente);

                        int rowsAffected = preparedStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            Main.showMessage("Cliente atualizado com sucesso!");
                        } else {
                            Main.showError("Erro ao atualizar o cliente. Tente novamente.");
                        }
                    }
                } catch (SQLException e) {
                    Main.showError("Erro ao atualizar o cliente.");
                }
            }
        }
    }
