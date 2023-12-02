package DAO;

import Controller.Main;
import Model.Moto;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MotoDAO {

    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String password = "123456";

    public static void removerMotoPorId() {
        JFrame frame = new JFrame("Remover Moto por ID");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel idLabel = new JLabel("ID da Moto:");
        JTextField idField = new JTextField();

        JButton removerButton = new JButton("Remover");
        removerButton.addActionListener(e -> {
            try {
                int idMoto = Integer.parseInt(idField.getText());

                // Chama o método para remover a moto pelo ID
                removerMotoDoBanco(idMoto);

                JOptionPane.showMessageDialog(null, "Moto removida com sucesso!");
                frame.dispose();
            } catch (NumberFormatException | SQLException ex) {
                Main.showError("Erro ao remover moto: " + ex.getMessage());
            }
        });

        panel.add(idLabel);
        panel.add(idField);
        panel.add(removerButton);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    private static void adicionarMotoAoBanco(Moto moto) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO moto (idMoto, modelo, marca, ano) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, moto.getIdMoto());
                preparedStatement.setString(2, moto.getModelo());
                preparedStatement.setString(3, moto.getMarca());
                preparedStatement.setInt(4, moto.getAnoFabricacao());
                preparedStatement.executeUpdate();
            }
        }
    }
    public static void adicionarMoto() {
        JFrame motoFrame = new JFrame("Adicionar Moto");
        motoFrame.setSize(400, 300);
        motoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        motoFrame.setLocationRelativeTo(null);

        JPanel motoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        motoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel modeloLabel = new JLabel("Modelo:");
        JTextField modeloField = new JTextField();
        JLabel marcaLabel = new JLabel("Marca:");
        JTextField marcaField = new JTextField();
        JLabel anoFabricacaoLabel = new JLabel("Ano de Fabricação:");
        JTextField anoFabricacaoField = new JTextField();

        JButton salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(e -> {
            try {
                String modelo = modeloField.getText();
                String marca = marcaField.getText();
                int anoFabricacao = Integer.parseInt(anoFabricacaoField.getText());

                // Usar a variável global como ID
                int idGlobal = 0;
                int novoId = idGlobal++;

                Moto moto = new Moto(novoId, modelo, marca, anoFabricacao);

                adicionarMotoAoBanco(moto);

                JOptionPane.showMessageDialog(null, "Moto adicionada com sucesso!");
                motoFrame.dispose();
            } catch (NumberFormatException | SQLException ex) {
                Main.showError("Erro ao adicionar moto: " + ex.getMessage());
            }
        });

        motoPanel.add(modeloLabel);
        motoPanel.add(modeloField);
        motoPanel.add(marcaLabel);
        motoPanel.add(marcaField);
        motoPanel.add(anoFabricacaoLabel);
        motoPanel.add(anoFabricacaoField);
        motoPanel.add(salvarButton);

        motoFrame.getContentPane().add(motoPanel, BorderLayout.CENTER);
        motoFrame.setVisible(true);
    }

    static Moto selectMoto(List<Moto> motos) {
        DefaultListModel<String> motosListModel = new DefaultListModel<>();
        JList<String> motosList = new JList<>(motosListModel);
        motosList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        for (int i = 0; i < motos.size(); i++) {
            motosListModel.addElement("ID: " + motos.get(i).getIdMoto() +
                    ", Marca: " + motos.get(i).getMarca() +
                    ", Modelo: " + motos.get(i).getModelo());
        }

        JScrollPane scrollPane = new JScrollPane(motosList);

        // Exibe a lista de motos
        JOptionPane.showConfirmDialog(null, scrollPane, "Motos", JOptionPane.DEFAULT_OPTION);

        int selectedIndex = motosList.getSelectedIndex();
        return motos.get(motosList.getSelectedIndex());
    }
    static List<Moto> listMotos() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT idMoto, marca, modelo, ano FROM moto";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Moto> motos = new ArrayList<>();
            while (resultSet.next()) {
                int idMoto = resultSet.getInt("idMoto");
                String marca = resultSet.getString("marca");
                String modelo = resultSet.getString("modelo");
                int anoFabricacao = resultSet.getInt("ano");

                Moto moto = new Moto(idMoto, marca, modelo, anoFabricacao);
                motos.add(moto);
            }
            return motos;
        } catch (SQLException e) {
            System.out.println("erro: " + e.getMessage());
            return null;
        }
    }
    public static void listarMotosDisponiveis() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT idMoto, modelo, ano FROM moto";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                StringBuilder motosStr = new StringBuilder("Motos Disponíveis:\n");
                while (resultSet.next()) {
                    int idMoto = resultSet.getInt("idMoto");
                    String modelo = resultSet.getString("modelo");
                    int ano = resultSet.getInt("ano");

                    motosStr.append("ID: ").append(idMoto)
                            .append(", Modelo: ").append(modelo)
                            .append(", Ano: ").append(ano)
                            .append("\n");
                }
                JOptionPane.showMessageDialog(null, motosStr.toString());
            }
        } catch (SQLException e) {
            Main.showError("Erro ao listar motos disponíveis: " + e.getMessage());
        }
    }
    private static void removerMotoDoBanco(int idMoto) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM moto WHERE idMoto = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, idMoto);
                preparedStatement.executeUpdate();
            }
        }
    }
}
