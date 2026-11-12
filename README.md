# T2.6 · Implementación de grafos y sistemas de recomendación en Java · Grupo 5

Universidad Técnica de Ambato · FICI · Carrera de Software · Estructura de Datos

Red de grupos de estudio: cada **estudiante es un vértice** y cada **conexión de estudio es una arista**. El grafo es **no dirigido** y se implementa con **listas de adyacencia de nodos propios**. Sobre él se hacen recorridos (BFS y DFS), recomendaciones de nuevos compañeros por amigos en común y el cálculo de componentes conectados.

## Integrantes

| Integrante | GitHub | Rama | Responsabilidad |
|---|---|---|---|
| Rommel Gamboa | [@gamboarommel580-rgb](https://github.com/gamboarommel580-rgb) | `feature/grafo` | Nodos, grafo base, datos de prueba, menú e integración |
| Esteban Manobanda | [@Esteban-EVIL](https://github.com/Esteban-EVIL) | `feature/esteban-grafos-corregida` | Eliminar conexión, eliminar estudiante y validación de integridad |
| Kerly Espinoza | [@kerlyespinoza298-dev](https://github.com/kerlyespinoza298-dev) | `feature/kerly-espinoza/bfs-dfs` | Cola, pila, BFS y DFS con traza |
| Gabriel Jijón | [@Gabriel143445](https://github.com/Gabriel143445) | `feature/gabriel-jijon-recomendador` | Vecinos comunes, candidatos y ordenamiento |
| Jonathan Tenorio | [@sr632252-crypto](https://github.com/sr632252-crypto) | `feature/componentes-pruebas` | Componentes conectados (reto), pruebas y casos de prueba |

## Requisitos funcionales

| Código | Requisito | Clase |
|---|---|---|
| RF01 | Registrar estudiante (vértice), sin duplicados ni nombres vacíos | `Grafo` |
| RF02 | Crear conexión no dirigida, sin lazos ni repetidas | `Grafo` |
| RF03 | Eliminar conexión | `Grafo` |
| RF04 | Eliminar estudiante y todas sus conexiones | `Grafo` |
| RF05 | Mostrar el grafo como listas de adyacencia | `Grafo` |
| RF06 | Recorrido BFS con traza (usa `Cola` propia) | `Recorridos` |
| RF07 | Recorrido DFS con traza (usa `Pila` propia) | `Recorridos` |
| RF08 | Amigos en común entre dos estudiantes | `Recomendador` |
| RF09 | Recomendar compañeros ordenados por amigos en común | `Recomendador` |
| RF10 | Validar la integridad del grafo | `Grafo` |
| Reto | Componentes conectados del grafo | `ComponentesConectados` |

## Diseño del grafo

```
primero -> [ANA] -> [BRUNO] -> [CARLA] -> ... -> null      (lista de vértices)
             |
             +--> (BRUNO) -> (CARLA) -> (DIEGO) -> null     (lista de adyacencia de ANA)
```

- `NodoVertice`: un estudiante; es nodo de la lista de vértices y cabeza de su propia lista de adyacencia.
- `NodoAdyacencia`: eslabón de la lista de adyacencia; apunta al vértice vecino (no a una copia del nombre).
- Los nombres se buscan sin distinguir mayúsculas.
- Vértices y vecinos se agregan al final, así los recorridos siempre dan el mismo resultado.
- No se usa `HashMap`, `HashSet`, `TreeMap` ni `TreeSet`. `ArrayList` solo se usa para devolver resultados.

## Estructura del proyecto

```
src/
├── modelo/
│   ├── NodoVertice.java          vértice (estudiante)
│   ├── NodoAdyacencia.java       eslabón de la lista de adyacencia
│   └── Candidato.java            estudiante recomendado y sus amigos en común
├── estructuras/
│   ├── Grafo.java                grafo no dirigido con listas de adyacencia
│   ├── Cola.java                 cola propia (BFS)
│   └── Pila.java                 pila propia (DFS)
├── algoritmos/
│   ├── Recorridos.java           BFS y DFS con traza
│   ├── Recomendador.java         amigos en común y recomendaciones
│   └── ComponentesConectados.java  reto: componentes conectados
├── app/
│   └── DatosPrueba.java          red de prueba (13 estudiantes, 17 conexiones)
└── pruebas/
    └── PruebasGrafo.java         pruebas automáticas (37 casos)
docs/
└── casos-de-prueba.md            tabla de casos de prueba
```

## Datos de prueba

`DatosPrueba` crea 13 estudiantes y 17 conexiones en tres grupos, para probar empates, un usuario aislado y un grafo desconectado:

- **Grupo A (8):** Ana, Bruno, Carla, Diego, Elena, Fabian, Gabriela, Hugo
- **Grupo B (4):** Ivan, Julia, Kevin, Laura (forman un ciclo)
- **Aislado:** Mateo

## Requisitos para ejecutar

- Java JDK 17 o superior
- VS Code con Extension Pack for Java (opcional)

## Compilar y ejecutar las pruebas

Desde la raíz del proyecto:

```bash
javac -encoding UTF-8 -d out src/modelo/*.java src/estructuras/*.java src/algoritmos/*.java src/app/*.java src/pruebas/*.java
java -cp out pruebas.PruebasGrafo
```

Resultado esperado: `37 aprobadas, 0 fallidas, 37 en total`. El detalle de cada caso está en [docs/casos-de-prueba.md](docs/casos-de-prueba.md).

## Reto: componentes conectados

Se lanza un BFS desde cada estudiante que todavía no fue visitado; cada BFS encuentra un componente completo. Complejidad O(V + E).

Con los datos de prueba:

```
Componentes conectados: 3 (grafo desconectado)
  C1 (8): Ana, Bruno, Carla, Diego, Elena, Fabian, Gabriela, Hugo
  C2 (4): Ivan, Julia, Laura, Kevin
  C3 (1): Mateo
Aislados: Mateo
```

## Flujo de trabajo en GitHub

Cada integrante trabaja en su rama `feature/...` y la integra a `main` mediante Pull Request.
