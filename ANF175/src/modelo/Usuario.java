package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {

    int id_usuario;
    String nombre_usuario;
    String contraseña;
    String nivel_acceso; // Administrador, Asesor, Cajero, Consulta
    String estado;       // Activo, Inactivo 
    
    
}
