-- ------------------------------------------------------------
-- Script corregido y completo para MySQL 8.x (InnoDB, utf8mb4)
-- ------------------------------------------------------------

DROP DATABASE IF EXISTS `dj_vettech`;
CREATE DATABASE `dj_vettech` CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
USE `dj_vettech`;
-- ------------------------------------------------------------
-- TABLA: rol
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS rol (
    id_rol TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre_rol ENUM('ADMINISTRADOR','VETERINARIO','CLIENTE') NOT NULL,
    descripcion VARCHAR(150)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- TABLA: usuario
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    tipo_doc ENUM('TI','CC','CE') NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(150) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    foto_perfil BLOB, -- considera LONGBLOB si esperas imágenes muy grandes
    id_rol TINYINT UNSIGNED NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- TABLA: veterinario
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS veterinario (
    id_veterinario INT UNSIGNED PRIMARY KEY,
    numero_licencia VARCHAR(30) NOT NULL UNIQUE,
    especialidad VARCHAR(80),
    anios_experiencia TINYINT UNSIGNED,
    CONSTRAINT fk_veterinario_usuario FOREIGN KEY (id_veterinario) REFERENCES usuario(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- TABLA: cliente
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cliente (
    id_cliente INT UNSIGNED PRIMARY KEY,
    fecha_registro DATE,
    mascotas_registradas TINYINT UNSIGNED DEFAULT 0,
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (id_cliente) REFERENCES usuario(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- TABLA: mascota
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS mascota (
    id_mascota INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    edad TINYINT UNSIGNED,
    especie VARCHAR(40) NOT NULL,
    sexo ENUM('HEMBRA','MACHO') NOT NULL,
    raza VARCHAR(50) NOT NULL,
    color VARCHAR(30),
    fecha_nacimiento DATE,
    cantidad_visitas TINYINT UNSIGNED DEFAULT 0,
    estado VARCHAR(50) DEFAULT 'Activo',
    id_cliente INT UNSIGNED NOT NULL,
    CONSTRAINT fk_mascotas_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- TABLA: tipo_servicio
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS tipo_servicio (
    id_servicio TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(60) NOT NULL,
    descripcion VARCHAR(250),
    precio DECIMAL(10,2) UNSIGNED NOT NULL,
    duracion TIME NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- TABLA: cita
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cita (
    id_cita INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT UNSIGNED NULL,
    id_mascota INT UNSIGNED NULL,
    id_veterinario INT UNSIGNED NOT NULL,
    id_servicio TINYINT UNSIGNED NULL,
    fecha_cita DATE NOT NULL,
    hora_cita TIME NOT NULL,
    estado_cita ENUM('PROGRAMADA','COMPLETADA','CANCELADA') NOT NULL DEFAULT 'PROGRAMADA',
    motivo VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cita_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_cita_mascota FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_cita_veterinario FOREIGN KEY (id_veterinario) REFERENCES veterinario(id_veterinario) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_cita_servicio FOREIGN KEY (id_servicio) REFERENCES tipo_servicio(id_servicio) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- TABLA: historial_clinico
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS historial_clinico (
    id_historial INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    motivo_consulta VARCHAR(255),
    diagnostico TEXT NOT NULL,
    tratamiento TEXT,
    medicacion VARCHAR(150),
    frecuencia VARCHAR(80),
    estado VARCHAR(50),
    id_mascota INT UNSIGNED NOT NULL,
    id_servicio TINYINT UNSIGNED NULL,
    id_veterinario INT UNSIGNED NULL,
    id_cita INT UNSIGNED NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_hist_mascota FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_hist_servicio FOREIGN KEY (id_servicio) REFERENCES tipo_servicio(id_servicio) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_hist_veterinario FOREIGN KEY (id_veterinario) REFERENCES veterinario(id_veterinario) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_hist_cita FOREIGN KEY (id_cita) REFERENCES cita(id_cita) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- TABLA: tipo_producto
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS tipo_producto (
    id_tipo_producto INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre_producto VARCHAR(120) NOT NULL,
    categoria VARCHAR(60) NOT NULL,
    descripcion TEXT,
    precio_unitario DECIMAL(10,2) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- TABLA: inventario
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS inventario (
    id_inventario INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_tipo_producto INT UNSIGNED NOT NULL,
    cantidad INT UNSIGNED NOT NULL DEFAULT 0,
    ubicacion VARCHAR(100),
    CONSTRAINT fk_inventario_tipo_producto FOREIGN KEY (id_tipo_producto) REFERENCES tipo_producto(id_tipo_producto) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- TABLA: entrada_producto
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS entrada_producto (
    id_entrada INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_inventario INT UNSIGNED NOT NULL,
    cantidad_entrada SMALLINT UNSIGNED NOT NULL,
    fecha_entrada DATETIME NOT NULL,
    CONSTRAINT fk_entrada_inventario FOREIGN KEY (id_inventario) REFERENCES inventario(id_inventario) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- TABLA: salida_producto
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS salida_producto (
    id_salida INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_inventario INT UNSIGNED NOT NULL,
    cantidad_salida SMALLINT UNSIGNED NOT NULL,
    fecha_salida DATETIME NOT NULL,
    motivo_salida VARCHAR(150),
    CONSTRAINT fk_salida_inventario FOREIGN KEY (id_inventario) REFERENCES inventario(id_inventario) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- TABLA: ventas (cabecera) y detalle_venta
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ventas (
    id_venta INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_vet INT UNSIGNED NULL,
    id_cliente INT UNSIGNED NULL,
    fecha_venta DATETIME NOT NULL,
    total DECIMAL(12,2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_ventas_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_ventas_vet FOREIGN KEY (id_vet) REFERENCES veterinario(id_veterinario) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS detalle_venta (
    id_detalle INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_venta INT UNSIGNED NOT NULL,
    id_inventario INT UNSIGNED NOT NULL,
    cantidad INT UNSIGNED NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_venta_venta FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_detalle_venta_inventario FOREIGN KEY (id_inventario) REFERENCES inventario(id_inventario) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ------------------------------------------------------------
-- POBLAMIENTO
-- ------------------------------------------------------------
-- roles
INSERT INTO rol (id_rol,nombre_rol,descripcion) VALUES
    (1,'ADMINISTRADOR','Acceso total'),
    (2,'VETERINARIO','Atención en clínica'),
    (3,'CLIENTE','Registro de mascotas');

-- usuarios
INSERT INTO usuario (id_usuario,tipo_doc,nombre,apellido,correo,telefono,direccion,contrasena,id_rol) VALUES
    (1,'CC','Ana','Gómez','ana@correo.com','3111111111','Cra 10 #20-30','12345',3),
    (2,'CC','Carlos','Pérez','carlos@correo.com','3222222222','Calle 45 #12-50','abcde',3),
    (3,'CE','Laura','Ríos','laura@correo.com','3333333333','Av 3 #4-56','qwerty',3),
    (4,'TI','Luis','Martínez','luis@correo.com','3444444444','Calle 9 #6-12','clave1',3),
    (5,'CC','Camila','Torres','camila@correo.com','3555555555','Cra 80 #25-50','123cam',3),
    (6,'CC','Maria','Elena','mariaelenaballesterosgonzalez@gmail.com','3106642475','Cra 37D Sur #72J-39','eflenab1972',2),
    (7,'CC','Marina','Ballestero','mballesteros1968@gmail.com','3005467125','Cra 86A #92-32','oifs3993k',2),
    (8,'CE','Viviana','López','vianloci@gmail.com','3008906953','Cra 69 #76A-12 Sur','~X>16)m9',2),
    (9,'CC','Yamile','López','yamile85lopez@gmail.com','3209840690','Cra 69 #76A-12 Sur','ya12lopa',2),
    (10,'CE','Katherin','Camacho','kathe0608@gmail.com','3022230382','Cra 69 #76A-12 Sur','9H575y4D',3),
    (11,'CC','Paula','Hernández','paulasamhernandez@gmail.com','3227320347','Cra 69 #76A-12 Sur','3r4srx2C',3),
    (12,'CC','Giovanny','Jimenez','giovanny79497600@gmail.com','3197825433','Cra 37D Sur #72J-39','FUSgc5it23',3),
    (13,'CC','Eliecer','Jimenez','eliecerjimenez1956@gmail.com','3106823411','Cra 80 #69-70','Vg0miQP53R',3),
    (14,'CC','Camila','Pascal','pascalc465@gmail.com','3112457821','Av 3 #4-56','10ERO1Ppof',3),
    (15,'CE','Ana','Gomez','ana12@gmail.com','3112457812','Cra 10 #20-30','n444D7T2l2',3),
    (16,'CC','Carlos','Perez','carlos45p@gmail.com','3265451232','Calle 45 #12-50','7jd9d7WBi2',3),
    (17,'CC','Laura','Rios','luarars132@gmail.com','3124577812','Cra 69 #76A-12 Sur','UQe87K1',3),
    (18,'CC','Jonathan','Jimenez','jonathanalejandroj218@gmail.com','3208623727','Cra 37D Sur #72J-39','hdkhdk809',1),
    (19,'TI','Juan','Cadenas','juanpahernandezc911@gmail.com','3212548712','KR 15A Este #71 Sur-32','Cjuan1245',1),
    (20,'TI','Danna','Arévalo','dannasofiaarevalo282904@gmail.com','3013455548','Cra 69 #76A-12 Sur','Danna123',1);

-- veterinarios (usuarios que actúan como veterinarios; su id debe existir en usuario)
INSERT INTO veterinario (id_veterinario,numero_licencia,especialidad,anios_experiencia) VALUES
    (6,'LIC001','General',5),
    (7,'LIC002','Dermatología',3),
    (8,'LIC003','Cirugía',5),
    (9,'LIC004','Dermatología',3);

-- clientes — generar registros clientes para todos los usuarios con rol=3
INSERT INTO cliente (id_cliente,fecha_registro,mascotas_registradas)
SELECT u.id_usuario, CURDATE(), 1
FROM usuario u
WHERE u.id_rol = 3
    AND u.id_usuario NOT IN (SELECT c.id_cliente FROM cliente c);

-- tipo_servicio
INSERT INTO tipo_servicio (id_servicio,tipo,descripcion,precio,duracion) VALUES
    (1,'CONSULTA_GENERAL','Consulta básica',50000.00,'00:30:00'),
    (2,'CONTROL_PLAGAS','Antipulgas',70000.00,'00:45:00'),
    (3,'GUÍA_NUTRICIONAL','Consejos nutricionales',60000.00,'00:40:00'),
    (4,'PLAN_SALUD','Plan de salud preventivo',150000.00,'01:00:00'),
    (5,'ESTETICA','Servicio de baño y peluquería',80000.00,'01:30:00');

-- mascotas 
INSERT INTO mascota (nombre,edad,especie,sexo,raza,color,fecha_nacimiento,cantidad_visitas,id_cliente) VALUES
    ('Firulais',3,'Perro','MACHO','Labrador','Marrón','2020-01-01',4,1),
    ('Misu',2,'Gato','HEMBRA','Persa','Blanco','2021-06-15',2,2),
    ('Rocky',1,'Perro','MACHO','Bulldog','Negro','2022-09-20',1,5),
    ('Luna',4,'Gato','HEMBRA','Siamés','Gris','2019-04-10',5,5),
    ('Max',2,'Perro','MACHO','Beagle','Tricolor','2022-02-20',3,10),
    ('Nala',1,'Gato','HEMBRA','Maine Coon','Marrón','2023-01-05',1,11),
    ('Thor',5,'Perro','MACHO','Pastor Alemán','Negro','2018-07-07',6,12),
    ('Milo',3,'Gato','MACHO','Bengalí','Naranja','2021-09-09',4,13),
    ('Bella',4,'Perro','HEMBRA','Golden','Dorado','2019-05-05',5,14),
    ('Simba',2,'Gato','MACHO','Persa','Blanco','2022-03-03',2,15),
    ('Oreo',3,'Conejo','MACHO','Angora','BlancoNegro','2021-11-11',3,16),
    ('Rocky Jr.',1,'Perro','MACHO','Bulldog','Gris','2024-02-02',1,17),
    ('Coco',2,'Loro','HEMBRA','Amazonas','Verde','2023-06-06',2,1),
    ('Rex',6,'Perro','MACHO','Rottweiler','Negro','2017-08-08',7,2),
    ('Kiwi',1,'Gato','HEMBRA','Exótico','Beige','2024-03-03',1,3);

-- tipo_producto
INSERT INTO tipo_producto (id_tipo_producto,nombre_producto,categoria,descripcion,precio_unitario) VALUES
    (1,'Shampoo para perros','Higiene','Limpieza de pelaje',15000.00),
    (2,'Alimento para gatos','Nutrición','Comida seca',30000.00),
    (3,'Juguete pelota','Entretenimiento','Pelota de goma',8000.00),
    (4,'Collar reflectante','Accesorios','Collar con luz',20000.00),
    (5,'Cama para perro','Descanso','Cama acolchada',45000.00),
    (6,'Plato acero','Accesorios','Plato de acero inox',10000.00),
    (7,'Antipulgas pipeta','Salud','Tratamiento tópico',25000.00),
    (8,'Cepillo para pelo','Higiene','Cepillo suave',12000.00),
    (9,'Leche para gatitos','Nutrición','Suplemento lácteo',18000.00),
    (10,'Transportadora','Accesorios','Caja de transporte',55000.00),
    (11,'Chaqueta invierno','Ropa','Abrigo cálido',30000.00),
    (12,'Snacks perro','Nutrición','Premios saludables',7000.00),
    (13,'Fuente agua','Accesorios','Fuente automática',60000.00),
    (14,'Arena para gato','Higiene','Arena aglomerante',25000.00);

-- inventario
INSERT INTO inventario (id_inventario,id_tipo_producto,cantidad,ubicacion) VALUES
    (1,1,50,'Estante A'),
    (2,2,100,'Estante B'),
    (3,3,30,'Estante C'),
    (4,4,20,'Estante C'),
    (5,5,15,'Estante D'),
    (6,6,40,'Estante D'),
    (7,7,25,'Estante E'),
    (8,8,60,'Estante E'),
    (9,9,35,'Estante F'),
    (10,10,10,'Estante F'),
    (11,11,18,'Estante G'),
    (12,12,80,'Estante G'),
    (13,13,12,'Estante H'),
    (14,14,55,'Estante H');

-- entradas / salidas
INSERT INTO entrada_producto (id_inventario,cantidad_entrada,fecha_entrada) VALUES
    (1,20,'2025-05-01 10:00:00'),
    (2,40,'2025-05-05 14:00:00'),
    (3,10,'2025-05-10 09:00:00'),
    (4,5,'2025-05-11 11:00:00'),
    (5,8,'2025-05-12 15:00:00'),
    (6,20,'2025-05-13 12:00:00'),
    (7,15,'2025-05-14 16:00:00'),
    (8,30,'2025-05-15 10:30:00'),
    (9,12,'2025-05-16 13:45:00'),
    (10,7,'2025-05-17 09:20:00'),
    (11,9,'2025-05-18 14:55:00'),
    (12,25,'2025-05-19 11:10:00'),
    (13,5,'2025-05-20 08:00:00'),
    (14,18,'2025-05-21 17:30:00');

INSERT INTO salida_producto (id_inventario,cantidad_salida,fecha_salida,motivo_salida) VALUES
    (1,5,'2025-05-10 09:00:00','Venta'),
    (2,10,'2025-05-12 11:00:00','Uso interno'),
    (3,3,'2025-05-11 10:00:00','Venta'),
    (4,2,'2025-05-12 12:00:00','Venta'),
    (5,1,'2025-05-13 14:00:00','Venta'),
    (6,4,'2025-05-14 15:30:00','Venta'),
    (7,2,'2025-05-15 09:15:00','Venta'),
    (8,5,'2025-05-16 16:45:00','Venta'),
    (9,3,'2025-05-17 08:20:00','Venta'),
    (10,1,'2025-05-18 13:50:00','Venta'),
    (11,2,'2025-05-19 10:05:00','Venta'),
    (12,6,'2025-05-20 11:25:00','Venta'),
    (13,1,'2025-05-21 12:00:00','Uso interno'),
    (14,7,'2025-05-22 14:40:00','Venta');

-- historial_clinico (valores NULL donde corresponde en medicacion/frecuencia/tratamiento)
INSERT INTO historial_clinico
(fecha,motivo_consulta,diagnostico,tratamiento,medicacion,frecuencia,estado,id_mascota,id_servicio,id_veterinario) VALUES
    ('2024-04-01','Dolor de estómago','Gastritis leve','Medicamento A','Amoxicilina','Cada 8h','Finalizado',1,1,6),
    ('2024-05-10','Rascado constante','Pulgas','Tratamiento B','Fipronil','Cada 30 días','Finalizado',2,2,7),
    ('2025-06-01','Chequeo general','Sano', NULL, NULL, NULL,'Cerrado',3,1,8),
    ('2025-06-02','Cortada en pata','Herida superficial','Venda','Diclofenaco','Cada 12h','En tratamiento',4,1,9),
    ('2025-06-03','Limpieza dental','Placa leve','Limpieza', NULL, NULL,'Finalizado',5,1,6),
    ('2025-06-04','Visita vacunación','Vacunado','Vacuna X','Vacuna Leishmania','Anual','Finalizado',6,2,7),
    ('2025-06-05','Control plagas','Libre de pulgas','Sesión plagas','Spray Antipulgas','Cada 30d','En tratamiento',7,2,8),
    ('2025-06-06','Nutrición','Peso adecuado','Plan nutrición', NULL, NULL,'Cerrado',8,3,9),
    ('2025-06-07','Chequeo oto','Óptimo', NULL, NULL, NULL,'Finalizado',9,1,6),
    ('2025-06-08','Aseo completo','Estético','Baño y corte', NULL, NULL,'Finalizado',10,5,7),
    ('2025-06-09','Consulta interna','Revisión', NULL, NULL, NULL,'Cerrado',11,1,8),
    ('2025-06-10','Chequeo externo','Todo bien', NULL, NULL, NULL,'Finalizado',12,1,9),
    ('2025-06-11','Vacunación anual','Vacunado','Vacuna Y','Vacuna Rabia','Anual','Finalizado',13,1,6),
    ('2025-06-12','Aseo y corte','Corte realizado', NULL, NULL, NULL,'Finalizado',14,5,7);

-- ventas (cabecera)
INSERT INTO ventas (id_venta,id_vet,id_cliente,fecha_venta,total) VALUES
    (1,6,1,'2025-05-10 09:10:00',45000.00),
    (2,7,2,'2025-05-12 11:10:00',150000.00),
    (3,6,10,'2025-05-11 10:05:00',16000.00),
    (4,7,11,'2025-05-12 12:15:00',20000.00),
    (5,8,12,'2025-05-13 14:10:00',45000.00),
    (6,9,13,'2025-05-14 15:40:00',30000.00),
    (7,6,14,'2025-05-15 09:20:00',50000.00),
    (8,7,15,'2025-05-16 16:50:00',48000.00),
    (9,8,16,'2025-05-17 08:25:00',18000.00),
    (10,9,17,'2025-05-18 13:55:00',55000.00),
    (11,6,1,'2025-05-19 10:15:00',60000.00),
    (12,7,2,'2025-05-20 11:35:00',25000.00),
    (13,8,5,'2025-05-21 12:05:00',60000.00),
    (14,9,10,'2025-05-22 14:45:00',50000.00);

-- detalle_venta (ejemplos)
INSERT INTO detalle_venta (id_venta,id_inventario,cantidad,precio_unitario) VALUES
    (1,1,3,15000.00),
    (2,2,5,30000.00),
    (3,3,2,8000.00),
    (4,4,1,20000.00),
    (5,5,1,45000.00),
    (6,6,3,10000.00),
    (7,7,2,25000.00),
    (8,8,4,12000.00),
    (9,9,1,18000.00),
    (10,10,1,55000.00),
    (11,11,2,30000.00),
    (12,12,1,25000.00),
    (13,13,1,60000.00),
    (14,14,2,25000.00);

-- ------------------------------------------------------------
-- PROCEDIMIENTOS / FUNCIONES (sin turnos)
-- ------------------------------------------------------------
DELIMITER //

-- Top productos vendidos
CREATE PROCEDURE top_productos_vendidos(IN p_limite INT)
BEGIN
    SELECT tp.id_tipo_producto, tp.nombre_producto, SUM(dv.cantidad) AS total_vendido
    FROM detalle_venta dv
    JOIN inventario i ON dv.id_inventario = i.id_inventario
    JOIN tipo_producto tp ON i.id_tipo_producto = tp.id_tipo_producto
    GROUP BY tp.id_tipo_producto, tp.nombre_producto
    ORDER BY total_vendido DESC
    LIMIT p_limite;
END;
//

-- Stock bajo
CREATE PROCEDURE productos_stock_bajo(IN p_umbral INT)
BEGIN
    SELECT tp.id_tipo_producto, tp.nombre_producto, i.cantidad AS stock_actual,
           CASE
               WHEN i.cantidad = 0 THEN 'SIN STOCK'
               WHEN i.cantidad < p_umbral THEN 'CRÍTICO'
               WHEN i.cantidad BETWEEN p_umbral AND p_umbral*2 THEN 'BAJO'
               ELSE 'ADECUADO'
           END AS estado_stock
    FROM inventario i
    JOIN tipo_producto tp ON i.id_tipo_producto = tp.id_tipo_producto
    WHERE i.cantidad < p_umbral*3
    ORDER BY i.cantidad ASC;
END;
//

-- Ingresos por veterinario
CREATE PROCEDURE ingresos_por_veterinario()
BEGIN
    SELECT u.id_usuario, u.nombre, u.apellido, COALESCE(SUM(v.total),0) AS ingresos
    FROM veterinario vt
    LEFT JOIN usuario u ON vt.id_veterinario = u.id_usuario
    LEFT JOIN ventas v ON vt.id_veterinario = v.id_vet
    GROUP BY u.id_usuario;
END;
//

-- Ventas mensuales
CREATE PROCEDURE ingresos_mensuales()
BEGIN
    SELECT YEAR(fecha_venta) AS anio, MONTH(fecha_venta) AS mes,
           ROUND(SUM(total),2) AS ingresos_totales,
           COUNT(id_venta) AS num_ventas
    FROM ventas
    GROUP BY YEAR(fecha_venta), MONTH(fecha_venta)
    ORDER BY anio DESC, mes DESC;
END;
//

-- Clientes frecuentes
CREATE PROCEDURE clientes_frecuentes(IN p_min_compras INT)
BEGIN
    SELECT u.id_usuario, u.nombre, u.apellido, COUNT(v.id_venta) AS total_compras,
           DATEDIFF(MAX(v.fecha_venta), MIN(v.fecha_venta)) AS dias_actividad
    FROM ventas v
    JOIN cliente c ON v.id_cliente = c.id_cliente
    JOIN usuario u ON c.id_cliente = u.id_usuario
    GROUP BY u.id_usuario
    HAVING total_compras >= p_min_compras
    ORDER BY total_compras DESC;
END;
//

-- registrar venta (cabecera). Los detalles se insertan en detalle_venta
CREATE PROCEDURE registrar_venta(
    IN p_id_vet INT,
    IN p_id_cliente INT,
    IN p_fecha DATETIME,
    IN p_total DECIMAL(12,2)
)
BEGIN
    INSERT INTO ventas (id_vet, id_cliente, fecha_venta, total)
    VALUES (p_id_vet, p_id_cliente, p_fecha, p_total);
END;
//

-- registrar entrada producto
CREATE PROCEDURE registrar_entrada_producto(
    IN p_id_inventario INT,
    IN p_cantidad_entrada INT,
    IN p_fecha_entrada DATETIME
)
BEGIN
    INSERT INTO entrada_producto (id_inventario, cantidad_entrada, fecha_entrada)
    VALUES (p_id_inventario, p_cantidad_entrada, p_fecha_entrada);

    UPDATE inventario
    SET cantidad = cantidad + p_cantidad_entrada
    WHERE id_inventario = p_id_inventario;
END;
//

-- registrar salida producto
CREATE PROCEDURE registrar_salida_producto(
    IN p_id_inventario INT,
    IN p_cantidad_salida INT,
    IN p_fecha_salida DATETIME,
    IN p_motivo VARCHAR(150)
)
BEGIN
    INSERT INTO salida_producto (id_inventario, cantidad_salida, fecha_salida, motivo_salida)
    VALUES (p_id_inventario, p_cantidad_salida, p_fecha_salida, p_motivo);

    UPDATE inventario
    SET cantidad = GREATEST(cantidad - p_cantidad_salida, 0)
    WHERE id_inventario = p_id_inventario;
END;
//

-- total servicios por veterinario
CREATE FUNCTION total_servicios_veterinario(vet_id INT) RETURNS INT DETERMINISTIC
BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total
    FROM historial_clinico
    WHERE id_veterinario = vet_id;
    RETURN total;
END;
//

DELIMITER ;

-- ------------------------------------------------------------
-- TRIGGERS (stock & historial)
-- ------------------------------------------------------------
DELIMITER //

-- 1) Antes de insertar detalle_venta: verificar stock suficiente
CREATE TRIGGER trg_prevent_out_of_stock_detalle_before_insert
BEFORE INSERT ON detalle_venta
FOR EACH ROW
BEGIN
    DECLARE stock_actual INT DEFAULT 0;
    SELECT cantidad INTO stock_actual
    FROM inventario
    WHERE id_inventario = NEW.id_inventario
    FOR UPDATE;

    IF stock_actual < NEW.cantidad THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Stock insuficiente para este producto.';
    END IF;
END;
//

-- 2) Después de insertar detalle_venta: descontar stock y recalcular total venta
CREATE TRIGGER trg_update_stock_after_detalle_insert
AFTER INSERT ON detalle_venta
FOR EACH ROW
BEGIN
    UPDATE inventario
    SET cantidad = GREATEST(cantidad - NEW.cantidad, 0)
    WHERE id_inventario = NEW.id_inventario;

    UPDATE ventas
    SET total = COALESCE((SELECT SUM(cantidad * precio_unitario) FROM detalle_venta WHERE id_venta = NEW.id_venta), 0)
    WHERE id_venta = NEW.id_venta;
END;
//

-- 3) Después de eliminar detalle_venta: restaurar stock y recalcular total venta
CREATE TRIGGER trg_restore_stock_after_detalle_delete
AFTER DELETE ON detalle_venta
FOR EACH ROW
BEGIN
    UPDATE inventario
    SET cantidad = cantidad + OLD.cantidad
    WHERE id_inventario = OLD.id_inventario;

    UPDATE ventas
    SET total = COALESCE((SELECT SUM(cantidad * precio_unitario) FROM detalle_venta WHERE id_venta = OLD.id_venta), 0)
    WHERE id_venta = OLD.id_venta;
END;
//

-- 4) Después de actualizar detalle_venta: ajustar stock y totales (maneja cambio de id_inventario o cantidad)
CREATE TRIGGER trg_adjust_stock_after_detalle_update
AFTER UPDATE ON detalle_venta
FOR EACH ROW
BEGIN
    -- si cambió producto, devolver cantidad al inventario antiguo y descontar del nuevo
    IF OLD.id_inventario <> NEW.id_inventario THEN
        UPDATE inventario SET cantidad = cantidad + OLD.cantidad WHERE id_inventario = OLD.id_inventario;
        UPDATE inventario SET cantidad = GREATEST(cantidad - NEW.cantidad, 0) WHERE id_inventario = NEW.id_inventario;
    ELSE
        -- mismo producto: ajustar por diferencia (si NEW>OLD se descuenta más; si NEW<OLD se resta menos)
        UPDATE inventario
        SET cantidad = GREATEST(cantidad - (NEW.cantidad - OLD.cantidad), 0)
        WHERE id_inventario = NEW.id_inventario;
    END IF;

    -- actualizar totales en ventas (para venta nueva y antigua si id_venta cambió)
    UPDATE ventas
    SET total = COALESCE((SELECT SUM(cantidad * precio_unitario) FROM detalle_venta WHERE id_venta = NEW.id_venta), 0)
    WHERE id_venta = NEW.id_venta;

    IF OLD.id_venta <> NEW.id_venta THEN
        UPDATE ventas
        SET total = COALESCE((SELECT SUM(cantidad * precio_unitario) FROM detalle_venta WHERE id_venta = OLD.id_venta), 0)
        WHERE id_venta = OLD.id_venta;
    END IF;
END;
//

-- 5) Después de insertar historial_clinico: incrementar visitas y actualizar estado mascota si aplica
CREATE TRIGGER trg_after_insert_historial
AFTER INSERT ON historial_clinico
FOR EACH ROW
BEGIN
    -- incrementar contador de visitas
    UPDATE mascotas
    SET cantidad_visitas = cantidad_visitas + 1
    WHERE id_mascota = NEW.id_mascota;

    -- actualizar estado si se proporcionó uno no vacío y no es placeholder
    IF NEW.estado IS NOT NULL AND TRIM(NEW.estado) <> '' AND NEW.estado NOT IN ('—','-') THEN
        UPDATE mascotas
        SET estado = NEW.estado
        WHERE id_mascota = NEW.id_mascota;
    END IF;
END;
//

DELIMITER ;

-- ------------------------------------------------------------
-- Consultas de ejemplo (útiles para toma de decisiones)
-- ------------------------------------------------------------

-- 1) Ingresos por mes
SELECT YEAR(fecha_venta) AS anio, MONTH(fecha_venta) AS mes, ROUND(SUM(total),2) AS ingresos
FROM ventas
GROUP BY YEAR(fecha_venta), MONTH(fecha_venta)
ORDER BY anio DESC, mes DESC;

-- 2) Top 5 productos más vendidos
SELECT
    tp.id_tipo_producto,
    tp.nombre_producto,
    SUM(dv.cantidad) AS total_vendido
FROM detalle_venta dv
JOIN inventario i ON dv.id_inventario = i.id_inventario
JOIN tipo_producto tp ON i.id_tipo_producto = tp.id_tipo_producto
GROUP BY tp.id_tipo_producto, tp.nombre_producto
ORDER BY total_vendido DESC
LIMIT 5;

-- 3) Productos con stock bajo (umbral 20)
SELECT
    tp.id_tipo_producto,
    tp.nombre_producto,
    i.cantidad AS stock_actual,
    CASE
        WHEN i.cantidad = 0 THEN 'SIN STOCK'
        WHEN i.cantidad < 20 THEN 'CRÍTICO'
        WHEN i.cantidad BETWEEN 20 AND 40 THEN 'BAJO'
        ELSE 'ADECUADO'
    END AS estado_stock
FROM inventario i
JOIN tipo_producto tp ON i.id_tipo_producto = tp.id_tipo_producto
WHERE i.cantidad < 60
ORDER BY i.cantidad ASC;

-- 4) Clientes frecuentes (>=3 compras)
SELECT
    u.id_usuario AS id_cliente,
    u.nombre,
    u.apellido,
    COUNT(v.id_venta) AS total_compras,
    DATEDIFF(MAX(v.fecha_venta), MIN(v.fecha_venta)) AS dias_actividad,
    ROUND(SUM(v.total),2) AS gasto_total
FROM ventas v
JOIN cliente c ON v.id_cliente = c.id_cliente
JOIN usuario u ON c.id_cliente = u.id_usuario
GROUP BY u.id_usuario, u.nombre, u.apellido
HAVING total_compras >= 3
ORDER BY total_compras DESC, gasto_total DESC;

-- Historial clinico versiones
CREATE TABLE historial_clinico_versiones (
    id_version INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_historial INT UNSIGNED NOT NULL,
    fecha DATE NOT NULL,
    motivo_consulta VARCHAR(255),
    diagnostico TEXT NOT NULL,
    tratamiento TEXT,
    medicacion VARCHAR(150),
    frecuencia VARCHAR(80),
    estado VARCHAR(50),
    id_mascota INT UNSIGNED NOT NULL,
    id_servicio TINYINT UNSIGNED NULL,
    id_veterinario INT UNSIGNED NULL,
    id_cita INT UNSIGNED NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version TINYINT UNSIGNED DEFAULT 1,
    
    -- FK CORREGIDAS
    CONSTRAINT fk_version_historial 
        FOREIGN KEY (id_historial) REFERENCES historial_clinico(id_historial) ON DELETE CASCADE,

    CONSTRAINT fk_version_mascota 
        FOREIGN KEY (id_mascota) REFERENCES mascota(id_mascota) ON DELETE CASCADE,

    CONSTRAINT fk_version_servicio 
        FOREIGN KEY (id_servicio) REFERENCES tipo_servicio(id_servicio) ON DELETE SET NULL,

    CONSTRAINT fk_version_veterinario 
        FOREIGN KEY (id_veterinario) REFERENCES veterinario(id_veterinario) ON DELETE SET NULL,

    CONSTRAINT fk_version_cita 
        FOREIGN KEY (id_cita) REFERENCES cita(id_cita) ON DELETE SET NULL
);


-- FIN del script corregido
flush tables;
