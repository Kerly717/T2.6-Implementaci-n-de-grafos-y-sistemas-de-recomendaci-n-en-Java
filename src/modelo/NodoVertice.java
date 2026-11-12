package modelo;

/**
 * Vértice del grafo: un estudiante de la red de grupos de estudio.
 * Cada vértice es un nodo de la lista principal de vértices (siguiente)
 * y es la cabeza de su propia lista de adyacencia (primerAdyacente).
 */
public class NodoVertice {

    private final String id;        // clave única, en mayúsculas (búsqueda sin distinguir mayúsculas)
    private final String nombre;    // nombre tal como se muestra
    private NodoAdyacencia primerAdyacente;
    private NodoVertice siguiente;
    private int grado;

    public NodoVertice(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del estudiante no puede estar vacío");
        }
        this.nombre = nombre.trim();
        this.id = normalizar(nombre);
    }

    /** Convierte un nombre en la clave que usa el grafo. */
    public static String normalizar(String texto) {
        return texto == null ? "" : texto.trim().toUpperCase();
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public NodoAdyacencia getPrimerAdyacente() {
        return primerAdyacente;
    }

    public void setPrimerAdyacente(NodoAdyacencia primerAdyacente) {
        this.primerAdyacente = primerAdyacente;
    }

    public NodoVertice getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoVertice siguiente) {
        this.siguiente = siguiente;
    }

    public int getGrado() {
        return grado;
    }

    public void aumentarGrado() {
        grado++;
    }

    public void disminuirGrado() {
        grado--;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
