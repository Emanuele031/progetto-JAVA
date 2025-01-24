package main.java.com.emanuele;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.java.models.Book;
import main.java.models.Magazine;
import main.java.models.Publication;
import main.java.models.User;
import main.java.utils.Library;
import main.java.dao.LoanDAO;
import main.java.dao.PublicationDAO;
import main.java.dao.UserDAO;
import main.java.models.Loan;

public final class App {
    private static Library library;

    public static void main(String[] args) {
        try {
            library = Library.randomLibrary(10);
            PublicationDAO publicationDAO = new PublicationDAO();
            publicationDAO.saveAll(library.getAllPublications());

            Book book = Book.randomBook();
            publicationDAO.save(book);

            Magazine magazine = Magazine.randomMagazine();
            publicationDAO.save(magazine);

            System.out.println(String.format(
                    "Publication removed by ISBN: %s\n\n %s",
                    book.getIsbn(), book));
            publicationDAO.removeByIsbn(book.getIsbn());

            String isbnToSearch = "00008";
            Publication publicationFound = publicationDAO.getByIsbn(isbnToSearch);
            if (publicationFound != null) {
                System.out.println(String.format(
                        "Publication found by ISBN: %s\n\n %s",
                        publicationFound.getIsbn(), publicationFound));
            } else {
                System.out.println(String.format("Publication with ISBN %s not found.", isbnToSearch));
            }

            int year = 2017;
            List<Publication> publicationsByYear = publicationDAO.getByYear(year);
            if (!publicationsByYear.isEmpty()) {
                System.out.println(String.format("Publications found by year: %s\n\n %s",
                        year, publicationsByYear));
            } else {
                System.out.println("No publications found for the year " + year);
            }

            String author = "George Orwell";
            List<Publication> publicationsByAuthor = publicationDAO.getByAuthor(author);
            if (!publicationsByAuthor.isEmpty()) {
                System.out.println(String.format(
                        "Publications found by author: %s\n\n %s",
                        author, publicationsByAuthor));
            } else {
                System.out.println("No publications found by author " + author);
            }

            String searchTerm = "The Great Gatsby";
            List<Publication> publicationsByTitle = publicationDAO.getByTitle(searchTerm);
            if (!publicationsByTitle.isEmpty()) {
                System.out.println(String.format(
                        "Publications found by title: %s\n\n %s",
                        searchTerm, publicationsByTitle));
            } else {
                System.out.println("No publications found with title containing: " + searchTerm);
            }

            List<User> users = randomUsers(5);
            UserDAO userDAO = new UserDAO();
            userDAO.saveAll(users);

            LoanDAO loanDAO = new LoanDAO();
            List<Loan> loans = randomLoans(10);
            System.out.println(loans);
            loanDAO.saveAll(loans);

            long cardNumber = 5;
            List<Loan> loansByUser = loanDAO.getLoansByCardNumber(cardNumber);
            if (loansByUser.isEmpty()) {
                System.out.println("No loans found for card number: " + cardNumber);
            } else {
                System.out.println(String.format(
                        "Loans found by card number: %s\n\n %s",
                        cardNumber, loansByUser));
            }

            List<Loan> loansNotReturned = loanDAO.getExpiredNotReturnedLoans();
            System.out.println(
                    String.format(
                            "Loans not returned: %s",
                            loansNotReturned.size()));

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<User> randomUsers(int quantity) {
        System.out.println(String.format("Generating %d random users\n", quantity));
        List<User> users = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            users.add(User.randomUser());
        }
        return users;
    }

    public static List<Loan> randomLoans(int quantity) {
        Random random = new Random();
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAll();
        PublicationDAO publicationDAO = new PublicationDAO();
        List<Publication> publications = publicationDAO.getAll();
        List<Loan> loans = new ArrayList<>();
        LocalDate startDateMin = LocalDate.now().minusYears(10);
        LocalDate startDateMax = LocalDate.now();
        for (int i = 0; i < quantity; i++) {
            User user = users.get(random.nextInt(users.size()));
            Publication publication = publications.get(random.nextInt(publications.size()));
            LocalDate startDate = getRandomStartDate(startDateMin, startDateMax, random);
            LocalDate endDate = startDate.plusMonths(1);
            LocalDate returnDate = getRandomReturnDate(startDate, endDate, i, random);
            Loan loan = new Loan(user, publication, startDate, endDate, returnDate);
            loans.add(loan);
        }
        return loans;
    }

    private static LocalDate getRandomStartDate(LocalDate startDateMin, LocalDate startDateMax, Random random) {
        long daysBetween = startDateMax.toEpochDay() - startDateMin.toEpochDay();
        long randomDays = (long) (random.nextDouble() * daysBetween);
        return startDateMin.plusDays(randomDays);
    }

    private static LocalDate getRandomReturnDate(LocalDate startDate, LocalDate endDate, int index, Random random) {
        LocalDate returnDate = null;
        if (index % 3 == 0) {
            int randomDaysOffset = random.nextInt(30);
            returnDate = endDate.plusDays(randomDaysOffset);
        } else if (index % 5 == 0) {
            int randomDaysOffset = random.nextInt(30);
            returnDate = startDate.minusDays(randomDaysOffset);
        }
        return returnDate;
    }
}
