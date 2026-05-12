package com.riwi.librotech.config;

import com.riwi.librotech.model.Book;
import com.riwi.librotech.model.Category;
import com.riwi.librotech.repository.BookRepository;
import com.riwi.librotech.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public DataSeeder(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        // only seed if database is empty
        if (bookRepository.count() > 0) return;

        // categories
        Category fiction = categoryRepository.save(new Category(null, "Fiction", "Novels and short stories"));
        Category science = categoryRepository.save(new Category(null, "Science", "Scientific and academic books"));
        Category history = categoryRepository.save(new Category(null, "History", "Historical events and biographies"));
        Category tech = categoryRepository.save(new Category(null, "Technology", "Programming and software"));
        Category philosophy = categoryRepository.save(new Category(null, "Philosophy", "Philosophical works"));

        // books
        String[][] books = {
                {"Cien Años de Soledad", "Gabriel García Márquez", "978-0-06-088328-7", "1967"},
                {"Don Quijote", "Miguel de Cervantes", "978-84-376-0494-7", "1605"},
                {"1984", "George Orwell", "978-0-451-52493-5", "1949"},
                {"Sapiens", "Yuval Noah Harari", "978-0-06-231609-7", "2011"},
                {"Clean Code", "Robert C. Martin", "978-0-13-235088-4", "2008"},
                {"The Pragmatic Programmer", "David Thomas", "978-0-13-595705-9", "1999"},
                {"Cosmos", "Carl Sagan", "978-0-345-53943-4", "1980"},
                {"El Aleph", "Jorge Luis Borges", "978-84-206-3539-3", "1949"},
                {"A Brief History of Time", "Stephen Hawking", "978-0-553-38016-3", "1988"},
                {"The Art of War", "Sun Tzu", "978-1-59030-227-7", "-500"},
                {"Brave New World", "Aldous Huxley", "978-0-06-085052-4", "1932"},
                {"Fahrenheit 451", "Ray Bradbury", "978-1-45-163222-7", "1953"},
                {"The Great Gatsby", "F. Scott Fitzgerald", "978-0-7432-7356-5", "1925"},
                {"To Kill a Mockingbird", "Harper Lee", "978-0-06-112008-4", "1960"},
                {"One Hundred Years of Solitude", "Gabriel García Márquez", "978-0-06-120009-2", "1967"},
                {"The Catcher in the Rye", "J.D. Salinger", "978-0-316-76948-0", "1951"},
                {"The Hobbit", "J.R.R. Tolkien", "978-0-547-92822-7", "1937"},
                {"Crime and Punishment", "Fyodor Dostoevsky", "978-0-14-044913-6", "1866"},
                {"Pride and Prejudice", "Jane Austen", "978-0-14-143951-8", "1813"},
                {"The Lord of the Rings", "J.R.R. Tolkien", "978-0-544-00341-5", "1954"},
                {"War and Peace", "Leo Tolstoy", "978-0-14-044793-4", "1869"},
                {"Moby Dick", "Herman Melville", "978-0-14-243724-7", "1851"},
                {"The Odyssey", "Homer", "978-0-14-026886-7", "-800"},
                {"Hamlet", "William Shakespeare", "978-0-14-071476-1", "1603"},
                {"The Divine Comedy", "Dante Alighieri", "978-0-14-243722-3", "1320"},
                {"Les Misérables", "Victor Hugo", "978-0-14-044430-8", "1862"},
                {"Anna Karenina", "Leo Tolstoy", "978-0-14-303500-8", "1877"},
                {"Meditations", "Marcus Aurelius", "978-0-14-044933-4", "180"},
                {"The Republic", "Plato", "978-0-14-044914-3", "-375"},
                {"Frankenstein", "Mary Shelley", "978-0-14-143947-1", "1818"},
                {"Dracula", "Bram Stoker", "978-0-14-143984-6", "1897"},
                {"Jane Eyre", "Charlotte Brontë", "978-0-14-144114-6", "1847"},
                {"Wuthering Heights", "Emily Brontë", "978-0-14-143955-6", "1847"},
                {"The Picture of Dorian Gray", "Oscar Wilde", "978-0-14-143957-0", "1890"},
                {"Great Expectations", "Charles Dickens", "978-0-14-143956-3", "1861"},
                {"The Brothers Karamazov", "Fyodor Dostoevsky", "978-0-14-044927-3", "1880"},
                {"The Stranger", "Albert Camus", "978-0-679-72020-1", "1942"},
                {"Beloved", "Toni Morrison", "978-1-400-03341-6", "2004"},
                {"Slaughterhouse Five", "Kurt Vonnegut", "978-0-385-33348-1", "1969"},
                {"Dune", "Frank Herbert", "978-0-441-01903-5", "1965"},
                {"Foundation", "Isaac Asimov", "978-0-553-29335-7", "1951"},
                {"Neuromancer", "William Gibson", "978-0-441-56956-4", "1984"},
                {"The Shining", "Stephen King", "978-0-307-74365-1", "1977"},
                {"It", "Stephen King", "978-1-501-14234-0", "1986"},
                {"The Alchemist", "Paulo Coelho", "978-0-06-112241-5", "1988"},
                {"The Little Prince", "Antoine de Saint-Exupéry", "978-0-15-601219-5", "1943"},
                {"The Metamorphosis", "Franz Kafka", "978-0-14-119720-6", "1915"},
                {"The Trial", "Franz Kafka", "978-0-14-018574-0", "1925"},
                {"Siddhartha", "Hermann Hesse", "978-0-553-20884-2", "1922"},
                {"Steppenwolf", "Hermann Hesse", "978-0-312-27867-8", "1927"}
        };

        for (String[] b : books) {
            bookRepository.save(new Book(null, b[0], b[1], b[2], Integer.parseInt(b[3])));
        }

        System.out.println(">>> Database seeded with 5 categories and 50 books");
    }
}