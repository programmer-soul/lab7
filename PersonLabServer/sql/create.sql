SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;
SELECT pg_catalog.set_config('search_path', '', false);

CREATE TYPE public.eye_color AS ENUM (
    'BLACK',
    'BLUE',
    'ORANGE',
    'WHITE'
    );

ALTER TYPE public.eye_color OWNER TO s408215;


CREATE TABLE public.users (
	id integer NOT NULL,
	name character varying(40) NOT NULL,
	password_hash character varying(64) NOT NULL
);

ALTER TABLE public.users OWNER TO s408215;

CREATE TABLE public.persons (
	id integer NOT NULL,
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

ALTER TABLE public.persons OWNER TO s408215;

CREATE SEQUENCE public.persons_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.persons_id_seq OWNER TO s408215;

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE public.users_id_seq OWNER TO s408215;

ALTER TABLE ONLY public.persons ALTER COLUMN id SET DEFAULT nextval('public.persons_id_seq'::regclass);

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.persons
    ADD CONSTRAINT persons_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.persons
    ADD CONSTRAINT persons_creator FOREIGN KEY (creator_id) REFERENCES public.users(id);
