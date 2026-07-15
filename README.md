# DecorCenter — Aplicación Web (MVC · JSP · Servlets · JavaBeans · JSTL)

Aplicación web tipo e-commerce para **DecorCenter** (cerámicos, tableros, baño y
spa, cocina y electrohogar), construida con el stack clásico de Java EE para
desplegar en **Apache Tomcat**.

## 🏗️ Arquitectura (MVC)

```
Modelo   -> com.decorcenter.model     (JavaBeans: Usuario, Producto, Categoria, Pedido, DetallePedido)
Vista    -> src/main/webapp/WEB-INF/views/*.jsp  (JSP + JSTL, sin lógica de negocio)
Controlador -> com.decorcenter.controller (Servlets: reciben la petición,
               llaman al DAO/Factory y despachan a la vista)
```

Capa de acceso a datos separada del controlador:

```
com.decorcenter.dao          -> interfaces (contrato)
com.decorcenter.dao.impl     -> implementación JDBC/MySQL
com.decorcenter.factory      -> fábrica que entrega los DAO a los controladores
com.decorcenter.util         -> ConexionBD (conexión) y PasswordUtil (hash)
```

## 🎨 Patrones de diseño utilizados (2+)

1. **DAO (Data Access Object)** — `UsuarioDAO`, `ProductoDAO`, `CategoriaDAO`,
   `PedidoDAO` (interfaces) + su implementación en `dao.impl`. Aísla el SQL/JDBC
   de los servlets.
2. **Singleton** — `ConexionBD` garantiza un único punto de configuración de
   conexión a la base de datos.
3. **Factory Method** — `DAOFactory` centraliza la creación de los objetos DAO;
   los controladores nunca hacen `new XxxDAOImpl()` directamente.

## 🗄️ Base de datos

Motor: **MySQL 8**. Script completo en `sql/schema.sql` (crea la BD, tablas y
datos de ejemplo, incluyendo un usuario **admin** y un usuario **cliente**).

Credenciales de prueba (después de correr el script):

| Rol      | Correo                     | Contraseña |
|----------|----------------------------|------------|
| ADMIN    | admin@decorcenter.com      | admin123   |
| USUARIO  | cliente@decorcenter.com    | user123    |

Las contraseñas se guardan hasheadas en SHA-256 (`PasswordUtil`).

## ⚙️ Configuración de conexión

Edita `src/main/java/com/decorcenter/util/ConexionBD.java` si tu usuario/clave
de MySQL son distintos a `root` / `root`, o si tu base de datos corre en otro
host/puerto:

```java
private static final String URL = "jdbc:mysql://localhost:3306/decorcenter_db?useSSL=false&serverTimezone=UTC";
private static final String USUARIO = "root";
private static final String PASSWORD = "root";
```

## 🚀 Despliegue en Apache Tomcat

1. Instala **MySQL** y ejecuta el script `sql/schema.sql`:
   ```
   mysql -u root -p < sql/schema.sql
   ```
2. Ajusta usuario/clave en `ConexionBD.java` si es necesario.
3. Compila el WAR con Maven (requiere conexión a internet la primera vez para
   bajar dependencias — servlet-api, jstl, mysql-connector-java):
   ```
   mvn clean package
   ```
   Esto genera `target/DecorCenterApp.war`.
4. Copia el WAR a la carpeta `webapps` de tu Apache Tomcat (9 o 10 en
   configuración Java EE / `javax.*`):
   ```
   cp target/DecorCenterApp.war  $CATALINA_HOME/webapps/
   ```
5. Inicia Tomcat:
   ```
   $CATALINA_HOME/bin/startup.sh      (Linux/Mac)
   $CATALINA_HOME/bin/startup.bat     (Windows)
   ```
6. Abre en el navegador:
   ```
   http://localhost:8080/DecorCenterApp/home
   ```

> **Nota:** si tu Tomcat es la versión 10.1+ (namespace `jakarta.*`), deberás
> cambiar los imports `javax.servlet.*` por `jakarta.servlet.*` en todo el
> código y usar la dependencia `jakarta.servlet:jakarta.servlet-api` +
> `jakarta.servlet.jsp.jstl:jakarta.servlet.jsp.jstl-api`. El proyecto está
> preparado por defecto para **Tomcat 9** (javax).

## 🖼️ Imágenes de productos

Coloca tus imágenes en `src/main/webapp/images/` y referencia el nombre de
archivo (ej. `images/producto1.jpg`) en el campo "URL de imagen" al crear o
editar un producto desde el panel admin. Si no subes imágenes propias, la
tarjeta del producto mostrará el texto "Sin imagen" en vez de romper el diseño.

## 👤 Roles y funcionalidades

**Usuario (rol USUARIO)**
- Ver catálogo, filtrar por categoría y buscar productos.
- Ver el detalle de un producto.
- Agregar productos al carrito (en sesión).
- Confirmar pedido (se descuenta stock y se guarda en la BD).

**Administrador (rol ADMIN)**
- Dashboard con totales de productos, categorías y pedidos.
- CRUD completo de productos (crear, editar, eliminar).
- CRUD de categorías (crear, eliminar).
- Ver listado de todos los pedidos realizados.

Un filtro (`AuthFilter`) protege `/carrito/*`, `/pedido/*` y `/admin/*`,
exigiendo sesión iniciada, y además exige rol ADMIN para todo lo que empiece
con `/admin/`.

## 📁 Estructura de carpetas

```
DecorCenterApp/
├── pom.xml
├── sql/schema.sql
├── README.md
└── src/main/
    ├── java/com/decorcenter/
    │   ├── model/         (JavaBeans)
    │   ├── dao/           (interfaces DAO)
    │   ├── dao/impl/      (implementación JDBC)
    │   ├── factory/       (DAOFactory)
    │   ├── util/          (ConexionBD, PasswordUtil)
    │   ├── filter/         (AuthFilter)
    │   └── controller/    (Servlets) + controller/admin
    └── webapp/
        ├── css/style.css
        ├── images/
        └── WEB-INF/
            ├── web.xml
            └── views/      (JSP con JSTL)
                ├── includes/ (header, footer, sidebar admin)
                ├── admin/
                └── usuario/
```

## 🎨 Estilo visual

El frontend replica la identidad de DecorCenter: verde bosque oscuro, beige
cálido y rojo de acento, tipografía serif para titulares (inspirado en "Todo
empieza con un buen material"), tarjetas de producto con sombra suave y
esquinas redondeadas, y un banner de contacto por WhatsApp igual al del sitio
original.
