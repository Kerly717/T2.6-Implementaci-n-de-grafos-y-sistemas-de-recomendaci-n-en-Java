package algoritmos;

import estructuras.Cola;
import estructuras.Grafo;
import estructuras.Pila;
import java.util.ArrayList;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * Recorridos del grafo con estructuras propias:
 *  - BFS (anchura) usa la Cola: visita por niveles.
 *  - DFS (profundidad) usa la Pila: baja por una rama hasta el fondo antes de volver.
 *  - Componentes conectados (reto Grupo 5): BFS repetido desde cada vértice no visitado.
 * Los vecinos se exploran en el orden de su lista de adyacencia (orden de inserción),
 * por eso los resultados son siempre los mismos para los mismos datos.
 * Complejidad: O(V + E) con el arreglo de visitados.
 */
public final class Recorridos {

    private Recorridos() {
    }

    // ===================== RF06 · BFS =====================

    /** Orden de visita BFS desde inicio. Lista vacía si el estudiante no existe. */
    public static ArrayList<String> bfs(Grafo grafo, String inicio) {
        return bfs(grafo, inicio, null);
    }

    /** BFS que, si traza != null, anota el contenido de la cola en cada paso y el nivel de cada vértice. */
    public static ArrayList<String> bfs(Grafo grafo, String inicio, StringBuilder traza) {
        ArrayList<String> orden = new ArrayList<>();
        NodoVertice origen = grafo.buscarVertice(inicio);
        if (origen == null) {
            return orden;
        }
        boolean[] visitado = new boolean[grafo.getTotalVertices()];
        int[] nivel = new int[grafo.getTotalVertices()];
        Cola cola = new Cola();

        visitado[grafo.indiceDe(origen)] = true;   // se marca al encolar: nadie entra dos veces
        cola.encolar(origen);
        anotar(traza, "Inicio: encolar " + origen.getNombre() + "  cola = " + cola.contenido());

        while (!cola.estaVacia()) {
            NodoVertice actual = cola.desencolar();
            int nivelActual = nivel[grafo.indiceDe(actual)];
            orden.add(actual.getNombre());
            StringBuilder nuevos = new StringBuilder();

            NodoAdyacencia a = actual.getPrimerAdyacente();
            while (a != null) {
                int i = grafo.indiceDe(a.getDestino());
                if (!visitado[i]) {
                    visitado[i] = true;
                    nivel[i] = nivelActual + 1;
                    cola.encolar(a.getDestino());
                    nuevos.append(nuevos.length() == 0 ? "" : ", ").append(a.getDestino().getNombre());
                }
                a = a.getSiguiente();
            }
            anotar(traza, String.format("Visita %-7s (nivel %d) encola [%s]  cola = %s",
                    actual.getNombre(), nivelActual, nuevos, cola.contenido()));
        }
        if (traza != null) {
            traza.append(porNiveles(grafo, orden, nivel)).append(System.lineSeparator());
        }
        return orden;
    }

    private static String porNiveles(Grafo grafo, ArrayList<String> orden, int[] nivel) {
        StringBuilder sb = new StringBuilder("Por niveles:");
        int nivelEscrito = -1;
        for (String nombre : orden) {
            int n = nivel[grafo.indiceDe(grafo.buscarVertice(nombre))];
            if (n != nivelEscrito) {
                sb.append(System.lineSeparator()).append("  Nivel ").append(n).append(": ");
                nivelEscrito = n;
            } else {
                sb.append(", ");
            }
            sb.append(nombre);
        }
        return sb.toString();
    }

    // ===================== RF07 · DFS =====================

    /** Orden de visita DFS desde inicio. Lista vacía si el estudiante no existe. */
    public static ArrayList<String> dfs(Grafo grafo, String inicio) {
        return dfs(grafo, inicio, null);
    }

    /**
     * DFS iterativo con Pila propia. Se marca al desapilar y los vecinos se apilan
     * en orden inverso, así se explora primero el primer vecino de la lista
     * (mismo orden que la versión recursiva).
     */
    public static ArrayList<String> dfs(Grafo grafo, String inicio, StringBuilder traza) {
        ArrayList<String> orden = new ArrayList<>();
        NodoVertice origen = grafo.buscarVertice(inicio);
        if (origen == null) {
            return orden;
        }
        boolean[] visitado = new boolean[grafo.getTotalVertices()];
        Pila pila = new Pila();
        pila.apilar(origen);
        anotar(traza, "Inicio: apilar " + origen.getNombre() + "  pila = " + pila.contenido());

        while (!pila.estaVacia()) {
            NodoVertice actual = pila.desapilar();
            int i = grafo.indiceDe(actual);
            if (visitado[i]) {
                anotar(traza, "Saca " + actual.getNombre() + " (ya visitado, se ignora)  pila = " + pila.contenido());
                continue;
            }
            visitado[i] = true;
            orden.add(actual.getNombre());

            NodoVertice[] vecinos = new NodoVertice[actual.getGrado()];
            int k = 0;
            NodoAdyacencia a = actual.getPrimerAdyacente();
            while (a != null) {
                vecinos[k++] = a.getDestino();
                a = a.getSiguiente();
            }
            StringBuilder apilados = new StringBuilder();
            for (int j = k - 1; j >= 0; j--) {
                if (!visitado[grafo.indiceDe(vecinos[j])]) {
                    pila.apilar(vecinos[j]);
                    apilados.append(apilados.length() == 0 ? "" : ", ").append(vecinos[j].getNombre());
                }
            }
            anotar(traza, String.format("Visita %-7s apila [%s]  pila = %s",
                    actual.getNombre(), apilados, pila.contenido()));
        }
        return orden;
    }

    // ===================== Reto Grupo 5 · Componentes conectados =====================

    /** Grupos de estudio aislados entre sí: cada componente es una lista de nombres. */
    public static ArrayList<ArrayList<String>> componentesConectados(Grafo grafo) {
        ArrayList<ArrayList<String>> componentes = new ArrayList<>();
        boolean[] asignado = new boolean[grafo.getTotalVertices()];
        NodoVertice v = grafo.getPrimero();
        while (v != null) {
            if (!asignado[grafo.indiceDe(v)]) {
                ArrayList<String> componente = bfs(grafo, v.getNombre());
                for (String nombre : componente) {
                    asignado[grafo.indiceDe(grafo.buscarVertice(nombre))] = true;
                }
                componentes.add(componente);
            }
            v = v.getSiguiente();
        }
        return componentes;
    }

    /** true si a y b pertenecen al mismo grupo de estudio (hay un camino entre ellos). */
    public static boolean mismoComponente(Grafo grafo, String a, String b) {
        NodoVertice vb = grafo.buscarVertice(b);
        return vb != null && bfs(grafo, a).contains(vb.getNombre());
    }

    private static void anotar(StringBuilder traza, String linea) {
        if (traza != null) {
            traza.append(linea).append(System.lineSeparator());
        }
    }
}
