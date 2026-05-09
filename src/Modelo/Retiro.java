package Modelo;

public class Retiro extends Transaccion {

    @Override
    public void Accion(Cuenta cuenta, double dineroRetirado) {
        
        cuenta.setSaldoCuenta(cuenta.getSaldoCuenta() - dineroRetirado);
        /*
        if (retiro <= getSaldo()) {
            transacciones = getSaldo();
            setSaldo(transacciones - retiro);
            JOptionPane.showMessageDialog(null, "----------------------"
                    + "Retiraste: " + retiro
                    + "Tu saldo actual es: " + getSaldo()
                    + "----------------------");
        } else {
            JOptionPane.showMessageDialog(null, "----------------------"
                    + "Saldo insuficientes."
                    + "----------------------");
        }
*/
    }
    
}
