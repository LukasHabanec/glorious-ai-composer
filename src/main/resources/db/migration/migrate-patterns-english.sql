--CREATE TABLE IF NOT EXISTS assets_tune_patterns (
--    id                  bigint(11) unsigned not null primary key,
--    body                varchar(32) not null,
--    form_association_id    bigint(11) comment 'eventual association with existing pattern source',
--    type                varchar(16)
--);

INSERT IGNORE INTO melody_tune_patterns (body, form_association_id)
VALUES
    ('0, 1, 2, 3, 4, 1, 2, 3', 1),
('0, 1, 2, 0, -1, 2, -2, -1, -2', 1),
('0, 1, 2, 0, 1, -1, 0, -2, -1', 1),
('0, -1, 0, 1, 0, 1, 2, 0, 3', 1),
('0, 1, -1, 0, 1, 2, 0, 1, 2', 1),
('0, -1, -2, -1, 0, -1, 0, -1, 0', 1),
('0, 1, 2, 0, -2, -1, 0, -2, 1', 1),
('0, 1, 2, 3, 4, 5, 6, 4, 7', 1),
('0, -1, 0, -2, -1, -2, -1, -3, -2', 1),
('0, -1, 0, 1, 0, -1, -2, -1, -3', 1),
('0, -3, -2, -1, 0, -3, -2, -1, 0', 1),
('0, -1, 0, -1, 0,-1, 0, -1, 0', 1),
('0, 1, 0, -1, -2, -1, 0, -2, -1', 1),
('0, 7, 4, 0, 7, 4, 0, 7, 4', 1),
('0, 1, 0, -1, -2, -3, -4, -5, -6', 1),
('0, -1, 0, 1, 0, -4, -3, -2, -1', 1),
('0, 7, 6, 5, 4, 3, 2, 1, 2', 1),
('0, -1, -2, -3, -4, -5, -6, -7, -8', 1),
('0, 1, -1, 3, 1, 0, -2, -1, -3', 1),
('0, 1, 2, 3, 4, 3, 2, 1, 0', 1),
('0, -2, 1, 0, -1, -2, -3, 0, 1', 1),
('0, 2, 1, 3, 2, 4, 0, 2, 1', 1),
('0, -2, -3, -4, -1, 0, -2, -3, -4', 1),
('0, -1, 1, 0, -1, 1, 0, -1, -2', 1),
('0, -4, -3, -4, -1, -4, -3, -4, 1', 1),
('0, -1, -2, -1, -3, -2, -1, 0, 1', 1),
('0, -1, -2, -1, -3, 0, -1, -2, 1', 1),
('0, 3, 5, 7, 6, 4, 5, 3, 4', 1),
('0, 1, 2, 3, 2, 1, 0, 2, 1', 1),
('0, 2, 4, 6, 5, 0, 2, 4, 6', 1),
('0, -3, 0, -3, 0, -3, 0, -3, 1', 1),
('0, -2, 0, -1, -2, -1, 0, 3, -2', 1),
('0, -1, -2, -3, 0, -1, -2, -3, 1', 1),
('0, 3, 0, 3, 0, 3, 1, 3, 2', 1),
('0, -2, -3, -4, -5, -3, -7, 0, -1', 1),
('0, 2, -2, 0, -4, -2, -6, -4, -3', 1),
('0, 5, 4, 3, 2, 1, 0, 3, 1', 1),
('0, 2, 1, 2, -1, 2, 1, 2, 0', 1),
('0, -2, -3, -4, 1, 0, -2, -3, -4', 1),
('0, 2, 4, 3, 2, 1, -1, 0, -2', 1),
('0, 3, 5, 7, 6, 0, 2, 4, 5', 1),
('0, -1, 0, -1, 0, -2, -1, 0, 1', 1),
('0, 6, 5, 4, 3, 2, 1, 0, -1', 1),
('0, 1, 2, 3, 4, 5, 6, 7, 8', 1),
('0, -3, -2, -1, 0, 1, 2, 3, 4', 1),
('0, 7, 6, 5, 4, 7, 6, 5, 4', 1),
('0, 2, 1, 3, 2, 4, 3, 5, 4', 1),
('0, -2, -3, -4, -1, -3, -4, -5, -2', 1);


INSERT IGNORE INTO melody_rhythm_patterns (body, form_association_id)
VALUES ('16', 1),
('6,2,2,2,2,2', 1),
('4,4,4,4', 1),
('8,2,2,2,2', 1),
('2,2,2,2,4,4', 1),
('4,4,2,2,2,2', 1),
('2,2,2,2,2,2,2,2', 1),
('4,8,2,2', 1),
('12,4', 1),
('2,2,2,2,8', 1),
('8,4,4', 1),
('4,8,4', 1),
('4,2,2,2,2,2,2', 1),
('4,4,8', 1),
('8,8', 1),
('6,1,1,8', 1),
('2,2,4,2,2,2,2', 1),
('2,1,1,4,4,4', 1),
('4,2,2,4,2,2', 1),
('12,2,2', 1),
('4,4,4,2,2', 1),
('2,2,2,2,4,2,2', 1),
('10,2,2,2', 1),
('2,2,4,8', 1),
('4,2,2,4,4', 1);