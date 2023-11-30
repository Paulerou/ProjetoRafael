public class Moto {
    private int idMoto;
    private String modelo, marca;
    private int anoFabricacao;

    public Moto(int idMoto, String modelo, String marca, int anoFabricacao) {
        this.idMoto = idMoto;
        this.modelo = modelo;
        this.marca = marca;
        this.anoFabricacao = anoFabricacao;
    }

    public int getIdMoto() {
        return idMoto;
    }

    public void setIdMoto(int idMoto) {
        this.idMoto = idMoto;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

}