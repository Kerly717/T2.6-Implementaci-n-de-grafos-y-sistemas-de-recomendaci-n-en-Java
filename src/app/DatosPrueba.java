package app;

import estructuras.Grafo;

/**
 * Red de grupos de estudio de prueba: 13 estudiantes y 17 conexiones.
 *  - Grupo A (8): Ana, Bruno, Carla, Diego, Elena, Fabian, Gabriela, Hugo
 *  - Grupo B (4): Ivan, Julia, Kevin, Laura (forman un ciclo de 4)
 *  - Mateo: estudiante aislado (sin compañeros)
 * Así se prueban empates, un usuario aislado y un grafo desconectado (3 componentes).
 */
public final class DatosPrueba {

    public static final String[] ESTUDIANTES = {
        "Ana", "Bruno", "Carla", "Diego", "Elena", "Fabian", "Gabriela", "Hugo",
        "Ivan", "Julia", "Kevin", "Laura", "Mateo"
    };

    public static final String[][] CONEXIONES = {
        {"Ana", "Bruno"}, {"Ana", "Carla"}, {"Ana", "Diego"},
        {"Bruno", "Carla"}, {"Bruno", "Elena"},
        {"Carla", "Elena"}, {"Carla", "Fabian"},
        {"Diego", "Fabian"}, {"Diego", "Gabriela"},
        {"Elena", "Hugo"}, {"Elena", "Fabian"},
        {"Fabian", "Hugo"}, {"Gabriela", "Hugo"},
        {"Ivan", "Julia"}, {"Julia", "Kevin"}, {"Kevin", "Laura"}, {"Laura", "Ivan"}
    };

    private DatosPrueba() {
    }

    public static Grafo crear() {
        Grafo grafo = new Grafo();
        for (String e : ESTUDIANTES) {
            grafo.registrarVertice(e);
        }
        for (String[] c : CONEXIONES) {
            grafo.crearConexion(c[0], c[1]);
        }
        return grafo;
    }
}
