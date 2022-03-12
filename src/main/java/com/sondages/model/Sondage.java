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
@JsonIgnoreProperties(value = {"id", "commentaires", "estOuvert", "votes"},
        allowGetters = true)
public class Sondage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nom;

    @NotBlank
    private String description;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLimite;

    @ElementCollection
    private List<String> dates;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Commentaire> commentaires;

    private boolean estOuvert;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<Vote> votes;

    public Sondage() {
        this.nom = "";
        this.description = "";
        this.dateLimite = new Date();
        this.dates = new ArrayList<>();
        this.commentaires = new ArrayList<>();
        this.estOuvert = true;
        this.votes = new ArrayList<>();
    }
    
    public Sondage(Long id, String nom, String description) {
        this.nom = nom;
        this.description = description;
        this.dateLimite = new Date();
        this.dates = new ArrayList<>();
        this.commentaires = new ArrayList<>();
        this.estOuvert = true;
        this.votes = new ArrayList<>();
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }

    public void addDate(String date) {
        this.dates.add(date);
    }

    public void deleteDate(String date) {
        this.dates.remove(date);
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public void addCommentaire(Commentaire commentaire) {
        this.commentaires.add(commentaire);
    }

    public void deleteCommentaire(Commentaire commentaire) {
        this.commentaires.remove(commentaire);
    }

    public boolean isEstOuvert() {
        return estOuvert;
    }

    public void setEstOuvert(boolean estOuvert) {
        this.estOuvert = estOuvert;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public void addVote(Vote vote) {
        this.votes.add(vote);
    }

    public void deleteVote(Vote vote) {
        this.votes.remove(vote);
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
                Objects.equals(commentaires, sondage.commentaires) &&
                Objects.equals(votes, sondage.votes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, description, dateLimite, dates, commentaires, estOuvert, votes);
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
                ", votes=" + votes +
                '}';
    }
}
