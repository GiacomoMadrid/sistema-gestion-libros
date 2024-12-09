-------------------------------------------------- TABLESPACES ------------------------------------------------------

CREATE TABLESPACE ts_crud --- data_ts_crud --- Bbcliente
    DATAFILE 'C:/app/DISK01/data_ts_crud.ora' --La carpeta "DISK01" debe estar creada en la ruta 'C./apps/'
            SIZE 3M
        EXTENT MANAGEMENT LOCAL AUTOALLOCATE


---------------------------------------------------------------------------------------------------------------
CREATE OR REPLACE PACKAGE types IS
    TYPE ref_cursor IS REF CURSOR;
END types;


-------------------------------------------------- TABLAS ------------------------------------------------------



Create table Pais(
    id integer GENERATED ALWAYS AS IDENTITY,
    nombre VARCHAR2(50) not null,

    CONSTRAINT pk_pais PRIMARY KEY (id)

)tablespace ts_crud;


Create table Autor(
    id INTEGER GENERATED ALWAYS AS IDENTITY,
    nombres VARCHAR2(30) not null,
    apellidos VARCHAR2(30) not null, 
    pais INTEGER,
    anno_nacimiento NUMBER,

    CONSTRAINT fk_autor_pais FOREIGN KEY (pais) References Pais(id),
    CONSTRAINT pk_autor PRIMARY KEY (id)

)tablespace ts_crud;


Create table Editorial(
    id integer GENERATED ALWAYS AS IDENTITY,
    nombre VARCHAR2(50) not null,
 
    CONSTRAINT pk_editorial PRIMARY KEY (id)
    
)tablespace ts_crud;


Create table Ejemplar(
    id integer GENERATED ALWAYS AS IDENTITY,
    autor integer not null,
    editorial integer not null,
    titulo VARCHAR2(100) not null,
    disponible number(1) default 1 not null,
    anno_publicacion Number,

    CONSTRAINT fk_ejemplar_autor FOREIGN KEY (autor) References Autor(id),
    CONSTRAINT fk_ejemplar_editorial FOREIGN KEY (editorial) References Editorial (id),
    CONSTRAINT pk_ejmplar PRIMARY KEY (id),
    CONSTRAINT ck_disponible check (disponible in (0, 1))

)tablespace ts_crud;



-------------------------------------------------- Procedimientos Almacenados ------------------------------------------------------

----------------------------- CRUD país
create or replace procedure registrar_pais (
    nombre in VARCHAR2
) is 
BEGIN
    INSERT INTO Pais(nombre)
        VALUES (to_char(nombre));
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END registrar_pais;
/

create or replace procedure eliminar_pais (
    cod in NUMBER
) is 
BEGIN
    DELETE FROM Pais
        Where id = cod;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END eliminar_pais;
/

create or replace procedure actualizar_pais (
    p_id in number, 
    nom in VARCHAR2
) is 
BEGIN
    UPDATE Pais
        SET nombre = nom
        where id = p_id;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END actualizar_pais;
/


----------------------------- CRUD Autor
create or replace procedure registrar_autor (
    nom in VARCHAR2,
    ape in VARCHAR2,
    p in number,
    fec in number
) is 
BEGIN
    INSERT INTO Autor(nombres, apellidos, pais, anno_nacimiento)
        VALUES (nom, ape, p, fec);
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END registrar_autor;
/

create or replace procedure eliminar_autor (
    autor_id in number
) is 
BEGIN
    DELETE FROM autor
        Where id = autor_id;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END eliminar_autor ;
/

create or replace procedure actualizar_autor (
    autor_id in number, 
    nom in VARCHAR2,
    ape in VARCHAR2,
    p in number,
    fec in number
) is 
BEGIN
    UPDATE autor
        SET nombres = nom,
            apellidos = ape,
            pais = p,
            anno_nacimiento = fec
        where id = autor_id;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END actualizar_autor;
/


----------------------------- CRUD Editorial
create or replace procedure registrar_editorial (
    nom in VARCHAR2
) is 
BEGIN
    INSERT INTO editorial(nombre)
        VALUES (nom);
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END registrar_editorial;
/

create or replace procedure eliminar_editorial (
    editorial_id in number
) is 
BEGIN
    DELETE FROM editorial
        Where id = editorial_id;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END;
/

create or replace procedure actualizar_editorial (
    editorial_id in number, 
    nom in VARCHAR2
) is 
BEGIN
    UPDATE editorial
        SET nombre = nom
        where id = editorial_id;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END;
/

----------------------------- CRUD DE LIBROS
create or replace procedure registrar_ejemplar (
    aut in number,
    ed in number,
    t in VARCHAR2,
    fec in number
) is 
BEGIN
    INSERT INTO ejemplar(autor, editorial, titulo, disponible, anno_publicacion)
        VALUES (aut, ed, t, 1, fec);
    COMMIT;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END registrar_ejemplar;
/

create or replace procedure eliminar_ejemplar (
    ejemplar_id in number
) is 
BEGIN
    DELETE FROM ejemplar
        Where id = ejemplar_id;
    COMMIT;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END;
/

create or replace procedure actualizar_ejemplar (
    ejm_id in number,
    aut in number,
    ed in number,
    t in VARCHAR2,
    disp in number,
    fec in number
) is 
BEGIN
    UPDATE Ejemplar
        SET autor = aut, 
            editorial = ed, 
            titulo = t, 
            disponible = disp,
            anno_publicacion = fec
        WHERE  id = ejm_id;
        COMMIT;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END actualizar_ejemplar;
/

--Libros:
create or replace procedure pedir_libro(
    ejm_id in NUMBER
) is
    libro Ejemplar%rowtype;
BEGIN
    select id, autor, editorial, titulo, disponible, anno_publicacion into libro 
        from EJEMPLAR
        Where id = ejm_id;

    IF libro.disponible = 1
        THEN
            UPDATE Ejemplar
                SET disponible = 0
                WHERE id = ejm_id;
    ELSE
        DBMS_OUTPUT.PUT_LINE('Ejemplar no disponible.');
    END IF;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END pedir_libro;
/

create or replace procedure devolver_libro(
    ejm_id in NUMBER
) is
    libro Ejemplar%rowtype;
BEGIN
    select * into libro 
        from EJEMPLAR
        Where id = ejm_id;

    IF libro.disponible = 0
        THEN
            UPDATE Ejemplar
                SET disponible = 1
                WHERE id = ejm_id;
    END IF;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END devolver_libro;
/

---------------------------------------------- REPORTES

set serveroutput on
create or replace procedure mostrar_data (
    lib_id in Number,
    lib_autor out number, 
    lib_editorial out number,
    lib_titulo out VARCHAR2, 
    lib_dis out number,
    lib_anno out number,
    pais_id out Autor.pais%type,
    autor_nombres out Autor.nombres%type,
    autor_apellidos out Autor.apellidos%type,
    ed_nombre out Editorial.nombre%type,
    pais_nombre out Pais.nombre%type

    ) is
BEGIN 
    dbms_output.put_line('*************************************************************************');
    select  autor, editorial, titulo, disponible, anno_publicacion into lib_autor , lib_editorial, lib_titulo, lib_dis, lib_anno
        from Ejemplar
        where id = lib_id;

    select nombres, apellidos, pais into autor_nombres, autor_apellidos, pais_id
        from Autor
        where id = lib_autor;

    select nombre into pais_nombre
        from Pais
        where id = pais_id;

    select nombre into ed_nombre
        from Editorial
        where id = lib_editorial;

    dbms_output.put_line('ID: '|| lib_id);
    dbms_output.put_line('Titulo: '|| lib_titulo);
    dbms_output.put_line('Autor: '|| autor_nombres ||' '||autor_apellidos);    
    dbms_output.put_line('Editorial: '|| ed_nombre);
    dbms_output.put_line('Año de publicación: '|| lib_anno);

    if(lib_dis = 0)
        then dbms_output.put_line('Disponible: No');
    else
       dbms_output.put_line('Disponible: Sí');   
    end if;
    
    dbms_output.put_line('Pais: '|| pais_nombre);
    dbms_output.put_line('*************************************************************************');

EXCEPTION
    when no_data_found
        then dbms_output.put_line('No se encontraron ejemplares.');
    when others
        then raise;

END mostrar_data;
/



-----------------------------------------------------------------------------------------------------------------------------
--------------------------------------------------------- FUNCIONES ---------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------------

-------------------------------------------------- LIBROS
create or replace function esta_disponible(
    ejm_id in number
) return boolean is
    libro Ejemplar%rowtype;
BEGIN
    select * into libro 
        from EJEMPLAR
        where id = ejm_id;
    return (libro.disponible = 1);
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END esta_disponible;
/

--Buscar por codigo
create or replace function buscar_ejemplar (ejm_id in number)
return types.ref_cursor
as
    crusor_libros types.ref_cursor;

BEGIN
    open crusor_libros for 
    select id, titulo, autor, editorial, disponible, anno_publicacion from Ejemplar
        where id = ejm_id;
    return crusor_libros;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END buscar_ejemplar;
/

--Bucar por autor
create or replace function buscar_por_autor (autor_id in number)
return types.ref_cursor
as
    cursor_libros types.ref_cursor;

BEGIN
    open cursor_libros for 
    select id, titulo, autor, editorial, disponible, anno_publicacion from Ejemplar
        where autor = autor_id;
    return cursor_libros;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END buscar_por_autor;
/

--Bucar por editorial
create or replace function buscar_por_editorial (editorial_id in number)
return types.ref_cursor
as
    crusor_libros types.ref_cursor;

BEGIN
    open crusor_libros for 
    select id, titulo, autor, editorial, disponible, anno_publicacion from Ejemplar
        where editorial = editorial_id;
    return crusor_libros;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END buscar_por_editorial;
/

--Buscar por nombre
create or replace function buscar_por_titulo (nom in VARCHAR2)
return types.ref_cursor
as
    crusor_libros types.ref_cursor;

BEGIN
    open crusor_libros for 
    select id, titulo, autor, editorial, disponible, anno_publicacion from Ejemplar
        where titulo = nom;
    return crusor_libros;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END buscar_por_titulo;
/

--mostrar todos
create or replace function mostrar_todo
return types.ref_cursor
as
    crusor_libros types.ref_cursor;

BEGIN
    open crusor_libros for 
    select id, titulo, autor, editorial, disponible, anno_publicacion from Ejemplar;
    return crusor_libros;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END mostrar_todo;
/

--Buscar por disponibilidad
create or replace function buscar_por_disponibilidad
return types.ref_cursor
as
    crusor_libros types.ref_cursor;

BEGIN
    open crusor_libros for 
    select id, titulo, autor, editorial, disponible, anno_publicacion from Ejemplar
        where disponible = 1;
    return crusor_libros;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END buscar_por_disponibilidad;
/

--Buscar por Pais
create or replace function buscar_por_pais (cod in number)
return types.ref_cursor
as
    crusor_libros types.ref_cursor;

BEGIN
    open crusor_libros for 
    select e.id, e.titulo, e.autor, e.editorial, e.disponible, e.anno_publicacion 
        from Ejemplar e
        Join Autor aut on e.AUTOR = aut.id 
        Join Pais p on aut.pais = p.id
        where p.id = cod;
    return crusor_libros;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END buscar_por_pais;
/


------------------------------------------------------- PAIS
create or replace function mostrar_paises 
return types.ref_cursor
as
    cursor_pais types.ref_cursor;

BEGIN
    open cursor_pais for 
        select id, nombre from Pais
        order by id;
    return cursor_pais;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END mostrar_paises;
/

create or replace function buscar_pais (cod in number) 
return types.ref_cursor
as
    cursor_pais types.ref_cursor;

BEGIN
    open cursor_pais for 
        select id, nombre from Pais
        where id = cod;
    return cursor_pais;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END buscar_pais;
/

--------------------------------------------------- EDITORIAL
create or replace function mostrar_editoriales 
return types.ref_cursor
as
    cursor_editorial types.ref_cursor;

BEGIN
    open cursor_editorial for 
        select id, nombre from Editorial
        order by id;
    return cursor_editorial;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END mostrar_editoriales;
/


create or replace function buscar_editorial (cod in number) 
return types.ref_cursor
as
    cursor_editorial types.ref_cursor;

BEGIN
    open cursor_editorial for 
        select id, nombre from Editorial
        where id = cod;
    return cursor_editorial;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END buscar_editorial;
/


------------------------------------------------------- AUTOR
create or replace function mostrar_autores 
return types.ref_cursor
as
    cursor_autor types.ref_cursor;

BEGIN
    open cursor_autor for 
        select id, nombres, apellidos, pais, anno_nacimiento from Autor
        order by id;
    return cursor_autor;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END mostrar_autores;
/


create or replace function buscar_autor (cod in number) 
return types.ref_cursor
as
    cursor_autor types.ref_cursor;

BEGIN
    open cursor_autor for 
        select id, nombres, apellidos, pais, anno_nacimiento 
        from Autor 
        where id = cod;
    return cursor_autor;
EXCEPTION
    when OTHERS
        then DBMS_OUTPUT.PUT_LINE('Error: '||SQLERRM);
        raise;
END buscar_autor;
/


select * from Pais;
-------------------------------------------------------------  TRIGGERS ---------------------------------------------------------

set serveroutput on
create or replace trigger trg_verificar_fecha_publicacion 
before insert or update on Ejemplar
for each row 
begin 
    IF :New.anno_publicacion IS NOT NULL AND :New.anno_publicacion > EXTRACT(YEAR FROM SYSDATE) THEN
        RAISE_APPLICATION_ERROR(-20001, 'El año de publicación de la obra no puede ser mayor que el año actual');
    END IF;
end trg_verificar_fecha_publicacion ;
/

set serveroutput on
create or replace trigger trg_verificar_fecha_nacimiento 
before insert or update on Autor
for each row 
begin 
    IF :New.anno_nacimiento IS NOT NULL AND :New.anno_nacimiento > EXTRACT(YEAR FROM SYSDATE) THEN
        RAISE_APPLICATION_ERROR(-20001, 'El año de nacimiento del autor no puede ser mayor que el año actual');
    END IF;
end trg_verificar_fecha_nacimiento;
/


--********************************************************************************************************************************************

alter table ejemplar drop column FECHA_publicacion


set SERVEROUTPUT on
DECLARE
    lib_id Number := :Id_Ejemplar;
     
    lib_rec Ejemplar%rowtype;
    pais_id Autor.pais%type;
    autor_nombres Autor.nombres%type;
    autor_apellidos Autor.apellidos%type;
    ed_nombre Editorial.nombre%type;
    pais_nombre Pais.nombre%type;
BEGIN
    mostrar_data(lib_id, lib_rec, pais_id, autor_nombres, autor_apellidos, ed_nombre, pais_nombre);
END;
/

select aut.id, aut.nombres, aut.apellidos, p.nombre, aut.anno_nacimiento from Autor aut
Join pais p on aut.id = p.id
order by id;

select * from Pais order by id;

--ALTER TABLE Ejemplar MODIFY (id GENERATED BY DEFAULT AS IDENTITY (START WITH 2));

CREATE USER trigadmin IDENTIFIED BY 1234;
GRANT CONNECT, RESOURCE TO trigadmin;

select * from CDB_USERS ;

Insert into EJEMPLAR(autor, editorial, titulo, disponible, anno_publicacion)
    values(1, 1,'La ciudad y los perros', 1, 1963);
COMMIT;

select * from ejemplar;


------------------------------------------------------------------------------











