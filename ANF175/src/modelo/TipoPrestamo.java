package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoPrestamo {

    int id_tipo;
    String nombre_tipo;
    double tasa_interes_normal;
    double tasa_interes_mora;
    double comision;
}
