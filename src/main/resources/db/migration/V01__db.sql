CREATE TABLE categoria(
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(40) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE pessoa(
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	cpf VARCHAR(20) NOT NULL,
	logradouro VARCHAR(30),
	numero VARCHAR(30),
	complemento VARCHAR(30),
	bairro VARCHAR(30),
	cep VARCHAR(30),
	cidade VARCHAR(30),
	uf VARCHAR(30),
	situacao VARCHAR(15) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE contato(
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(100) NOT NULL,
	telefone VARCHAR(20) NOT NULL,
	id_pessoa BIGINT(20) NOT NULL,
	CONSTRAINT fk_pessoa_contato FOREIGN KEY (id_pessoa) REFERENCES pessoa(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE lancamento(
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(50) NOT NULL,
	data_vencimento DATE NOT NULL,
	data_pagamento DATE,
	valor DECIMAL(10,2) NOT NULL,
	observacao VARCHAR(100),
	tipo VARCHAR(20) NOT NULL,
	anexo VARCHAR(200),
	id_categoria BIGINT(20) NOT NULL,
	id_pessoa BIGINT(20) NOT NULL,
	CONSTRAINT fk_categoria_lancamento FOREIGN KEY (id_categoria) REFERENCES categoria(id),
	CONSTRAINT fk_pessoa_lancamento FOREIGN KEY (id_pessoa) REFERENCES pessoa(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usuario (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(150) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permissao (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	descricao VARCHAR(50) NOT NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usuario_permissao (
	id_usuario BIGINT(20) NOT NULL,
	id_permissao BIGINT(20) NOT NULL,
	CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id),
	CONSTRAINT fk_permissao FOREIGN KEY (id_permissao) REFERENCES permissao(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO categoria(nome) VALUES('Lazer');
INSERT INTO categoria(nome) VALUES('Alimentação');
INSERT INTO categoria(nome) VALUES('Supermercado');
INSERT INTO categoria(nome) VALUES('Farmácia');
INSERT INTO categoria(nome) VALUES('Outros');

INSERT INTO pessoa(nome,cpf,situacao) 
VALUES('Abel Gasque','119.331.109-88','ATIVO');

INSERT INTO contato(nome, email, telefone, id_pessoa) VALUES('Abel Gasque L. Silva', 'abelgasque20@gmail.com', '(48) 99999-9999',1);

INSERT INTO lancamento(descricao,data_vencimento,valor,tipo,id_categoria,id_pessoa) 
VALUES('Crédito salário', '2020-07-07', 1047.98, 'RECEITA', 5, 1);

INSERT INTO lancamento(descricao,data_vencimento,valor,tipo,id_categoria,id_pessoa) 
VALUES('Pensão', '2020-07-01', 608.0, 'RECEITA', 5, 1);

INSERT INTO lancamento(descricao,data_vencimento,data_pagamento,valor,tipo,id_categoria,id_pessoa) 
VALUES('Mensalidade Faculdade', '2020-07-10','2020-07-08', 499.99, 'DESPESA', 5, 1);

INSERT INTO lancamento(descricao,data_vencimento,data_pagamento,valor,tipo,id_categoria,id_pessoa) 
VALUES('Cartão Crédito', '2020-07-15','2020-07-08', 150.99, 'DESPESA', 5, 1);

INSERT INTO usuario(nome, email, senha) values('Administrador', 'admin@financeiro.com', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.');
INSERT INTO usuario(nome, email, senha) values('Abel', 'abel@financeiro.com', '$2a$10$Zc3w6HyuPOPXamaMhh.PQOXvDnEsadztbfi6/RyZWJDzimE8WQjaq');

INSERT INTO permissao(descricao) values('ROLE_CADASTRAR_categoria');
INSERT INTO permissao(descricao) values('ROLE_REMOVER_categoria');
INSERT INTO permissao(descricao) values('ROLE_PESQUISAR_categoria');

INSERT INTO permissao(descricao) values('ROLE_CADASTRAR_pessoa');
INSERT INTO permissao(descricao) values('ROLE_REMOVER_pessoa');
INSERT INTO permissao(descricao) values('ROLE_PESQUISAR_pessoa');

INSERT INTO permissao(descricao) values('ROLE_CADASTRAR_lancamento');
INSERT INTO permissao(descricao) values('ROLE_REMOVER_lancamento');
INSERT INTO permissao(descricao) values('ROLE_PESQUISAR_lancamento');

INSERT INTO usuario_permissao(id_usuario, id_permissao) values(1, 1);
INSERT INTO usuario_permissao(id_usuario, id_permissao) values(1, 2);
INSERT INTO usuario_permissao(id_usuario, id_permissao) values(1, 3);
INSERT INTO usuario_permissao(id_usuario, id_permissao) values(1, 4);
INSERT INTO usuario_permissao(id_usuario, id_permissao) values(1, 5);
INSERT INTO usuario_permissao(id_usuario, id_permissao) values(1, 6);
INSERT INTO usuario_permissao(id_usuario, id_permissao) values(1, 7);
INSERT INTO usuario_permissao(id_usuario, id_permissao) values(1, 8);
INSERT INTO usuario_permissao(id_usuario, id_permissao) values(1, 9);
INSERT INTO usuario_permissao(id_usuario, id_permissao) values(2, 7);
INSERT INTO usuario_permissao(id_usuario, id_permissao) values(2, 9);

