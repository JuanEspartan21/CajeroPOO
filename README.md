# CajeroPOO — Análisis estructural 
---
## Aurotes: 
Jonathan Esteban Cruz Fuentes - 20231020098
Juan Steban Valbuena Nuncira - 20232020340
---

> Proyecto académico: Cajero automático (ATM) desarrollado en Java con interfaz Swing.  
> Librerías externas requeridas: **JCalendar** y **AbsoluteLayout**.

---

## Descripción del Proyecto

**CajeroPOO** es una simulación de cajero automático bancario que permite realizar operaciones básicas como depósitos, retiros, consulta de saldo, cambio de clave y solicitud de préstamos. El sistema está construido bajo una arquitectura de tres capas: `Vista` (interfaz gráfica Swing), `Logica` (controladores) y `Modelo` (entidades de negocio).

---

## Estructura del Proyecto

```
src/
├── Logica/
│   ├── Banco.java          # Gestión de préstamos y solicitantes
│   ├── ControlGeneral.java # Controlador principal del sistema
│   └── Launcher.java       # Punto de entrada de la aplicación
├── Modelo/
│   ├── Consulta.java       # Acción de consulta de saldo
│   ├── Cuenta.java         # Entidad cuenta bancaria
│   ├── Deposito.java       # Acción de depósito
│   ├── Persona.java        # Clase base para personas
│   ├── Prestamo.java       # Entidad préstamo
│   ├── Retiro.java         # Acción de retiro
│   ├── Solicitante.java    # Entidad solicitante de préstamo
│   └── Transaccion.java    # Clase abstracta de transacciones
└── Vista/
    ├── Bienvenida.java     # Pantalla de bienvenida
    ├── FormularioPrestamo.java  # Formulario de solicitud de préstamo
    ├── MenuCajero.java     # Menú principal del cajero
    ├── Teclado.java        # Teclado numérico para montos
    └── TecladoClave.java   # Teclado numérico para clave PIN
```

---

## Patrones de Diseño GoF Aplicables

Los siguientes patrones del catálogo **Gang of Four** se identificaron como los más beneficiosos para mejorar la estructura actual del proyecto. Se presentan de forma práctica, enfocándose en el problema concreto que resuelven dentro del código existente.

---

### Patrón 1 — Command (Comportamiento)

#### ¿Cuál es el problema actual?

En `MenuCajero.java`, cuando el usuario hace clic en un botón (Depósito, Retiro, Consulta), el código hace llamadas directas al `ControlGeneral`:

```java
// MenuCajero.java
private void jButton1ActionPerformed(...) {
    xd.setVisible(true);
    xd.operador = 1; // operador 1 = depósito
}

private void jButton2ActionPerformed(...) {
    xd.setVisible(true);
    xd.operador = 2; // operador 2 = retiro
}
```

Y en `Teclado.java`, se usa un `switch` con números mágicos para saber qué operación ejecutar:

```java
public void transaccionAControl() {
    switch (operador) {
        case 1: cg.depositar(valor); break;
        case 2: cg.retirar(valor);   break;
    }
}
```

Esto significa que cada vez que se quiera agregar una nueva operación (por ejemplo, transferencia), hay que tocar múltiples archivos, recordar qué número le corresponde, y el riesgo de errores aumenta.

#### ¿Cómo lo resuelve el patrón Command?

El patrón **Command** propone encapsular cada operación como un objeto independiente. Cada "comando" sabe exactamente qué hacer cuando se ejecuta, sin que nadie más necesite conocer los detalles internos.

**Cómo quedaría en la práctica:**

Se crea una interfaz común:

```java
// Interfaz Command
public interface ComandoTransaccion {
    void ejecutar(double valor);
}
```

Luego, cada operación se convierte en su propia clase:

```java
public class ComandoDeposito implements ComandoTransaccion {
    private ControlGeneral cg;
    public ComandoDeposito(ControlGeneral cg) { this.cg = cg; }

    @Override
    public void ejecutar(double valor) {
        cg.depositar(valor);
    }
}

public class ComandoRetiro implements ComandoTransaccion {
    private ControlGeneral cg;
    public ComandoRetiro(ControlGeneral cg) { this.cg = cg; }

    @Override
    public void ejecutar(double valor) {
        cg.retirar(valor);
    }
}
```

Ahora `Teclado.java` no necesita saber qué operación es ni usar números mágicos:

```java
public class Teclado extends JFrame {
    private ComandoTransaccion comandoActivo; // solo guarda el comando

    public void setComando(ComandoTransaccion comando) {
        this.comandoActivo = comando;
    }

    // Al presionar "Aceptar"
    private void jButton23ActionPerformed(...) {
        double valor = Double.parseDouble(this.Out.getText());
        comandoActivo.ejecutar(valor); // sin switch, sin números mágicos
        dispose();
    }
}
```

Y en `MenuCajero.java`:

```java
private void jButton1ActionPerformed(...) { // Depósito
    xd.setComando(new ComandoDeposito(cg));
    xd.setVisible(true);
}

private void jButton2ActionPerformed(...) { // Retiro
    xd.setComando(new ComandoRetiro(cg));
    xd.setVisible(true);
}
```

#### ¿Por qué aplicarlo?

- **Elimina los números mágicos** (`operador = 1`, `operador = 2`) que son difíciles de mantener.
- **Agregar nuevas operaciones** (transferencia, pago de servicios) solo requiere crear una nueva clase, sin modificar el código existente.
- **El teclado numérico** se vuelve completamente reutilizable y desacoplado de la lógica de negocio.
- Sienta las bases para funcionalidades futuras como **historial de transacciones** o **deshacer operaciones**.

---

### Patrón 2 — Facade (Estructural)

#### ¿Cuál es el problema actual?

La clase `ControlGeneral.java` actualmente actúa como el punto de coordinación de todo el sistema, y las vistas deben hablar con ella para cualquier cosa. Sin embargo, hay responsabilidades mezcladas: valida claves, muestra diálogos, gestiona depósitos, gestiona préstamos, abre ventanas, etc.

```java
// ControlGeneral.java — hace demasiadas cosas distintas
public void depositar(double dinero) { ... }
public void retirar(double dinero) { ... }
public void consultar() { ... }
public void cambiarClave(int claveNueva) { ... }
public boolean validarClave(int clave) { ... }
public void mostrarMensajeValor0() { ... }
public void abrirBienvenida() { ... }
public void enviarSolicitanteBanco(...) { ... }
public void enviarPrestamoBanco(...) { ... }
public void mostrarJOption(String mensaje) { ... }
```

Adicionalmente, la vista `FormularioPrestamo.java` tiene que coordinar dos llamadas separadas para completar un solo proceso de préstamo:

```java
// FormularioPrestamo.java — la vista coordina la lógica de negocio
cg.enviarSolicitanteBanco(telefonoCasa, telefonoMovil, nroIdentidad, ...);
cg.enviarPrestamoBanco(valorPrestamo, numeroPrestamo, fechaAutorizacion);
```

La vista está tomando decisiones sobre el orden de operaciones del negocio, lo cual no es su responsabilidad.

#### ¿Cómo lo resuelve el patrón Facade?

El patrón **Facade** (Fachada) propone crear subsistemas especializados y ocultar su complejidad detrás de una interfaz simple y unificada. La vista solo habla con la fachada, y la fachada sabe cómo coordinar los subsistemas internos.

**Cómo quedaría en la práctica:**

Se crean subsistemas con responsabilidades claras:

```java
// Subsistema 1: solo maneja operaciones bancarias (depósito, retiro, consulta)
public class ServicioCuenta {
    private Cuenta cuenta;

    public void depositar(double monto) { /* lógica de depósito */ }
    public void retirar(double monto)   { /* lógica de retiro */ }
    public double consultarSaldo()      { return cuenta.getSaldoCuenta(); }
    public boolean validarClave(int clave) { return cuenta.getClaveCuenta() == clave; }
    public void cambiarClave(int nuevaClave) { cuenta.setClaveCuenta(nuevaClave); }
}

// Subsistema 2: solo maneja préstamos
public class ServicioPrestamo {
    private Banco banco;

    // Un solo método que coordina todo el proceso internamente
    public void procesarSolicitud(String nroId, String nombre, String apellido1,
                                   String apellido2, String telCasa, String telMovil,
                                   String nroPrestamo, String valor, LocalDate fecha) {
        banco.capturarSolicitante(telCasa, telMovil, nroId, nombre, apellido1, apellido2);
        banco.capturarPrestamo(valor, nroPrestamo, fecha);
    }
}
```

La fachada (`ControlGeneral`) simplifica su rol:

```java
// ControlGeneral como Facade — delega, no implementa
public class ControlGeneral {
    private ServicioCuenta servicioCuenta = new ServicioCuenta();
    private ServicioPrestamo servicioPrestamo = new ServicioPrestamo();

    public void depositar(double monto)         { servicioCuenta.depositar(monto); }
    public void retirar(double monto)           { servicioCuenta.retirar(monto); }
    public boolean validarClave(int clave)      { return servicioCuenta.validarClave(clave); }

    // Un solo método para el formulario de préstamo
    public void procesarPrestamo(String nroId, ..., LocalDate fecha) {
        servicioPrestamo.procesarSolicitud(nroId, ..., fecha);
    }
}
```

Ahora `FormularioPrestamo.java` hace una sola llamada ordenada:

```java
// FormularioPrestamo.java — mucho más simple
cg.procesarPrestamo(
    ingresoNumeroPrestamos1.getText(), // nroIdentidad
    ingresoNumeroPrestamos2.getText(), // primerNombre
    // ... resto de campos
    fechaAutorizacion
);
```

#### ¿Por qué aplicarlo?

- **La vista deja de coordinar lógica de negocio**: el formulario solo entrega datos, no decide el orden de las operaciones.
- **`ControlGeneral` se vuelve más limpio**: delega responsabilidades en lugar de implementarlas todas él mismo.
- **Los subsistemas pueden evolucionar independientemente**: si mañana se cambia cómo funciona el sistema de préstamos, `FormularioPrestamo` no necesita cambiar.
- **Facilita las pruebas unitarias**: se puede probar `ServicioCuenta` y `ServicioPrestamo` por separado.

---

## Antipatrones y Malas Prácticas Identificadas

A continuación se documentan **15 antipatrones y malas prácticas** identificados en el código fuente del proyecto, con descripción de cómo se manifiestan y cómo afectan la calidad del sistema.

---

### 1. God Class (Clase Dios)

**¿Cómo se ve en el código?**  
`ControlGeneral.java` acumula responsabilidades que no le corresponden: valida claves, gestiona depósitos y retiros, muestra mensajes de diálogo, abre ventanas de la interfaz, coordina préstamos y actúa como punto de comunicación entre todas las capas.

**¿Cómo afecta?**  
Una clase que lo hace todo se vuelve un cuello de botella: cualquier cambio en cualquier parte del sistema requiere modificarla. Es difícil de entender, difícil de probar y casi imposible de reutilizar en otro contexto. A medida que el sistema crezca, esta clase se volverá cada vez más frágil.

---

### 2. Magic Numbers (Números Mágicos)

**¿Cómo se ve en el código?**  
En `Teclado.java`, el tipo de operación se controla con enteros sin ningún nombre descriptivo:

```java
xd.operador = 1; // ¿qué es 1?
xd.operador = 2; // ¿qué es 2?

switch (operador) {
    case 1: cg.depositar(valor); break;
    case 2: cg.retirar(valor);   break;
}
```

**¿Cómo afecta?**  
Si alguien más (o el mismo desarrollador meses después) lee `operador = 1`, no tiene forma de saber qué significa sin rastrear todo el código. Agregar una nueva operación implica recordar qué números ya están usados y esperar no colisionar con uno existente. Esto es una fuente directa de bugs.

---

### 3. Variable Names Without Meaning (Nombres sin Significado)

**¿Cómo se ve en el código?**  
En `MenuCajero.java` hay una referencia llamada `xd`:

```java
private Teclado xd;
```

En `FormularioPrestamo.java`, los campos de texto tienen nombres como `ingresoNumeroPrestamos`, `ingresoNumeroPrestamos1`, `ingresoNumeroPrestamos2`... hasta `ingresoNumeroPrestamos7`, sin que quede claro qué dato captura cada uno.

**¿Cómo afecta?**  
El código se vuelve ilegible. Para entender qué hace `ingresoNumeroPrestamos3`, hay que rastrear su uso en toda la clase. Esto aumenta el tiempo de mantenimiento y la probabilidad de usar el campo equivocado al extender la funcionalidad.

---

### 4. Lógica de Negocio en la Vista

**¿Cómo se ve en el código?**  
`FormularioPrestamo.java` no solo muestra el formulario, sino que también valida los datos, decide el orden de las llamadas al banco y controla la lógica de fechas:

```java
// Dentro de un botón de la Vista:
if (fechaAutorizacion.getDayOfMonth() <= 20) {
    cg.enviarSolicitanteBanco(...);
    cg.enviarPrestamoBanco(...);
}
```

**¿Cómo afecta?**  
La regla de negocio "los préstamos solo se autorizan en los primeros 20 días del mes" está atrapada en una clase de interfaz gráfica. Si se crea una nueva forma de solicitar préstamos (por ejemplo, vía API o aplicación móvil), esa regla tendría que reescribirse desde cero, generando duplicación y riesgo de inconsistencia.

---

### 5. Raw Types (Tipos Crudos / Sin Genéricos)

**¿Cómo se ve en el código?**  
En `FormularioPrestamo.java`:

```java
private List jtextos; // sin tipo genérico

jtextos = new ArrayList(); // sin tipo genérico
jtextos.add(ingresoNumeroPrestamos.getText());
```

**¿Cómo afecta?**  
Al no especificar el tipo de la lista (`List<Object>` o `List<String>`), el compilador no puede detectar errores de tipo en tiempo de compilación. El código requiere casteos explícitos que pueden fallar en tiempo de ejecución, y la intención del código es ambigua. Esto es especialmente evidente cuando se mezclan `String` y `Date` en la misma lista.

---

### 6. Duplicated Code (Código Duplicado)

**¿Cómo se ve en el código?**  
Tanto `Solicitante.java` como `Persona.java` definen exactamente los mismos getters y setters (`getNroIdentidad`, `getPrimerNombre`, `getPrimerApellido`, `getSegundoApellido`). `Solicitante` extiende a `Persona` pero reescribe todos sus métodos con `@Override` sin añadir ninguna lógica distinta:

```java
// Persona.java tiene esto
public String getPrimerNombre() { return primerNombre; }

// Solicitante.java lo repite idéntico
@Override
public String getPrimerNombre() { return primerNombre; }
```

**¿Cómo afecta?**  
Si se necesita cambiar la lógica de algún getter (por ejemplo, hacer trim o normalizar el texto), hay que recordar cambiarlo en dos lugares. Si se olvida uno, el comportamiento es inconsistente dependiendo de cómo se acceda al objeto.

---

### 7. Acoplamiento Excesivo entre Vista y Lógica

**¿Cómo se ve en el código?**  
Las clases de vista (`MenuCajero`, `FormularioPrestamo`, `TecladoClave`) reciben directamente una instancia de `ControlGeneral` y la guardan como atributo. Toda la interfaz está directamente ligada al controlador concreto.

**¿Cómo afecta?**  
No es posible reutilizar ninguna vista con un controlador diferente, ni probar las vistas de forma aislada. Cualquier cambio en la firma de un método de `ControlGeneral` puede romper múltiples clases de vista a la vez.

---

### 8. JOptionPane Disperso por Todo el Código

**¿Cómo se ve en el código?**  
Los diálogos de mensajes se muestran desde múltiples lugares: dentro del modelo (`Consulta.java`), dentro de la lógica (`ControlGeneral.java`, `Banco.java`) y dentro de las vistas (`FormularioPrestamo.java`). En total hay más de 10 llamadas directas a `JOptionPane.showMessageDialog`.

**¿Cómo afecta?**  
La forma en que se comunican los mensajes al usuario está dispersa e inconsistente. Si en el futuro se quiere cambiar el sistema de notificaciones (por ejemplo, mostrar mensajes dentro de la misma ventana en lugar de en pop-ups), sería necesario rastrear y modificar docenas de puntos en el código.

---

### 9. Hardcoded Data (Datos Hardcodeados)

**¿Cómo se ve en el código?**  
En `ControlGeneral.java`, la cuenta del cliente se crea con valores fijos directamente en el código:

```java
cliente = new Cuenta(1234, 5000000); // clave: 1234, saldo: $5.000.000
```

Igualmente en `Banco.java`:

```java
private double valorMaximoTotal = 10000000.00;
```

**¿Cómo afecta?**  
Cambiar la clave o el saldo inicial requiere modificar y recompilar el código fuente. En un sistema real, estos valores deberían venir de una base de datos o archivo de configuración. El valor máximo de préstamos tampoco puede ajustarse sin tocar el código, lo que hace el sistema rígido ante cualquier cambio de parámetros de negocio.

---

### 10. Constructor que Abre Ventanas (Side Effects en Constructor)

**¿Cómo se ve en el código?**  
En `ControlGeneral.java`, el constructor no solo inicializa los objetos sino que también lanza una ventana gráfica:

```java
public ControlGeneral() {
    cliente = new Cuenta(1234, 5000000);
    banco = new Banco(this);
    abrirBienvenida(); // ← efecto secundario: abre una ventana
    deposito = new Deposito();
    consulta = new Consulta();
    retiro = new Retiro();
}
```

**¿Cómo afecta?**  
Un constructor debería únicamente inicializar el estado del objeto. Cuando un constructor hace más que eso, crear una instancia se vuelve una operación impredecible con efectos visuales. Esto hace imposible instanciar `ControlGeneral` en un contexto de prueba sin que se abra una ventana de interfaz gráfica, mezclando completamente la inicialización con la presentación.

---

### 11. Doble Registro de ActionListeners

**¿Cómo se ve en el código?**  
En `TecladoClave.java`, cada botón numérico tiene **dos listeners** registrados: uno en forma de lambda (añadido desde el `.form`) y otro registrado por el método generado por NetBeans:

```java
jButton6.addActionListener(e -> {
    if (CampoClave.getPassword().length < 4) { ... }
});
jButton6.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(...) {
        jButton6ActionPerformed(evt); // método vacío
    }
});
```

**¿Cómo afecta?**  
Cuando el usuario hace clic, se disparan dos eventos. Aunque el segundo esté vacío actualmente, es un estado ambiguo y confuso. Si alguien agrega lógica al método generado por NetBeans sin saber que ya existe el lambda, el botón ejecutará código dos veces. Esto es una fuente directa de bugs difíciles de detectar.

---

### 12. Validación Siempre Falsa

**¿Cómo se ve en el código?**  
En `TecladoClave.java`, el `KeyListener` del campo de contraseña tiene una condición que siempre es verdadera, bloqueando completamente la escritura manual:

```java
CampoClave.addKeyListener(new KeyAdapter() {
    public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();
        // Esta condición siempre es true: length >= 4 OR length <= 4 cubre todos los casos
        if (!Character.isDigit(c) || CampoClave.getPassword().length >= 4
                                  || CampoClave.getPassword().length <= 4) {
            e.consume();
        }
    }
});
```

**¿Cómo afecta?**  
La condición `length >= 4 || length <= 4` cubre todos los enteros posibles, por lo que el campo nunca acepta entrada de teclado. Si bien la intención (obligar a usar los botones del teclado virtual) puede tener sentido, la lógica es incorrecta y engañosa. Un desarrollador que lea esto podría no darse cuenta del error y confiar en que la validación funciona cuando en realidad está completamente rota.

---

### 13. Falta de Separación entre Cambio de Clave y Validación de Acceso

**¿Cómo se ve en el código?**  
En `TecladoClave.java`, la misma pantalla y el mismo botón "Aceptar" sirven tanto para iniciar sesión como para cambiar la clave, diferenciado únicamente por un contador `i`:

```java
private void jButton23ActionPerformed(...) {
    if (i == 1) {
        // modo: validar clave de acceso
        opcion = cg.validarClave(Integer.parseInt(texto));
        i++;
    } else {
        // modo: cambiar clave
        validarClave(texto);
    }
}
```

**¿Cómo afecta?**  
Una misma clase cumple dos propósitos distintos controlados por un estado interno implícito. Esto viola el Principio de Responsabilidad Única (SRP). El flujo es difícil de seguir y propenso a bugs: si `i` no se reinicia correctamente entre sesiones, el comportamiento puede ser completamente inesperado.

---

### 14. Comentarios de Código Muerto

**¿Cómo se ve en el código?**  
En varios archivos existe código extenso comentado que nunca se eliminó, por ejemplo en `Retiro.java`:

```java
public void Accion(Cuenta cuenta, double dineroRetirado) {
    cuenta.setSaldoCuenta(cuenta.getSaldoCuenta() - dineroRetirado);
    /*
    if (retiro <= getSaldo()) {
        transacciones = getSaldo();
        setSaldo(transacciones - retiro);
        JOptionPane.showMessageDialog(null, "Retiraste: " + retiro + ...);
    } else {
        JOptionPane.showMessageDialog(null, "Saldo insuficientes.");
    }
    */
}
```

Lo mismo ocurre en `Banco.java` con un bloque `if` comentado, y en múltiples vistas con bloques `main` comentados.

**¿Cómo afecta?**  
El código comentado genera ruido visual y hace difícil distinguir qué está activo y qué no. Sugiere que hay lógica incompleta o decisiones de diseño que nunca se tomaron formalmente. Si este código era útil, debería estar en el historial de versiones del repositorio (Git), no bloqueando la lectura del código activo.

---

### 15. Convenciones de Nomenclatura Inconsistentes

**¿Cómo se ve en el código?**  
El proyecto mezcla distintas convenciones de nombres sin un criterio uniforme:

- La clase `Teclado.java` tiene un campo público llamado `Out` (en mayúscula, como si fuera una constante), cuando debería ser `out` o `displayLabel`.
- El método `Accion` en `Transaccion.java` y sus subclases usa mayúscula inicial, lo que en Java corresponde a nombres de clases, no de métodos (debería ser `accion` o `ejecutar`).
- La variable `SegundoNumero` en `Teclado.java` usa PascalCase cuando debería ser `segundoNumero`.
- El campo `CampoClave` en `TecladoClave.java` usa PascalCase siendo un atributo de instancia.

**¿Cómo afecta?**  
Las convenciones de nomenclatura son el primer contrato de legibilidad en cualquier lenguaje. Cuando no son consistentes, el lector del código no puede confiar en las señales visuales para entender el rol de cada elemento. Esto aumenta el tiempo de comprensión y puede llevar a errores al asumir, por ejemplo, que un nombre en mayúscula es una constante cuando en realidad es un componente de interfaz mutable.

---

*La revision de este proyecto se realizo con la ayuda de la Inteligencia Artificial Claude*

