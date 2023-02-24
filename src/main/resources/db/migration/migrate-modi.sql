CREATE TABLE IF NOT EXISTS assets_modi (
    id          bigint(11) unsigned not null primary key auto_increment,
    label       varchar(64) not null unique,
    intervals   varchar(64) not null unique,
    enabled     tinyint(2)
);

INSERT IGNORE INTO assets_modi (label, intervals, enabled)
VALUES ('MAJOR', '2,2,1,2,2,2,1', 1),
       ('MINOR_AEOLIAN', '2,1,2,2,1,2,2', 0),
       ('MINOR_DORIAN', '2,1,2,2,2,1,2', 0),
       ('MINOR_HARMONIC', '2,1,2,2,1,3,1', 0),
       ('CHROMATIC', '1,1,1,1,1,1,1,1,1,1,1,1', 0),
       ('PENTATONIC', '2,2,3,2,3', 0),
       ('DIATONIC', '2,1,2,1,2,1,2,1', 0);
