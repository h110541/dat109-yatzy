DROP SCHEMA IF EXISTS yatzy CASCADE;
CREATE SCHEMA yatzy;
SET search_path TO yatzy;

CREATE TABLE bruker (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    brukernavn text UNIQUE NOT NULL CHECK(length(brukernavn) <= 20),
    navn text NOT NULL CHECK(length(navn) <= 100),
    epost text NOT NULL CHECK(length(epost) <= 100),
    admin boolean NOT NULL,
    pwhash text NOT NULL
);

CREATE TABLE spill (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    startet boolean NOT NULL
);

CREATE TABLE bruker_spill (
    bruker_id bigint REFERENCES bruker(id),
    spill_id bigint REFERENCES spill(id),
    PRIMARY KEY (bruker_id, spill_id)
);

CREATE TABLE runderesultat (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    t1 int NOT NULL CHECK(t1 BETWEEN 1 AND 6),
    t2 int NOT NULL CHECK(t2 BETWEEN 1 AND 6),
    t3 int NOT NULL CHECK(t3 BETWEEN 1 AND 6),
    t4 int NOT NULL CHECK(t4 BETWEEN 1 AND 6),
    t5 int NOT NULL CHECK(t5 BETWEEN 1 AND 6),
    kombinasjonstype int NOT NULL,
    kastnr int NOT NULL CHECK(kastnr BETWEEN 1 AND 3),
    bruker_id bigint NOT NULL REFERENCES bruker(id),
    spill_id bigint NOT NULL REFERENCES spill(id),
    UNIQUE (bruker_id, spill_id, kombinasjonstype)
);

INSERT INTO bruker (brukernavn, navn, epost, admin, pwhash)
VALUES
    ('Ole', 'Ole Olsen', 'ole@mail.net', TRUE, '{bcrypt}$2a$10$/ocvKi3mpZtbZAq1tN7gHejyXIiyPaazaNACe011i7XalF1rhfvs2'), -- pw=pass1234
    ('Hanne', 'Hanne Olsen', 'hanne@mail.net', FALSE, '{bcrypt}$2a$10$/ocvKi3mpZtbZAq1tN7gHejyXIiyPaazaNACe011i7XalF1rhfvs2');

/*    ('Ole'),
    ('Hanne'),
    ('Silje'),
    ('Øystein'),
    ('Lene'),
    ('Morten'),
    ('Anders'),
    ('Stine');*/
