/*Sara Vázquez Rivera*/

CREATE SCHEMA IF NOT EXISTS SwapCloset;
SET search_path TO SwapCloset, public;


DROP TYPE IF EXISTS roles_usuarios CASCADE;
CREATE TYPE roles_usuarios AS ENUM ('ADMINISTRADOR', 'USUARIO', 'MODERADOR');

DROP TYPE IF EXISTS tipo_notificacion CASCADE;
CREATE TYPE  tipo_notificacion AS ENUM ('PRESTAMO_SOLICITADO','INTERCAMBIO_SOLICITADO','RECORDATORIO_DEVOLUCIÓN');

DROP TYPE IF EXISTS tipo_solicitud CASCADE;
CREATE TYPE  tipo_solicitud AS ENUM ('PRESTAMO','INTERCAMBIO');

DROP TYPE IF EXISTS estado_solicitud CASCADE;
CREATE TYPE  estado_solicitud AS ENUM ('PENDIENTE', 'ACEPTADA', 'RECHAZADA', 'CANCELADA','CADUCADA','RETRASADA','FINALIZADA');

DROP TYPE IF EXISTS condicion_prenda CASCADE;
CREATE TYPE  condicion_prenda AS ENUM ('NUEVA', 'COMO_NUEVA', 'BUEN_ESTADO', 'USADO');

DROP TYPE IF EXISTS estado_prenda CASCADE;
CREATE TYPE  estado_prenda AS ENUM ('BORRADOR', 'DISPONIBLE', 'RESERVADA', 'PRESTADA','RETIRADA');

DROP TYPE IF EXISTS talla_zapatos CASCADE;
CREATE TYPE  talla_zapatos AS ENUM ('T35', 'T36', 'T37', 'T38', 'T39', 'T40', 'T41', 'T42', 'T43', 'T44', 'T45', 'T46');

DROP TYPE IF EXISTS talla_pantalon CASCADE;
CREATE TYPE  talla_pantalon AS ENUM ('T34', 'T36', 'T38', 'T40', 'T42', 'T44', 'T46', 'T48', 'T50');

DROP TYPE IF EXISTS talla_camiseta CASCADE;
CREATE TYPE  talla_camiseta AS ENUM ('XS', 'S', 'M', 'L', 'XL', 'XXL');

DROP TYPE IF EXISTS rol_prenda_solicitud CASCADE;
CREATE TYPE rol_prenda_solicitud AS ENUM ('SOLICITADA', 'OFRECIDA');


DROP TABLE IF EXISTS categorias CASCADE;
CREATE TABLE categorias (
	id SERIAL PRIMARY KEY,
	nombre VARCHAR(150) NOT NULL
);

DROP TABLE IF EXISTS estilos CASCADE;
CREATE TABLE estilos (
	id SERIAL PRIMARY KEY,
	nombre VARCHAR (50) NOT NULL
);

DROP TABLE IF EXISTS usuarios CASCADE;
CREATE TABLE usuarios (
	id SERIAL PRIMARY KEY,
	rol roles_usuarios DEFAULT 'USUARIO' NOT NULL,
	nombreusu VARCHAR (50) UNIQUE NOT NULL,
	contrasena VARCHAR (250) NOT NULL
);

DROP TABLE IF EXISTS perfil CASCADE;
CREATE TABLE perfil (
	id SERIAL PRIMARY KEY,
	id_usuario INT UNIQUE NOT NULL,
	nombre VARCHAR (100),
	ape1 VARCHAR (100),
	ape2 VARCHAR (100),
	fecha_nacimiento DATE NOT NULL CHECK (fecha_nacimiento <= CURRENT_DATE - INTERVAL '16 years'),
	ciudad VARCHAR (100),
	email VARCHAR(150) UNIQUE NOT NULL,
	deposito NUMERIC(6,2) DEFAULT 0.00,
	nivel INT DEFAULT 0,
	biografia VARCHAR (500),
	talla_zapatos talla_zapatos NOT NULL,
	talla_camiseta talla_camiseta NOT NULL,
	talla_pantalon talla_pantalon NOT NULL,
	CONSTRAINT fk_perfil_usuario 
        FOREIGN KEY (id_usuario) 
        REFERENCES usuarios(id) 
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS estilos_perfil CASCADE;
CREATE TABLE estilos_perfil (
	id_estilo INT,
	id_perfil INT,
	UNIQUE (id_estilo, id_perfil),
	CONSTRAINT fk_estilos_perfil_estilo 
        FOREIGN KEY (id_estilo) 
        REFERENCES estilos(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_estilos_perfil_perfil 
        FOREIGN KEY (id_perfil) 
        REFERENCES perfil(id) 
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS notificacion CASCADE;
CREATE TABLE notificacion (
	id SERIAL PRIMARY KEY,
	id_perfil INT NOT NULL,
	tipo tipo_notificacion NOT NULL,
	mensaje TEXT,
	leida BOOLEAN DEFAULT FALSE NOT NULL,
	fecha DATE DEFAULT CURRENT_DATE,
	CONSTRAINT fk_notificacion_perfil
        FOREIGN KEY (id_perfil) 
        REFERENCES perfil(id) 
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS solicitud CASCADE;
CREATE TABLE solicitud (
	id SERIAL PRIMARY KEY,
	id_perfil INT NOT NULL,
	fecha_creacion DATE DEFAULT CURRENT_DATE,
	fecha_respuesta DATE,
	fecha_inicio DATE,
	fecha_fin DATE,
	tipo_solicitud tipo_solicitud,
	estado estado_solicitud,
	CONSTRAINT fk_solicitud_perfil
        FOREIGN KEY (id_perfil) 
        REFERENCES perfil(id) 
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS valoracion CASCADE;
CREATE TABLE valoracion (
	id SERIAL PRIMARY KEY,
	id_solicitud INT NOT NULL,
	id_perfil INT NOT NULL,
	UNIQUE (id_solicitud, id_perfil),
	comentario TEXT,
	fecha DATE DEFAULT CURRENT_DATE,
	puntuacion INT NOT NULL CHECK (puntuacion BETWEEN 1 AND 5),
	CONSTRAINT fk_valoracion_solicitud
        FOREIGN KEY (id_solicitud) 
        REFERENCES solicitud(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_valoracion_perfil
        FOREIGN KEY (id_perfil) 
        REFERENCES perfil(id) 
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS mensaje CASCADE;
CREATE TABLE mensaje (
	id SERIAL PRIMARY KEY,
	id_solicitud INT NOT NULL,
	fecha_envio DATE DEFAULT CURRENT_DATE,
	hora_envio TIME DEFAULT CURRENT_TIME,
	contenido VARCHAR (1000) NOT NULL CHECK (LENGTH(TRIM(contenido)) >= 1),
	CONSTRAINT fk_mensaje_solicitud
        FOREIGN KEY (id_solicitud) 
        REFERENCES solicitud(id) 
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS envia_mensaje CASCADE;
CREATE TABLE envia_mensaje (
	id_mensaje INT UNIQUE NOT NULL,
	id_perfil INT NOT NULL,
	CONSTRAINT fk_envia_mensaje_mensaje 
        FOREIGN KEY (id_mensaje) 
        REFERENCES mensaje(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_envia_mensaje_perfil
        FOREIGN KEY (id_perfil) 
        REFERENCES perfil(id) 
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS recibe_mensaje CASCADE;
CREATE TABLE recibe_mensaje (
	id_mensaje INT  UNIQUE NOT NULL,
	id_perfil INT NOT NULL,
	CONSTRAINT fk_recibe_mensaje_mensaje 
        FOREIGN KEY (id_mensaje) 
        REFERENCES mensaje(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_recibe_mensaje_perfil
        FOREIGN KEY (id_perfil) 
        REFERENCES perfil(id) 
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS prenda CASCADE;
CREATE TABLE prenda (
	id SERIAL PRIMARY KEY,
	id_categorias INT NOT NULL,
	id_perfil INT NOT NULL,
	titulo VARCHAR (100),
	descripcion VARCHAR (500),
	marca VARCHAR (150),
	talla VARCHAR (10),
	fecha_publicacion DATE DEFAULT CURRENT_DATE,
	permite_intercambio BOOLEAN DEFAULT FALSE,
	permite_prestamo BOOLEAN DEFAULT FALSE,
	condicion condicion_prenda,
	estado estado_prenda DEFAULT 'BORRADOR' NOT NULL,
	CONSTRAINT fk_prenda_categorias
        FOREIGN KEY (id_categorias) 
        REFERENCES categorias(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_prenda_perfil
        FOREIGN KEY (id_perfil) 
        REFERENCES perfil(id) 
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS foto CASCADE;
CREATE TABLE foto (
	id SERIAL PRIMARY KEY,
	id_prenda INT NOT NULL,
	url VARCHAR(2000) NOT NULL,
	orden INT CHECK (orden BETWEEN 1 AND 5),
	CONSTRAINT fk_foto_prenda
        FOREIGN KEY (id_prenda) 
        REFERENCES prenda(id) 
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS solicitud_prenda CASCADE;
CREATE TABLE solicitud_prenda (
	id SERIAL PRIMARY KEY,
	id_solicitud INT NOT NULL,
	id_prenda INT NOT NULL,
	rol_prenda rol_prenda_solicitud,
	CONSTRAINT fk_solicitud_prenda_solicitud
        FOREIGN KEY (id_solicitud) 
        REFERENCES solicitud(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_solicitud_prenda_prenda
        FOREIGN KEY (id_prenda) 
        REFERENCES prenda(id) 
        ON DELETE CASCADE
);

/*Vista para obtener la reputación y el número de valoraciones de un usuario*/

DROP VIEW IF EXISTS reputacion_perfil CASCADE;

CREATE VIEW reputacion_perfil AS
SELECT 
	p.id AS id_perfil,
    COUNT(v.id) AS num_valoraciones,
    ROUND(AVG(v.puntuacion), 1) AS media_puntuacion,
    CASE 
        WHEN COUNT(v.id) = 0 THEN 'Sin valoraciones'
        ELSE CONCAT(ROUND(AVG(v.puntuacion), 1), ' (', COUNT(v.id), ' valoracion/es)')
    END AS reputacion
FROM perfil p
LEFT JOIN valoracion v ON p.id = v.id_perfil
GROUP BY p.id;

/*Inserts para realizar pruebas*/

INSERT INTO categorias (nombre) VALUES
('Zapatos'),
('Camisetas y Tops'),
('Pantalones'),
('Vestidos'),
('Accesorios'),
('Calzado'),
('Abrigos y Chaquetas'),
('Faldas'),
('Sudaderas y Jerséis');

INSERT INTO estilos (nombre) VALUES
('Casual'),
('Formal'),
('Streetwear'),
('Deportivo'),
('Boho'),
('Vintage'),
('Minimalista');

INSERT INTO usuarios (rol, nombreusu, contrasena) VALUES
('ADMINISTRADOR', 'admin_swap', '4DM1NS4WP'),
('USUARIO', 'laura_g', 'LauraGonzalez27'),
('USUARIO', 'carlos_m', '_Carlos_Martinez_Lopez'),
('USUARIO', 'marta_s', 'Martuki123123'),
('MODERADOR', 'mod_ana', 'ModAna2026!Pass'),
('USUARIO', 'david_r', 'DavidRamiro99_'),
('USUARIO', 'elena_v', 'ElenaVidal_88#');

INSERT INTO perfil (id_usuario, nombre, ape1, ape2, fecha_nacimiento, ciudad, email, biografia, talla_zapatos, talla_camiseta, talla_pantalon) VALUES
(1, 'Admin', 'Swap', 'System', '2000-01-01', 'Madrid', 'admin@swapcloset.com', 'Cuenta oficial de administración', 'T40', 'M', 'T38'),
(2, 'Laura', 'García', 'Ruiz', '1998-05-14', 'Barcelona', 'laura.garcia@email.com', 'Apasionada de la moda sostenible y las prendas vintage', 'T38', 'S', 'T36'),
(3, 'Carlos', 'Martínez', 'López', '1995-11-20', 'Valencia', 'carlos.martinez@email.com', 'Buscando renovar mi armario de forma responsable', 'T43', 'L', 'T42'),
(4, 'Marta', 'Sánchez', 'Pérez', '2001-03-08', 'Madrid', 'marta.sanchez@email.com', 'Me encanta probar nuevos estilos y compartir ropa', 'T37', 'M', 'T38'),
(5, 'Ana', 'Torres', 'Blanco', '1992-08-15', 'Sevilla', 'ana.torres@email.com', 'Moderadora de la comunidad SwapCloset. Promoviendo el consumo responsable.', 'T39', 'M', 'T38'),
(6, 'David', 'Ramiro', 'Gómez', '1999-12-03', 'Valencia', 'david.ramiro@email.com', 'Aficionado al streetwear, sneakers y coleccionismo textil.', 'T44', 'L', 'T40'),
(7, 'Elena', 'Vidal', 'Sanz', '1994-04-22', 'Madrid', 'elena.vidal@email.com', 'Buscando ropa elegante para trabajo y eventos especiales.', 'T37', 'S', 'T36');

INSERT INTO estilos_perfil (id_estilo, id_perfil) VALUES
(2, 2),
(5, 2),
(1, 3),
(4, 4),
(3, 6),
(4, 6),
(2, 7),
(7, 7),
(1, 5);

INSERT INTO prenda (id_categorias, id_perfil, titulo, descripcion, marca, talla, fecha_publicacion, permite_intercambio, permite_prestamo, condicion, estado) VALUES
(1, 2, 'Chaqueta Vaquera Oversize', 'Chaqueta vaquera estilo años 90 en muy buen estado.', 'Levi''s', 'M', '2026-09-01', TRUE, TRUE, 'COMO_NUEVA', 'BORRADOR'),
(3, 3, 'Pantalón Beige', 'Pantalón ideal para ocasiones casuales o de oficina.', 'Dockers', '42/L', '2026-09-10', TRUE, FALSE, 'BUEN_ESTADO', 'RETIRADA'),
(4, 4, 'Vestido de Flores Veraniego', 'Vestido corto con estampado floral ideal para eventos.', 'Zara', 'S', '2026-09-15', FALSE, TRUE, 'NUEVA', 'DISPONIBLE'),
(2, 7, 'Blazer Negra Ajustada', 'Blazer elegante ideal para entrevistas o eventos formales.', 'Zara', 'S', '2026-09-02', TRUE, TRUE, 'COMO_NUEVA', 'DISPONIBLE'),
(6, 6, 'Zapatillas Air Max 90', 'Sneakers clásicos en color blanco y gris, muy cómodos.', 'Nike', 'T44', '2026-09-05', TRUE, TRUE, 'BUEN_ESTADO', 'DISPONIBLE'),
(9, 2, 'Sudadera Oversize Grey', 'Sudadera de algodón muy suave con estética retro.', 'Adidas', 'S', '2026-09-12', TRUE, TRUE, 'BUEN_ESTADO', 'PRESTADA'),
(7, 3, 'Abrigo Largo de Lana', 'Abrigo de invierno elegante en tono marrón café.', 'Mango', 'L', '2026-09-18', FALSE, TRUE, 'COMO_NUEVA', 'RESERVADA'),
(8, 4, 'Falda Plisada Verde', 'Falda midi verde esmeralda con cintura elástica.', 'Stradivarius', 'M', '2026-09-19', TRUE, FALSE, 'NUEVA', 'DISPONIBLE');

INSERT INTO foto (id_prenda, url, orden) VALUES
(1, 'https://swapcloset.com/prendas/1_frontal.jpg', 1),
(1, 'https://swapcloset.com/prendas/1_trasera.jpg', 2),
(2, 'https://swapcloset.com/prendas/2_frontal.jpg', 1),
(3, 'https://swapcloset.com/prendas/3_frontal.jpg', 1),
(4, 'https://swapcloset.com/prendas/4_frontal.jpg', 1),
(4, 'https://swapcloset.com/prendas/4_detalle.jpg', 2),
(5, 'https://swapcloset.com/prendas/5_frontal.jpg', 1),
(5, 'https://swapcloset.com/prendas/5_suela.jpg', 2),
(6, 'https://swapcloset.com/prendas/6_frontal.jpg', 1),
(7, 'https://swapcloset.com/prendas/7_frontal.jpg', 1),
(8, 'https://swapcloset.com/prendas/8_frontal.jpg', 1);

INSERT INTO solicitud (id_perfil, fecha_creacion, fecha_respuesta, fecha_inicio, fecha_fin, tipo_solicitud, estado) VALUES
(3, '2026-09-20', '2026-09-21', NULL, NULL, 'INTERCAMBIO', 'ACEPTADA'),
(4, '2026-09-22', NULL, NULL, NULL, 'PRESTAMO', 'PENDIENTE'),
(7, '2026-09-21', '2026-09-22', '2026-09-23', '2026-10-05', 'PRESTAMO', 'ACEPTADA'),
(6, '2026-09-24', '2026-09-24', NULL, NULL, 'INTERCAMBIO', 'RECHAZADA'),
(2, '2026-09-25', '2026-09-26', '2026-09-28', '2026-10-10', 'PRESTAMO', 'ACEPTADA'),
(3, '2026-09-27', NULL, NULL, NULL, 'PRESTAMO', 'PENDIENTE');

INSERT INTO solicitud_prenda (id_solicitud, id_prenda, rol_prenda) VALUES
(1, 1, 'SOLICITADA'),
(2, 3, 'SOLICITADA'),
(3, 6, 'SOLICITADA'),
(4, 8, 'SOLICITADA'),
(4, 5, 'OFRECIDA'),
(5, 7, 'SOLICITADA'),
(6, 4, 'SOLICITADA');

INSERT INTO mensaje (id_solicitud, fecha_envio, hora_envio, contenido) VALUES 
(1, '2026-09-21', '09:15:00', 'Hola Carlos, quedamos el 1 si te viene bien'),
(3, '2026-09-21', '14:20:00', 'Hola Laura Me encanta la sudadera oversize, ¿la tendrías disponible para dos semanas?'),
(3, '2026-09-22', '10:05:00', 'Hola Elena Sí, sin problema. Te la preparo para envío.'),
(4, '2026-09-24', '16:45:00', 'Hola Marta, ¿te interesaría cambiar la falda por mis Air Max? Están en muy buenas condiciones.'),
(4, '2026-09-24', '18:10:00', 'Hola David, muchas gracias pero no estoy buscando calzado de esa talla ahora mismo.'),
(1, '2026-09-20', '18:30:00', 'Hola Laura, la chaqueta la necesito para el día 3');

INSERT INTO envia_mensaje (id_mensaje, id_perfil) VALUES (2, 2),(3, 7), (4, 2), (5, 6), (6, 4),(1, 3);

INSERT INTO recibe_mensaje (id_mensaje, id_perfil) VALUES (2, 3),(3, 2), (4, 7), (5, 4), (6, 6),(1, 2);

INSERT INTO notificacion (id_perfil, tipo, mensaje, leida, fecha) VALUES
(2, 'INTERCAMBIO_SOLICITADO', 'Carlos M. te ha enviado una solicitud de intercambio.', FALSE, '2026-09-20'),
(4, 'PRESTAMO_SOLICITADO', 'Tu solicitud de préstamo se ha registrado correctamente.', TRUE, '2026-09-22'),
(7, 'PRESTAMO_SOLICITADO', 'Tu solicitud de préstamo para "Sudadera Oversize Grey" ha sido aceptada.', TRUE, '2026-09-22'),
(6, 'INTERCAMBIO_SOLICITADO', 'Marta S. ha rechazado tu propuesta de intercambio.', TRUE, '2026-09-24'),
(3, 'PRESTAMO_SOLICITADO', 'Laura G. te ha solicitado el préstamo de "Abrigo Largo de Lana".', TRUE, '2026-09-25'),
(7, 'PRESTAMO_SOLICITADO', 'Carlos M. te ha enviado una solicitud de préstamo para "Blazer Negra Ajustada".', FALSE, '2026-09-27');

INSERT INTO valoracion (id_solicitud, id_perfil, comentario, fecha, puntuacion) VALUES
(1, 3, 'Excelente trato y la prenda estaba como en las fotos, aunque algo liosa la quedada', '2026-09-25', 4),
(3, 7, 'La prenda llegó impecable y el proceso fue rapidísimo. ¡Repetiría sin duda!', '2026-09-27', 5),
(3, 2, 'Elena cuidó la sudadera perfectamente y la devolución fue puntual.', '2026-09-28', 5),
(2, 7, 'Prenda en mal estado, no repetiría', '2026-09-25', 1),
(5, 2, 'Prenda impoluta, buen cliente', '2026-09-27', 5),
(6, 6, 'Trato sin más y prenda en condiciones óptimas', '2026-09-28', 4);


/*Pruebas de selects*/


/*Endpoint 1: Consulta paginada completa de las prendas (paginacion se realiza en java en la siguiente entrega) --- Hecha en Springboot*/

SELECT 
	pr.id,
    pr.titulo,
    pr.descripcion,
    pr.marca,
    pr.talla,
    pr.condicion,
    pr.estado,
    pr.permite_intercambio,
    pr.permite_prestamo,
    pr.fecha_publicacion
FROM prenda pr
ORDER BY pr.fecha_publicacion DESC;

/*Endpoint 2: Detalles de las prendas con fotos y su propietaria con la reputación*/

SELECT 
    pr.id AS id_prenda,
    pr.titulo,
    pr.descripcion,
    pr.marca,
    pr.talla,
    pr.condicion,
    pr.estado,
    pr.permite_intercambio,
    pr.permite_prestamo,
    pr.fecha_publicacion,
    c.nombre AS categoria,
    p.id AS id_propietario,
    CONCAT(p.nombre, ' ', p.ape1) AS propietario, --Nombre completo del propietario
    rep.reputacion, --Reputacion con num de valoraciones
    STRING_AGG(f.url, ', ' ORDER BY f.orden) AS fotos --Concatena las urls de una misma prenda para que no salgan 2 veces repetidas
FROM prenda pr
	JOIN categorias c ON pr.id_categorias = c.id --Para mostrar a que categoría pertenece la prenda 
	JOIN perfil p ON pr.id_perfil = p.id --Para añadir la vista de reputacion
	JOIN reputacion_perfil rep ON p.id = rep.id_perfil --Vista creada anteriormente para obtener las reputaciones de los perfiles
	JOIN foto f ON pr.id = f.id_prenda --Para añadir las URLs
GROUP BY pr.id, c.nombre, p.id, rep.reputacion,rep.num_valoraciones;

/*Endpoint 6: Categorías con número de prendas disponibles --- Hecha en Springboot*/

SELECT 
    c.nombre,
    COUNT(p.id) AS prendas_disponibles
FROM SwapCloset.categorias c
	LEFT JOIN SwapCloset.prenda p ON p.id_categorias = c.id AND p.estado = 'DISPONIBLE' --Left para que muestre las categorias que no tienen anuncios y AND para que muestre solo los activos
GROUP BY c.id, c.nombre
ORDER BY c.nombre ASC;

/*Endpoint 7: Perfil público: estilos, tallas, reputación y prendas ---- Hecha en Springboot*/

SELECT 
	CONCAT(p.nombre, ' ', p.ape1, ' ', p.ape2) AS nombre_completo,
	p.ciudad,
	STRING_AGG(e.nombre, ', ') AS estilos,
	p.biografia,
	CONCAT('Talla pantalón: ', p."talla_pantalon"  ,' | Talla camiseta:', p."talla_camiseta"  ,' | Talla zapato:',p."talla_zapatos") AS tallas,
	rep.reputacion
FROM perfil p
	JOIN reputacion_perfil rep ON rep.id_perfil = p.id
	JOIN estilos_perfil ep ON p.id = ep.id_perfil
	JOIN estilos e ON ep.id_estilo = e.id
GROUP BY p.id, rep.reputacion;

/*Endpoint 9: Perfiles con estilos comunes y buena reputacion ---- Hecho en Springboot*/

SELECT 
    p.id AS id_perfil_coincidente,
    CONCAT(p.nombre, ' ', p.ape1, ' ', p.ape2) AS nombre_completo,
    p.ciudad,
    STRING_AGG(e.nombre, ', ') AS estilos_comunes,
    rep.reputacion
FROM perfil p
	JOIN estilos_perfil ep ON p.id = ep.id_perfil
	JOIN estilos e ON ep.id_estilo = e.id
	JOIN reputacion_perfil rep ON p.id = rep.id_perfil
WHERE p.id != :id_perfil -- Excluimos al usuario que realiza la consulta para que no salga el mismo
  AND ep.id_estilo IN (  -- Subconsulta para obtener solo los estilos que coincide con el usuario
      SELECT id_estilo 
      FROM estilos_perfil 
      WHERE id_perfil = :id_perfil)
  AND (rep.media_puntuacion >= 4.0 OR rep.media_puntuacion IS NOT NULL) -- Criterio de buena reputación
GROUP BY p.id, rep.reputacion, rep.media_puntuacion
ORDER BY rep.media_puntuacion DESC;

/*Endpoint 11: Solicitudes enviadas y recibidas ---- Hecha en springboot*/


/*Consulta donde és solicitante (las que él ha solicitado)*/
SELECT 
	*
FROM solicitud s
WHERE s.id_perfil = :id_perfil;


/*Consulta donde él es el propietario (le han solicitado)*/
SELECT 
	*
FROM solicitud_prenda sp 
JOIN prenda p ON sp.id_prenda = p.id
WHERE p.id_perfil = :id_perfil; 


/*Endpoint 14: Conversacion paginada (paginacion se realiza en java en la siguiente entrega) ---- Hecha en Springboot*/

SELECT 
    m.id AS id_mensaje,
    m.id_solicitud,
    p_emisor.id AS id_emisor,
    CONCAT(p_emisor.nombre, ' ', p_emisor.ape1) AS emisor,
    p_receptor.id AS id_receptor,
    CONCAT(p_receptor.nombre, ' ', p_receptor.ape1) AS receptor,
    m.contenido,
    m.fecha_envio,
    m.hora_envio
FROM mensaje m
	JOIN envia_mensaje em ON m.id = em.id_mensaje -- Relación para obtener el perfil que envía
	JOIN perfil p_emisor ON em.id_perfil = p_emisor.id
	JOIN recibe_mensaje rm ON m.id = rm.id_mensaje -- Relación para obtener el perfil que recibe
	JOIN perfil p_receptor ON rm.id_perfil = p_receptor.id
WHERE m.id_solicitud = :id_solicitud
ORDER BY m.fecha_envio DESC, m.hora_envio DESC;

/*Endpoint 16: Informe de publicaciones intercambios y préstamos por meses*/



