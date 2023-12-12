


## User API - API REST para gestión de usuarios
### Descripción del Proyecto
Este proyecto consiste en una API REST desarrollada con Spring Boot para la gestión de usuarios, enfocándose en la seguridad y autenticación mediante Spring Security. Utilizando Java como lenguaje principal, este aplicativo ofrece robustez y dinamismo, acorde a las últimas versiones de Java. El manejo de dependencias se realiza a través de Gradle.

#### Configuración e Instalación
##### Requisitos Previos
Java (Versión recomendada: 17)
Spring Boot (Versión recomendada: 3)
Gradle
IDE (desarrollado en IntelliJ IDEA)

#### Instalación y Ejecución
##### Clonar el repositorio:
`git clone https://github.com/darwgom7/user-api.git`

##### Compilar el proyecto con Gradle:
`gradle build`

##### Ejecutar la aplicación:
`gradle bootRun`

La aplicación se ejecutará normalmente en el puerto 8080.
#### Seguridad y Configuración
- El proyecto se conecta a una base de datos H2, configurando automáticamente la tabla para Roles.
- Algunas rutas están restringidas por defecto por Spring Security. Configuraciones personalizadas se pueden realizar en **application > config > SecurityConfig**.
- El archivo de configuraciones application.yaml contiene detalles importantes para la conexión a la base de datos y configuraciones de JWT, incluyendo la llave secreta y el tiempo de autenticación.

#### Documentación de la API
##### Registrar usuario
`POST /api/users/register`
###### Ejemplo de solicitud:
```json
{
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.org",
    "password": "Hunter2+",
    "role": "role_admin",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "countrycode": "57"
        }
    ]
}
```
##### Iniciar Sesión
`POST /api/users/login`
###### Ejemplo de solicitud:
```json
{
  "email": "juan@rodriguez.org",
  "password": "Hunter2+"
}
```
##### Obtener Usuarios
`GET /api/users`

##### Obtener Usuario por ID
`GET /api/users/{id}`

##### Actualizar Usuario
`PUT /api/users/{id}`

##### Eliminar Usuario
`DELETE /api/users/{id}`

### Contribuciones
Para contribuir al proyecto, se recomienda seguir los estándares de código y documentación apropiados. Las contribuciones pueden realizarse mediante Pull Requests al repositorio principal.