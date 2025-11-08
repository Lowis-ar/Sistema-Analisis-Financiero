package modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ClienteNatural {
    private int idCliente;
    private String codigoCliente;
    private String nombre;
    private String direccion;
    private String telefono;
    private String dui;
    private String estadoCivil;
    private BigDecimal ingresos;
    private BigDecimal egresos;
    private List<LugarTrabajo> lugaresTrabajo;
    private BigDecimal maximoPrestar;
    private BigDecimal cuotaCalculada;

    public ClienteNatural() {
        this.lugaresTrabajo = new ArrayList<>();
        this.ingresos = BigDecimal.ZERO;
        this.egresos = BigDecimal.ZERO;
        this.maximoPrestar = BigDecimal.ZERO;
        this.cuotaCalculada = BigDecimal.ZERO;
    }

    // Getters y Setters
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public String getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(String codigoCliente) { this.codigoCliente = codigoCliente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getDui() { return dui; }
    public void setDui(String dui) { this.dui = dui; }

    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }

    public BigDecimal getIngresos() { return ingresos; }
    public void setIngresos(BigDecimal ingresos) { this.ingresos = ingresos; }

    public BigDecimal getEgresos() { return egresos; }
    public void setEgresos(BigDecimal egresos) { this.egresos = egresos; }

    public List<LugarTrabajo> getLugaresTrabajo() { return lugaresTrabajo; }
    public void setLugaresTrabajo(List<LugarTrabajo> lugaresTrabajo) { this.lugaresTrabajo = lugaresTrabajo; }

    public BigDecimal getMaximoPrestar() { return maximoPrestar; }
    public void setMaximoPrestar(BigDecimal maximoPrestar) { this.maximoPrestar = maximoPrestar; }

    public BigDecimal getCuotaCalculada() { return cuotaCalculada; }
    public void setCuotaCalculada(BigDecimal cuotaCalculada) { this.cuotaCalculada = cuotaCalculada; }

    public void agregarLugarTrabajo(LugarTrabajo lugarTrabajo) {
        this.lugaresTrabajo.add(lugarTrabajo);
    }

    public void removerLugarTrabajo(int index) {
        if (index >= 0 && index < lugaresTrabajo.size()) {
            this.lugaresTrabajo.remove(index);
        }
    }
}