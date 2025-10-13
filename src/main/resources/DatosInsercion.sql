-- =====================================================
-- 1️⃣ TABLA: metodo_pago
-- =====================================================
INSERT INTO metodo_pago (id_metodo_pago, activo, descripcion, requisitos, tipo_metodo) VALUES
    (1, b'1', 'Pago con Plin', 'Codigo Seguridad: 083', 'PLIN');


-- =====================================================
-- 2️⃣ TABLA: pais
-- =====================================================
INSERT INTO pais (id_pais, codigo, nombre, codigo_telefono) VALUES
                                                                (1, 'AR', 'Argentina', '+57'),
                                                                (2, 'PE', 'Perú', '+51');


-- =====================================================
-- 3️⃣ TABLA: tipo_identificacion
-- =====================================================
INSERT INTO tipo_identificacion (id_tipo_identificacion, nombre) VALUES
                                                                     (1, 'DNI'),
                                                                     (2, 'RUC'),
                                                                     (3, 'CE');


-- =====================================================
-- 4️⃣ TABLA: usuario
-- =====================================================
INSERT INTO usuario (id_usuario, apellido, correo, estado, nombre, numero_celular, numero_identificacion, password, rol, id_codigo_pais, id_tipo_identificacion) VALUES
                                                                                                                                                                     (1, 'Castillo No Jara', 'alejandro@gmail.com', 'ACTIVO', 'Alejandro', '981142779', '73200675', '$2a$10$5.y3X90p639rwaGvhGrsjutlH/kWgCNEZs.s7v8UIX8alZ1abX17C', 'ALUMNO', 2, 1),
                                                                                                                                                                     (4, 'Castillo Jara', 'luchito@gmail.com', 'ACTIVO', 'Luchito', '987654321', '87654321', '$2a$10$yfPFyCsoo2HA.K5KMMtMq.HQpY.Li.u91nY0.79PPyT06FiHwjy5q', 'ALUMNO', 2, 1),
                                                                                                                                                                     (5, 'Castillo Jara', 'pepito@gmail.com', 'ACTIVO', 'pepito', '987654321', '12345678', '$2a$10$TFcvVAyWulpwfzDnJP4lmOgCFS2KAx27xZMxlQ0/yQS8rsUqcE3MK', 'ALUMNO', 2, 1),
                                                                                                                                                                     (10, 'Jara C', 'lddfeo@gmail.com', 'ACTIVO', 'Aledjandro', '987654321', '87654321', '$2a$10$5U.l.VPSIa9GjMb6WClt6urMf9sydu16eICo3NmNi1sj/Lja7MSnG', 'ALUMNO', 2, 1),
                                                                                                                                                                     (11, 'Castillo', 'luhito2@gmail.com', 'ACTIVO', 'luchito', '187654321', '12345678', '$2a$10$xnm3r1FjT2RmFUDgnAYacuV7qiYVIQSJYlTp4X9GSKcsLwdvriP.S', 'ADMINISTRADOR', 2, 1);


-- =====================================================
-- 5️⃣ TABLA: categoria
-- =====================================================
INSERT INTO categoria (id_categoria, descripcion, nombre, id_usuario) VALUES
                                                                          (1, 'Cursos de programación', 'Programación', NULL),
                                                                          (2, 'SQL', 'Base de Datos', NULL),
                                                                          (3, 'Python', 'Seguridad Informatica', NULL);


-- =====================================================
-- 6️⃣ TABLA: curso_diplomado
-- =====================================================
INSERT INTO curso_diplomado (id_curso_diplomado, objetivo, otorga_certificado, tipo, titulo, url_curso, id_categoria, id_usuario) VALUES
                                                                                                                                      (1, 'Curso avanzado de Java para desarrolladores', b'1', b'0', 'Java Avanzado', 'https://storage.googleapis.com/site.esss.co/4b6d4869-img-blog-5-melhores-treinamentos.jpg', 1, 1),
                                                                                                                                      (2, 'Curso avanzado de Java para desarrolladores', b'0', b'1', 'SQL SERVER', 'https://tenomiedo.jpg', 2, 1);


-- =====================================================
-- 7️⃣ TABLA: descuento
-- =====================================================
INSERT INTO descuento (id_descuento, fecha_fin, fecha_inicio, tipo_descuento, valor, vigente, id_usuario) VALUES
                                                                                                              (1, '2025-10-30', '2025-01-01', 'MONTO', 25.50, b'1', 1),
                                                                                                              (2, '2025-12-31', '2024-01-01', 'MONTO', 50.00, b'1', 1),
                                                                                                              (3, '2025-12-31', '2024-01-01', 'MONTO', 10.00, b'1', 1);


-- =====================================================
-- 8️⃣ TABLA: programacion_curso
-- =====================================================
INSERT INTO programacion_curso (id_programacion_curso, duracion_curso, fecha_fin, fecha_inicio, horas_semanales, modalidad, id_usuario, monto, id_curso_diplomado) VALUES
    (1, 6.00, '2025-10-30', '2025-10-09', 8.00, 'VIRTUAL', 4, 500.00, 2);


-- =====================================================
-- 9️⃣ TABLA: descuento_aplicacion
-- =====================================================
INSERT INTO descuento_aplicacion (id_descuento_aplicacion, tipo_aplicacion, id_categoria, id_curso_diplomado, id_descuento, id_usuario) VALUES
                                                                                                                                            (1, 'CATEGORIA', 1, NULL, 1, 1),
                                                                                                                                            (2, 'CURSO', NULL, 2, 3, NULL);


-- =====================================================
-- 🔟 TABLA: matricula
-- =====================================================
INSERT INTO matricula (id_matricula, estado, fecha_matricula, id_descuento, monto, monto_base, monto_descontado, id_administrador, id_alumno, id_programacion_curso) VALUES
                                                                                                                                                                         (2, 'PENDIENTE', '2025-10-03 23:40:03.000000', NULL, NULL, NULL, NULL, 1, 1, 1),
                                                                                                                                                                         (3, 'PENDIENTE', '2025-10-07 23:08:56.000000', 1, 0.00, 0.00, 0.00, NULL, 1, 1),
                                                                                                                                                                         (4, 'PENDIENTE', '2025-10-07 23:13:43.000000', 1, 300.00, 300.00, 0.00, NULL, 1, 1),
                                                                                                                                                                         (5, 'PENDIENTE', '2025-10-08 16:25:37.000000', 1, 300.00, 300.00, 0.00, NULL, 4, 1),
                                                                                                                                                                         (6, 'PENDIENTE', '2025-10-08 16:32:42.000000', 1, 300.00, 300.00, 0.00, NULL, 4, 1),
                                                                                                                                                                         (7, 'PENDIENTE', '2025-10-08 23:45:37.000000', 1, 274.50, 300.00, 25.50, NULL, 4, 1),
                                                                                                                                                                         (8, 'PENDIENTE', '2025-10-10 00:31:01.000000', 2, 250.00, 300.00, 50.00, NULL, 1, 1),
                                                                                                                                                                         (9, 'PENDIENTE', '2025-10-11 00:20:45.000000', NULL, 300.00, 300.00, 0.00, NULL, 1, 1),
                                                                                                                                                                         (10, 'PAGADO', '2025-10-11 11:39:06.000000', 3, 490.00, 500.00, 10.00, NULL, 4, 1),
                                                                                                                                                                         (12, 'PENDIENTE', '2025-10-13 08:34:24.000000', NULL, 490.00, 500.00, 10.00, NULL, 1, 1);


-- =====================================================
-- 1️⃣1️⃣ TABLA: pago
-- =====================================================
INSERT INTO pago (id_pago, fecha_pago, monto, numero_cuota, id_matricula, id_metodo_pago, id_usuario) VALUES
    (1, '2025-10-11 19:01:47.000000', 90.00, 1, 10, 1, 11);
