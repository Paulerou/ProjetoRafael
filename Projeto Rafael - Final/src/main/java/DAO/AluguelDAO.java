package DAO;

import Controller.Main;
import Model.Cliente;
import Model.Moto;

import javax.swing.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AluguelDAO {
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";
    private static final String user = "postgres";
    private static final String password = "123456";

    public static void alugarMoto() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Permitindo ao usuário escolher o cliente
            List<Cliente> clientes = ClienteDAO.listClient();
            Cliente clienteSelecionado = selectClient(clientes);

            if (clienteSelecionado == null) {
                Main.showError("Operação de aluguel cancelada. Cliente não selecionado.");
                return;
            }

            // Permitindo ao usuário escolher a moto
            List<Moto> motos = MotoDAO.listMotos();
            Moto motoSelecionada = MotoDAO.selectMoto(motos);

            if (motoSelecionada == null) {
                Main.showError("Operação de aluguel cancelada. Moto não selecionada.");
                return;
            }

            // Permitindo ao usuário escolher a data de início
            String dataInicioStr = JOptionPane.showInputDialog("Digite a data de início (Formato: yyyy-MM-dd HH:mm):");
            Date dataInicio = parseDate(dataInicioStr);

            // Permitindo ao usuário escolher a data de fim
            String dataFimStr = JOptionPane.showInputDialog("Digite a data de fim (Formato: yyyy-MM-dd HH:mm):");
            Date dataFim = parseDate(dataFimStr);

            double valor = 100.0; // Valor

            String insertQuery = "INSERT INTO aluguel (dataInicio, dataFim, valor, idMoto, idCliente) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setTimestamp(1, new java.sql.Timestamp(dataInicio.getTime()));
                preparedStatement.setTimestamp(2, new java.sql.Timestamp(dataFim.getTime()));
                preparedStatement.setDouble(3, valor);
                preparedStatement.setInt(4, motoSelecionada.getIdMoto());
                preparedStatement.setInt(5, clienteSelecionado.getIdCliente());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // Recuperar o ID gerado
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int idAluguel = generatedKeys.getInt(1);
                            Main.showMessage("Moto alugada com sucesso. Seu código de aluguel é " + idAluguel);
                        } else {
                            Main.showError("Erro ao obter o ID do aluguel. Tente novamente.");
                        }
                    }
                } else {
                    Main.showError("Erro ao alugar a moto. Tente novamente.");
                }
            }
        } catch (SQLException e) {
            Main.showError("Erro ao alugar a moto: " + e.getMessage());
        }
    }

    public static Cliente selectClient(List<Cliente> clientes) {
        DefaultListModel<String> clientesListModel = new DefaultListModel<>();
        JList<String> clientesList = new JList<>(clientesListModel);
        clientesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        for (int i = 0; i < clientes.size(); i++) {
            clientesListModel.addElement("ID: " + clientes.get(i).getIdCliente() +
                    ", Nome: " + clientes.get(i).getNome() +
                    ", CPF: " + clientes.get(i).getCpf() +
                    ", Endereço: " + clientes.get(i).getEndereco() +
                    ", Telefone: " + clientes.get(i).getTelefone());
        }

        JScrollPane scrollPane = new JScrollPane(clientesList);

        // Adiciona botões de opção
        Object[] options = {"Selecionar", "Cancelar"};
        int result = JOptionPane.showOptionDialog(null, scrollPane, "Clientes", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (result == 0) { // 0 indica que o usuário clicou em "Selecionar"
            int selectedIndex = clientesList.getSelectedIndex();

            if (selectedIndex != -1) {
                return clientes.get(selectedIndex);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            Main.showError("Formato de data inválido. Use o formato yyyy-MM-dd HH:mm");
            throw new RuntimeException("Erro ao converter data.", e);
        }
    }

    public static void cancelarAluguel() {
        int idAluguel = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do aluguel a ser cancelado:"));
        removerAluguel(idAluguel);
    }

    public static void visualizarAlugueis() {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT idAluguel, dataInicio, dataFim, valor, idMoto, idCliente FROM aluguel";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                StringBuilder alugueisStr = new StringBuilder("Aluguéis:\n");
                while (resultSet.next()) {
                    int idAluguel = resultSet.getInt("idAluguel");
                    Date dataInicio = resultSet.getDate("dataInicio");
                    Date dataFim = resultSet.getDate("dataFim");
                    double valor = resultSet.getDouble("valor");
                    int idMoto = resultSet.getInt("idMoto");
                    int idCliente = resultSet.getInt("idCliente");

                    alugueisStr.append("ID Aluguel: ").append(idAluguel)
                            .append(", Data Início: ").append(dataInicio)
                            .append(", Data Fim: ").append(dataFim)
                            .append(", Valor: ").append(valor)
                            .append(", ID Moto: ").append(idMoto)
                            .append(", ID Cliente: ").append(idCliente)
                            .append("\n");
                }
                JOptionPane.showMessageDialog(null, alugueisStr.toString());
            }
        } catch (SQLException e) {
            Main.showError("Erro ao visualizar aluguéis.");
        }
    }

    private static void removerAluguel(int idAluguel) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Verificar se o aluguel existe
            String selectQuery = "SELECT * FROM aluguel WHERE idAluguel = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setInt(1, idAluguel);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        Main.showError("Aluguel não encontrado. Verifique o ID do aluguel.");
                        return;
                    }
                }
            }

            // Remover o aluguel
            String deleteQuery = "DELETE FROM aluguel WHERE idAluguel = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                deleteStatement.setInt(1, idAluguel);
                int rowsAffected = deleteStatement.executeUpdate();

                if (rowsAffected > 0) {
                    Main.showMessage("Aluguel removido com sucesso!");
                } else {
                    Main.showError("Erro ao remover o aluguel. Tente novamente.");
                }
            }
        } catch (SQLException e) {
            Main.showError("Erro ao remover o aluguel: " + e.getMessage());
        }
    }
}
