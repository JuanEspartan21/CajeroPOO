
package Modelo;

public class Cuenta {
    
    private int claveCuenta;
    private double saldoCuenta;

    public Cuenta(int claveCuenta, double saldoCuenta) {
        this.claveCuenta = claveCuenta;
        this.saldoCuenta = saldoCuenta;
    }

    public Cuenta() {
    }

    public int getClaveCuenta() {
        return claveCuenta;
    }

    public void setClaveCuenta(int claveCuenta) {
        this.claveCuenta = claveCuenta;
    }

    public double getSaldoCuenta() {
        return saldoCuenta;
    }

    public void setSaldoCuenta(double saldoCuenta) {
        this.saldoCuenta = saldoCuenta;
    }
    
}
