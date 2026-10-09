# Semillero CI/CD - Demo E2E

Proyecto didáctico para recorrer el ciclo completo **aplicación → pruebas → CI → despliegue**.

## Stack

- Java 17
- Spring Boot 4.1.1
- Gradle 8.14.3
- Thymeleaf
- Spring Data JPA
- H2 para validación local
- PostgreSQL preparado para Neon
- JUnit 5 / Mockito
- Cypress 16.1.1

## Casos de uso

1. Registro de usuario.
2. Recuperación de contraseña simulada.

> La recuperación no envía correo real. La pantalla devuelve un mensaje genérico, suficiente para demostrar el flujo E2E sin agregar complejidad innecesaria.

## 1. Requisitos

- Java 17 o superior
- Node.js 20 o 22
- npm
- Conexión a Internet en la primera ejecución para descargar dependencias

Comprueba:

```bash
java -version
node --version
npm --version
```

## 2. Ejecutar la aplicación en Windows

Desde la raíz del proyecto:

```bat
gradlew.bat bootRun
```

El script incluido descarga Gradle 8.14.3 la primera vez. No necesitas instalar Gradle globalmente.

Abre:

- Aplicación: http://localhost:8080
- Registro: http://localhost:8080/registro
- Recuperación: http://localhost:8080/recuperar
- H2 Console: http://localhost:8080/h2-console

Para H2 Console usa:

- JDBC URL: `jdbc:h2:mem:semillerodb`
- User: `sa`
- Password: vacío

## 3. Ejecutar pruebas Java

```bat
gradlew.bat test
```

## 4. Instalar Cypress

En otra terminal:

```bash
npm install
```

Abrir modo visual:

```bash
npm run cy:open
```

Ejecutar modo consola:

```bash
npm run cy:run
```

**La aplicación Spring Boot debe estar levantada en el puerto 8080 mientras ejecutas Cypress.**

## 5. Flujo recomendado de validación

Terminal 1:

```bat
gradlew.bat bootRun
```

Terminal 2:

```bash
npm install
npm run cy:run
```

Debes ver las pruebas E2E en verde.

## 6. PostgreSQL / Neon (para la siguiente fase)

Cuando pasemos a Neon, activa el perfil `prod` y define:

```text
SPRING_PROFILES_ACTIVE=prod
DB_URL=jdbc:postgresql://HOST/DATABASE?sslmode=require
DB_USERNAME=usuario
DB_PASSWORD=clave
```

No guardes credenciales reales en el repositorio.

## 7. GitHub Actions

El proyecto ya trae `.github/workflows/ci.yml`. Cuando lo subamos a GitHub, el pipeline hará:

1. Checkout.
2. Configura Java 17.
3. Configura Gradle 8.14.3.
4. Ejecuta pruebas Java.
5. Instala Cypress.
6. Arranca Spring Boot con H2.
7. Ejecuta las pruebas E2E.

Después agregaremos el despliegue a Render.
