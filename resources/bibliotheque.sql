PGDMP      '                 }            bibliotheque    17.2    17.2 %               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false                       1262    24747    bibliotheque    DATABASE        CREATE DATABASE bibliotheque WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'French_France.1252';
    DROP DATABASE bibliotheque;
                     postgres    false            �            1255    49343     mise_a_jour_exemplaires_delete()    FUNCTION       CREATE FUNCTION public.mise_a_jour_exemplaires_delete() RETURNS trigger
    LANGUAGE plpgsql
    AS $$ BEGIN     IF OLD.statut = TRUE THEN         UPDATE livres         SET nombre_exemplaires = nombre_exemplaires + 1         WHERE id = OLD.livre_id;     END IF;     RETURN OLD; END; $$;
 7   DROP FUNCTION public.mise_a_jour_exemplaires_delete();
       public               postgres    false            �            1255    24792 !   mise_a_jour_exemplaires_emprunt()    FUNCTION     �   CREATE FUNCTION public.mise_a_jour_exemplaires_emprunt() RETURNS trigger
    LANGUAGE plpgsql
    AS $$ BEGIN     UPDATE livres     SET nombre_exemplaires = nombre_exemplaires - 1     WHERE id = NEW.livre_id;     RETURN NEW; END; $$;
 8   DROP FUNCTION public.mise_a_jour_exemplaires_emprunt();
       public               postgres    false            �            1255    49341 !   mise_a_jour_exemplaires_idlivre()    FUNCTION     z  CREATE FUNCTION public.mise_a_jour_exemplaires_idlivre() RETURNS trigger
    LANGUAGE plpgsql
    AS $$ BEGIN     IF OLD.livre_id != NEW.livre_id THEN         UPDATE livres SET nombre_exemplaires = nombre_exemplaires - 1 WHERE id = NEW.livre_id;         UPDATE livres SET nombre_exemplaires = nombre_exemplaires + 1 WHERE id = OLD.livre_id;     END IF;     RETURN NEW; END; $$;
 8   DROP FUNCTION public.mise_a_jour_exemplaires_idlivre();
       public               postgres    false            �            1255    24794     mise_a_jour_exemplaires_statut()    FUNCTION     �  CREATE FUNCTION public.mise_a_jour_exemplaires_statut() RETURNS trigger
    LANGUAGE plpgsql
    AS $$ BEGIN     IF OLD.statut = TRUE AND NEW.statut = FALSE THEN         UPDATE livres         SET nombre_exemplaires = nombre_exemplaires + 1         WHERE id = NEW.livre_id;     ELSIF OLD.statut = FALSE AND NEW.statut = TRUE THEN         UPDATE livres         SET nombre_exemplaires = nombre_exemplaires - 1         WHERE id = NEW.livre_id;     END IF;     RETURN NEW; END; $$;
 7   DROP FUNCTION public.mise_a_jour_exemplaires_statut();
       public               postgres    false            �            1255    24790 #   verifierdisponibilitelivre(integer)    FUNCTION        CREATE FUNCTION public.verifierdisponibilitelivre(livre_id integer) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    nbr_exemplaires INT;
BEGIN
    SELECT nombre_exemplaires INTO nbr_exemplaires
    FROM livres
    WHERE id = livre_id;

    RETURN nbr_exemplaires > 0;
END;
$$;
 C   DROP FUNCTION public.verifierdisponibilitelivre(livre_id integer);
       public               postgres    false            �            1259    24771    emprunts    TABLE       CREATE TABLE public.emprunts (
    id_emprunt integer NOT NULL,
    membre_id integer,
    livre_id integer,
    date_emprunt date NOT NULL,
    date_retour_prevue date NOT NULL,
    date_retour_effective date,
    statut boolean DEFAULT true,
    penalite integer DEFAULT 0
);
    DROP TABLE public.emprunts;
       public         heap r       postgres    false            �            1259    24770    emprunts_id_emprunt_seq    SEQUENCE     �   CREATE SEQUENCE public.emprunts_id_emprunt_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 .   DROP SEQUENCE public.emprunts_id_emprunt_seq;
       public               postgres    false    222                       0    0    emprunts_id_emprunt_seq    SEQUENCE OWNED BY     S   ALTER SEQUENCE public.emprunts_id_emprunt_seq OWNED BY public.emprunts.id_emprunt;
          public               postgres    false    221            �            1259    24749    livres    TABLE     �   CREATE TABLE public.livres (
    id integer NOT NULL,
    titre character varying(255) NOT NULL,
    auteur character varying(255) NOT NULL,
    categorie character varying(255),
    nombre_exemplaires integer DEFAULT 1
);
    DROP TABLE public.livres;
       public         heap r       postgres    false            �            1259    24748    livres_id_seq    SEQUENCE     �   CREATE SEQUENCE public.livres_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 $   DROP SEQUENCE public.livres_id_seq;
       public               postgres    false    218                       0    0    livres_id_seq    SEQUENCE OWNED BY     ?   ALTER SEQUENCE public.livres_id_seq OWNED BY public.livres.id;
          public               postgres    false    217            �            1259    24759    membres    TABLE     �   CREATE TABLE public.membres (
    id integer NOT NULL,
    nom character varying(255) NOT NULL,
    prenom character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    adhesion_date date DEFAULT CURRENT_DATE
);
    DROP TABLE public.membres;
       public         heap r       postgres    false            �            1259    24758    membres_id_seq    SEQUENCE     �   CREATE SEQUENCE public.membres_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.membres_id_seq;
       public               postgres    false    220                       0    0    membres_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.membres_id_seq OWNED BY public.membres.id;
          public               postgres    false    219            j           2604    24774    emprunts id_emprunt    DEFAULT     z   ALTER TABLE ONLY public.emprunts ALTER COLUMN id_emprunt SET DEFAULT nextval('public.emprunts_id_emprunt_seq'::regclass);
 B   ALTER TABLE public.emprunts ALTER COLUMN id_emprunt DROP DEFAULT;
       public               postgres    false    221    222    222            f           2604    24752 	   livres id    DEFAULT     f   ALTER TABLE ONLY public.livres ALTER COLUMN id SET DEFAULT nextval('public.livres_id_seq'::regclass);
 8   ALTER TABLE public.livres ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    218    217    218            h           2604    24762 
   membres id    DEFAULT     h   ALTER TABLE ONLY public.membres ALTER COLUMN id SET DEFAULT nextval('public.membres_id_seq'::regclass);
 9   ALTER TABLE public.membres ALTER COLUMN id DROP DEFAULT;
       public               postgres    false    220    219    220                      0    24771    emprunts 
   TABLE DATA           �   COPY public.emprunts (id_emprunt, membre_id, livre_id, date_emprunt, date_retour_prevue, date_retour_effective, statut, penalite) FROM stdin;
    public               postgres    false    222   1                 0    24749    livres 
   TABLE DATA           R   COPY public.livres (id, titre, auteur, categorie, nombre_exemplaires) FROM stdin;
    public               postgres    false    218   m1                 0    24759    membres 
   TABLE DATA           H   COPY public.membres (id, nom, prenom, email, adhesion_date) FROM stdin;
    public               postgres    false    220   "2                  0    0    emprunts_id_emprunt_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.emprunts_id_emprunt_seq', 16, true);
          public               postgres    false    221                       0    0    livres_id_seq    SEQUENCE SET     ;   SELECT pg_catalog.setval('public.livres_id_seq', 7, true);
          public               postgres    false    217                       0    0    membres_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.membres_id_seq', 7, true);
          public               postgres    false    219            t           2606    24776    emprunts emprunts_pkey 
   CONSTRAINT     \   ALTER TABLE ONLY public.emprunts
    ADD CONSTRAINT emprunts_pkey PRIMARY KEY (id_emprunt);
 @   ALTER TABLE ONLY public.emprunts DROP CONSTRAINT emprunts_pkey;
       public                 postgres    false    222            n           2606    24757    livres livres_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.livres
    ADD CONSTRAINT livres_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.livres DROP CONSTRAINT livres_pkey;
       public                 postgres    false    218            p           2606    24769    membres membres_email_key 
   CONSTRAINT     U   ALTER TABLE ONLY public.membres
    ADD CONSTRAINT membres_email_key UNIQUE (email);
 C   ALTER TABLE ONLY public.membres DROP CONSTRAINT membres_email_key;
       public                 postgres    false    220            r           2606    24767    membres membres_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.membres
    ADD CONSTRAINT membres_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.membres DROP CONSTRAINT membres_pkey;
       public                 postgres    false    220            w           2620    24814 #   emprunts trigger_mise_a_jour_delete    TRIGGER     �   CREATE TRIGGER trigger_mise_a_jour_delete AFTER DELETE ON public.emprunts FOR EACH ROW EXECUTE FUNCTION public.mise_a_jour_exemplaires_delete();
 <   DROP TRIGGER trigger_mise_a_jour_delete ON public.emprunts;
       public               postgres    false    225    222            x           2620    24793 $   emprunts trigger_mise_a_jour_emprunt    TRIGGER     �   CREATE TRIGGER trigger_mise_a_jour_emprunt AFTER INSERT ON public.emprunts FOR EACH ROW EXECUTE FUNCTION public.mise_a_jour_exemplaires_emprunt();
 =   DROP TRIGGER trigger_mise_a_jour_emprunt ON public.emprunts;
       public               postgres    false    222    224            y           2620    49342 $   emprunts trigger_mise_a_jour_idlivre    TRIGGER     �   CREATE TRIGGER trigger_mise_a_jour_idlivre AFTER UPDATE OF livre_id ON public.emprunts FOR EACH ROW EXECUTE FUNCTION public.mise_a_jour_exemplaires_idlivre();
 =   DROP TRIGGER trigger_mise_a_jour_idlivre ON public.emprunts;
       public               postgres    false    226    222    222            z           2620    24795 #   emprunts trigger_mise_a_jour_statut    TRIGGER     �   CREATE TRIGGER trigger_mise_a_jour_statut AFTER UPDATE OF statut ON public.emprunts FOR EACH ROW EXECUTE FUNCTION public.mise_a_jour_exemplaires_statut();
 <   DROP TRIGGER trigger_mise_a_jour_statut ON public.emprunts;
       public               postgres    false    222    222    227            u           2606    49359    emprunts emprunts_livre_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.emprunts
    ADD CONSTRAINT emprunts_livre_id_fkey FOREIGN KEY (livre_id) REFERENCES public.livres(id) ON DELETE CASCADE;
 I   ALTER TABLE ONLY public.emprunts DROP CONSTRAINT emprunts_livre_id_fkey;
       public               postgres    false    222    4718    218            v           2606    49364     emprunts emprunts_membre_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.emprunts
    ADD CONSTRAINT emprunts_membre_id_fkey FOREIGN KEY (membre_id) REFERENCES public.membres(id) ON DELETE CASCADE;
 J   ALTER TABLE ONLY public.emprunts DROP CONSTRAINT emprunts_membre_id_fkey;
       public               postgres    false    4722    220    222               R   x�}̱�0D�����}��)� 5�����tt_�'��P��Qɬl�i&N$�=�\��<�/<&��<,N+���M�D�Rl�         �   x��1�@@�z�s�&Z� XH$bi��'��dP����bb���� a�³�	vV[��le����'H�v"��p��!#�[�1��ԋ� Vk��3OZ0��W``Y;LY�-ym8����T`�	��p ?W�Ɍl�|4�����k�����9E         �   x�m���0D��iC�"����7/�@��!��m!\�}��dvVB1�Ύp$����ld�E�w$*g@E*�Rq�J4X�	�N{��� �Ơ��D�#�e�R87� \5A?/a��_�Fvp�'k��FCC�	���n���P�~.�<�f	0p�Z��۹�`�� �,P�     