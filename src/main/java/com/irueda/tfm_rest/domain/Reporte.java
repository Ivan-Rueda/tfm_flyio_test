package com.irueda.tfm_rest.domain;

import java.util.List;

public class Reporte {
    private int alertasMes;
    private int alertaToday;
    private int empleadosActivos;
    private List<ReporteLatLog> ubicacionesDispositivos;
    private List<Actividad> actividades;

    public Reporte(int alertasMes, int alertaToday, int empleadosActivos, List<ReporteLatLog> ubicacionesDispositivos,
            List<Actividad> actividades) {
        this.alertasMes = alertasMes;
        this.alertaToday = alertaToday;
        this.empleadosActivos = empleadosActivos;
        this.ubicacionesDispositivos = ubicacionesDispositivos;
        this.actividades = actividades;
    }

    public int getAlertasMes() {
        return alertasMes;
    }

    public void setAlertasMes(int alertasMes) {
        this.alertasMes = alertasMes;
    }

    public int getAlertaToday() {
        return alertaToday;
    }

    public void setAlertaToday(int alertaToday) {
        this.alertaToday = alertaToday;
    }

    public int getEmpleadosActivos() {
        return empleadosActivos;
    }

    public void setEmpleadosActivos(int empleadosActivos) {
        this.empleadosActivos = empleadosActivos;
    }

    public List<ReporteLatLog> getUbicacionesDispositivos() {
        return ubicacionesDispositivos;
    }

    public void setUbicacionesDispositivos(List<ReporteLatLog> ubicacionesDispositivos) {
        this.ubicacionesDispositivos = ubicacionesDispositivos;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

}
