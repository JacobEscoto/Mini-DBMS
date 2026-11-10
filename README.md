# Mini DBMS

## Estructura de Carpetas
- `com.minidbms`:
  - Contiene únicamente la clase principal para el arranque de la aplicación.
- `com.minidbms.gui`:
  - Agrupa las vistas y componentes de la interfaz gráfica de usuario (pantallas y menús).
- `com.minidbms.model`:
  - Define las estructuras de datos, clases de metadatos y la representación lógicade los registros.

## Instalación
Guía de instalación y ejecución del proyecto

### Requisitos
- [Apache Maven](https://maven.apache.org/install.html)
- [Java SE JDK 25](https://www.oracle.com/java/technologies/javase/jdk25-archive-downloads.html)

### 1. Clonar Repositorio
```bash
git clone https://github.com/JacobEscoto/Mini-DBMS.git
cd Mini-DBMS
```

### 2. Compilar
```bash
mvn clean compile
```

### 3. Ejecutar el proyecto
```bash
mvn exec:java -Dexec.mainClass="com.minidbms.App"
```
