package estructuras;

import modelo.NodoVertice;

/** Cola FIFO propia de vértices, usada por BFS. */
public class Cola {

    private static class Nodo {
        final NodoVertice dato;
        Nodo siguiente;

        Nodo(NodoVertice dato) {
            this.dato = dato;
        }
    }

    private Nodo frente;
    private Nodo fin;

    public void encolar(NodoVertice v) {
        Nodo nuevo = new Nodo(v);
        if (fin == null) {
            frente = nuevo;
        } else {
            fin.siguiente = nuevo;
        }
        fin = nuevo;
    }

    public NodoVertice desencolar() {
        if (frente == null) {
            return null;
        }
        NodoVertice dato = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        return dato;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    /** Contenido de la cola, del frente al final, para la traza. */
    public String contenido() {
        StringBuilder sb = new StringBuilder("[");
        Nodo n = frente;
        while (n != null) {
            sb.append(n.dato.getNombre());
            if (n.siguiente != null) {
                sb.append(", ");
            }
            n = n.siguiente;
        }
        return sb.append("]").toString();
    }
}
