package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Contrato {

    int id_contrato;
    int id_prestamo;
    String numero_contrato;
    String fecha_contrato;
    String contenido;
}
