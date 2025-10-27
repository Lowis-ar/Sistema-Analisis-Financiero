package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    int id_cliente;
    String codigo_cliente;
    String tipo_cliente; // Natural, Juridica
    String nombre;
    String direccion;
    String telefono;
    String estado;       // Activo, Inactivo
}
