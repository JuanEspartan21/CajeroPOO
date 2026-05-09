package Modelo;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author juan-
 */
public class Prestamo {

    int nroPrestamo;
    Solicitante solicitante;
    double valorPrestamo;
    LocalDate fechaAutorizacion;
    LocalDate fechaEntrega;
    ArrayList<LocalDate> fechasPago = new ArrayList<>();

    public Prestamo(int nroPrestamo, Solicitante solicitante, double valorPrestamo, LocalDate fechaAutorizacion) throws IllegalArgumentException {

        this.nroPrestamo = nroPrestamo;
        this.solicitante = solicitante;
        this.valorPrestamo = valorPrestamo;
        this.fechaAutorizacion = fechaAutorizacion;

        // Calculamos la fecha tentativa de entrega del préstamo (7 días después de la autorización)
        this.fechaEntrega = fechaAutorizacion.plusDays(7);

        // Calculamos las fechas de pago de las cuotas, sumando 30 días a la fecha de entrega
        for (int i = 1; i <= 6; i++) {
            fechasPago.add(fechaEntrega.plusDays(30 * i));
        }

    }

    public int getNroPrestamo() {
        return nroPrestamo;
    }

    public void setNroPrestamo(int nroPrestamo) {
        this.nroPrestamo = nroPrestamo;
    }

    public Solicitante getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Solicitante solicitante) {
        this.solicitante = solicitante;
    }

    public double getValorPrestamo() {
        return valorPrestamo;
    }

    public void setValorPrestamo(double valorPrestamo) {
        this.valorPrestamo = valorPrestamo;
    }

    public LocalDate getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(LocalDate fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public ArrayList<LocalDate> getFechasPago() {
        return fechasPago;
    }

    public void setFechasPago(ArrayList<LocalDate> fechasPago) {
        this.fechasPago = fechasPago;
    }
    

}
