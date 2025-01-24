package main.java.models;

import java.util.List;
import java.util.Random;



import javax.persistence.*;
import main.java.enumerates.Periodicity;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "magazines")
public class Magazine extends Publication {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Periodicity periodicity;

    public Magazine() {}

    public Magazine(String title, int year, int numPages, Periodicity periodicity) {
        super(title, year, numPages);
        this.periodicity = periodicity;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "\nisbn='" + this.getIsbn() + '\'' +
                ",\n \ttitle='" + this.getTitle() + '\'' +
                ",\n \tyear=" + this.getYear() +
                ",\n \tnumPages=" + this.getNumPages() +
                ",\n\tperiodicity=" + periodicity +
                "\n}";
    }

    public static Magazine randomMagazine() {
        Random rand = new Random();
        String[] titles = {"Time", "National Geographic", "Scientific American", "The Economist", "Wired"};
        String title = titles[rand.nextInt(titles.length)];
        int year = rand.nextInt(21) + 2000;
        int pageCount = rand.nextInt(100) + 50;
        Periodicity[] periodicities = Periodicity.values();
        Periodicity periodicity = periodicities[rand.nextInt(periodicities.length)];
        return new Magazine(title, year, pageCount, periodicity);
    }
}
