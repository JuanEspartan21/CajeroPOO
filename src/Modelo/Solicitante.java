package Modelo;

public class Solicitante extends Persona {

    private int telefonoCasa;
    private int telefonoMovil;

    public Solicitante() {

    }

    public Solicitante(int telefonoCasa, int telefonoMovil, String nroIdentidad, String primerNombre, String primerApellido, String segundoApellido) {
        super(nroIdentidad, primerNombre, primerApellido, segundoApellido);
        this.telefonoCasa = telefonoCasa;
        this.telefonoMovil = telefonoMovil;
    }

    public int getTelefonoCasa() {
        return telefonoCasa;
    }

    public void setTelefonoCasa(int telefonoCasa) {
        this.telefonoCasa = telefonoCasa;
    }

    public int getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(int telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    @Override
    public String getNroIdentidad() {
        return nroIdentidad;
    }

    @Override
    public void setNroIdentidad(String nroIdentidad) {
        this.nroIdentidad = nroIdentidad;
    }

    @Override
    public String getPrimerNombre() {
        return primerNombre;
    }

    @Override
    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    @Override
    public String getPrimerApellido() {
        return primerApellido;
    }

    @Override
    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    @Override
    public String getSegundoApellido() {
        return segundoApellido;
    }

    @Override
    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }
}
