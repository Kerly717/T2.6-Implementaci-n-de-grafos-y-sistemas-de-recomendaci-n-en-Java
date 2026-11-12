package algoritmos;

import estructuras.Grafo;
import java.util.ArrayList;
import java.util.Comparator;
import modelo.Candidato;
import modelo.NodoVertice;

/** Recomienda amistades a partir de vecinos compartidos del grafo no dirigido. */
public final class Recomendador {
    private final Grafo grafo;

    public Recomendador(Grafo grafo) {
        if (grafo == null) {
            throw new IllegalArgumentException("El grafo es obligatorio");
        }
        this.grafo = grafo;
    }

    /** RF08: amigos directos que comparten dos estudiantes, en orden de inserción. */
    public ArrayList<String> amigosEnComun(String estudiante, String posibleAmigo) {
        ArrayList<String> comunes = new ArrayList<>();
        if (estudiante == null || posibleAmigo == null) {
            return comunes;
        }
        NodoVertice origen = grafo.buscarVertice(estudiante);
        NodoVertice destino = grafo.buscarVertice(posibleAmigo);
        if (origen == null || destino == null || origen == destino) {
            return comunes;
        }
        for (String amigo : grafo.vecinos(origen.getNombre())) {
            if (grafo.existeConexion(amigo, destino.getNombre())) {
                comunes.add(amigo);
            }
        }
        return comunes;
    }

    /** RF09: sugiere personas no conectadas, ordenadas por amigos compartidos. */
    public ArrayList<Candidato> recomendar(String estudiante) {
        ArrayList<Candidato> candidatos = new ArrayList<>();
        if (estudiante == null) {
            return candidatos;
        }
        NodoVertice origen = grafo.buscarVertice(estudiante);
        if (origen == null) {
            return candidatos;
        }
        NodoVertice actual = grafo.getPrimero();
        while (actual != null) {
            if (actual != origen && !grafo.existeConexion(origen, actual)) {
                ArrayList<String> comunes = amigosEnComun(origen.getNombre(), actual.getNombre());
                if (!comunes.isEmpty()) {
                    candidatos.add(new Candidato(actual.getNombre(), comunes));
                }
            }
            actual = actual.getSiguiente();
        }
        candidatos.sort(Comparator.comparingInt(Candidato::getCantidadAmigosEnComun).reversed()
                .thenComparing(Candidato::getNombre, String.CASE_INSENSITIVE_ORDER));
        return candidatos;
    }

    /** Primeras {@code limite} recomendaciones; cero devuelve una lista vacía. */
    public ArrayList<Candidato> recomendar(String estudiante, int limite) {
        if (limite < 0) {
            throw new IllegalArgumentException("El límite no puede ser negativo");
        }
        ArrayList<Candidato> todos = recomendar(estudiante);
        return new ArrayList<>(todos.subList(0, Math.min(limite, todos.size())));
    }
}
