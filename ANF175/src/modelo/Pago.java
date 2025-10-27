package modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    int id_pago;
    int id_cuota;
    String fecha_pago;
    double monto_pagado;
    double interes_pagado;
    double comision_pagada;
    double abono_capital;
    String metodo_pago;
}
