package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class LugarTrabajo {

    int id_lugar;
    int id_cliente;
    String nombre_empresa;
    String direccion_empresa;
    String telefono_empresa;
}
