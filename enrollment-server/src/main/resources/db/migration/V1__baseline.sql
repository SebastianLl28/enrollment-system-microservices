CREATE TABLE public.career (
    active boolean NOT NULL,
    career_id integer NOT NULL,
    faculty_id integer NOT NULL,
    semester_length integer NOT NULL,
    registration_date timestamp(6) without time zone NOT NULL,
    degree_awarded character varying(100),
    name character varying(100) NOT NULL,
    description text
);

CREATE SEQUENCE public.career_career_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.career_career_id_seq OWNED BY public.career.career_id;

CREATE TABLE public.career_course (
    career_id integer NOT NULL,
    course_id integer NOT NULL,
    id integer NOT NULL,
    semester_level integer NOT NULL
);

CREATE SEQUENCE public.career_course_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.career_course_id_seq OWNED BY public.career_course.id;

CREATE TABLE public.career_offering (
    active boolean NOT NULL,
    capacity integer NOT NULL,
    career_id integer NOT NULL,
    enrolled_count integer NOT NULL,
    id integer NOT NULL,
    price numeric(10,2),
    term_id integer NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL
);

CREATE SEQUENCE public.career_offering_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.career_offering_id_seq OWNED BY public.career_offering.id;

CREATE TABLE public.classroom (
    active boolean NOT NULL,
    capacity integer,
    id integer NOT NULL,
    is_virtual boolean NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    code character varying(20) NOT NULL,
    name character varying(100)
);

CREATE SEQUENCE public.classroom_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.classroom_id_seq OWNED BY public.classroom.id;

CREATE TABLE public.course (
    active boolean NOT NULL,
    course_id integer NOT NULL,
    credits integer NOT NULL,
    registration_date timestamp(6) without time zone,
    code character varying(20) NOT NULL,
    name character varying(100) NOT NULL,
    description text
);

CREATE SEQUENCE public.course_course_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.course_course_id_seq OWNED BY public.course.course_id;

CREATE TABLE public.enrollment (
    career_offering_id integer NOT NULL,
    enrollment_id integer NOT NULL,
    student_id integer NOT NULL,
    user_id integer NOT NULL,
    created_at timestamp(6) without time zone NOT NULL,
    enrollment_date timestamp(6) without time zone NOT NULL,
    paid_at timestamp(6) without time zone,
    unenrollment_date timestamp(6) without time zone,
    updated_at timestamp(6) without time zone NOT NULL,
    status character varying(20) NOT NULL,
    payment_status character varying(30),
    payment_id character varying(64),
    CONSTRAINT enrollment_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'PAID'::character varying, 'CANCELLED'::character varying, 'COMPLETED'::character varying])::text[])))
);

CREATE SEQUENCE public.enrollment_enrollment_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.enrollment_enrollment_id_seq OWNED BY public.enrollment.enrollment_id;

CREATE TABLE public.faculty (
    active boolean NOT NULL,
    faculty_id integer NOT NULL,
    registration_date timestamp(6) without time zone NOT NULL,
    dean character varying(100),
    location character varying(100),
    name character varying(100) NOT NULL,
    description text
);

CREATE SEQUENCE public.faculty_faculty_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.faculty_faculty_id_seq OWNED BY public.faculty.faculty_id;

CREATE TABLE public.outbox_event (
    career_id integer,
    id integer NOT NULL,
    retry_count integer,
    student_id integer,
    user_id integer NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    processed_at timestamp(6) with time zone,
    aggregate_id character varying(255) NOT NULL,
    aggregate_type character varying(255) NOT NULL,
    enrollment_status character varying(255),
    event_type character varying(255) NOT NULL,
    payload text NOT NULL,
    status character varying(255) NOT NULL,
    CONSTRAINT outbox_event_enrollment_status_check CHECK (((enrollment_status)::text = ANY ((ARRAY['PENDING'::character varying, 'PAID'::character varying, 'CANCELLED'::character varying, 'COMPLETED'::character varying])::text[]))),
    CONSTRAINT outbox_event_event_type_check CHECK (((event_type)::text = 'ENROLLMENT_EVENT'::text)),
    CONSTRAINT outbox_event_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'SENT'::character varying, 'FAILED'::character varying])::text[])))
);

CREATE SEQUENCE public.outbox_event_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.outbox_event_id_seq OWNED BY public.outbox_event.id;

CREATE TABLE public.section (
    active boolean NOT NULL,
    classroom_id integer NOT NULL,
    course_id integer NOT NULL,
    id integer NOT NULL,
    term_id integer NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    updated_at timestamp(6) with time zone NOT NULL,
    section_code character varying(10) NOT NULL
);

CREATE SEQUENCE public.section_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.section_id_seq OWNED BY public.section.id;

CREATE TABLE public.shedlock (
    name character varying(64) NOT NULL,
    lock_until timestamp without time zone NOT NULL,
    locked_at timestamp without time zone NOT NULL,
    locked_by character varying(255) NOT NULL
);

CREATE TABLE public.student (
    active boolean NOT NULL,
    date_of_birth date NOT NULL,
    student_id integer NOT NULL,
    created_at timestamp(6) without time zone,
    document_number character varying(20) NOT NULL,
    phone_number character varying(20),
    last_name character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    email character varying(100) NOT NULL,
    address character varying(200)
);

CREATE SEQUENCE public.student_student_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.student_student_id_seq OWNED BY public.student.student_id;

CREATE TABLE public.term (
    active boolean NOT NULL,
    end_date date NOT NULL,
    id integer NOT NULL,
    start_date date NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    code character varying(20) NOT NULL
);

CREATE SEQUENCE public.term_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.term_id_seq OWNED BY public.term.id;

ALTER TABLE ONLY public.career ALTER COLUMN career_id SET DEFAULT nextval('public.career_career_id_seq'::regclass);

ALTER TABLE ONLY public.career_course ALTER COLUMN id SET DEFAULT nextval('public.career_course_id_seq'::regclass);

ALTER TABLE ONLY public.career_offering ALTER COLUMN id SET DEFAULT nextval('public.career_offering_id_seq'::regclass);

ALTER TABLE ONLY public.classroom ALTER COLUMN id SET DEFAULT nextval('public.classroom_id_seq'::regclass);

ALTER TABLE ONLY public.course ALTER COLUMN course_id SET DEFAULT nextval('public.course_course_id_seq'::regclass);

ALTER TABLE ONLY public.enrollment ALTER COLUMN enrollment_id SET DEFAULT nextval('public.enrollment_enrollment_id_seq'::regclass);

ALTER TABLE ONLY public.faculty ALTER COLUMN faculty_id SET DEFAULT nextval('public.faculty_faculty_id_seq'::regclass);

ALTER TABLE ONLY public.outbox_event ALTER COLUMN id SET DEFAULT nextval('public.outbox_event_id_seq'::regclass);

ALTER TABLE ONLY public.section ALTER COLUMN id SET DEFAULT nextval('public.section_id_seq'::regclass);

ALTER TABLE ONLY public.student ALTER COLUMN student_id SET DEFAULT nextval('public.student_student_id_seq'::regclass);

ALTER TABLE ONLY public.term ALTER COLUMN id SET DEFAULT nextval('public.term_id_seq'::regclass);

ALTER TABLE ONLY public.career_course
    ADD CONSTRAINT career_course_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.career_offering
    ADD CONSTRAINT career_offering_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.career
    ADD CONSTRAINT career_pkey PRIMARY KEY (career_id);

ALTER TABLE ONLY public.classroom
    ADD CONSTRAINT classroom_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_code_key UNIQUE (code);

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (course_id);

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT enrollment_pkey PRIMARY KEY (enrollment_id);

ALTER TABLE ONLY public.faculty
    ADD CONSTRAINT faculty_name_key UNIQUE (name);

ALTER TABLE ONLY public.faculty
    ADD CONSTRAINT faculty_pkey PRIMARY KEY (faculty_id);

ALTER TABLE ONLY public.outbox_event
    ADD CONSTRAINT outbox_event_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.section
    ADD CONSTRAINT section_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.shedlock
    ADD CONSTRAINT shedlock_pkey PRIMARY KEY (name);

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_document_number_key UNIQUE (document_number);

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_email_key UNIQUE (email);

ALTER TABLE ONLY public.student
    ADD CONSTRAINT student_pkey PRIMARY KEY (student_id);

ALTER TABLE ONLY public.term
    ADD CONSTRAINT term_code_key UNIQUE (code);

ALTER TABLE ONLY public.term
    ADD CONSTRAINT term_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.career
    ADD CONSTRAINT uk_career_name UNIQUE (name);

ALTER TABLE ONLY public.career_course
    ADD CONSTRAINT uq_career_course UNIQUE (career_id, course_id);

ALTER TABLE ONLY public.career_offering
    ADD CONSTRAINT uq_career_offering_career_term UNIQUE (career_id, term_id);

ALTER TABLE ONLY public.classroom
    ADD CONSTRAINT uq_classroom_code UNIQUE (code);

ALTER TABLE ONLY public.section
    ADD CONSTRAINT uq_section_course_term_code UNIQUE (course_id, term_id, section_code);

CREATE INDEX idx_career_course_career ON public.career_course USING btree (career_id);

CREATE INDEX idx_career_course_course ON public.career_course USING btree (course_id);

CREATE INDEX idx_career_offering_active ON public.career_offering USING btree (active);

CREATE INDEX idx_career_offering_career ON public.career_offering USING btree (career_id);

CREATE INDEX idx_career_offering_term ON public.career_offering USING btree (term_id);

CREATE INDEX idx_enroll_offering ON public.enrollment USING btree (career_offering_id);

CREATE INDEX idx_enroll_status ON public.enrollment USING btree (status);

CREATE INDEX idx_enroll_student ON public.enrollment USING btree (student_id);

CREATE INDEX idx_section_classroom ON public.section USING btree (classroom_id);

CREATE INDEX idx_section_course ON public.section USING btree (course_id);

CREATE INDEX idx_section_term ON public.section USING btree (term_id);

CREATE INDEX idx_term_active ON public.term USING btree (active);

ALTER TABLE ONLY public.career_course
    ADD CONSTRAINT fk_career_course_career FOREIGN KEY (career_id) REFERENCES public.career(career_id);

ALTER TABLE ONLY public.career_course
    ADD CONSTRAINT fk_career_course_course FOREIGN KEY (course_id) REFERENCES public.course(course_id);

ALTER TABLE ONLY public.career_offering
    ADD CONSTRAINT fk_career_offering_career FOREIGN KEY (career_id) REFERENCES public.career(career_id);

ALTER TABLE ONLY public.career_offering
    ADD CONSTRAINT fk_career_offering_term FOREIGN KEY (term_id) REFERENCES public.term(id);

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT fk_enrollment_career_offering FOREIGN KEY (career_offering_id) REFERENCES public.career_offering(id);

ALTER TABLE ONLY public.career
    ADD CONSTRAINT fk_faculty FOREIGN KEY (faculty_id) REFERENCES public.faculty(faculty_id);

ALTER TABLE ONLY public.section
    ADD CONSTRAINT fk_section_classroom FOREIGN KEY (classroom_id) REFERENCES public.classroom(id);

ALTER TABLE ONLY public.section
    ADD CONSTRAINT fk_section_course FOREIGN KEY (course_id) REFERENCES public.course(course_id);

ALTER TABLE ONLY public.section
    ADD CONSTRAINT fk_section_term FOREIGN KEY (term_id) REFERENCES public.term(id);

ALTER TABLE ONLY public.enrollment
    ADD CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES public.student(student_id);

