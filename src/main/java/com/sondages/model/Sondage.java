package com.sondages.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)
public class Sondage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom, description;

    @NotNull
    @Temporal(TemporalType.TIME)
    private Date dateLimite;

    @ElementCollection
    private List<Date> dates;

    @ElementCollection
    private List<String> commentaires;

    private boolean estOuvert;

    public Sondage(String nom, String description, Date dateLimite, List<Date> dates) {
        this.nom = nom;
        this.description = description;
        this.dateLimite = dateLimite;
        this.dates = dates;
        this.commentaires = new ArrayList<>();
        this.estOuvert = true;
    }

    public Sondage() {
        this.nom = "";
        this.description = "";
        this.dateLimite = new Date();
        this.dates = new ArrayList<>();
        this.commentaires = new ArrayList<>();
        this.estOuvert = true;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(Date dateLimite) {
        this.dateLimite = dateLimite;
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public void addDate(Date date) {
        this.dates.add(date);
    }

    public void deleteDate(Long dateId) {
        this.dates.remove(dateId);
    }

    public List<String> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<String> commentaires) {
        this.commentaires = commentaires;
    }

    public boolean isEstOuvert() {
        return estOuvert;
    }

    public void setEstOuvert(boolean estOuvert) {
        this.estOuvert = estOuvert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sondage sondage = (Sondage) o;
        return estOuvert == sondage.estOuvert &&
                Objects.equals(id, sondage.id) &&
                Objects.equals(nom, sondage.nom) &&
                Objects.equals(description, sondage.description) &&
                Objects.equals(dateLimite, sondage.dateLimite) &&
                Objects.equals(dates, sondage.dates) &&
                Objects.equals(commentaires, sondage.commentaires);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, description, dateLimite, dates, commentaires, estOuvert);
    }

    @Override
    public String toString() {
        return "Sondage{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", dateLimite=" + dateLimite +
                ", dates=" + dates +
                ", commentaires=" + commentaires +
                ", estOuvert=" + estOuvert +
                '}';
    }
}
