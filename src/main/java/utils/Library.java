package main.java.utils;

import main.java.models.Book;
import main.java.models.Magazine;
import main.java.models.Publication;

import java.util.*;
import java.util.stream.Collectors;

public class Library {

    private Map<String, Publication> publications;
    private static long isbnCounter = 10000;

    public Library() {
        this.publications = new HashMap<>();
    }

    // Genera un ISBN univoco
    private String generateISBN() {
        isbnCounter++;
        return String.format("%05d", isbnCounter);
    }

    // Aggiungi una pubblicazione al catalogo
    public Publication put(Publication publication) {
        String isbn = publication.getIsbn();
        if (isbn == null || isbn.isEmpty()) {
            publication.setIsbn(generateISBN());
        }
        return publications.put(publication.getIsbn(), publication);
    }

    // Rimuovi una pubblicazione per ISBN
    public Publication remove(String isbn) {
        return publications.remove(isbn);
    }

    // Ricerca per ISBN
    public Publication searchByIsbn(String isbn) {
        return publications.get(isbn);
    }

    // Ricerca per anno di pubblicazione
    public List<Publication> searchByYear(int year) {
        return publications.values().stream()
                .filter(publication -> publication.getYear() == year)
                .collect(Collectors.toList());
    }

    // Ricerca per autore (solo per i libri)
    public List<Book> searchByAuthor(String author) {
        return publications.values().stream()
                .filter(publication -> publication instanceof Book)
                .filter(publication -> ((Book) publication).getAuthor().equals(author))
                .map(publication -> (Book) publication)
                .collect(Collectors.toList());
    }

    // Ricerca per tipo (libro o rivista)
    public List<Publication> searchByType(Class<?> type) {
        return publications.values().stream()
                .filter(publication -> publication.getClass().equals(type))
                .collect(Collectors.toList());
    }

    // Ricerca per titolo o parte del titolo
    public List<Publication> searchByTitle(String titlePart) {
        return publications.values().stream()
                .filter(publication -> publication.getTitle().contains(titlePart))
                .collect(Collectors.toList());
    }

    // Restituisce la lista di tutte le pubblicazioni
    public List<Publication> getAllPublications() {
        return new ArrayList<>(publications.values());
    }

    // Aggiungi un libro o una rivista casuale al catalogo
    public static Library randomLibrary(int quantity) {
        Library library = new Library();
        Random rand = new Random();
        for (int i = 0; i < quantity; i++) {
            if (rand.nextBoolean()) {
                Book book = Book.randomBook();
                library.put(book);
            } else {
                Magazine magazine = Magazine.randomMagazine();
                library.put(magazine);
            }
        }
        return library;
    }

    // Metodo toString per stampare l'intero catalogo
    @Override
    public String toString() {
        return publications.values().stream()
                .map(Publication::toString)
                .collect(Collectors.joining("\n"));
    }

    // Restituisce la dimensione del catalogo
    public int size() {
        return publications.size();
    }
}
