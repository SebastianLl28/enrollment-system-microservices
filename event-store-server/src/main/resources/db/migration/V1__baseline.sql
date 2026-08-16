-- Baseline: captura el esquema tal como quedo con hibernate ddl-auto: update, generado con
-- pg_dump --schema-only sobre una base creada desde cero por las entidades JPA actuales.
-- De aqui en adelante los cambios de esquema van en V2__..., V3__..., etc. No editar este
-- archivo una vez aplicado en algun entorno.


CREATE TABLE public.audit_event (
    id integer NOT NULL,
    status integer,
    occurred_at timestamp(6) with time zone NOT NULL,
    received_at timestamp(6) with time zone NOT NULL,
    method character varying(10) NOT NULL,
    service_name character varying(100) NOT NULL,
    user_id character varying(100),
    uri character varying(500) NOT NULL,
    path character varying(255) NOT NULL,
    payload text
);

CREATE SEQUENCE public.audit_event_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.audit_event_id_seq OWNED BY public.audit_event.id;

ALTER TABLE ONLY public.audit_event ALTER COLUMN id SET DEFAULT nextval('public.audit_event_id_seq'::regclass);

ALTER TABLE ONLY public.audit_event
    ADD CONSTRAINT audit_event_pkey PRIMARY KEY (id);

