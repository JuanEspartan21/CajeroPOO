package Modelo;

public class Deposito extends Transaccion {

    @Override
    public void Accion(Cuenta cuenta, double dineroDepositado) {
        cuenta.setSaldoCuenta(cuenta.getSaldoCuenta() + dineroDepositado);
    }

}
