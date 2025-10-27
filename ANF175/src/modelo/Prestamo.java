package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {

    int id_prestamo;
    int id_cliente;
    int id_tipo;
    double monto;
    int plazo_meses;
    double tasa_interes;
    String fecha_otorgamiento;
    String estado;       // Normal, En Mora, Incobrable, Cancelado
    String zona_geografica;
    String sucursal;
    String asesor;
}
