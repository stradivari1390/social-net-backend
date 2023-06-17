INSERT INTO socialnet.friends (
    id,
    status_code,
    account_from_id,
    requested_account_id,
    previous_status,
    rating
)
VALUES
    (1000, 'FRIEND', 1000, 1001, 'REQUEST_TO', 5),
    (1001, 'FRIEND', 1001, 1000, 'REQUEST_FROM', 5),
    (1002, 'FRIEND', 1000, 1002, 'REQUEST_TO', 4),
    (1003, 'FRIEND', 1002, 1000, 'REQUEST_FROM', 4),
    (1004, 'REQUEST_TO', 1000, 1003, 'NONE', NULL),
    (1005, 'REQUEST_FROM', 1003, 1000, 'NONE', NULL),
    (1006, 'BLOCKED', 1000, 1004, 'FRIEND', NULL),
    (1007, 'BLOCKED', 1004, 1000, 'FRIEND', NULL),
    (1008, 'DECLINED', 1000, 1005, 'REQUEST_TO', NULL),
    (1009, 'REJECTING', 1005, 1000, 'REQUEST_FROM', NULL),
    (1010, 'SUBSCRIBED', 1000, 1006, 'NONE', NULL),
    (1011, 'WATCHING', 1006, 1000, 'NONE', NULL),
    (1012, 'NONE', 1000, 1007, 'NONE', NULL),
    (1013, 'NONE', 1007, 1000, 'NONE', NULL),
    (1014, 'RECOMMENDATION', 1000, 1008, 'NONE', NULL),
    (1015, 'RECOMMENDATION', 1008, 1000, 'NONE', NULL),
    (1016, 'FRIEND', 1000, 1009, 'REQUEST_TO', 3),
    (1017, 'FRIEND', 1009, 1000, 'REQUEST_FROM', 3),
    (1018, 'FRIEND', 1001, 1009, 'REQUEST_TO', 5),
    (1019, 'FRIEND', 1009, 1001, 'REQUEST_FROM', 5),
    (1020, 'REQUEST_TO', 1009, 1008, 'NONE', NULL),
    (1021, 'REQUEST_FROM', 1008, 1009, 'NONE', NULL),
    (1022, 'REQUEST_TO', 1005, 1003, 'NONE', NULL),
    (1023, 'REQUEST_FROM', 1003, 1005, 'NONE', NULL);