-- Creación da base de datos
CREATE DATABASE IF NOT EXISTS mazmorras;
USE mazmorras;

CREATE TABLE usuarios(
    correo VARCHAR(100) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contrasenna VARCHAR(200) NOT NULL,
    foto_perfil VARCHAR(300)
);

CREATE TABLE campannas(
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    foto VARCHAR(300)
);

CREATE TABLE usuarios_campannas(
    usuario VARCHAR(100),
    campanna VARCHAR(50),
    horario TEXT,
    rol CHAR NOT NULL,
    aceptada BOOLEAN NOT NULL,

    PRIMARY KEY (usuario, campanna),

    FOREIGN KEY (usuario) REFERENCES usuarios(correo),
    FOREIGN KEY (campanna) REFERENCES campannas(id)
);

CREATE TABLE personajes(
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    clase VARCHAR(100),
    subclase VARCHAR(100),
    pg SMALLINT,
    pgmaximos SMALLINT,
    foto VARCHAR(300),
    campanna VARCHAR(50),

    FOREIGN KEY (campanna) REFERENCES campannas(id)
);

CREATE TABLE objetos(
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio FLOAT,
    foto VARCHAR(300),
    campanna VARCHAR(50),

    FOREIGN KEY (campanna) REFERENCES campannas(id)
);

CREATE TABLE inventario(
    personaje VARCHAR(50),
    objeto VARCHAR(50),

    PRIMARY KEY (personaje, objeto),

    FOREIGN KEY (personaje) REFERENCES personajes(id),
    FOREIGN KEY (objeto) REFERENCES objetos(id)
);

CREATE TABLE criaturas(
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    raza VARCHAR(100),
    foto VARCHAR(300),
    campanna VARCHAR(50),

    FOREIGN KEY (campanna) REFERENCES campannas(id)
);

CREATE TABLE lugares(
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    foto VARCHAR(300),
    campanna VARCHAR(50),

    FOREIGN KEY (campanna) REFERENCES campannas(id)
);

CREATE TABLE notas(
    id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    contenido TEXT,
    dm BOOLEAN NOT NULL,
    editando BOOLEAN NOT NULL,
    propietario VARCHAR(50)
);