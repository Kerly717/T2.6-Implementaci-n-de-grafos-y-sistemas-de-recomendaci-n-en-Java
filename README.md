# T2.6 · Implementación de grafos y sistemas de recomendación en Java · Grupo 5

Red de grupos de estudio: estudiantes como vértices y conexiones de estudio como aristas (grafo no dirigido con listas de adyacencia de nodos propios).

> Documentación completa en construcción.

## Integrantes

| Integrante | GitHub | Rama | Responsabilidad |
|---|---|---|---|
| Rommel Gamboa | [@gamboarommel580-rgb](https://github.com/gamboarommel580-rgb) | `feature/grafo`, `feature/menu-integracion` | Nodos, grafo base, datos de prueba, menú e integración |
| Esteban Manobanda | [@Esteban-EVIL](https://github.com/Esteban-EVIL) | `feature/crud-integridad` | Eliminar conexión, eliminar estudiante y validación de integridad |
| Kerly Espinoza | [@kerlyespinoza298-dev](https://github.com/kerlyespinoza298-dev) | `feature/bfs-dfs` | Cola, pila, BFS y DFS con traza |
| Gabriel Jijón | [@Gabriel143445](https://github.com/Gabriel143445) | `feature/recomendador` | Vecinos comunes, candidatos y ordenamiento |
| Jonathan Tenorio | [@sr632252-crypto](https://github.com/sr632252-crypto) | `feature/componentes-pruebas` | Componentes conectados (reto), pruebas y casos de prueba |

## Pruebas

```bash
javac -encoding UTF-8 -d out src/modelo/*.java src/estructuras/*.java src/algoritmos/*.java src/app/*.java src/pruebas/*.java
java -cp out pruebas.PruebasGrafo
```

Detalle de cada caso en [docs/casos-de-prueba.md](docs/casos-de-prueba.md).
