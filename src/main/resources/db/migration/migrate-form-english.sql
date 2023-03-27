CREATE TABLE IF NOT EXISTS composition_forms (
                                                 id                  bigint(11) unsigned not null primary key,
                                                 title               varchar(32),
                                                 measure_count       int not null,
                                                 melody_rhythm_scheme    varchar(64) not null,
                                                 melody_tune_scheme      varchar(64) not null,
                                                 key_scheme              varchar(64) not null,
                                                 harmony_fragmentation   varchar(64),
                                                 harmony_figuration      varchar(128),
                                                 harmony_rhythm_scheme   varchar(128)
);

INSERT IGNORE INTO composition_forms (id, title, measure_count, melody_rhythm_scheme, melody_tune_scheme, key_scheme,
                                      harmony_fragmentation, harmony_figuration, harmony_rhythm_scheme)
VALUES (1, 'Hook D', 16, 'ABCDAEFGCCCCFFFF', 'ABCDABEFGHGHIGIJ', '0_0 7_1',
        'BABABDEABABDDFCA', 'aaaaabcc_aaaaddee_ccccccca_aaadaaaf', 'aaabacab_aaaaaadb_ddddddde_bebaaaaf'),
    (2, 'test four', 4, 'ABCB', 'AABA', 'AAAA', 'ABAC', 'aaaaabcc', 'aabbaabb');