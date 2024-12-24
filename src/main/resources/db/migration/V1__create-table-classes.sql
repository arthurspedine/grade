CREATE TABLE classes
(
    id         CHAR(36)     NOT NULL DEFAULT (UUID()) PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_by VARCHAR(255) NOT NULL
);
