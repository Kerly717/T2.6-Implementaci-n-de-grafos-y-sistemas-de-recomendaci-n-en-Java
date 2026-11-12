package pruebas;

import algoritmos.ComponentesConectados;
import algoritmos.Recomendador;
import app.DatosPrueba;
import estructuras.Grafo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import modelo.Candidato;

/**
 * Pruebas automáticas del proyecto (sin JUnit, solo Java estándar).
 * Cada caso tiene un código CP-xx que coincide con docs/casos-de-prueba.md.
 *
 * Ejecutar desde la raíz del proyecto:
 *   javac -d out $(find src -name "*.java")
 *   java -cp out pruebas.PruebasGrafo
 */
public final class PruebasGrafo {

    private static int aprobadas;
    private static int fallidas;

    private PruebasGrafo() {
    }

    public static void main(String[] args) {
        System.out.println("=== Pruebas del grafo · Grupo 5 ===");
        pruebasGrafo();
        pruebasRecomendador();
        pruebasComponentes();
        System.out.println();
        System.out.printf("Resultado: %d aprobadas, %d fallidas, %d en total%n",
                aprobadas, fallidas, aprobadas + fallidas);
        if (fallidas > 0) {
            System.exit(1);
        }
    }

    // ===================== Grafo base =====================

    private static void pruebasGrafo() {
        System.out.println();
        System.out.println("-- Grafo --");
        Grafo g = DatosPrueba.crear();
        verificar("CP-01 Carga de datos: 13 estudiantes", 13, g.getTotalVertices());
        verificar("CP-02 Carga de datos: 17 conexiones", 17, g.getTotalAristas());

        Grafo vacio = new Grafo();
        verificar("CP-03 Registrar estudiante nuevo", true, vacio.registrarVertice("Ana"));
        verificar("CP-04 Rechaza duplicado (sin distinguir mayúsculas)", false, vacio.registrarVertice("ANA"));
        verificar("CP-05 Rechaza nombre vacío", false, vacio.registrarVertice("   "));
        verificar("CP-06 Rechaza nombre null", false, vacio.registrarVertice(null));

        verificar("CP-07 Conexión no dirigida (a—b)", true, g.existeConexion("Ana", "Bruno"));
        verificar("CP-08 Conexión no dirigida (b—a)", true, g.existeConexion("Bruno", "Ana"));
        verificar("CP-09 Rechaza conexión repetida", false, g.crearConexion("Bruno", "Ana"));
        verificar("CP-10 Rechaza lazo (a—a)", false, g.crearConexion("Ana", "Ana"));
        verificar("CP-11 Rechaza conexión con estudiante inexistente", false, g.crearConexion("Ana", "Zoe"));
        verificar("CP-12 Vecinos en orden de inserción", lista("Bruno", "Carla", "Diego"), g.vecinos("Ana"));
        verificar("CP-13 Vecinos de inexistente: lista vacía", lista(), g.vecinos("Zoe"));
        verificar("CP-14 Grado de Elena", 4, g.buscarVertice("elena").getGrado());
        verificar("CP-15 Densidad 2E/[V(V-1)]", 34.0 / 156.0, g.densidad());
    }

    // ===================== Recomendador =====================

    private static void pruebasRecomendador() {
        System.out.println();
        System.out.println("-- Recomendador --");
        Recomendador r = new Recomendador(DatosPrueba.crear());
        verificar("CP-16 Amigos en común Ana–Elena", lista("Bruno", "Carla"), r.amigosEnComun("Ana", "Elena"));
        verificar("CP-17 Sin amigos en común entre componentes", lista(), r.amigosEnComun("Ana", "Ivan"));

        ArrayList<String> nombres = new ArrayList<>();
        for (Candidato c : r.recomendar("Ana")) {
            nombres.add(c.getNombre() + ":" + c.getCantidadAmigosEnComun());
        }
        verificar("CP-18 Recomendaciones de Ana ordenadas (empate por nombre)",
                lista("Elena:2", "Fabian:2", "Gabriela:1"), nombres);
        verificar("CP-19 Aislado no recibe recomendaciones", 0, r.recomendar("Mateo").size());
        verificar("CP-20 Límite de recomendaciones", 1, r.recomendar("Ana", 1).size());
        verificarExcepcion("CP-21 Límite negativo lanza excepción", () -> r.recomendar("Ana", -1));
    }

    // ===================== Componentes conectados (reto) =====================

    private static void pruebasComponentes() {
        System.out.println();
        System.out.println("-- Componentes conectados (reto) --");
        ComponentesConectados cc = new ComponentesConectados(DatosPrueba.crear());
        ArrayList<ArrayList<String>> comps = cc.calcular();

        verificar("CP-22 Datos de prueba: 3 componentes", 3, comps.size());
        verificar("CP-23 Componente 1 (Grupo A, orden BFS)",
                lista("Ana", "Bruno", "Carla", "Diego", "Elena", "Fabian", "Gabriela", "Hugo"), comps.get(0));
        verificar("CP-24 Componente 2 (Grupo B, ciclo)", lista("Ivan", "Julia", "Laura", "Kevin"), comps.get(1));
        verificar("CP-25 Componente 3 (aislado)", lista("Mateo"), comps.get(2));
        verificar("CP-26 Grafo desconectado no es conexo", false, cc.esConexo());
        verificar("CP-27 Mismo componente (Ana, Hugo)", true, cc.mismoComponente("Ana", "Hugo"));
        verificar("CP-28 Distinto componente (Ana, Ivan)", false, cc.mismoComponente("Ana", "Ivan"));
        verificar("CP-29 Componente de un estudiante (sin distinguir mayúsculas)",
                lista("Kevin", "Julia", "Laura", "Ivan"), cc.componenteDe("kevin"));
        verificar("CP-30 Componente de inexistente: vacío", lista(), cc.componenteDe("Zoe"));
        verificar("CP-31 Componente más grande", 8, cc.masGrande().size());
        verificar("CP-32 Estudiantes aislados", lista("Mateo"), cc.aislados());

        Grafo g = DatosPrueba.crear();
        g.crearConexion("Hugo", "Ivan");
        g.crearConexion("Laura", "Mateo");
        ComponentesConectados unido = new ComponentesConectados(g);
        verificar("CP-33 Al unir los grupos el grafo es conexo", true, unido.esConexo());
        verificar("CP-34 Sin aislados tras conectar a Mateo", 0, unido.aislados().size());

        ComponentesConectados vacio = new ComponentesConectados(new Grafo());
        verificar("CP-35 Grafo vacío: 0 componentes", 0, vacio.cantidad());
        verificar("CP-36 Grafo vacío no es conexo", false, vacio.esConexo());
        verificarExcepcion("CP-37 Grafo null lanza excepción", () -> new ComponentesConectados(null));

        System.out.println();
        System.out.println(cc.reporte());
    }

    // ===================== Utilidades =====================

    private static List<String> lista(String... valores) {
        return Arrays.asList(valores);
    }

    private static void verificar(String caso, Object esperado, Object obtenido) {
        boolean ok;
        if (esperado instanceof Double && obtenido instanceof Double) {
            ok = Math.abs((Double) esperado - (Double) obtenido) < 1e-9;
        } else if (esperado instanceof List && obtenido instanceof List) {
            ok = new ArrayList<>((List<?>) esperado).equals(new ArrayList<>((List<?>) obtenido));
        } else {
            ok = esperado.equals(obtenido);
        }
        registrar(caso, ok, ok ? "" : "esperado " + esperado + ", obtenido " + obtenido);
    }

    private static void verificarExcepcion(String caso, Runnable accion) {
        try {
            accion.run();
            registrar(caso, false, "no lanzó IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            registrar(caso, true, "");
        }
    }

    private static void registrar(String caso, boolean ok, String detalle) {
        if (ok) {
            aprobadas++;
            System.out.println("  [OK]    " + caso);
        } else {
            fallidas++;
            System.out.println("  [FALLA] " + caso + " -> " + detalle);
        }
    }
}
