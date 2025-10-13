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
CREATE TABLE Usuario (
    IdUsuario               SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    Rol                     ENUM('ADMINISTRADOR','DOCENTE','ALUMNO') NOT NULL,
    Nombre                  VARCHAR(50) NOT NULL,
    Apellido                VARCHAR(50),
    Correo                  VARCHAR(255),
    Password                VARCHAR(255),
    Estado                  ENUM('ACTIVO','INACTIVO','SUSPENDIDO') DEFAULT 'activo',
    IdCodigoPais            TINYINT UNSIGNED,
    NumeroCelular           VARCHAR(15),
    IdTipoIdentificacion    TINYINT UNSIGNED,
    NumeroIdentificacion    VARCHAR(20),
    PRIMARY KEY (IdUsuario),
    UNIQUE KEY uk_usuario_correo (Correo),
    CONSTRAINT fk_usuario_pais FOREIGN KEY (IdCodigoPais)
        REFERENCES Pais(IdPais) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_usuario_tipoident FOREIGN KEY (IdTipoIdentificacion)
        REFERENCES TipoIdentificacion(IdTipoIdentificacion) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
CREATE TABLE Categoria (
    IdCategoria     TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
    Nombre          VARCHAR(50) NOT NULL,
    Descripcion     VARCHAR(100),
    IdUsuario       SMALLINT UNSIGNED, -- Usuario creador/administrador
    PRIMARY KEY (IdCategoria),
    CONSTRAINT fk_categoria_usuario FOREIGN KEY (IdUsuario)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7) CURSOS / DIPLOMADOS
CREATE TABLE CursoDiplomado (
    IdCursoDiplomado    SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    IdCategoria         TINYINT UNSIGNED,
    Tipo                BOOLEAN DEFAULT FALSE, -- 0: CURSO, 1: DIPLOMADO
    OtorgaCertificado   BOOLEAN DEFAULT FALSE,
    Titulo              VARCHAR(100) NOT NULL,
    UrlCurso            VARCHAR(255),
    Objetivo            VARCHAR(255),
    Descripcion         VARCHAR(255),
    IdUsuario           SMALLINT UNSIGNED, -- Administrador que crea el curso
    PRIMARY KEY (IdCursoDiplomado),
    CONSTRAINT fk_cursod_categoria FOREIGN KEY (IdCategoria)
        REFERENCES Categoria(IdCategoria) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_cursod_usuario FOREIGN KEY (IdUsuario)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
CREATE TABLE ProgramacionCurso (
    IdProgramacionCurso       INT UNSIGNED NOT NULL AUTO_INCREMENT,
    Modalidad           ENUM('PRESENCIAL','VIRTUAL','VIRTUAL_24_7') DEFAULT 'VIRTUAL',
    DuracionCurso       DECIMAL(6,2),
    HorasSemanales      DECIMAL(6,2),
    FechaInicio         DATE,
    FechaFin            DATE,
    IdUsuario           SMALLINT UNSIGNED, -- Docente/administrador responsable
    IdCursoDiplomado    SMALLINT UNSIGNED NOT NULL,
    Monto               DECIMAL(10,2), -- Precio base del curso programado
    PRIMARY KEY (IdProgramacionCurso),
    CONSTRAINT fk_prog_usuario FOREIGN KEY (IdUsuario)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_prog_cursod FOREIGN KEY (IdCursoDiplomado)
        REFERENCES CursoDiplomado(IdCursoDiplomado) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10) MATRÍCULAS
CREATE TABLE Matricula (
    IdMatricula               SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    IdProgramacionCurso       INT UNSIGNED,
    IdAlumno                  SMALLINT UNSIGNED,
    IdAdministrador                 SMALLINT UNSIGNED,
    FechaMatricula            DATETIME DEFAULT CURRENT_TIMESTAMP,
    Estado                    ENUM('PENDIENTE','PAGADO','CANCELADO','EN_PROCESO') DEFAULT 'PENDIENTE',
    MontoBase                 DECIMAL(10,2),    -- Monto inicial del curso (antes del descuento)
    MontoDescontado           DECIMAL(10,2),    -- La cantidad total de dinero descontada (S/.0.00 si IdDescuento es NULL)
    Monto                     DECIMAL(10,2),    -- Monto neto final a pagar (MontoBase - MontoDescontado)
    IdDescuento               TINYINT UNSIGNED, -- Opcional (NULL si no hay descuento)
    PRIMARY KEY (IdMatricula),
    CONSTRAINT fk_mat_progcurso FOREIGN KEY (IdProgramacionCurso)
        REFERENCES ProgramacionCurso(IdProgramacionCurso) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_mat_alumno FOREIGN KEY (IdAlumno)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_mat_usuario FOREIGN KEY (IdAdministrador)
        REFERENCES Usuario(IdUsuario) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_mat_desc FOREIGN KEY (IdDescuento)
        REFERENCES Descuento(IdDescuento) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 11) PAGOS
CREATE TABLE Pago (
    IdPago          SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
    IdMatricula     SMALLINT UNSIGNED, -- FK a Matricula
    IdMetodoPago    TINYINT UNSIGNED, -- FK a MetodoPago
    Monto           DECIMAL(10,2) NOT NULL,
    NumeroCuota     TINYINT UNSIGNED,
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
