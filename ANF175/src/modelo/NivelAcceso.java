package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NivelAcceso {

    private int id_nivel;
    private String nombre_nivel;
    private String descripcion;
}
