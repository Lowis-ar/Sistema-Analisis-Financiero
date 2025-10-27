package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Garantia {

    int id_garantia;
    int id_prestamo;
    String tipo;         // Hipotecaria, Prendaria, Personal
    String descripcion;
    double valor_estimado;
}
