package Modelo;

/**
 *
 * @author juan-
 */
public class Persona {

    protected String nroIdentidad;
    protected String primerNombre;
    protected String primerApellido;
    protected String segundoApellido;

    public Persona() {

    }

    public Persona(String nroIdentidad, String primerNombre, String primerApellido, String segundoApellido) {
        this.nroIdentidad = nroIdentidad;
        this.primerNombre = primerNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
    }

    public String getNroIdentidad() {
        return nroIdentidad;
    }

    public void setNroIdentidad(String nroIdentidad) {
        this.nroIdentidad = nroIdentidad;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }
}
