# Integrante 2 — Estados y monitoreo de hilos

## 1. Objetivo

Implementar un sistema de monitoreo de hilos en Java que permita observar los estados de ejecución de dos ventanillas que trabajan concurrentemente.

## 2. Clases desarrolladas

### MonitorHilos.java

Esta clase implementa la interfaz `Runnable` y permite monitorear dos hilos mediante el método `getState()`.

Sus principales funcionalidades son:

- Consultar periódicamente el estado de ambos hilos.
- Detectar y registrar cambios de estado.
- Realizar consultas aproximadamente cada 100 milisegundos.
- Finalizar automáticamente cuando ambos hilos terminan.
- Permitir solicitar la detención del monitor mediante una variable `volatile`.

### PruebaEstados.java

Esta clase permite comprobar el funcionamiento del monitor sin depender de la interfaz JavaFX.

Se crean dos hilos llamados `Ventanilla-A` y `Ventanilla-B`, los cuales procesan cinco unidades de trabajo cada uno.

Durante la ejecución se utiliza `Thread.sleep(500)` para simular el tiempo de procesamiento y facilitar la observación de los estados.

## 3. Estados de los hilos en Java

| Estado | Descripción |
|---|---|
| NEW | El hilo fue creado, pero todavía no se ha iniciado. |
| RUNNABLE | El hilo está ejecutándose o disponible para ejecutarse. |
| BLOCKED | El hilo espera adquirir un bloqueo de sincronización. |
| WAITING | El hilo espera indefinidamente una señal o evento. |
| TIMED_WAITING | El hilo permanece esperando durante un tiempo determinado. |
| TERMINATED | El hilo ha finalizado su ejecución. |

## 4. Funcionamiento del monitor

El monitor utiliza `Thread.getState()` para consultar los estados de ambas ventanillas.

Se ejecuta en un hilo independiente para evitar bloquear el hilo principal de la aplicación.

Cuando detecta un cambio de estado, registra un mensaje. Cuando las dos ventanillas alcanzan el estado `TERMINATED`, termina el monitoreo.

## 5. Pruebas realizadas

Ejecutar `PruebaEstados.java` tres veces y completar la siguiente tabla con los resultados obtenidos:

| Ejecución | NEW | TIMED_WAITING | TERMINATED |
|---|---|---|---|
| 1 | Pendiente | Pendiente | Pendiente |
| 2 | Pendiente | Pendiente | Pendiente |
| 3 | Pendiente | Pendiente | Pendiente |

Los estados observados pueden variar entre ejecuciones, debido a la planificación de los hilos y al intervalo de monitoreo.

## 6. Integración con JavaFX

La clase `MonitorHilos` se diseñó para integrarse posteriormente con la interfaz JavaFX del proyecto.

Los mensajes generados por el monitor pueden mostrarse en un `TextArea` utilizando `Platform.runLater()`, evitando actualizar controles gráficos directamente desde hilos secundarios.

## 7. Conclusión

El monitoreo permite comprender cómo cambian los estados de los hilos durante una ejecución concurrente.

El uso de `getState()` facilita observar su comportamiento, mientras que la ejecución del monitor en un hilo independiente permite evitar bloqueos innecesarios en la interfaz gráfica.