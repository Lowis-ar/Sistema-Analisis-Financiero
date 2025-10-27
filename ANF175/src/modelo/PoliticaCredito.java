package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PoliticaCredito {

    int id_politica;
    String tipo_politica; // Cobro, Interes, MontoMaximo, Incobrable, Clasificacion
    String descripcion;
    String valor;
}
