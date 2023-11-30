import java.util.Date;

public class Aluguel {
    private int idAluguel;
    private Date dataInicio;
    private Date dataFim;
    private double valor;
    private int idMoto;
    private int idCliente;

    public Aluguel() {
    }

    public Aluguel(int idAluguel, Date dataInicio, Date dataFim, double valor, int idMoto, int idCliente) {
        this.idAluguel = idAluguel;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valor = valor;
        this.idMoto = idMoto;
        this.idCliente = idCliente;
    }

    public int getIdAluguel() {
        return idAluguel;
    }

    public void setIdAluguel(int idAluguel) {
        this.idAluguel = idAluguel;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getIdMoto() {
        return idMoto;
    }

    public void setIdMoto(int idMoto) {
        this.idMoto = idMoto;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
