package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Cuota {

    int id_cuota;
    int id_prestamo;
    int numero_cuota;
    String fecha_vencimiento;
    double monto_capital;
    double monto_interes;
    double monto_comision;
    double saldo_restante;
    String estado;       // Pendiente, Pagada, Mora 
}
