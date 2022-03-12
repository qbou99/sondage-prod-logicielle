package com.sondages.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@JsonIgnoreProperties(value = {"id", "creationDate"},
        allowGetters = true)
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Participant participant;

    @NotNull
    private Long sondageId;

    @NotBlank
    private String texte;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public Commentaire() {
        this.participant = null;
        this.creationDate = new Date();
        this.texte = "";
    }

    public Commentaire(String texte) {
        this.participant = null;
        this.creationDate = new Date();
        this.texte = texte;
    }

    public long getId() {
        return id;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Long getSondageId() {
        return sondageId;
    }

    public void setSondageId(Long sondageId) {
        this.sondageId = sondageId;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commentaire that = (Commentaire) o;
        return id == that.id &&
                Objects.equals(participant, that.participant) &&
                Objects.equals(sondageId, that.sondageId) &&
                Objects.equals(texte, that.texte) &&
                Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, participant, sondageId, texte, creationDate);
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", participant=" + participant +
                ", sondageId=" + sondageId +
                ", texte='" + texte + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
