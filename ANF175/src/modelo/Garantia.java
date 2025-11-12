package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Garantia {

    public int id_garantia;
    public int id_prestamo;
    public int id_tipo_garantia;
    public String descripcion;
    public double valor_estimado;
}
