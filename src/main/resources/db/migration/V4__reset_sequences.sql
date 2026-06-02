-- Reset all sequences to match the max existing ID in each table.
-- Necessary because V3 inserted rows with explicit IDs, which bypasses the sequence.

SELECT setval(pg_get_serial_sequence('books',      'id'), (SELECT MAX(id) FROM books));
SELECT setval(pg_get_serial_sequence('categories',  'id'), (SELECT MAX(id) FROM categories));
SELECT setval(pg_get_serial_sequence('publishers',  'id'), (SELECT MAX(id) FROM publishers));
SELECT setval(pg_get_serial_sequence('genres',      'id'), (SELECT MAX(id) FROM genres));