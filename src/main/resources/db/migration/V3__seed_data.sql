-- 1. Limpieza total en el orden correcto para evitar bloqueos
TRUNCATE TABLE book_genre CASCADE;
TRUNCATE TABLE books CASCADE;
TRUNCATE TABLE genres CASCADE;
TRUNCATE TABLE categories CASCADE;
TRUNCATE TABLE publishers CASCADE;

-- 2. Insertamos la categoría base obligatoria (ID 1)
INSERT INTO categories (id, name) VALUES 
(1, 'General');

-- 3. Insertamos las editoriales incluyendo la columna 'address' requerida
INSERT INTO publishers (id, name, address, country) VALUES
(1, 'Editorial Alpha', 'Calle 10 #45-12', 'Colombia'),
(2, 'Penguin Random House', 'Avenida Diagonal 640', 'España');

-- 4. Insertamos los géneros base que necesitas
INSERT INTO genres (id, name, description) VALUES
(1, 'Novel',           'Long narrative prose'),
(2, 'Dystopia',        'Oppressive future societies'),
(3, 'Essay',           'Reflection and analysis'),
(4, 'Adventure',       'Action and travel stories'),
(5, 'Horror',          'Fear and suspense'),
(6, 'Science Fiction', 'Scientific speculation');

-- 5. Generamos automáticamente los 300 libros
INSERT INTO books (id, title, author, isbn, year_publication, category_id, publisher_id)
SELECT
    i AS id,
    'Libro Técnico Vol. ' || i AS title,
    'Autor Detalle ' || ((i % 15) + 1) AS author,
    '978-0-14-' || lpad(i::text, 6, '0') || '-3' AS isbn,
    (1980 + (i % 45)) AS year_publication,
    1 AS category_id,
    2 AS publisher_id
FROM generate_series(1, 300) AS i;

-- 6. Vinculamos los 300 libros con sus géneros correspondientes
INSERT INTO book_genre (book_id, genre_id)
SELECT 
    i AS book_id,
    ((i % 6) + 1) AS genre_id
FROM generate_series(1, 300) AS i;