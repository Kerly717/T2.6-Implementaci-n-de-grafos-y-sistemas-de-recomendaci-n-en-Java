# Casos de prueba · Grupo 5

Responsable: Jonathan Tenorio ([@sr632252-crypto](https://github.com/sr632252-crypto)) · rama `feature/componentes-pruebas`

Datos usados: `app.DatosPrueba` (13 estudiantes, 17 conexiones).

- **Grupo A (8):** Ana, Bruno, Carla, Diego, Elena, Fabian, Gabriela, Hugo
- **Grupo B (4):** Ivan, Julia, Kevin, Laura (ciclo de 4)
- **Aislado:** Mateo

Las pruebas automáticas están en `src/pruebas/PruebasGrafo.java`. Cada fila de estas tablas tiene el mismo código (CP-xx) que imprime el programa.

## Cómo ejecutarlas

Desde la raíz del proyecto (PowerShell o terminal de VS Code):

```bash
javac -encoding UTF-8 -d out src/modelo/*.java src/estructuras/*.java src/algoritmos/*.java src/app/*.java src/pruebas/*.java
java -cp out pruebas.PruebasGrafo
```

Resultado esperado: `37 aprobadas, 0 fallidas, 37 en total`.

## Grafo base

| Código | Caso | Entrada | Resultado esperado |
|---|---|---|---|
| CP-01 | Carga de datos | `DatosPrueba.crear()` | 13 vértices |
| CP-02 | Carga de datos | `DatosPrueba.crear()` | 17 aristas |
| CP-03 | Registrar estudiante nuevo | `registrarVertice("Ana")` en grafo vacío | `true` |
| CP-04 | Duplicado sin distinguir mayúsculas | `registrarVertice("ANA")` | `false` |
| CP-05 | Nombre vacío | `registrarVertice("   ")` | `false` |
| CP-06 | Nombre null | `registrarVertice(null)` | `false` |
| CP-07 | Conexión no dirigida (a—b) | `existeConexion("Ana","Bruno")` | `true` |
| CP-08 | Conexión no dirigida (b—a) | `existeConexion("Bruno","Ana")` | `true` |
| CP-09 | Conexión repetida | `crearConexion("Bruno","Ana")` | `false` |
| CP-10 | Lazo | `crearConexion("Ana","Ana")` | `false` |
| CP-11 | Estudiante inexistente | `crearConexion("Ana","Zoe")` | `false` |
| CP-12 | Orden de vecinos | `vecinos("Ana")` | `[Bruno, Carla, Diego]` |
| CP-13 | Vecinos de inexistente | `vecinos("Zoe")` | `[]` |
| CP-14 | Grado | grado de `elena` | `4` |
| CP-15 | Densidad | `densidad()` | `34 / 156 ≈ 0.2179` |

## Recomendador

| Código | Caso | Entrada | Resultado esperado |
|---|---|---|---|
| CP-16 | Amigos en común | `amigosEnComun("Ana","Elena")` | `[Bruno, Carla]` |
| CP-17 | Distinto componente | `amigosEnComun("Ana","Ivan")` | `[]` |
| CP-18 | Orden con empate | `recomendar("Ana")` | `Elena(2), Fabian(2), Gabriela(1)` |
| CP-19 | Usuario aislado | `recomendar("Mateo")` | lista vacía |
| CP-20 | Límite | `recomendar("Ana", 1)` | 1 candidato |
| CP-21 | Límite negativo | `recomendar("Ana", -1)` | `IllegalArgumentException` |

## Componentes conectados (reto)

| Código | Caso | Entrada | Resultado esperado |
|---|---|---|---|
| CP-22 | Cantidad | `calcular()` | 3 componentes |
| CP-23 | Componente 1 (orden BFS) | `calcular().get(0)` | `[Ana, Bruno, Carla, Diego, Elena, Fabian, Gabriela, Hugo]` |
| CP-24 | Componente 2 (ciclo) | `calcular().get(1)` | `[Ivan, Julia, Laura, Kevin]` |
| CP-25 | Componente 3 | `calcular().get(2)` | `[Mateo]` |
| CP-26 | Grafo desconectado | `esConexo()` | `false` |
| CP-27 | Mismo componente | `mismoComponente("Ana","Hugo")` | `true` |
| CP-28 | Distinto componente | `mismoComponente("Ana","Ivan")` | `false` |
| CP-29 | Componente de un estudiante | `componenteDe("kevin")` | `[Kevin, Julia, Laura, Ivan]` |
| CP-30 | Estudiante inexistente | `componenteDe("Zoe")` | `[]` |
| CP-31 | Componente más grande | `masGrande()` | 8 estudiantes |
| CP-32 | Aislados | `aislados()` | `[Mateo]` |
| CP-33 | Unir grupos | conectar Hugo—Ivan y Laura—Mateo | `esConexo()` = `true` |
| CP-34 | Sin aislados | después de CP-33 | `aislados()` vacío |
| CP-35 | Grafo vacío | `cantidad()` | `0` |
| CP-36 | Grafo vacío | `esConexo()` | `false` |
| CP-37 | Grafo null | `new ComponentesConectados(null)` | `IllegalArgumentException` |

## Cómo funciona el reto

`ComponentesConectados` recorre la lista de vértices y lanza un **BFS** desde cada estudiante que aún no fue visitado. Cada BFS descubre un componente completo y marca a sus integrantes en un arreglo `boolean[]` indexado con `Grafo.indiceDe`. La cola del BFS es un arreglo propio, sin `HashMap`, `HashSet`, `TreeMap` ni `TreeSet`.

Complejidad: O(V + E) para el recorrido.

Salida del reporte con los datos de prueba:

```
Componentes conectados: 3 (grafo desconectado)
  C1 (8): Ana, Bruno, Carla, Diego, Elena, Fabian, Gabriela, Hugo
  C2 (4): Ivan, Julia, Laura, Kevin
  C3 (1): Mateo
Aislados: Mateo
```
