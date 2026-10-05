**SwapCloset** es un proyecto desarrollado en **Spring Boot** para la gestión de un armario virtual colaborativo. La plataforma permite a los usuarios dar una segunda vida a su ropa mediante la publicación, intercambio y préstamo de prendas de vestir, fomentando la moda sostenible.

---

## Características Principales

- 👤 **Gestión de Usuarios y Perfiles:** 
  - Control de accesos por roles (`ADMINISTRADOR`, `USUARIO`, `MODERADOR`).
  - Configuración del perfil con tallas de ropa/calzado, biografía y reputación basada en valoraciones.
- 👕 **Gestión del Armario Virtual:**
  - Publicación y catálogo de prendas con fotos, marcas, tallas, categorías y condiciones.
  - Opciones para habilitar o deshabilitar disponibilidad para **préstamo** o **intercambio**.
- 🎨 **Preferencias y Estilos:** Vinculación de estilos de moda al perfil del usuario.
- 💬 **Mensajería Interna:** Comunicación entre usuarios para la coordinación de intercambios y préstamos.
- 🔔 **Notificaciones:** Sistema de alertas para solicitudes de préstamos, intercambios y recordatorios de devolución.

---

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Java 17+
- **Framework:** Spring Boot 3.x
- **Persistencia:** Spring Data JPA / Hibernate
- **Base de Datos:** PostgreSQL
- **Gestor de Dependencias:** Apache Maven
- **Librerías Adicionales:** Lombok

---

## 🗄️ Modelo de Base de Datos

El sistema se apoya en un modelo relacional en **PostgreSQL** compuesto por las siguientes entidades clave:

- `Usuarios`: Gestión de credenciales y autenticación.
- `Perfiles`: Información personal del usuario y tallas.
- `Prendas`: Artículos del armario virtual.
- `Categorias` y `Estilos`: Clasificación de prendas y gustos del usuario (`estilos_perfil`).
- `Mensajes`: Chat entre usuarios.
- `Notificaciones`: Historial de alertas del sistema.

---

## 🚀 Instalación y Configuración

### Prerrequisitos

1. Tener instalado **JDK 17** o superior.
2. Tener instalado y en ejecución **PostgreSQL 14+**.
3. Un IDE recomendado, en mi caso se usa **IntelliJ IDEA**.

### Pasos para Ejecutar el Proyecto

1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/Saravr2701/SwapCloset.git](https://github.com/Saravr2701/SwapCloset.git)
   cd SwapCloset
