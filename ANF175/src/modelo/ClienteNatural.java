package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteNatural {

    int id_cliente;
    String dui;
    String estado_civil;
    double ingresos;
    double egresos;
}
