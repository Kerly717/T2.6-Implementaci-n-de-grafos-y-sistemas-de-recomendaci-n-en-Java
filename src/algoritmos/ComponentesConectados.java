package algoritmos;

import estructuras.Grafo;
import java.util.ArrayList;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * Reto: componentes conectados de la red de grupos de estudio.
 *
 * Un componente conectado es un grupo de estudiantes en el que todos pueden
 * llegar a todos siguiendo conexiones. Se calcula con un BFS desde cada
 * vértice todavía no visitado; cada BFS descubre un componente completo.
 *
 * Usa un arreglo de visitados indexado con Grafo.indiceDe y una cola propia
 * hecha con un arreglo (sin HashMap, HashSet, TreeMap ni TreeSet).
 * Complejidad: O(V + E) para el recorrido (más O(V) por cada indiceDe).
 */
public final class ComponentesConectados {

    private final Grafo grafo;

    public ComponentesConectados(Grafo grafo) {
        if (grafo == null) {
            throw new IllegalArgumentException("El grafo es obligatorio");
        }
        this.grafo = grafo;
    }

    /** Todos los componentes, en el orden en que aparece su primer estudiante. */
    public ArrayList<ArrayList<String>> calcular() {
        ArrayList<ArrayList<String>> componentes = new ArrayList<>();
        boolean[] visitado = new boolean[grafo.getTotalVertices()];
        NodoVertice v = grafo.getPrimero();
        while (v != null) {
            if (!visitado[grafo.indiceDe(v)]) {
                componentes.add(explorar(v, visitado));
            }
            v = v.getSiguiente();
        }
        return componentes;
    }

    /** BFS desde inicio que marca y devuelve todos los estudiantes alcanzables. */
    private ArrayList<String> explorar(NodoVertice inicio, boolean[] visitado) {
        ArrayList<String> componente = new ArrayList<>();
        NodoVertice[] cola = new NodoVertice[grafo.getTotalVertices()];
        int frente = 0;
        int fin = 0;
        cola[fin++] = inicio;
        visitado[grafo.indiceDe(inicio)] = true;
        while (frente < fin) {
            NodoVertice actual = cola[frente++];
            componente.add(actual.getNombre());
            NodoAdyacencia a = actual.getPrimerAdyacente();
            while (a != null) {
                int i = grafo.indiceDe(a.getDestino());
                if (!visitado[i]) {
                    visitado[i] = true;
                    cola[fin++] = a.getDestino();
                }
                a = a.getSiguiente();
            }
        }
        return componente;
    }

    /** Número de componentes; 0 si el grafo está vacío. */
    public int cantidad() {
        return calcular().size();
    }

    /** true si todos los estudiantes forman un solo componente (grafo vacío: false). */
    public boolean esConexo() {
        return cantidad() == 1;
    }

    /** Componente al que pertenece el estudiante; lista vacía si no existe. */
    public ArrayList<String> componenteDe(String estudiante) {
        NodoVertice v = grafo.buscarVertice(estudiante);
        if (v == null) {
            return new ArrayList<>();
        }
        return explorar(v, new boolean[grafo.getTotalVertices()]);
    }

    /** true si existe un camino entre a y b (están en el mismo componente). */
    public boolean mismoComponente(String a, String b) {
        NodoVertice va = grafo.buscarVertice(a);
        NodoVertice vb = grafo.buscarVertice(b);
        if (va == null || vb == null) {
            return false;
        }
        for (String nombre : componenteDe(va.getNombre())) {
            if (nombre.equals(vb.getNombre())) {
                return true;
            }
        }
        return false;
    }

    /** Componente con más estudiantes; en empate gana el que aparece primero. */
    public ArrayList<String> masGrande() {
        ArrayList<String> mayor = new ArrayList<>();
        for (ArrayList<String> c : calcular()) {
            if (c.size() > mayor.size()) {
                mayor = c;
            }
        }
        return mayor;
    }

    /** Estudiantes sin ninguna conexión (componentes de un solo vértice). */
    public ArrayList<String> aislados() {
        ArrayList<String> lista = new ArrayList<>();
        NodoVertice v = grafo.getPrimero();
        while (v != null) {
            if (v.getGrado() == 0) {
                lista.add(v.getNombre());
            }
            v = v.getSiguiente();
        }
        return lista;
    }

    /** Reporte legible para el menú. */
    public String reporte() {
        if (grafo.estaVacio()) {
            return "(grafo vacío: no hay componentes)";
        }
        ArrayList<ArrayList<String>> componentes = calcular();
        StringBuilder sb = new StringBuilder();
        sb.append("Componentes conectados: ").append(componentes.size())
          .append(componentes.size() == 1 ? " (grafo conexo)" : " (grafo desconectado)")
          .append(System.lineSeparator());
        for (int i = 0; i < componentes.size(); i++) {
            ArrayList<String> c = componentes.get(i);
            sb.append(String.format("  C%d (%d): %s%n", i + 1, c.size(), String.join(", ", c)));
        }
        ArrayList<String> aislados = aislados();
        sb.append("Aislados: ").append(aislados.isEmpty() ? "ninguno" : String.join(", ", aislados));
        return sb.toString();
    }
}
