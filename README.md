# 🎓 CEPSAC - Sistema de Gestión Académica

Sistema completo para la gestión de cursos, diplomados, matrículas y pagos con autenticación JWT y arquitectura REST.

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Autenticación](#-autenticación)
- [API Endpoints](#-api-endpoints)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Buenas Prácticas](#-buenas-prácticas)
- [Contribución](#-contribución)

## ✨ Características

- 🔐 **Autenticación JWT** con roles (Administrador, Alumno, Docente)
- 👥 **Gestión de Usuarios** con soft delete
- 🌍 **Gestión de Países** y tipos de identificación
- 📚 **Gestión de Cursos** y diplomados
- 💰 **Sistema de Pagos** y matrículas
- 🛡️ **Validaciones robustas** en DTOs y entidades
- 📊 **Soft Delete** para mejor rendimiento y auditoría
- 🔄 **Mapeo automático** con MapStruct
- 📝 **Manejo de excepciones** centralizado
- 🎯 **DTOs separados** por propósito (Create, Update, Patch, Response)

## 🛠️ Tecnologías

### Backend
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.5.6** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **MySQL** - Base de datos
- **JWT (jjwt)** - Tokens de autenticación
- **MapStruct** - Mapeo de objetos
- **Lombok** - Reducción de código boilerplate
- **Maven** - Gestión de dependencias

### Frontend
- **Angular 20** - Framework frontend
- **TypeScript** - Lenguaje de programación
- **RxJS** - Programación reactiva

## 🏗️ Arquitectura

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Backend       │    │   Database      │
│   (Angular)     │◄──►│   (Spring Boot) │◄──►│   (MySQL)       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌─────────────────┐
                       │   JWT Service   │
                       │   (Autenticación)│
                       └─────────────────┘
```

### Patrón de Arquitectura
- **Controller** → **Service** → **Repository** → **Entity**
- **DTOs** para transferencia de datos
- **Mappers** para conversión automática
- **Filtros JWT** para autenticación automática

## 🚀 Instalación

### Prerrequisitos
- Java 21+
- Maven 3.6+
- MySQL 8.0+
- Node.js 18+ (para frontend)

### Backend
```bash
# Clonar el repositorio
git clone <repository-url>
cd CEPSAC/cepsacBackend

# Instalar dependencias
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

### Frontend
```bash
cd CEPSAC/frontend

# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm start
```

## ⚙️ Configuración

### Base de Datos
```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/cepsac
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
```

### JWT
```properties
# Configuración JWT
application.security.jwt.secret-key=${JWT_SECRET_KEY:MARIOCHIPIRECONTRASAUYSUNOVIACRISDIEGOGEISAPOSETENTATETRADOBLETRIPLEIJUEPUTAxd}
application.security.jwt.expiration=86400000
```

## 🔐 Autenticación

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "correo": "usuario@ejemplo.com",
  "password": "MiPassword123!"
}
```

### Respuesta
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Uso del Token
```http
GET /api/usuarios/listar
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 📡 API Endpoints

### 🔐 Autenticación
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/login` | Iniciar sesión |

### 👥 Usuarios
| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| GET | `/api/usuarios/listar` | Listar usuarios activos | ✅ |
| GET | `/api/usuarios/listar/{rol}` | Listar por rol | ✅ |
| GET | `/api/usuarios/obtener/{id}` | Obtener usuario | ✅ |
| POST | `/api/usuarios/crear` | Crear usuario | ✅ |
| PUT | `/api/usuarios/actualizar` | Actualizar usuario completo | ✅ |
| PATCH | `/api/usuarios/actualizar-parcial` | Actualizar usuario parcial | ✅ |
| DELETE | `/api/usuarios/eliminar/{id}` | Eliminar usuario (soft delete) | ✅ |
| POST | `/api/usuarios/restaurar/{id}` | Restaurar usuario suspendido | ✅ |

### 🌍 Países
| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| GET | `/api/paises/listar` | Listar países | ✅ |
| GET | `/api/paises/obtener/{id}` | Obtener país | ✅ |
| POST | `/api/paises/crear` | Crear país | ✅ |
| PUT | `/api/paises/actualizar` | Actualizar país | ✅ |
| DELETE | `/api/paises/eliminar/{id}` | Eliminar país | ✅ |

### 🆔 Tipos de Identificación
| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| GET | `/api/tipos-identificacion/listar` | Listar tipos | ✅ |
| GET | `/api/tipos-identificacion/obtener/{id}` | Obtener tipo | ✅ |
| POST | `/api/tipos-identificacion/crear` | Crear tipo | ✅ |
| PUT | `/api/tipos-identificacion/actualizar` | Actualizar tipo | ✅ |
| DELETE | `/api/tipos-identificacion/eliminar/{id}` | Eliminar tipo | ✅ |

## 📁 Estructura del Proyecto

```
cepsacBackend/
├── src/main/java/com/example/cepsacbackend/
│   ├── Config/                          # Configuraciones
│   │   ├── ApplicationConfig.java       # Configuración de seguridad
│   │   ├── GlobalExceptionHandler.java  # Manejo de excepciones
│   │   └── Security/                    # Configuración JWT
│   │       ├── CustomUserDetails.java   # Detalles de usuario
│   │       ├── JwtFilter.java          # Filtro JWT
│   │       ├── JwtService.java         # Servicio JWT
│   │       └── SecurityConfig.java     # Configuración de seguridad
│   ├── Controller/                      # Controladores REST
│   │   ├── AuthController.java         # Autenticación
│   │   ├── UsuarioController.java      # Gestión de usuarios
│   │   ├── PaisController.java         # Gestión de países
│   │   └── TipoIdentificacionController.java
│   ├── Dto/                            # Data Transfer Objects
│   │   ├── Login/                      # DTOs de autenticación
│   │   │   ├── LoginRequestDTO.java    # Login request
│   │   │   └── AuthResponseDTO.java    # Login response
│   │   └── Usuario/                    # DTOs de usuario
│   │       ├── UsuarioCreateDTO.java   # Crear usuario
│   │       ├── UsuarioUpdateDTO.java   # Actualizar usuario
│   │       ├── UsuarioPatchDTO.java    # Actualización parcial
│   │       └── UsuarioResponseDTO.java # Respuesta de usuario
│   ├── Entity/                         # Entidades JPA
│   │   ├── Usuario.java               # Entidad usuario
│   │   ├── Pais.java                  # Entidad país
│   │   └── TipoIdentificacion.java    # Entidad tipo identificación
│   ├── Enums/                         # Enumeraciones
│   │   ├── EstadoUsuario.java         # Estados de usuario
│   │   └── Rol.java                   # Roles de usuario
│   ├── Mapper/                        # Mapeadores MapStruct
│   │   └── UsuarioMapper.java         # Mapeo de usuarios
│   ├── Repository/                    # Repositorios JPA
│   │   ├── UsuarioRepository.java     # Repositorio usuarios
│   │   ├── PaisRepository.java        # Repositorio países
│   │   └── TipoIdentificacionRepository.java
│   └── Service/                       # Servicios de negocio
│       ├── UsuarioService.java        # Interfaz servicio usuarios
│       └── Impl/
│           └── UsuarioServiceImpl.java # Implementación servicio
└── src/main/resources/
    └── application.properties         # Configuración de la aplicación
```

## 🎯 Buenas Prácticas Implementadas

### 🔒 Seguridad
- ✅ Autenticación JWT con expiración
- ✅ Validación de contraseñas robustas
- ✅ Soft delete para mantener auditoría
- ✅ Validaciones en DTOs y entidades
- ✅ Manejo centralizado de excepciones

### 🚀 Rendimiento
- ✅ Soft delete para consultas más rápidas
- ✅ Mapeo automático con MapStruct
- ✅ Consultas optimizadas con JPA
- ✅ Lazy loading en relaciones

### 🧹 Código Limpio
- ✅ Separación de DTOs por propósito
- ✅ Arquitectura en capas
- ✅ Inyección de dependencias
- ✅ Nombres consistentes entre entidades y DTOs
- ✅ Documentación clara

### 📊 Validaciones
- ✅ Validaciones de formato (email, teléfono)
- ✅ Validaciones de longitud
- ✅ Validaciones de negocio
- ✅ Mensajes de error descriptivos

## 🎓 Roles y Permisos

| Rol | Descripción | Permisos |
|-----|-------------|----------|
| **administrador** | Administrador del sistema | Acceso completo a todos los endpoints |
| **docente** | Profesor/instructor | Gestión de cursos y estudiantes |
| **alumno** | Estudiante | Acceso a sus propios datos y matrículas |
| **Otro** | Rol personalizado | Permisos específicos según configuración |

## 🗄️ Estados de Usuario

| Estado | Descripción | ¿Visible en Frontend? | ¿Puede hacer Login? |
|--------|-------------|----------------------|-------------------|
| **activo** | Usuario normal | ✅ Sí | ✅ Sí |
| **inactivo** | Usuario deshabilitado | ✅ Sí | ❌ No |
| **suspendido** | Usuario eliminado (soft delete) | ❌ No | ❌ No |

## 📝 Ejemplos de Uso

### Crear Usuario
```json
POST /api/usuarios/crear
{
  "rol": "alumno",
  "nombre": "Alejandro",
  "apellido": "Castillo J",
  "correo": "ale@gmail.com",
  "password": "1234!",
  "numeroCelular": "987654321",
  "numeroIdentificacion": "12345678",
  "nombrePais": "Perú",
  "idTipoIdentificacion": 1
}
```

### Actualizar Usuario (PATCH)
```json
PATCH /api/usuarios/actualizar-parcial
{
  "idUsuario": 1,
  "nombre": "Juan Carlos",
  "numeroCelular": "+51987654322"
}
```

### Listar Usuarios por Rol
```http
GET /api/usuarios/listar/alumno
Authorization: Bearer tu-jwt-token
```

### Soft Delete (Suspender Usuario)
```http
DELETE /api/usuarios/eliminar/1
Authorization: Bearer tu-jwt-token
```

### Restaurar Usuario
```http
POST /api/usuarios/restaurar/1
Authorization: Bearer tu-jwt-token
```

## 🔧 DTOs Implementados

### UsuarioCreateDTO
- Para crear nuevos usuarios
- Todos los campos obligatorios
- Validaciones completas

### UsuarioUpdateDTO
- Para actualización completa (PUT)
- Incluye ID del usuario
- Todos los campos obligatorios

### UsuarioPatchDTO
- Para actualización parcial (PATCH)
- Campos opcionales
- Solo actualiza campos enviados

### UsuarioResponseDTO
- Para respuestas de la API
- Incluye datos relacionados (país, tipo identificación)
- Sin información sensible

## 🤝 Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request
