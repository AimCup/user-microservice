CREATE TABLE "user"
(
    id            UUID PRIMARY KEY,
    username      VARCHAR NOT NULL,
    osu_id        VARCHAR  NOT NULL,
    is_restricted BOOLEAN NOT NULL,
    CONSTRAINT user_username_unique UNIQUE (username),
    CONSTRAINT user_osuId_unique UNIQUE (osu_id)
);

CREATE INDEX idx_user_username ON "user" (username);
CREATE INDEX idx_user_osuId ON "user" (osu_id);
