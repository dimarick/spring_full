CREATE TABLE audit
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description VARCHAR(255),
    data        JSONB,
    session_id  VARCHAR(255),
    owner_id    BIGINT,
    created_at  TIMESTAMP WITH TIME ZONE                NOT NULL,
    CONSTRAINT pk_audit PRIMARY KEY (id)
);

ALTER TABLE audit
    ADD CONSTRAINT FK_AUDIT_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE SET NULL;
