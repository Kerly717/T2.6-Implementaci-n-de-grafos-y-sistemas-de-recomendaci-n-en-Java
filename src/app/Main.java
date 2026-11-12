package app;

import algoritmos.Recomendador;
import algoritmos.Recorridos;
import estructuras.Grafo;
import java.util.ArrayList;
import java.util.Scanner;
import modelo.Candidato;
import modelo.NodoVertice;

/**
 * T3 · Laboratorio de grafos y predicción de conexiones
 * Grupo 5 · Red de grupos de estudio
 * Vértices = estudiantes, aristas = conexiones de estudio (no dirigidas).
 */
public class Main {

    private static Grafo grafo = DatosPrueba.crear();
    private static Scanner entrada;

    public static void main(String[] args) {
        entrada = new Scanner(System.in);
        System.out.println("Se cargaron los datos de prueba: "
                + grafo.getTotalVertices() + " estudiantes y " + grafo.getTotalAristas() + " conexiones.");
        boolean seguir = true;
        while (seguir) {
            mostrarMenu();
            if (!entrada.hasNextLine()) {
                break;
            }
            String opcion = entrada.nextLine().trim();
            switch (opcion) {
                case "1" -> registrar();
                case "2" -> crearConexion();
                case "3" -> eliminarConexion();
                case "4" -> eliminarEstudiante();
                case "5" -> visualizar();
                case "6" -> recorrerBfs();
                case "7" -> recorrerDfs();
                case "8" -> vecinosComunes();
                case "9" -> recomendar();
                case "10" -> estadisticas();
                case "11" -> componentes();
                case "12" -> validarIntegridad();
                case "13" -> {
                    grafo = DatosPrueba.crear();
                    System.out.println("Datos de prueba recargados.");
                }
                case "0" -> {
                    System.out.println("Fin del programa.");
                    seguir = false;
                }
                default -> System.out.println("Opción inválida: escriba un número del 0 al 13.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("=========== RED DE GRUPOS DE ESTUDIO · GRUPO 5 ===========");
        System.out.println(" 1. Registrar estudiante");
        System.out.println(" 2. Crear conexión");
        System.out.println(" 3. Eliminar conexión");
        System.out.println(" 4. Eliminar estudiante");
        System.out.println(" 5. Visualizar grafo");
        System.out.println(" 6. Recorrer mediante BFS");
        System.out.println(" 7. Recorrer mediante DFS");
        System.out.println(" 8. Mostrar vecinos comunes");
        System.out.println(" 9. Recomendar conexiones");
        System.out.println("10. Mostrar estadísticas");
        System.out.println("11. Detectar componentes conectados (reto)");
        System.out.println("12. Validar integridad");
        System.out.println("13. Recargar datos de prueba");
        System.out.println(" 0. Salir");
        System.out.print("Opción: ");
    }

    // ---------- lectura con validación ----------

    /** Pide un texto no vacío, con solo letras y espacios. */
    private static String leerNombre(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (!entrada.hasNextLine()) {
                return "";
            }
            String texto = entrada.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("  El nombre no puede estar vacío.");
            } else if (!texto.matches("[\\p{L} ]{2,40}")) {
                System.out.println("  Use solo letras (2 a 40 caracteres).");
            } else {
                return texto;
            }
        }
    }

    /** Pide el nombre de un estudiante que ya exista en el grafo. null si no existe. */
    private static NodoVertice leerExistente(String mensaje) {
        String nombre = leerNombre(mensaje);
        NodoVertice v = grafo.buscarVertice(nombre);
        if (v == null) {
            System.out.println("  No existe el estudiante \"" + nombre + "\".");
        }
        return v;
    }

    // ---------- opciones ----------

    private static void registrar() {
        String nombre = leerNombre("Nombre del estudiante: ");
        if (grafo.registrarVertice(nombre)) {
            System.out.println("Estudiante registrado: " + nombre);
        } else {
            System.out.println("No se registró: \"" + nombre + "\" ya existe.");
        }
    }

    private static void crearConexion() {
        NodoVertice a = leerExistente("Primer estudiante: ");
        if (a == null) {
            return;
        }
        NodoVertice b = leerExistente("Segundo estudiante: ");
        if (b == null) {
            return;
        }
        if (a == b) {
            System.out.println("No se permite conectar a un estudiante consigo mismo.");
        } else if (grafo.crearConexion(a.getNombre(), b.getNombre())) {
            System.out.println("Conexión creada: " + a + " — " + b);
        } else {
            System.out.println("La conexión " + a + " — " + b + " ya existe.");
        }
    }

    private static void eliminarConexion() {
        NodoVertice a = leerExistente("Primer estudiante: ");
        if (a == null) {
            return;
        }
        NodoVertice b = leerExistente("Segundo estudiante: ");
        if (b == null) {
            return;
        }
        if (grafo.eliminarConexion(a.getNombre(), b.getNombre())) {
            System.out.println("Conexión eliminada en ambos extremos: " + a + " — " + b);
        } else {
            System.out.println("No existe la conexión " + a + " — " + b + ".");
        }
    }

    private static void eliminarEstudiante() {
        NodoVertice v = leerExistente("Estudiante a eliminar: ");
        if (v == null) {
            return;
        }
        ArrayList<String> antes = grafo.vecinos(v.getNombre());
        grafo.eliminarVertice(v.getNombre());
        System.out.println("Estudiante eliminado: " + v + ". Conexiones retiradas con: "
                + (antes.isEmpty() ? "(ninguna)" : String.join(", ", antes)));
    }

    private static void visualizar() {
        System.out.println("Listas de adyacencia (" + grafo.getTotalVertices() + " estudiantes, "
                + grafo.getTotalAristas() + " conexiones):");
        System.out.println(grafo.mostrar());
    }

    private static void recorrerBfs() {
        NodoVertice v = leerExistente("Estudiante de inicio: ");
        if (v == null) {
            return;
        }
        StringBuilder traza = new StringBuilder();
        ArrayList<String> orden = Recorridos.bfs(grafo, v.getNombre(), traza);
        System.out.print(traza);
        System.out.println("BFS desde " + v + ": " + String.join(" -> ", orden));
    }

    private static void recorrerDfs() {
        NodoVertice v = leerExistente("Estudiante de inicio: ");
        if (v == null) {
            return;
        }
        StringBuilder traza = new StringBuilder();
        ArrayList<String> orden = Recorridos.dfs(grafo, v.getNombre(), traza);
        System.out.print(traza);
        System.out.println("DFS desde " + v + ": " + String.join(" -> ", orden));
    }

    private static void vecinosComunes() {
        NodoVertice a = leerExistente("Primer estudiante: ");
        if (a == null) {
            return;
        }
        NodoVertice b = leerExistente("Segundo estudiante: ");
        if (b == null) {
            return;
        }
        ArrayList<String> comunes = new Recomendador(grafo).amigosEnComun(a.getNombre(), b.getNombre());
        System.out.println("Compañeros en común de " + a + " y " + b + ": "
                + (comunes.isEmpty() ? "(ninguno)" : String.join(", ", comunes)));
        System.out.println("Puntuación: " + comunes.size());
        if (grafo.existeConexion(a, b)) {
            System.out.println("(Ya son compañeros directos.)");
        }
    }

    private static void recomendar() {
        NodoVertice v = leerExistente("Estudiante: ");
        if (v == null) {
            return;
        }
        if (v.getGrado() == 0) {
            System.out.println(v + " no tiene compañeros de estudio: no hay a quién recomendar por conexiones comunes.");
            return;
        }
        ArrayList<Candidato> mejores = new Recomendador(grafo).recomendar(v.getNombre(), 3);
        if (mejores.isEmpty()) {
            System.out.println("No hay candidatos: todos los compañeros de sus compañeros ya están conectados con " + v + ".");
            return;
        }
        System.out.println("Compañeros de estudio recomendados para " + v + ":");
        for (int i = 0; i < mejores.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + mejores.get(i));
        }
    }

    private static void estadisticas() {
        System.out.println("Estudiantes: " + grafo.getTotalVertices());
        System.out.println("Conexiones:  " + grafo.getTotalAristas());
        System.out.printf("Densidad:    %.2f %%%n", grafo.densidad() * 100);
        if (grafo.estaVacio()) {
            return;
        }
        NodoVertice mayor = grafo.getPrimero();
        int suma = 0;
        ArrayList<String> aislados = new ArrayList<>();
        NodoVertice v = grafo.getPrimero();
        while (v != null) {
            suma += v.getGrado();
            if (v.getGrado() > mayor.getGrado()) {
                mayor = v;
            }
            if (v.getGrado() == 0) {
                aislados.add(v.getNombre());
            }
            v = v.getSiguiente();
        }
        System.out.printf("Grado promedio: %.2f%n", (double) suma / grafo.getTotalVertices());
        System.out.println("Mayor grado: " + mayor + " (" + mayor.getGrado() + " compañeros)");
        System.out.println("Aislados:    " + (aislados.isEmpty() ? "(ninguno)" : String.join(", ", aislados)));
        System.out.println("Componentes conectados: " + Recorridos.componentesConectados(grafo).size());
    }

    private static void componentes() {
        ArrayList<ArrayList<String>> comps = Recorridos.componentesConectados(grafo);
        System.out.println("Grupos de estudio detectados (componentes conectados): " + comps.size());
        for (int i = 0; i < comps.size(); i++) {
            ArrayList<String> c = comps.get(i);
            System.out.println("  Grupo " + (i + 1) + " (" + c.size() + "): " + String.join(", ", c)
                    + (c.size() == 1 ? "  <- estudiante aislado" : ""));
        }
    }

    private static void validarIntegridad() {
        ArrayList<String> problemas = grafo.validarIntegridad();
        if (problemas.isEmpty()) {
            System.out.println("Integridad correcta: sin duplicados, sin lazos, sin referencias inválidas, "
                    + "conexiones simétricas y suma de grados = 2 · conexiones.");
        } else {
            System.out.println("Se encontraron " + problemas.size() + " problemas:");
            for (String p : problemas) {
                System.out.println("  - " + p);
            }
        }
    }
}
