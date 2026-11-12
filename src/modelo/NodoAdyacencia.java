package modelo;

/**
 * Eslabón de la lista de adyacencia de un vértice.
 * Guarda una referencia al vértice vecino (no una copia del nombre),
 * así la conexión siempre apunta a un vértice que existe en el grafo.
 */
public class NodoAdyacencia {

    private final NodoVertice destino;
    private NodoAdyacencia siguiente;

    public NodoAdyacencia(NodoVertice destino) {
        if (destino == null) {
            throw new IllegalArgumentException("El destino de la conexión no puede ser null");
        }
        this.destino = destino;
    }

    public NodoVertice getDestino() {
        return destino;
    }

    public NodoAdyacencia getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoAdyacencia siguiente) {
        this.siguiente = siguiente;
    }
}
