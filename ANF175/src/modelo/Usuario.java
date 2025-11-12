package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private int id_usuario;
    private String nombre_usuario;
    private String contraseña;
    private int id_nivel;    
    private boolean estado;  

}
