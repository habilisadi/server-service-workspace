--liquibase formatted sql

-- changeset system:v0.0.0-create-workspaces-table
CREATE TABLE IF NOT EXISTS workspaces
(
    id          CHAR(27) PRIMARY KEY                NOT NULL,
    name        VARCHAR(255)                        NOT NULL,
    description VARCHAR(255)                        NOT NULL,
    isActive    BOOLEAN   DEFAULT TRUE              NOT NULL,
    domain      VARCHAR(255)                        NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted_at  TIMESTAMP DEFAULT NULL
);
--rollback DROP TABLE IF EXISTS workspaces CASCADE;

-- changeset system:v0.0.0-create-workspace-users-table
CREATE TABLE IF NOT EXISTS workspace_users
(
    id             CHAR(27) PRIMARY KEY                NOT NULL,
    workspace_pk   CHAR(27)                            NOT NULL,
    user_pk        CHAR(27)                            NOT NULL,
    workspace_name VARCHAR(255)                        NOT NULL,
    profile_img    VARCHAR(255),
    role           VARCHAR(255),
    is_active      BOOLEAN   DEFAULT TRUE              NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted_at     TIMESTAMP DEFAULT NULL
);
-- rollback DROP TABLE IF EXISTS workspace_users CASCADE;

CREATE TABLE IF NOT EXISTS workspace_invitations
(
    id           CHAR(27) PRIMARY KEY NOT NULL,
    workspace_pk CHAR(27)             NOT NULL,
    user_pk      CHAR(27)             NOT NULL,
    code         VARCHAR(255),
    status       VARCHAR(255)         NOT NULL,
    expired_at   TIMESTAMP            NOT NULL
);

-- changeset system:v0.0.0-create-workspace-channels-table
CREATE TABLE IF NOT EXISTS workspace_channels
(
    id           CHAR(27) PRIMARY KEY                NOT NULL,
    workspace_pk CHAR(27)                            NOT NULL,
    name         VARCHAR(255)                        NOT NULL,
    isPublic     BOOLEAN   DEFAULT TRUE              NOT NULL,
    isActive     BOOLEAN   DEFAULT TRUE              NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted_at   TIMESTAMP DEFAULT NULL
);
-- rollback DROP TABLE IF EXISTS workspace_channels CASCADE;

-- changeset system:v0.0.0-create-workspace-channel-users-table
CREATE TABLE IF NOT EXISTS workspace_channel_users
(
    id                CHAR(27) PRIMARY KEY                NOT NULL,
    channel_pk        CHAR(27)                            NOT NULL,
    workspace_user_pk CHAR(27)                            NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
-- rollback DROP TABLE IF EXISTS workspace_channel_users CASCADE;

-- changeset system:v0.0.0-create-workspace-channel-messages-table
CREATE TABLE IF NOT EXISTS workspace_channel_messages
(
    id                CHAR(27) PRIMARY KEY                NOT NULL,
    parent_id         CHAR(27)  DEFAULT NULL,
    channel_pk        CHAR(27)                            NOT NULL,
    workspace_user_pk CHAR(27)                            NOT NULL,
    message           JSONB                               NOT NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted_at        TIMESTAMP DEFAULT NULL
);
-- rollback DROP TABLE IF EXISTS workspace_channel_messages CASCADE;

-- changeset system:v0.0.0-create-workspace-channel-message-comments-table
CREATE TABLE IF NOT EXISTS workspace_channel_message_comments
(
    id                 CHAR(27) PRIMARY KEY                NOT NULL,
    parent_id          CHAR(27)  DEFAULT NULL,
    channel_message_pk CHAR(27)                            NOT NULL,
    workspace_user_pk  CHAR(27)                            NOT NULL,
    message            JSONB                               NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
-- rollback DROP TABLE IF EXISTS workspace_channel_message_comments CASCADE;

-- changeset system:v0.0.0-create-workspace-channel-message-bookmarks-table
CREATE TABLE IF NOT EXISTS workspace_channel_message_bookmarks
(
    id                 CHAR(27) PRIMARY KEY                NOT NULL,
    channel_message_pk CHAR(27)                            NOT NULL,
    workspace_user_pk  CHAR(27)                            NOT NULL,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
-- rollback DROP TABLE IF EXISTS workspace_channel_message_bookmarks CASCADE;

--changeset system:v0.0.0-add-foreign-keys
ALTER TABLE workspace_users
    ADD CONSTRAINT fk_workspace_pk FOREIGN KEY (workspace_pk) REFERENCES workspaces (id);

ALTER TABLE workspace_channel_users
    ADD CONSTRAINT fk_channel_pk FOREIGN KEY (channel_pk) REFERENCES workspace_channels (id);

ALTER TABLE workspace_channel_users
    ADD CONSTRAINT fk_workspace_user_pk FOREIGN KEY (workspace_user_pk) REFERENCES workspace_users (id);

ALTER TABLE workspace_channel_messages
    ADD CONSTRAINT fk_channel_pk FOREIGN KEY (channel_pk) REFERENCES workspace_channels (id);

ALTER TABLE workspace_channel_messages
    ADD CONSTRAINT fk_workspace_user_pk FOREIGN KEY (workspace_user_pk) REFERENCES workspace_users (id);

ALTER TABLE workspace_channel_message_comments
    ADD CONSTRAINT fk_channel_message_pk FOREIGN KEY (channel_message_pk) REFERENCES workspace_channel_messages (id);

ALTER TABLE workspace_channel_message_comments
    ADD CONSTRAINT fk_workspace_user_pk FOREIGN KEY (workspace_user_pk) REFERENCES workspace_users (id);

ALTER TABLE workspace_channel_message_bookmarks
    ADD CONSTRAINT fk_channel_message_pk FOREIGN KEY (channel_message_pk) REFERENCES workspace_channel_messages (id);

ALTER TABLE workspace_channel_message_bookmarks
    ADD CONSTRAINT fk_workspace_user_pk FOREIGN KEY (workspace_user_pk) REFERENCES workspace_users (id);

