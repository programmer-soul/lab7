BEGIN;

CREATE TYPE eye_color AS ENUM (
    'BLACK',
    'BLUE',
    'ORANGE',
    'WHITE'
    );


CREATE TABLE users (
	id SERIAL PRIMARY KEY,
	name character varying(40) NOT NULL,
	password_hash character varying(64) NOT NULL
);


CREATE TABLE IF NOT EXISTS persons (
	id SERIAL PRIMARY KEY,
	name character varying(255) NOT NULL,
	passport character varying(255) NOT NULL,
	creation_date timestamp with time zone NOT NULL,
	height bigint NOT NULL,
	weight integer NOT NULL,
	coord_x double precision,
	coord_y integer,
	eye_color character varying(30),
	x double precision,
	y real,
	z double precision,
	creator_id integer NOT NULL
);



ALTER TABLE persons ALTER COLUMN id SET DEFAULT nextval('persons_id_seq'::regclass);

ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);

ALTER TABLE users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE persons
    ADD CONSTRAINT persons_pkey PRIMARY KEY (id);

ALTER TABLE persons
ADD CONSTRAINT fk_creator_id
FOREIGN KEY (creator_id) REFERENCES users(id);

END;
