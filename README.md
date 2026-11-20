# Mini DBMS

Sistema gestor de bases de datos con la capacidad de manejar archivos en disco sin cargar toda la información a la memoria RAM.

## Estructura de Carpetas
- `com.minidbms`:
  - Contiene únicamente la clase principal para el arranque de la aplicación.
- `com.minidbms.gui`:
  - Agrupa las vistas y componentes de la interfaz gráfica de usuario (pantallas y menús).
- `com.minidbms.modelo`:
  - Define las estructuras de datos, clases de metadatos y la representación lógica de los registros.
- `com.minidbms.excepciones`:
  - Excepciones personalizadas para manejo de registros, campos y archivos.
- `com.minidbms.utilidades`:
  - Clases utilitarias de apoyo para la lógica de la aplicación.

## Instalación
Guía de instalación y ejecución del proyecto

### Requisitos
- [Apache Maven](https://maven.apache.org/install.html)
- [Java SE JDK 25](https://www.oracle.com/java/technologies/javase/jdk25-archive-downloads.html)

### Opción A. Instalación Manual

#### 1. Clonar Repositorio
```bash
git clone https://github.com/JacobEscoto/Mini-DBMS.git
cd Mini-DBMS
```

#### 2. Compilar
```bash
mvn clean compile
```

#### 3. Ejecutar el proyecto
```bash
mvn exec:java -Dexec.mainClass="com.minidbms.App"
```

### Opción B. Ejecutar Shell Script

```bash
./scripts/run.sh
``` 