# Nombre del Proyecto

Analizador léxico-sintáctico

# Breve descripción del proyecto (1-2 líneas)

Herramienta desarrollada en Java para realizar el análisis léxico y sintáctico de un lenguaje simplificado de creación de tablas, identificando tokens y validando la estructura gramatical.

# Información del Curso

* **Materia:** Programación de Sistemas de Base 1
* **Institución:** Universidad Autonoma de Tamaulipas
* **Semestre:** 8vo Semestre
* **Profesor(es):** Muñoz Quintero Dante Adolfo

# Integrantes del Equipo

* Duran Lugo Jesus Antonio
* Hernández Lara Ana Patricia
* Antonio Guzman Lucero Iris
* Saldaña Sanchez Carlos Alfonso

---

## Descripción Detallada 

Este proyecto consiste en la implementación de un analizador léxico y un analizador sintáctico para un lenguaje específico definido para la creación de tablas. El analizador léxico se encarga de leer el código fuente de entrada y descomponerlo en una secuencia de componentes léxicos (tokens). Posteriormente, el analizador sintáctico toma estos tokens y verifica que la secuencia cumpla con las reglas gramaticales definidas para el lenguaje, construyendo una representación intermedia de la estructura.

### Funcionalidades

* **Análisis Léxico:**
    * Identificación de palabras clave (ej: `Create`, `Table`, `Text`, `Number`, `Bool`).
    * Reconocimiento de identificadores (nombres de tablas y variables).
    * Detección de delimitadores (ej: `{`, `}`, `;`).
    * Manejo de errores léxicos (caracteres no reconocidos).
* **Análisis Sintáctico:**
    * Validación de la estructura de sentencias de creación de tablas.
    * Verificación de la correcta definición de variables (tipo y nombre).
    * Generación de una estructura de datos interna que representa la tabla y sus columnas.
    * Manejo de errores sintácticos (estructuras incorrectas, palabras clave faltantes, etc.).
* **Interfaz Gráfica:**
    * Área para ingresar el código fuente.
    * Visualización de los tokens generados.
    * Presentación del resultado del análisis sintáctico y la estructura de la tabla identificada.
    * Ventana emergente para la notificación de errores.

### Lenguaje Soportado (Ejemplo de la Gramática)

El analizador está diseñado para un lenguaje con la siguiente estructura básica para la creación de tablas:

    Create Table &lt;nombre_tabla> {
    &lt;tipo_dato> &lt;nombre_variable>;
    &lt;tipo_dato> &lt;nombre_variable>;
    ...
    }

Donde:
* `<nombre_tabla>`: Un identificador válido para el nombre de la tabla.
* `<tipo_dato>`: Puede ser `Text`, `Number`, o `Bool`.
* `<nombre_variable>`: Un identificador válido para el nombre de la columna/variable.

### Herramientas y Lenguaje de Programación

* **Lenguaje:** Java
* **Entorno de Desarrollo (IDE):** (Ej: IntelliJ IDEA, Eclipse, NetBeans - *especifica el que usaron*)
* **Gestión de Interfaz Gráfica:** Swing

## Cómo Compilar y Ejecutar

*(Esta sección es muy importante. Debes detallar los pasos)*

1.  **Requisitos Previos:**
    * Tener instalado el JDK (Java Development Kit) versión [Tu versión de JDK, ej: 17 o superior].
    * (Opcional) Apache Maven o Gradle si el proyecto está configurado con estas herramientas.
2.  **Compilación:**
    * *(Si no usan Maven/Gradle)*: Navegar al directorio `src` y compilar los archivos Java:
        ```bash
        javac analizador/*.java Main.java
        ```
    * *(Si usan Maven)*:
        ```bash
        mvn compile
        ```
    * *(Si usan Gradle)*:
        ```bash
        gradle build
        ```
3.  **Ejecución:**
    * *(Si no usan Maven/Gradle)*: Desde el directorio `src` (o el directorio raíz si los .class están allí después de compilar):
        ```bash
        java Main
        ```
    * *(Si usan Maven)*:
        ```bash
        mvn exec:java -Dexec.mainClass="Main"
        ```
    * *(Si usan Gradle y tienen configurada la tarea de ejecución)*:
        ```bash
        gradle run
        ```

*(Puedes añadir capturas de pantalla de la interfaz y ejemplos de uso aquí si lo deseas)*
