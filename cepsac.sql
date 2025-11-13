-- 1) PAÍSES
CREATE TABLE Pais (
    IdPais          TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    Nombre          VARCHAR(50) NOT NULL,
    Codigo          VARCHAR(5),
    CodigoTelefono  VARCHAR(5),
    PRIMARY KEY (IdPais)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2) TIPOS DE IDENTIFICACIÓN
CREATE TABLE TipoIdentificacion (
    IdTipoIdentificacion    TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    Nombre                  VARCHAR(50) NOT NULL,
    PRIMARY KEY (IdTipoIdentificacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3) USUARIOS
-- tabla de usuarios del sistema, puede ser alumno, docente o admin
-- se usa para login, matriculas, asignacion de docentes y gestion administrativa
CREATE TABLE Usuario (
    IdUsuario               SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    Rol                     ENUM('ADMINISTRADOR','DOCENTE','ALUMNO') NOT NULL, -- rol del usuario en el sistema
    Nombre                  VARCHAR(50) NOT NULL,
    Apellido                VARCHAR(50),
    Correo                  VARCHAR(255), -- usado para login
    Password                VARCHAR(255), -- hash bcrypt
    Estado                  ENUM('ACTIVO','INACTIVO','SUSPENDIDO') DEFAULT 'ACTIVO',
    IdCodigoPais            TINYINT UNSIGNED, -- pais del usuario
    NumeroCelular           VARCHAR(15),
    IdTipoIdentificacion    TINYINT UNSIGNED, -- tipo documento (dni, pasaporte, etc)
    NumeroIdentificacion    VARCHAR(20),
    UsuarioCreador          SMALLINT UNSIGNED, -- quien registro a este usuario
    FechaCreacion           DATETIME DEFAULT CURRENT_TIMESTAMP,
    UsuarioModificador      SMALLINT UNSIGNED,
    FechaModificacion       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (IdUsuario),
    UNIQUE KEY uk_usuario_correo (Correo),
    CONSTRAINT fk_usuario_pais FOREIGN KEY (IdCodigoPais)
        REFERENCES Pais(IdPais) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_usuario_tipoident FOREIGN KEY (IdTipoIdentificacion)
        REFERENCES TipoIdentificacion(IdTipoIdentificacion) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_usuario_rol (Rol),
    INDEX idx_usuario_estado (Estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='usuarios del sistema: alumnos, docentes y administradores';

-- 4) MÉTODOS DE PAGO
CREATE TABLE MetodoPago (
    IdMetodoPago    TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    TipoMetodo      ENUM('EFECTIVO','TRANSFERENCIA','YAPE','PLIN') NOT NULL,
    Descripcion     VARCHAR(100),
    Requisitos      VARCHAR(500),
    Estado          BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (IdMetodoPago)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5) SPONSORS
CREATE TABLE Sponsors (
    IdSponsor       TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    Nombre          VARCHAR(50) NOT NULL,
    UrlSponsor      VARCHAR(255),
    IdUsuario       SMALLINT UNSIGNED,
    PRIMARY KEY (IdSponsor),
    CONSTRAINT fk_sponsor_usuario FOREIGN KEY (IdUsuario)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6) CATEGORÍAS
-- categorias para organizar los cursos/diplomados en la landing page
-- ej: tecnologia, administracion, marketing, diseño, idiomas
CREATE TABLE Categoria (
    IdCategoria     TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    Nombre          VARCHAR(50) NOT NULL, -- nombre visible en el frontend
    Descripcion     VARCHAR(100), -- descripcion breve opcional
    UsuarioCreador  SMALLINT UNSIGNED, -- quien creo esta categoria
    FechaCreacion   DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (IdCategoria),
    CONSTRAINT fk_categoria_usuario FOREIGN KEY (UsuarioCreador)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='categorias para organizar cursos y diplomados';

-- 7) CURSOS / DIPLOMADOS
-- entidad principal para cursos y diplomados que se ofrecen en la plataforma
-- contiene toda la info descriptiva y de marketing del curso/diplomado
-- se relaciona con programacioncurso para las fechas y horarios específicos
CREATE TABLE CursoDiplomado (
    IdCursoDiplomado    SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    IdCategoria         TINYINT UNSIGNED, -- categoria para organizarlo (ej: tecnologia, administracion)
    Tipo                BOOLEAN DEFAULT FALSE, -- false: curso (corto), true: diplomado (largo y completo)
    OtorgaCertificado   BOOLEAN DEFAULT FALSE, -- indica si al finalizar se entrega certificado oficial
    Titulo              VARCHAR(100) NOT NULL, -- nombre del curso/diplomado mostrado en landing
    UrlCurso            VARCHAR(255), -- url de imagen de portada para mostrarlo en cards
    Objetivo            VARCHAR(500), -- descripcion de lo que el alumno aprendera
    MaterialesIncluidos TEXT, -- lista separada por | (ej: "videos|pdfs|certificado|acceso de por vida")
    Requisitos          TEXT, -- lista separada por | (ej: "conocimientos de java|pc con 8gb ram")
    UsuarioCreador      SMALLINT UNSIGNED, -- administrador que crea el curso
    FechaCreacion       DATETIME DEFAULT CURRENT_TIMESTAMP, -- cuando se creo el registro
    UsuarioModificador  SMALLINT UNSIGNED, -- ultimo usuario que modifico
    FechaModificacion   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- ultima modificacion
    PRIMARY KEY (IdCursoDiplomado),
    CONSTRAINT fk_cursod_categoria FOREIGN KEY (IdCategoria)
        REFERENCES Categoria(IdCategoria) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_cursod_usuario FOREIGN KEY (UsuarioCreador)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='cursos y diplomados ofrecidos en la plataforma';

-- 8) DESCUENTOS
CREATE TABLE Descuento (
    IdDescuento     TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    TipoDescuento   ENUM('PORCENTAJE','MONTO') NOT NULL,
    Valor           DECIMAL(8,2) NOT NULL,
    Vigente         BOOLEAN DEFAULT TRUE,
    FechaInicio     DATE,
    FechaFin        DATE,
    IdUsuario       SMALLINT UNSIGNED, -- usuario creador de descuento
    PRIMARY KEY (IdDescuento),
    CONSTRAINT fk_desc_usuario FOREIGN KEY (IdUsuario)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8.1) APLICACIÓN DE DESCUENTOS
CREATE TABLE DescuentoAplicacion (
    IdDescuentoAplicacion      INT UNSIGNED NOT NULL AUTO_INCREMENT,
    IdDescuento                TINYINT UNSIGNED NOT NULL,
    TipoAplicacion             ENUM('GENERAL','CATEGORIA','CURSO') NOT NULL,
    IdCategoria                TINYINT UNSIGNED NULL,     -- NULL para 'general' o 'curso'
    IdCursoDiplomado           SMALLINT UNSIGNED NULL,    -- NULL para 'general' o 'categoria'
    IdUsuario                  SMALLINT UNSIGNED NULL,    -- usuario que registra la regla de aplicacion
    PRIMARY KEY (IdDescuentoAplicacion),
    CONSTRAINT fk_descap_desc FOREIGN KEY (IdDescuento)
        REFERENCES Descuento(IdDescuento) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_descap_categoria FOREIGN KEY (IdCategoria)
        REFERENCES Categoria(IdCategoria) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_descap_curso FOREIGN KEY (IdCursoDiplomado)
        REFERENCES CursoDiplomado(IdCursoDiplomado) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_apdesc_usuario FOREIGN KEY (IdUsuario)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9) PROGRAMACIONES DE CURSO
-- programacion concreta de un curso/diplomado con fechas, horarios y docente asignado
-- un mismo curso puede tener multiples programaciones en diferentes fechas/horarios
-- aqui se define cuando se dicta, quien enseña, el precio y las modalidades de pago
CREATE TABLE ProgramacionCurso (
    IdProgramacionCurso INT UNSIGNED NOT NULL AUTO_INCREMENT,
    Modalidad           ENUM('PRESENCIAL','VIRTUAL','VIRTUAL_24_7') DEFAULT 'VIRTUAL', -- como se dicta
    DuracionCurso       DECIMAL(6,2), -- total de horas del curso completo (ej: 40 horas)
    HorasSemanales      DECIMAL(6,2), -- horas de clase por semana (ej: 12 horas semanales)
    FechaInicio         DATE, -- fecha en que empieza esta programacion
    FechaFin            DATE, -- fecha en que termina, para filtrar disponibles (fechafin > hoy)
    Horario             VARCHAR(100), -- dias y horas (ej: "lunes y miercoles 8:00 - 11:30 am")
    IdDocente           SMALLINT UNSIGNED, -- docente/profesor asignado para esta programacion
    IdCursoDiplomado    SMALLINT UNSIGNED NOT NULL, -- curso o diplomado al que pertenece
    Monto               DECIMAL(10,2), -- precio total de esta programacion
    DuracionMeses       TINYINT UNSIGNED, -- si tiene valor genera cuotas automaticas (ej: 3 = 3 cuotas)
    UsuarioCreador      SMALLINT UNSIGNED, -- quien registro esta programacion
    FechaCreacion       DATETIME DEFAULT CURRENT_TIMESTAMP,
    UsuarioModificador  SMALLINT UNSIGNED,
    FechaModificacion   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (IdProgramacionCurso),
-- 10) MATRÍCULAS
-- inscripcion de un alumno a una programacion especifica de un curso
-- registra el proceso de matricula, aplicacion de descuentos y generacion de cuotas/pagos
-- una matricula puede estar pendiente, pagado, cancelado o en_proceso
CREATE TABLE Matricula (
    IdMatricula             SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    IdProgramacionCurso     INT UNSIGNED, -- programacion concreta a la que se inscribe
    IdAlumno                SMALLINT UNSIGNED, -- alumno que se matricula
    IdAdministrador         SMALLINT UNSIGNED, -- admin que aprueba/registra la matricula
    FechaMatricula          DATETIME DEFAULT CURRENT_TIMESTAMP,
    Estado                  ENUM('PENDIENTE','APROBADO','RECHAZADO','CANCELADO') DEFAULT 'PENDIENTE',
    MontoBase               DECIMAL(10,2), -- precio original del curso
    MontoDescontado         DECIMAL(10,2), -- cantidad descontada (0.00 si no hay descuento)
    Monto                   DECIMAL(10,2), -- monto final a pagar (montobase - montodescontado)
    IdDescuento             TINYINT UNSIGNED, -- descuento aplicado (null si no hay)
    PagoPersonalizado       BOOLEAN DEFAULT FALSE, -- true: cuotas manuales, false: cuotas automaticas
    UsuarioCreador          SMALLINT UNSIGNED,
    FechaCreacion           DATETIME DEFAULT CURRENT_TIMESTAMP,
    UsuarioModificador      SMALLINT UNSIGNED,
    FechaModificacion       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (IdMatricula),
    CONSTRAINT fk_mat_progcurso FOREIGN KEY (IdProgramacionCurso)
        REFERENCES ProgramacionCurso(IdProgramacionCurso) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_mat_alumno FOREIGN KEY (IdAlumno)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_mat_admin FOREIGN KEY (IdAdministrador)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_mat_desc FOREIGN KEY (IdDescuento)
        REFERENCES Descuento(IdDescuento) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_mat_alumno (IdAlumno),
    INDEX idx_mat_estado (Estado)
-- 11) PAGOS
-- representa una cuota o pago asociado a una matricula
-- puede ser cuota automatica (generada por duracionmeses) o manual (creada por admin)
-- registra el estado de cada cuota: pendiente, pagada, vencida, etc
CREATE TABLE Pago (
    IdPago              SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    IdMatricula         SMALLINT UNSIGNED, -- matricula a la que pertenece esta cuota
    IdMetodoPago        TINYINT UNSIGNED, -- como se pago (efectivo, transferencia, yape, etc)
    Monto               DECIMAL(10,2) NOT NULL, -- monto de esta cuota
    NumeroCuota         TINYINT UNSIGNED, -- numero de cuota (1, 2, 3...) null si es pago unico
    FechaPago           DATETIME, -- cuando se efectuo el pago (null si aun no se pago)
    FechaVencimiento    DATE, -- fecha limite para pagar esta cuota
    EstadoCuota         ENUM('PENDIENTE','PAGADA','VENCIDA','CANCELADA') DEFAULT 'PENDIENTE',
    MontoPagado         DECIMAL(10,2), -- cuanto se pago realmente (puede ser parcial)
    EsAutomatico        BOOLEAN DEFAULT FALSE, -- true: generada por sistema, false: creada por admin
    UsuarioRegistro     SMALLINT UNSIGNED, -- usuario que registro el pago
    FechaCreacion       DATETIME DEFAULT CURRENT_TIMESTAMP,
    UsuarioModificador  SMALLINT UNSIGNED,
    FechaModificacion   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (IdPago),
    CONSTRAINT fk_pago_matricula FOREIGN KEY (IdMatricula)
        REFERENCES Matricula(IdMatricula) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_pago_metodop FOREIGN KEY (IdMetodoPago)
        REFERENCES MetodoPago(IdMetodoPago) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_pago_matricula (IdMatricula),
    INDEX idx_pago_estado (EstadoCuota)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='cuotas y pagos de las matriculas';
    FechaPago       DATETIME DEFAULT CURRENT_TIMESTAMP,
    IdUsuario       SMALLINT UNSIGNED, -- usuario que registro el pago
    PRIMARY KEY (IdPago),
    CONSTRAINT fk_pago_matricula FOREIGN KEY (IdMatricula)
        REFERENCES Matricula(IdMatricula) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_pago_metodop FOREIGN KEY (IdMetodoPago)
        REFERENCES MetodoPago(IdMetodoPago) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_pago_usuario FOREIGN KEY (IdUsuario)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
