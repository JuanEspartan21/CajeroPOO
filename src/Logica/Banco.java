package Logica;

import Modelo.Prestamo;
import Modelo.Solicitante;
import java.time.LocalDate;

/**
 *
 * @author juan-
 */
public class Banco {

    private double valorMaximoTotal = 10000000.00;  // Máximo valor total para préstamos en el banco
    private double valorTotalPrestamos = 0.0;     // Variable para llevar el control de los préstamos otorgados
    private Solicitante solicitante;
    private Prestamo prestamo;
    private ControlGeneral cg;

    public Banco(ControlGeneral cg) {
        this.cg = cg;
    }

    public void capturarPrestamo(String valorPrestamo, String nroPrestamo, LocalDate fechaAutorizacion) {
        Double vlPrestamo = Double.valueOf(valorPrestamo);
        if (vlPrestamo > 0) {
            if (vlPrestamo + valorTotalPrestamos <= valorMaximoTotal) {
                prestamo = new Prestamo(Integer.parseInt(nroPrestamo), solicitante, vlPrestamo, fechaAutorizacion);
                valorTotalPrestamos += vlPrestamo;
            } else {
                cg.mostrarJOption("El valor total del o los préstamos excede el límite máximo del banco.");
                return;
            }
        } else {
            cg.mostrarJOption("El valor del préstamo debe ser mayor que cero.");
            return;
        }

        mostrarDatosPrestamo();
    }

    public Solicitante capturarSolicitante(String telefonoCasa, String telefonoMovil, String nroIdentidad, String primerNombre, String primerApellido, String segundoApellido) {
        if (solicitante == null || !solicitante.getNroIdentidad().equals(nroIdentidad)) {
            return solicitante = new Solicitante(Integer.parseInt(telefonoCasa), Integer.parseInt(telefonoMovil), nroIdentidad, primerNombre, primerApellido, segundoApellido);
        }
//        if () {
//            return solicitante = new Solicitante(Integer.parseInt(telefonoCasa), Integer.parseInt(telefonoMovil), nroIdentidad, primerNombre, primerApellido, segundoApellido);
//        }
        return solicitante;
    }

    public String mostrarDatosSolicitante() {
        String st = "\nNombre: " + solicitante.getPrimerNombre() + " " + solicitante.getPrimerApellido() + " " + solicitante.getSegundoApellido()
                + "\nNro. de Identidad: " + solicitante.getNroIdentidad()
                + "\nTeléfono de Casa: " + solicitante.getTelefonoCasa()
                + "\nTeléfono Móvil: " + solicitante.getTelefonoMovil();
        return st;
    }

    public void mostrarDatosPrestamo() {
        String sb = "Préstamo Número: " + prestamo.getNroPrestamo()
                + mostrarDatosSolicitante()
                + "\nValor del Préstamo: " + prestamo.getValorPrestamo()
                + "\nFecha de Autorización: " + prestamo.getFechaAutorizacion()
                + "\nFecha Tentativa de Entrega: " + prestamo.getFechaEntrega()
                + "\nFechas de Pago de las Cuotas: " + mostrarFechasPago();

        cg.mostrarJOption(sb);
    }

    public String mostrarFechasPago() {
        String fp = "";
        for (LocalDate fecha : prestamo.getFechasPago()) {
            fp += ("\n" + fecha.toString());
        }
        return fp;
    }
}
