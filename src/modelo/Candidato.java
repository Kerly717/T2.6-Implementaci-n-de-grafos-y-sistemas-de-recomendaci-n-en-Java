package modelo;

import java.util.ArrayList;
import java.util.List;

/** Estudiante sugerido y amigos que tiene en común con quien consulta. */
public final class Candidato {
    private final String nombre;
    private final ArrayList<String> amigosEnComun;

    public Candidato(String nombre, List<String> amigosEnComun) {
        if (nombre == null || nombre.isBlank() || amigosEnComun == null || amigosEnComun.isEmpty()) {
            throw new IllegalArgumentException("El candidato necesita nombre y amigos en común");
        }
        this.nombre = nombre.trim();
        this.amigosEnComun = new ArrayList<>(amigosEnComun);
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadAmigosEnComun() {
        return amigosEnComun.size();
    }

    public ArrayList<String> getAmigosEnComun() {
        return new ArrayList<>(amigosEnComun);
    }

    @Override
    public String toString() {
        return nombre + " (" + getCantidadAmigosEnComun() + " amigos en común: "
                + String.join(", ", amigosEnComun) + ")";
    }
}
