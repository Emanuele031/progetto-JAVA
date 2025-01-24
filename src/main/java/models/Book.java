package main.java.models;

import javax.persistence.*;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "books")
public class Book extends Publication {

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    public Book() {}

    public Book(String title, int year, int numPages, Author author, Genre genre) {
        super(title, year, numPages);
        this.author = author;
        this.genre = genre;
    }

    public static Book randomBook() {
        List<Author> authors = List.of(
                new Author("George Orwell"),
                new Author("J.K. Rowling"),
                new Author("J.R.R. Tolkien"),
                new Author("Agatha Christie")
        );

        List<Genre> genres = List.of(
                new Genre("Fantasy"),
                new Genre("Science Fiction"),
                new Genre("Mystery"),
                new Genre("Horror")
        );

        Random random = new Random();
        Author randomAuthor = authors.get(random.nextInt(authors.size()));
        Genre randomGenre = genres.get(random.nextInt(genres.size()));

        String title = "Book " + (random.nextInt(1000) + 1);
        int year = random.nextInt(1900, 2026);
        int numPages = random.nextInt(100, 1001);

        return new Book(title, year, numPages, randomAuthor, randomGenre);
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Book{" +
                "\nisbn='" + this.getIsbn() + '\'' +
                ",\n \ttitle='" + this.getTitle() + '\'' +
                ",\n \tyear=" + this.getYear() +
                ",\n \tnumPages=" + this.getNumPages() +
                ",\n \tauthor=" + author.getName() +
                ",\n \tgenre=" + genre.getName() +
                "\n}";
    }
}
