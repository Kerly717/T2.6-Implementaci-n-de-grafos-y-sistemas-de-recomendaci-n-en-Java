package estructuras;

import modelo.NodoVertice;

/** Pila LIFO propia de vértices, usada por DFS. */
public class Pila {

    private static class Nodo {
        final NodoVertice dato;
        final Nodo debajo;

        Nodo(NodoVertice dato, Nodo debajo) {
            this.dato = dato;
            this.debajo = debajo;
        }
    }

    private Nodo cima;

    public void apilar(NodoVertice v) {
        cima = new Nodo(v, cima);
    }

    public NodoVertice desapilar() {
        if (cima == null) {
            return null;
        }
        NodoVertice dato = cima.dato;
        cima = cima.debajo;
        return dato;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    /** Contenido de la pila, de la cima hacia abajo, para la traza. */
    public String contenido() {
        StringBuilder sb = new StringBuilder("[");
        Nodo n = cima;
        while (n != null) {
            sb.append(n.dato.getNombre());
            if (n.debajo != null) {
                sb.append(", ");
            }
            n = n.debajo;
        }
        return sb.append("]").toString();
    }
}
