--
-- PostgreSQL database dump
--

-- Dumped from database version 16.9
-- Dumped by pg_dump version 16.9

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: delivrance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.delivrance (
    id_delivrance integer NOT NULL,
    date_sortie date,
    id_patient integer,
    id_medicament integer,
    quantite integer,
    id_utilisateur integer,
    motif character varying(200)
);


ALTER TABLE public.delivrance OWNER TO postgres;

--
-- Name: delivrance_id_delivrance_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.delivrance_id_delivrance_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.delivrance_id_delivrance_seq OWNER TO postgres;

--
-- Name: delivrance_id_delivrance_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.delivrance_id_delivrance_seq OWNED BY public.delivrance.id_delivrance;


--
-- Name: eleve; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.eleve (
    id_patient integer NOT NULL,
    matricule character varying(20) NOT NULL,
    classe character varying(20)
);


ALTER TABLE public.eleve OWNER TO postgres;

--
-- Name: enseignant; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.enseignant (
    id_patient integer NOT NULL,
    departement character varying(50),
    fonction character varying(50)
);


ALTER TABLE public.enseignant OWNER TO postgres;

--
-- Name: medicament; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.medicament (
    id_medicament integer NOT NULL,
    nom character varying(100) NOT NULL,
    categorie character varying(50),
    quantite_stock integer DEFAULT 0,
    date_peremption date
);


ALTER TABLE public.medicament OWNER TO postgres;

--
-- Name: medicament_id_medicament_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.medicament_id_medicament_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.medicament_id_medicament_seq OWNER TO postgres;

--
-- Name: medicament_id_medicament_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.medicament_id_medicament_seq OWNED BY public.medicament.id_medicament;


--
-- Name: patient; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.patient (
    id_patient integer NOT NULL,
    nom character varying(100) NOT NULL,
    sexe character(1),
    date_naissance date,
    type_patient character varying(20) NOT NULL
);


ALTER TABLE public.patient OWNER TO postgres;

--
-- Name: patient_id_patient_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.patient_id_patient_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.patient_id_patient_seq OWNER TO postgres;

--
-- Name: patient_id_patient_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.patient_id_patient_seq OWNED BY public.patient.id_patient;


--
-- Name: utilisateur; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utilisateur (
    id_utilisateur integer NOT NULL,
    nom character varying(100) NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(100) NOT NULL,
    role character varying(20) DEFAULT 'pharmacien'::character varying
);


ALTER TABLE public.utilisateur OWNER TO postgres;

--
-- Name: utilisateur_id_utilisateur_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.utilisateur_id_utilisateur_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.utilisateur_id_utilisateur_seq OWNER TO postgres;

--
-- Name: utilisateur_id_utilisateur_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.utilisateur_id_utilisateur_seq OWNED BY public.utilisateur.id_utilisateur;


--
-- Name: delivrance id_delivrance; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.delivrance ALTER COLUMN id_delivrance SET DEFAULT nextval('public.delivrance_id_delivrance_seq'::regclass);


--
-- Name: medicament id_medicament; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medicament ALTER COLUMN id_medicament SET DEFAULT nextval('public.medicament_id_medicament_seq'::regclass);


--
-- Name: patient id_patient; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.patient ALTER COLUMN id_patient SET DEFAULT nextval('public.patient_id_patient_seq'::regclass);


--
-- Name: utilisateur id_utilisateur; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur ALTER COLUMN id_utilisateur SET DEFAULT nextval('public.utilisateur_id_utilisateur_seq'::regclass);


--
-- Data for Name: delivrance; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.delivrance (id_delivrance, date_sortie, id_patient, id_medicament, quantite, id_utilisateur, motif) FROM stdin;
7	2025-11-05	4	5	2	1	maux de tete
\.


--
-- Data for Name: eleve; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.eleve (id_patient, matricule, classe) FROM stdin;
1	E2025-001	3eA
2	E2025-002	3eB
4	E2025-004	5eB
5	E2025-005	Terminale S
3	E2025-003	4eb
\.


--
-- Data for Name: enseignant; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.enseignant (id_patient, departement, fonction) FROM stdin;
6	Sciences	Professeur de Physique
7	Lettres	Professeur de Français
8	Mathématiques	Professeur de Maths
9	Infirmier	Responsable Santé
10	Administration	Censeur
\.


--
-- Data for Name: medicament; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.medicament (id_medicament, nom, categorie, quantite_stock, date_peremption) FROM stdin;
3	paracetanol	anti	150	2025-12-31
6	Ibuprofène	Antiinflammatoire	80	2025-07-20
7	Amoxicilline	Antibiotique	50	2025-12-01
8	Vitamine C	Complément	200	2027-01-15
9	Sirop Toux Enfant	Antitussif	30	2025-11-25
10	Pansement adhésif	Matériel médical	500	2030-01-01
11	Alcool 70°	Désinfectant	75	2028-05-15
12	Doliprane 500mg	Antalgique	60	2025-12-20
13	Hydroxychloroquine	Antipaludéen	25	2026-02-05
14	Bétadine	Antiseptique	40	2027-08-12
5	Paracétamol	Antipyrétique	118	2026-03-10
\.


--
-- Data for Name: patient; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.patient (id_patient, nom, sexe, date_naissance, type_patient) FROM stdin;
1	Rakoto Jean	M	2008-04-12	Eleve
2	Rasoa Marie	F	2007-09-25	Eleve
4	Andriambelo Tiana	F	2009-11-08	Eleve
5	Raherinirina Kanto	F	2005-02-18	Eleve
6	Ratsimbazafy Alain	M	1985-06-10	Enseignant
7	Rakotomalala Fara	F	1990-12-02	Enseignant
8	Andrianarivo Tovo	M	1980-03-22	Enseignant
9	Ranaivo Saholy	F	1987-09-14	Enseignant
10	Rabe Liva	M	1978-11-29	Enseignant
3	Randrianina Lova	M	2006-03-15	Eleve
\.


--
-- Data for Name: utilisateur; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.utilisateur (id_utilisateur, nom, username, password, role) FROM stdin;
1	Razanadrakoto	vatosoa	sedera	admin
\.


--
-- Name: delivrance_id_delivrance_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.delivrance_id_delivrance_seq', 7, true);


--
-- Name: medicament_id_medicament_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.medicament_id_medicament_seq', 14, true);


--
-- Name: patient_id_patient_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.patient_id_patient_seq', 10, true);


--
-- Name: utilisateur_id_utilisateur_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.utilisateur_id_utilisateur_seq', 1, true);


--
-- Name: delivrance delivrance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.delivrance
    ADD CONSTRAINT delivrance_pkey PRIMARY KEY (id_delivrance);


--
-- Name: eleve eleve_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eleve
    ADD CONSTRAINT eleve_pkey PRIMARY KEY (id_patient);


--
-- Name: enseignant enseignant_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.enseignant
    ADD CONSTRAINT enseignant_pkey PRIMARY KEY (id_patient);


--
-- Name: medicament medicament_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.medicament
    ADD CONSTRAINT medicament_pkey PRIMARY KEY (id_medicament);


--
-- Name: patient patient_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.patient
    ADD CONSTRAINT patient_pkey PRIMARY KEY (id_patient);


--
-- Name: utilisateur utilisateur_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_pkey PRIMARY KEY (id_utilisateur);


--
-- Name: utilisateur utilisateur_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utilisateur
    ADD CONSTRAINT utilisateur_username_key UNIQUE (username);


--
-- Name: delivrance delivrance_id_medicament_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.delivrance
    ADD CONSTRAINT delivrance_id_medicament_fkey FOREIGN KEY (id_medicament) REFERENCES public.medicament(id_medicament);


--
-- Name: delivrance delivrance_id_patient_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.delivrance
    ADD CONSTRAINT delivrance_id_patient_fkey FOREIGN KEY (id_patient) REFERENCES public.patient(id_patient);


--
-- Name: delivrance delivrance_id_utilisateur_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.delivrance
    ADD CONSTRAINT delivrance_id_utilisateur_fkey FOREIGN KEY (id_utilisateur) REFERENCES public.utilisateur(id_utilisateur);


--
-- Name: eleve eleve_id_patient_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.eleve
    ADD CONSTRAINT eleve_id_patient_fkey FOREIGN KEY (id_patient) REFERENCES public.patient(id_patient) ON DELETE CASCADE;


--
-- Name: enseignant enseignant_id_patient_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.enseignant
    ADD CONSTRAINT enseignant_id_patient_fkey FOREIGN KEY (id_patient) REFERENCES public.patient(id_patient) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

