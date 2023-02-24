CREATE TABLE IF NOT EXISTS assets_quint_circle (
    id              bigint(11) unsigned not null primary key,
    name            varchar(16) not null unique,
    label           varchar(8) not null unique,
    midi_ini_tone   int not null,
    enabled         tinyint(2)
);

INSERT IGNORE INTO assets_quint_circle (id, name, label, midi_ini_tone)
VALUES (1, 'F_FLAT', 'Fb', 4),
       (2, 'C_FLAT', 'Cb', 11),
       (3, 'G_FLAT', 'Gb', 6),
       (4, 'D_FLAT', 'Db', 1),
       (5, 'A_FLAT', 'Ab', 8),
       (6, 'E_FLAT', 'Eb', 3),
       (7, 'B_FLAT', 'Bb', 10),
       (8, 'F', 'F', 5),
       (9, 'C', 'C', 0),
       (10, 'G', 'G', 7),
       (11, 'D', 'D', 2),
       (12, 'A', 'A', 9),
       (13, 'E', 'E', 4),
       (14, 'B', 'B', 11),
       (15, 'F_SHARP', 'F#', 6),
       (16, 'C_SHARP', 'C#', 1),
       (17, 'G_SHARP', 'G#', 8),
       (18, 'D_SHARP', 'D#', 3),
       (19, 'A_SHARP', 'A#', 10);
