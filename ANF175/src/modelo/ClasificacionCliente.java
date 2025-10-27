package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClasificacionCliente {

    int id_cliente;
    String categoria;      // A, B, C, D
    String fecha_actualizacion;
    String observacion;
}
