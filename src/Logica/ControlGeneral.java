package Logica;

import Modelo.*;
import Vista.Bienvenida;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public final class ControlGeneral {

    private Cuenta cliente;
    private Deposito deposito;
    private Consulta consulta;
    private Retiro retiro;
    private Banco banco;

    public ControlGeneral() {
        cliente = new Cuenta(1234, 5000000);
        banco = new Banco(this);
        abrirBienvenida();
        deposito = new Deposito();
        consulta = new Consulta();
        retiro = new Retiro();
    }

    public void depositar(double dinero) {
        deposito.Accion(cliente, dinero);
        ResultadoOperacion(true);
    }

    public void retirar(double dinero) {
        if (dinero <= cliente.getSaldoCuenta()) {
            retiro.Accion(cliente, dinero);
            ResultadoOperacion(true);
        } else {
            ResultadoOperacion(false);
        }
    }

    public void consultar() {
        consulta.mostrarSaldo(cliente);
    }
    
    public void cambiarClave(int claveNueva){
        cliente.setClaveCuenta(claveNueva);
        JOptionPane.showMessageDialog(null,"!Clave nueva registrada con exito!");
    }

    public void ResultadoOperacion(boolean resultado) {
        if (resultado == true) {
            JOptionPane.showMessageDialog(null, "¡Transaccion Exitosa!\n Saldo total: " + cliente.getSaldoCuenta());
        } else {
            JOptionPane.showMessageDialog(null, "¡Transaccion Fallida!");
        }
    }

    public boolean validarClave(int clave) {
        if (cliente.getClaveCuenta() != clave) {
            JOptionPane.showMessageDialog(null, "Clave Incorrecta");
            return false;
        }
        JOptionPane.showMessageDialog(null, "Bienvenido");
        return true;
    }

    public void mostrarMensajeValor0() {
        JOptionPane.showMessageDialog(null, """
                                            \u00a1No puede realizarce la transaccion con valor $0!
                                            \t Intente nuevamente""");
    }
    
    public void abrirBienvenida(){
        new Bienvenida(this).setVisible(true);
    }
    
    public void enviarSolicitanteBanco(String telefonoCasa , String telefonoMovil, String nroIdentidad, String primerNombre, String primerApellido, String segundoApellido){
        banco.capturarSolicitante(telefonoCasa, telefonoMovil, nroIdentidad, primerNombre, primerApellido, segundoApellido);
    }
    
    public void enviarPrestamoBanco(String valorPrestamo, String numeroPrestamo, LocalDate fechaAutorizacion){
        banco.capturarPrestamo(valorPrestamo, numeroPrestamo, fechaAutorizacion);
    }
    
    public void mostrarJOption(String mensaje){
        JOptionPane.showMessageDialog(null, mensaje);
    }
}
