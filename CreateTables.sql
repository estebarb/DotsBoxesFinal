--drop table "usuarios" cascade constraints;
create table "usuarios"
(
  id bigserial not null primary key,
  email character varying(255),
  nombre character varying(255),
  password character varying(32),
  salt character varying(32),
  lastlogin timestamp,
  ranking integer
);
create index "mail_idx" on "usuarios" (lower(email));
create index "nombre_idx" on "usuarios" (lower(nombre));
create index "ranking_idx" on "usuarios" (ranking);
alter table "usuarios"
  owner to proyecto;
--                       ---------------------------------
--drop table "equipos" cascade constraints;
create table "equipos"
(
    id bigserial not null primary key,
    nombre character varying(255),
    ranking integer
);
create index on "equipos" (lower(nombre));
create index on "equipos" (ranking);
alter table "equipos"
  owner to proyecto;

--drop table "miembrosequipo" cascade constraints;
create table "miembrosequipo"
(
    id bigserial not null primary key,
    jugador bigint,
    constraint "jugador_con" foreign key (jugador)
      references "usuarios" (id) match simple,
    equipo bigint,
    constraint "equipo_con" foreign key (equipo)
      references "equipos" (id) match simple
);
alter table "miembrosequipo"
  owner to proyecto;


--------------------------------------------

create table "jugadorespc"
(
  id bigserial not null primary key,
  nombre character varying(255),
  descripcion character varying(255),
  ranking integer
);
alter table "jugadorespc"
  owner to proyecto;

--------------------------------------------
--drop table "juegos" cascade constraints;
create table "juegos" (
    id bigserial not null primary key,
    isterminado boolean,
    ganador bigint,
    turnoactual int,
    fecha timestamp,
    torneo bigint, --opcional
filas integer,
  columnas integer,
  tablero bytea
);
alter table "juegos"
  owner to proyecto;

--drop table "jugadoresjuego" cascade constraints;
create table "jugadoresjuego"
(
  id bigserial not null primary key,
  juego bigint,
  jugador bigint,
  puntaje integer
);
alter table "jugadoresjuego"
  owner to proyecto;


-------------------------------------------
--drop table "pendientes" cascade constraints;
create table "pendientes"
(
  id bigserial not null primary key,
  jugador bigint,
  juego bigint,
  turno boolean -- ya le toca jugar...
);
--create index on "pendientes" (jugador);
alter table "pendientes"
  owner to proyecto;



-------------------------

--drop table "ganadores" cascade constraints;
create table "ganadores"
(
    id bigserial not null primary key,
    juego bigint    
);
alter table "ganadores"
  owner to proyecto;

--------------------------------------------
create table "jugadores"
(
    id bigserial not null primary key,
    type int,
    usuario bigint,
    equipo bigint,
    ganador bigint,
    jugadorpc bigint   
);
alter table "jugadores"
    owner to proyecto;



-----------------------------------------------
create table "participantestorneo"
(
    id bigserial not null primary key,
    jugador bigint,
    torneo bigint
);
alter table "participantestorneo"
  owner to proyecto;

create table "torneos"
(
  id bigserial not null primary key,
  administrador bigint,
  tipotorneo int,
  numparticipantes int,
  nombretorneo character varying not null
);
alter table "torneos"
  owner to proyecto;


-- agrega las constraints --


-- jugadores --
alter table "jugadores" add constraint "usuario_con" foreign key (usuario)
        references "usuarios"(id) match simple;
alter table "jugadores" add constraint "equipo_con" foreign key (equipo)
        references "equipos"(id) match simple;
alter table "jugadores" add constraint "ganador_con" foreign key (ganador)
        references "ganadores"(id) match simple;
alter table "jugadores" add constraint "jugadorpc_con" foreign key (jugadorpc)
        references "jugadorespc"(id) match simple;

-- participantes torneo --
alter table "participantestorneo" add constraint jugador_fk foreign key (jugador)
        references "jugadores"(id) match simple;
alter table "participantestorneo" add constraint torneo_fk foreign key (torneo)
        references "torneos"(id) match simple;

-- torneo --
alter table "torneos" add constraint torneo_admin_fkey foreign key (administrador)
      references "usuarios" (id) match simple
      on update no action on delete no action;

-- ganadores --
alter table "ganadores" add constraint "juego_con" foreign key (juego)
        references "juegos"(id) match simple;

-- pendientes --
alter table "pendientes" add constraint "jugador_fk" foreign key (jugador)
      references "usuarios" (id) match simple
      on update no action on delete no action;
alter table "pendientes" add constraint "juego_fk" foreign key (juego)
      references "juegos" (id) match simple
      on update no action on delete no action;

-- juegos --
alter table "juegos" add constraint "ganador_fkey" foreign key (ganador)
      references "jugadores" (id) match simple
      on update no action on delete no action;

-- jugadoresjuego --
alter table "jugadoresjuego" add   constraint "jugadoresjuego_juego_fkey" foreign key (juego)
      references "juegos" (id) match simple
      on update no action on delete no action;
alter table "jugadoresjuego" add constraint "jugadoresjuego_jugadores_fkey" foreign key (jugador)
      references "jugadores" (id) match simple
      on update no action on delete no action;