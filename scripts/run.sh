#!/usr/bin/bash

# Script para formatear, compilar y correr el proyecto

print_status() {
  echo ""
  echo "------- $1 --------"
}

# Formateo de código
print_status "Aplicando formateo de código"
mvn spotless:apply

# Compilación del proyecto
print_status "Compilando proyecto"
mvn clean compile

# Ejecución de la aplicación
print_status "Ejecutando la aplicación"
mvn exec:java -Dexec.mainClass="com.minidbms.App"
