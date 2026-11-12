package estructuras;

import java.util.ArrayList;
import modelo.NodoAdyacencia;
import modelo.NodoVertice;

/**
 * Grafo NO dirigido implementado con listas de adyacencia de nodos propios.
 *
 *   primero -> [ANA] -> [BRUNO] -> [CARLA] -> ... -> null      (lista de vértices)
 *                |
 *                +--> (BRUNO) -> (CARLA) -> (DIEGO) -> null     (lista de adyacencia de ANA)
 *
 * Los vértices y los vecinos se agregan al final de su lista, por eso el
 * orden de inserción define el orden de los recorridos (resultado determinista).
 * No se usa HashMap, HashSet, TreeMap ni TreeSet. ArrayList solo para devolver resultados.
 */
public class Grafo {

    private NodoVertice primero;
    private NodoVertice ultimo;
    private int totalVertices;
    private int totalAristas;

    // ===================== RF01 · Registrar vértice =====================

    /** Registra un estudiante. false si el nombre es vacío o ya existe. O(V) */
    public boolean registrarVertice(String nombre) {
        if (nombre == null || nombre.isBlank() || buscarVertice(nombre) != null) {
            return false;
        }
        NodoVertice nuevo = new NodoVertice(nombre);
        if (primero == null) {
            primero = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);
        }
        ultimo = nuevo;
        totalVertices++;
        return true;
    }

    /** Busca un vértice sin distinguir mayúsculas. null si no existe. O(V) */
    public NodoVertice buscarVertice(String nombre) {
        String id = NodoVertice.normalizar(nombre);
        NodoVertice actual = primero;
        while (actual != null) {
            if (actual.getId().equals(id)) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    // ===================== RF02 · Crear conexión =====================

    /**
     * Crea la conexión a—b en los dos sentidos.
     * false si algún estudiante no existe, si es un lazo (a—a) o si ya estaban conectados.
     * O(V + grado(a))
     */
    public boolean crearConexion(String a, String b) {
        NodoVertice va = buscarVertice(a);
        NodoVertice vb = buscarVertice(b);
        if (va == null || vb == null || va == vb || existeConexion(va, vb)) {
            return false;
        }
        agregarAlFinal(va, vb);
        agregarAlFinal(vb, va);   // no dirigido: también b—a
        totalAristas++;
        return true;
    }

    private void agregarAlFinal(NodoVertice origen, NodoVertice destino) {
        NodoAdyacencia nuevo = new NodoAdyacencia(destino);
        if (origen.getPrimerAdyacente() == null) {
            origen.setPrimerAdyacente(nuevo);
        } else {
            NodoAdyacencia actual = origen.getPrimerAdyacente();
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        origen.aumentarGrado();
    }

    /** true si a y b están conectados directamente. */
    public boolean existeConexion(String a, String b) {
        NodoVertice va = buscarVertice(a);
        NodoVertice vb = buscarVertice(b);
        return va != null && vb != null && existeConexion(va, vb);
    }

    /** Recorre la lista de adyacencia de a. O(grado(a)) */
    public boolean existeConexion(NodoVertice a, NodoVertice b) {
        NodoAdyacencia actual = a.getPrimerAdyacente();
        while (actual != null) {
            if (actual.getDestino() == b) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    // ===================== RF05 · Mostrar grafo =====================

    /** Listas de adyacencia, una línea por vértice. */
    public String mostrar() {
        if (primero == null) {
            return "(grafo vacío)";
        }
        StringBuilder sb = new StringBuilder();
        NodoVertice v = primero;
        while (v != null) {
            sb.append(String.format("%-8s (grado %d) -> ", v.getNombre(), v.getGrado()));
            NodoAdyacencia a = v.getPrimerAdyacente();
            if (a == null) {
                sb.append("[sin conexiones]");
            }
            while (a != null) {
                sb.append(a.getDestino().getNombre());
                if (a.getSiguiente() != null) {
                    sb.append(" -> ");
                }
                a = a.getSiguiente();
            }
            sb.append(System.lineSeparator());
            v = v.getSiguiente();
        }
        return sb.toString().stripTrailing();
    }

    /** Vecinos directos en el orden de su lista de adyacencia. */
    public ArrayList<String> vecinos(String nombre) {
        ArrayList<String> lista = new ArrayList<>();
        NodoVertice v = buscarVertice(nombre);
        if (v == null) {
            return lista;
        }
        NodoAdyacencia a = v.getPrimerAdyacente();
        while (a != null) {
            lista.add(a.getDestino().getNombre());
            a = a.getSiguiente();
        }
        return lista;
    }

    // ===================== Apoyo para algoritmos y estadísticas =====================

    /** Posición del vértice en la lista (0..V-1); sirve para los arreglos de visitados. */
    public int indiceDe(NodoVertice buscado) {
        int i = 0;
        NodoVertice v = primero;
        while (v != null) {
            if (v == buscado) {
                return i;
            }
            i++;
            v = v.getSiguiente();
        }
        return -1;
    }

    public NodoVertice getPrimero() {
        return primero;
    }

    public int getTotalVertices() {
        return totalVertices;
    }

    public int getTotalAristas() {
        return totalAristas;
    }

    public boolean estaVacio() {
        return primero == null;
    }

    /** Densidad de un grafo no dirigido simple: 2E / [V(V-1)]. */
    public double densidad() {
        if (totalVertices < 2) {
            return 0.0;
        }
        return (2.0 * totalAristas) / (totalVertices * (totalVertices - 1.0));
    }
}
