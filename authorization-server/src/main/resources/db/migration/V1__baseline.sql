-- Baseline: captura el esquema tal como quedo con hibernate ddl-auto: update, generado con
-- pg_dump --schema-only sobre una base creada desde cero por las entidades JPA actuales.
-- De aqui en adelante los cambios de esquema van en V2__..., V3__..., etc. No editar este
-- archivo una vez aplicado en algun entorno.


CREATE TABLE public.permission (
    id integer NOT NULL,
    operation character varying(50) NOT NULL,
    resource character varying(50) NOT NULL,
    scope character varying(50) NOT NULL,
    description character varying(255),
    CONSTRAINT permission_operation_check CHECK (((operation)::text = ANY ((ARRAY['READ'::character varying, 'CREATE'::character varying, 'UPDATE'::character varying, 'DELETE'::character varying])::text[]))),
    CONSTRAINT permission_resource_check CHECK (((resource)::text = ANY ((ARRAY['STUDENT'::character varying, 'ENROLLMENT'::character varying, 'UI_VIEW'::character varying])::text[]))),
    CONSTRAINT permission_scope_check CHECK (((scope)::text = ANY ((ARRAY['SELF'::character varying, 'ALL'::character varying])::text[])))
);

CREATE SEQUENCE public.permission_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.permission_id_seq OWNED BY public.permission.id;

CREATE TABLE public.role (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    description character varying(255)
);

CREATE SEQUENCE public.role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;

CREATE TABLE public.role_permission (
    permission_id integer NOT NULL,
    role_id integer NOT NULL
);

CREATE TABLE public.role_view (
    role_id integer NOT NULL,
    view_code character varying(50) NOT NULL
);

CREATE TABLE public.ui_view (
    active boolean NOT NULL,
    sort_order integer,
    code character varying(50) NOT NULL,
    label character varying(100) NOT NULL,
    module character varying(100),
    route character varying(200) NOT NULL
);

CREATE TABLE public."user" (
    id integer NOT NULL,
    two_factor_enabled boolean,
    email character varying(255),
    full_name character varying(255),
    password character varying(255),
    two_factor_secret character varying(255),
    username character varying(255)
);

CREATE SEQUENCE public.user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.user_id_seq OWNED BY public."user".id;

CREATE TABLE public.user_role (
    role_id integer NOT NULL,
    user_id integer NOT NULL
);

ALTER TABLE ONLY public.permission ALTER COLUMN id SET DEFAULT nextval('public.permission_id_seq'::regclass);

ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.role_id_seq'::regclass);

ALTER TABLE ONLY public."user" ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);

ALTER TABLE ONLY public.permission
    ADD CONSTRAINT permission_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_name_key UNIQUE (name);

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT role_permission_pkey PRIMARY KEY (permission_id, role_id);

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.role_view
    ADD CONSTRAINT role_view_pkey PRIMARY KEY (role_id, view_code);

ALTER TABLE ONLY public.ui_view
    ADD CONSTRAINT ui_view_pkey PRIMARY KEY (code);

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (role_id, user_id);

ALTER TABLE ONLY public.role_view
    ADD CONSTRAINT fk2wlv70qd1e0slg8d48ucb0gg0 FOREIGN KEY (role_id) REFERENCES public.role(id);

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fka68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES public.role(id);

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT fka6jx8n8xkesmjmv6jqug6bg68 FOREIGN KEY (role_id) REFERENCES public.role(id);

ALTER TABLE ONLY public.role_permission
    ADD CONSTRAINT fkf8yllw1ecvwqy3ehyxawqa1qp FOREIGN KEY (permission_id) REFERENCES public.permission(id);

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fkfgsgxvihks805qcq8sq26ab7c FOREIGN KEY (user_id) REFERENCES public."user"(id);

ALTER TABLE ONLY public.role_view
    ADD CONSTRAINT fksppuk7hcmnq2iunhu7t2b9ud3 FOREIGN KEY (view_code) REFERENCES public.ui_view(code);

