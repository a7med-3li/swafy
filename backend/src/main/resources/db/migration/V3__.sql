CREATE TABLE notifications
(
    id            UUID         NOT NULL,
    receiver_id   UUID         NOT NULL,
    receiver_name VARCHAR(255) NOT NULL,
    type          VARCHAR(255) NOT NULL,
    title         VARCHAR(255) NOT NULL,
    short_message VARCHAR(255) NOT NULL,
    message       VARCHAR(255) NOT NULL,
    status        VARCHAR(255) NOT NULL,
    metadata      TEXT,
    created_at    date         NOT NULL,
    updated_at    date         NOT NULL,
    read_at       date,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);
