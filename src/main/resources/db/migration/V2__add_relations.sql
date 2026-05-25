CREATE INDEX idx_books_deleted      ON books(deleted);
CREATE INDEX idx_categories_deleted ON categories(deleted);
CREATE INDEX idx_genres_deleted     ON genres(deleted);
CREATE INDEX idx_books_category     ON books(category_id);
CREATE INDEX idx_books_publisher    ON books(publisher_id);