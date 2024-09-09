CREATE DATABASE IF NOT EXISTS db_lavacao3;
USE db_lavacao3;

CREATE TABLE IF NOT EXISTS marca(
   id int NOT NULL auto_increment,
   nome  varchar(50) NOT NULL,
   CONSTRAINT pk_marca
      PRIMARY KEY(id)
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS cor(
   id int NOT NULL auto_increment,
   nome varchar(50) NOT NULL,
   CONSTRAINT pk_cor
      PRIMARY KEY(id)
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS servico(
   id int NOT NULL auto_increment,
   descricao varchar(200),
   valor decimal(10,2) NOT NULL,
   pontos int,
   CONSTRAINT pk_servico
      PRIMARY KEY(id)
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS modelo(
   id int NOT NULL auto_increment,
   descricao varchar(200),
   id_marca int NOT NULL,
   CONSTRAINT pk_modelo
      PRIMARY KEY(id),
   CONSTRAINT pk_modelo_marca
		FOREIGN KEY (id_marca) REFERENCES marca(id)
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS motor(
	id_modelo int NOT NULL REFERENCES modelo(id),
    potencia int NOT NULL,
    combustivel ENUM ('GASOLINA','ETANOL','FLEX','DIESEL','GNV') NOT NULL DEFAULT 'GASOLINA',
    CONSTRAINT pk_motor PRIMARY KEY (id_modelo),
    CONSTRAINT fk_motor_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id) ON DELETE CASCADE
);

INSERT INTO marca(nome) VALUES('Ford');
INSERT INTO marca(nome) VALUES('Ferrari');
INSERT INTO marca(nome) VALUES('Audi');
INSERT INTO marca(nome) VALUES('Fiat');

INSERT INTO cor(nome) VALUES('Azul');
INSERT INTO cor(nome) VALUES('Preto');
INSERT INTO cor(nome) VALUES('Branco');

INSERT INTO servico(descricao, valor, pontos) VALUES ('Lavação Completa', 70.0, 10);
INSERT INTO servico(descricao, valor, pontos) VALUES ('Polimento', 50.0, 10);
INSERT INTO servico(descricao, valor, pontos) VALUES ('Lavação Motor', 100.0, 10);