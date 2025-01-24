package main.java.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import main.java.models.Book;
import main.java.models.Magazine;
import main.java.models.Publication;
import main.java.utils.JpaUtil;
import main.java.interfaces.IPublicationDAO;

public class PublicationDAO implements IPublicationDAO {

    @Override
    public void save(Publication publication) {
        if (publication.getIsbn() == null || publication.getIsbn().isEmpty()) {
            System.out.println("ISBN non valido");
            return;
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();

            Publication existingPublication = em.find(Publication.class, publication.getIsbn());
            if (existingPublication != null) {
                publication = em.merge(publication);
                System.out.println("Publication updated by ISBN: " + publication.getIsbn());
            } else {
                em.persist(publication);
                System.out.println("Publication saved with ISBN: " + publication.getIsbn());
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println(String.format("Error saving publication: %s", e.getMessage()));
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void saveBook(Book book) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(book);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public void saveMagazine(Magazine magazine) {
        if (magazine.getIsbn() == null || magazine.getIsbn().isEmpty()) {
            System.out.println("ISBN non valido");
            return;
        }

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();

            Magazine existingMagazine = em.find(Magazine.class, magazine.getIsbn());
            if (existingMagazine != null) {
                magazine = em.merge(magazine);
                System.out.println("Magazine updated by ISBN: " + magazine.getIsbn());
            } else {
                em.persist(magazine);
                System.out.println("Magazine saved with ISBN: " + magazine.getIsbn());
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println(String.format("Error saving magazine: %s", e.getMessage()));
        } finally {
            em.close();
        }
    }

    @Override
    public void saveAll(List<Publication> publications) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();

            for (Publication publication : publications) {
                if (publication instanceof Book) {
                    saveBook((Book) publication);
                } else if (publication instanceof Magazine) {
                    saveMagazine((Magazine) publication);
                } else {
                    save(publication);
                }
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error saving publications: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public Publication getByIsbn(String isbn) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        Publication publication = null;
        try {
            em.getTransaction().begin();
            TypedQuery<Publication> query = em.createQuery(
                    "SELECT p FROM Publication p WHERE p.isbn = :isbn", Publication.class);
            query.setParameter("isbn", isbn);
            publication = query.getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println(String.format("Error getting publication by ISBN: %s", isbn));
        } finally {
            em.close();
        }
        return publication;
    }

    @Override
    public List<Publication> getAll() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        List<Publication> publications = null;
        try {
            em.getTransaction().begin();
            TypedQuery<Publication> q = em.createQuery("SELECT p FROM Publication p", Publication.class);
            publications = q.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error getting all publications");
        } finally {
            em.close();
        }
        return publications;
    }

    @Override
    public void removeByIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            System.out.println("ISBN non valido");
            return;
        }

        isbn = isbn.trim();

        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();

            TypedQuery<Long> checkMagazinesQuery = em.createQuery(
                    "SELECT COUNT(m) FROM Magazine m WHERE m.isbn = :isbn", Long.class);
            checkMagazinesQuery.setParameter("isbn", isbn);
            Long magazineCount = checkMagazinesQuery.getSingleResult();

            if (magazineCount > 0) {
                System.out.println("L'ISBN è presente nella tabella magazines. Rimuovere prima i dati correlati.");
            }

            Publication publication = em.find(Publication.class, isbn);

            if (publication != null) {
                em.remove(publication);
                System.out.println("Publication removed by ISBN: " + isbn);
            } else {
                System.out.println("No publication found with ISBN: " + isbn);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println(String.format("Error removing publication by ISBN: %s", isbn));
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Publication> getByYear(int year) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        List<Publication> publications = null;
        try {
            em.getTransaction().begin();
            TypedQuery<Publication> query = em.createQuery(
                    "SELECT p FROM Publication p WHERE YEAR(p.publicationDate) = :year", Publication.class);
            query.setParameter("year", year);
            publications = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error getting publications by year: " + e.getMessage());
        } finally {
            em.close();
        }
        return publications;
    }

    @Override
    public List<Publication> getByAuthor(String author) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        List<Publication> publications = null;
        try {
            em.getTransaction().begin();
            TypedQuery<Publication> query = em.createQuery(
                    "SELECT p FROM Publication p WHERE p.author = :author", Publication.class);
            query.setParameter("author", author);
            publications = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error getting publications by author: " + e.getMessage());
        } finally {
            em.close();
        }
        return publications;
    }

    @Override
    public List<Publication> getByTitle(String title) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        List<Publication> publications = null;
        try {
            em.getTransaction().begin();
            TypedQuery<Publication> query = em.createQuery(
                    "SELECT p FROM Publication p WHERE p.title LIKE :title", Publication.class);
            query.setParameter("title", "%" + title + "%");
            publications = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error getting publications by title: " + e.getMessage());
        } finally {
            em.close();
        }
        return publications;
    }
}
