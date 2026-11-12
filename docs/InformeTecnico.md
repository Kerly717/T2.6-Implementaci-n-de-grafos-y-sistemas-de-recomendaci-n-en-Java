# Informe técnico · T3 Laboratorio de grafos · Grupo 5

**Caso:** red de grupos de estudio (estudiantes = vértices, conexiones de estudio = aristas no dirigidas).
**Reto:** recomendar compañeros de estudio y detectar componentes conectados.

## 1. Decisiones de diseño

### ¿Por qué listas de adyacencia y no matriz?
- Una red de estudio es **dispersa**: cada estudiante estudia con pocos compañeros. Con los datos de prueba la densidad es
  2·17 / (13·12) = **21,8 %**; en un curso real de 40 estudiantes con unos 4 compañeros cada uno sería ≈ 10 %.
- **Memoria:** la lista ocupa O(V + E) = 13 vértices + 34 referencias; la matriz ocuparía 13 × 13 = 169 celdas, la mayoría en 0.
- **Consultas dominantes:** BFS, DFS, componentes y recomendaciones **recorren vecinos**. Con lista cuestan O(grado) por
  vértice; con matriz, O(V) por vértice aunque el estudiante tenga un solo compañero.
- **Cambios:** registrar un estudiante es agregar un nodo al final; con matriz habría que crear otra más grande y copiarla.

### Nodos propios
- `NodoVertice`: nombre, clave en mayúsculas, grado, `primerAdyacente` y `siguiente` (lista de vértices).
- `NodoAdyacencia`: referencia al `NodoVertice` vecino y `siguiente`. Al guardar la referencia y no el nombre, una
  conexión nunca apunta a un texto suelto; la validación de integridad comprueba que el vecino siga en el grafo.
- `Cola` y `Pila` propias (nodos enlazados) para BFS y DFS.

### Determinismo
Vértices y vecinos se insertan al final de su lista, los recorridos siguen ese orden y los empates de la recomendación se
resuelven por nombre. Los mismos datos producen siempre la misma salida, por eso las pruebas son reproducibles.

## 2. Algoritmos y complejidad

| Operación | Idea | Tiempo | Espacio extra |
|---|---|---|---|
| Registrar / buscar | Recorrer la lista de vértices | O(V) | O(1) |
| Crear / eliminar conexión | Buscar ambos y actualizar las dos listas | O(V + grado) | O(1) |
| Eliminar estudiante | Quitarlo de la lista de cada vecino y de la lista de vértices | O(V + Σ grado vecinos) | O(1) |
| BFS / DFS | Cola o pila + `boolean[] visitado` | O(V + E)* | O(V) |
| Componentes conectados | BFS desde cada vértice no asignado (`Recorridos.componentesConectados` y `ComponentesConectados`) | O(V + E)* | O(V) |
| Amigos en común (a, b) | Para cada vecino de a, ¿está conectado con b? | O(V + grado(a) · (V + grado máx.)) | O(grado(a)) |
| Recomendar | Evaluar cada vértice no conectado una vez + ordenar k candidatos | O(V · (V + grado(u) · grado máx.) + k log k) | O(k) |
| Validar integridad | Revisar cada eslabón, simetría y duplicados | O(V · (V + E)) | O(1) |

\* `indiceDe` recorre la lista de vértices (O(V)) para obtener la posición en el arreglo de visitados; con los tamaños del
laboratorio es despreciable. Una mejora sería guardar el índice dentro de cada `NodoVertice`.

## 3. Validaciones

- Menú: opción numérica 0–13; nombres no vacíos, solo letras, 2 a 40 caracteres; estudiante existente para conexiones,
  recorridos y recomendaciones.
- Negocio (`Grafo`): sin duplicados (no distingue mayúsculas), sin lazos, sin conexiones repetidas, eliminación en ambos
  extremos, eliminación de un estudiante con todas sus conexiones.
- Integridad (opción 12 / RF10): simetría, sin lazos, sin duplicados, sin referencias a vértices eliminados, grado
  guardado = tamaño real de la lista, Σ grados = 2·E.

## 4. Pruebas

`src/pruebas/PruebasGrafo.java` ejecuta **37 casos** (resultado real: 37 aprobadas, 0 fallidas) sobre el grafo, el
recomendador y los componentes conectados. Los casos y sus salidas están en `pruebas/CasosPrueba.md`.
Además, el menú (`java -cp out app.Main`) se probó con entradas válidas e inválidas: duplicados, lazos, estudiantes
inexistentes, eliminación de conexiones y de estudiantes, y validación de integridad después de cada operación.

## 5. Resultados principales

| Consulta | Resultado real |
|---|---|
| BFS(Ana) | Ana, Bruno, Carla, Diego, Elena, Fabian, Gabriela, Hugo |
| DFS(Ana) | Ana, Bruno, Carla, Elena, Hugo, Fabian, Diego, Gabriela |
| Recomendar(Ana) | Elena (2), Fabian (2), Gabriela (1) |
| Recomendar(Ana) tras Ana—Elena | Fabian (3), Gabriela (1), Hugo (1) |
| Componentes | {Ana…Hugo} (8), {Ivan, Julia, Laura, Kevin} (4), {Mateo} (1) |

## 6. Conclusiones

1. La lista de adyacencia con nodos propios es adecuada para una red de estudio dispersa: ahorra memoria y los recorridos
   solo visitan conexiones reales.
2. BFS es el recorrido natural para recomendar (los candidatos están a distancia 2) y para encontrar componentes; DFS
   explora una rama completa antes de volver y sirve para verificar alcanzabilidad.
3. El puntaje por amigos en común es simple y explicable: cada recomendación muestra qué compañeros la justifican.
4. Detectar componentes revela estudiantes o grupos aislados que el algoritmo de recomendación no puede alcanzar.

## 7. Trabajo futuro

Recomendar recorriendo solo los vecinos de los vecinos (distancia 2) en lugar de toda la lista de vértices; guardar el índice en cada vértice para BFS/DFS en O(V + E) estricto; ponderar la recomendación por asignaturas en común;
recomendar a estudiantes aislados por afinidad cuando no tienen conexiones.
