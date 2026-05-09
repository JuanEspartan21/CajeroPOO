package Modelo;

import javax.swing.JOptionPane;

public class Consulta {

    public void mostrarSaldo(Cuenta cuenta){
        
        JOptionPane.showMessageDialog(null, "El saldo actual de su cuenta es: " + cuenta.getSaldoCuenta());
    }

}
