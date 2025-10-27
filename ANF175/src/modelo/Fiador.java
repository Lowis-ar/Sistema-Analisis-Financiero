package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fiador {

    int id_fiador;
    int id_prestamo;
    String nombre;
    String direccion;
    String telefono;
    String dui;
    double ingresos;
    double egresos;
}
