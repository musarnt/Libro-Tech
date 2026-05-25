CREATE TABLE categories (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    deleted     BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at  TIMESTAMP
);

CREATE TABLE genres (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    deleted     BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at  TIMESTAMP
);

CREATE TABLE publishers (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    address    VARCHAR(255) NOT NULL,
    country    VARCHAR(100) NOT NULL,
    founded_in INT,
    deleted    BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP
);

CREATE TABLE books (
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    author           VARCHAR(255) NOT NULL,
    isbn             VARCHAR(20)  NOT NULL UNIQUE,
    year_publication INT          NOT NULL,
    publisher_id     BIGINT       NOT NULL REFERENCES publishers(id),
    category_id      BIGINT       NOT NULL REFERENCES categories(id),
    active           BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted          BOOLEAN      NOT NULL DEFAULT FALSE,
    deleted_at       TIMESTAMP
);



CREATE TABLE book_genre (
    book_id  BIGINT NOT NULL REFERENCES books(id),
    genre_id BIGINT NOT NULL REFERENCES genres(id),
    PRIMARY KEY (book_id, genre_id)
);