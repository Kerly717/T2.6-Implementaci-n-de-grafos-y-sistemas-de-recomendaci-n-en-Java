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

    // ===================== RF03 · Eliminar conexión =====================

    /** Elimina a—b de los dos extremos. false si no existía. */
    public boolean eliminarConexion(String a, String b) {
        NodoVertice va = buscarVertice(a);
        NodoVertice vb = buscarVertice(b);
        if (va == null || vb == null || va == vb || !existeConexion(va, vb)) {
            return false;
        }
        quitarDeLista(va, vb);
        quitarDeLista(vb, va);
        totalAristas--;
        return true;
    }

    private boolean quitarDeLista(NodoVertice origen, NodoVertice destino) {
        NodoAdyacencia anterior = null;
        NodoAdyacencia actual = origen.getPrimerAdyacente();
        while (actual != null && actual.getDestino() != destino) {
            anterior = actual;
            actual = actual.getSiguiente();
        }
        if (actual == null) {
            return false;
        }
        if (anterior == null) {
            origen.setPrimerAdyacente(actual.getSiguiente());
        } else {
            anterior.setSiguiente(actual.getSiguiente());
        }
        origen.disminuirGrado();
        return true;
    }

    // ===================== RF04 · Eliminar vértice =====================

    /**
     * Elimina al estudiante y todas sus conexiones:
     * primero se quita de la lista de cada vecino y luego de la lista de vértices.
     * O(V + suma de grados de sus vecinos)
     */
    public boolean eliminarVertice(String nombre) {
        NodoVertice objetivo = buscarVertice(nombre);
        if (objetivo == null) {
            return false;
        }
        NodoAdyacencia vecino = objetivo.getPrimerAdyacente();
        while (vecino != null) {
            quitarDeLista(vecino.getDestino(), objetivo);
            totalAristas--;
            vecino = vecino.getSiguiente();
        }
        objetivo.setPrimerAdyacente(null);

        NodoVertice anterior = null;
        NodoVertice actual = primero;
        while (actual != objetivo) {
            anterior = actual;
            actual = actual.getSiguiente();
        }
        if (anterior == null) {
            primero = objetivo.getSiguiente();
        } else {
            anterior.setSiguiente(objetivo.getSiguiente());
        }
        if (objetivo == ultimo) {
            ultimo = anterior;
        }
        totalVertices--;
        return true;
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

    // ===================== RF10 · Validar integridad =====================

    /**
     * Revisa toda la estructura y devuelve los problemas encontrados
     * (lista vacía = grafo íntegro):
     * - cada vecino existe en la lista de vértices (sin referencias inválidas)
     * - sin lazos ni conexiones duplicadas
     * - la conexión está en los dos extremos (simetría)
     * - el grado guardado coincide con el tamaño real de la lista
     * - suma de grados = 2 · aristas y contador de vértices correcto
     */
    public ArrayList<String> validarIntegridad() {
        ArrayList<String> problemas = new ArrayList<>();
        int contados = 0;
        int sumaGrados = 0;
        NodoVertice v = primero;
        while (v != null) {
            contados++;
            int largo = 0;
            NodoAdyacencia a = v.getPrimerAdyacente();
            while (a != null) {
                largo++;
                NodoVertice d = a.getDestino();
                if (d == v) {
                    problemas.add("Lazo en " + v.getNombre());
                }
                if (!pertenece(d)) {
                    problemas.add(v.getNombre() + " apunta a un vértice eliminado: " + d.getNombre());
                } else if (!existeConexion(d, v)) {
                    problemas.add("Conexión sin simetría: " + v.getNombre() + " -> " + d.getNombre());
                }
                NodoAdyacencia b = a.getSiguiente();
                while (b != null) {
                    if (b.getDestino() == d) {
                        problemas.add("Conexión duplicada: " + v.getNombre() + " — " + d.getNombre());
                    }
                    b = b.getSiguiente();
                }
                a = a.getSiguiente();
            }
            if (largo != v.getGrado()) {
                problemas.add("Grado incorrecto en " + v.getNombre());
            }
            sumaGrados += largo;
            v = v.getSiguiente();
        }
        if (contados != totalVertices) {
            problemas.add("El contador de vértices no coincide");
        }
        if (sumaGrados != 2 * totalAristas) {
            problemas.add("Suma de grados (" + sumaGrados + ") distinta de 2 · aristas (" + 2 * totalAristas + ")");
        }
        return problemas;
    }

    private boolean pertenece(NodoVertice buscado) {
        NodoVertice v = primero;
        while (v != null) {
            if (v == buscado) {
                return true;
            }
            v = v.getSiguiente();
        }
        return false;
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
