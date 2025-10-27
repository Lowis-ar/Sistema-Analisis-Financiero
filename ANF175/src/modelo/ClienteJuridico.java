package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteJuridico {

    int id_cliente;
    String representante_legal;
    String informacion_financiera;
}
