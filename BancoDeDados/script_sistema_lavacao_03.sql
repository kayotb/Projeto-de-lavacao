CREATE DATABASE IF NOT EXISTS db_lavacao4;
USE db_lavacao4;

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
   categoria ENUM ('PEQUENO','MEDIO','GRANDE','MOTO','PADRAO') NOT NULL DEFAULT 'PADRAO',
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
) engine = InnoDB;


CREATE TABLE IF NOT EXISTS cliente(
	id int NOT NULL auto_increment,
    nome varchar(100) NOT NULL,
    celular varchar (14) NOT NULL,
    email varchar(20),
    data_cadastro date,
     CONSTRAINT pk_cliente
     primary key(id)
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS fisica(
	id_cliente int NOT NULL REFERENCES cliente(id),
    cpf varchar(14) NOT NULL,
    CONSTRAINT pk_fisica PRIMARY KEY (id_cliente),
    CONSTRAINT fk_fisica_fornecedor FOREIGN KEY (id_cliente) REFERENCES fornecedor(id) 
		ON DELETE CASCADE
        ON UPDATE CASCADE
) engine = InnoDB;

CREATE TABLE IF NOT EXISTS juridica(
	id_cliente int NOT NULL REFERENCES cliente(id),
    cnpj varchar(20) NOT NULL,
    CONSTRAINT pk_juridica PRIMARY KEY (id_cliente),
    CONSTRAINT fk_juridica_fornecedor FOREIGN KEY (id_cliente) REFERENCES fornecedor(id) 
		ON DELETE CASCADE
        ON UPDATE CASCADE
) engine = InnoDB;


CREATE TABLE Veiculo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    placa VARCHAR(8) UNIQUE NOT NULL,
    observacoes TEXT,
    modelo_id INT,
    cor_id INT,
    cliente_id INT,
    FOREIGN KEY (modelo_id) REFERENCES Modelo(id),
    FOREIGN KEY (cor_id) REFERENCES Cor(id),
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
) engine InnoDB;

/*INSERT INTO marca(nome) VALUES('Ford');
INSERT INTO marca(nome) VALUES('Ferrari');
INSERT INTO marca(nome) VALUES('Audi');
INSERT INTO marca(nome) VALUES('Fiat');

INSERT INTO cor(nome) VALUES('Azul');
INSERT INTO cor(nome) VALUES('Preto');
INSERT INTO cor(nome) VALUES('Branco');

INSERT INTO servico(descricao, valor, pontos) VALUES ('Lavação Completa', 70.0, 10);
INSERT INTO servico(descricao, valor, pontos) VALUES ('Polimento', 50.0, 10);
INSERT INTO servico(descricao, valor, pontos) VALUES ('Lavação Motor', 100.0, 10);

INSERT INTO modelo(descricao, id_marca, categoria) VALUES('New Fiesta', 1, 'PEQUENO');
INSERT INTO motor(id_modelo, potencia, combustivel) VALUES(1 ,100, 'FLEX');*/