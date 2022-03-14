package com.sondages.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"id", "creationDate"},
        allowGetters = true)
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Participant participant;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @NotNull
    private ChoixVote choix;

    @NotNull
    private String choixDate;

    public Vote() {
        this.participant = null;
        this.creationDate = new Date();
        this.choixDate = "";
    }

    public Vote(String choixDate) {
        this.participant = null;
        this.creationDate = new Date();
        this.choixDate = choixDate;
    }

    public Vote(String choixDate, ChoixVote choix) {
        this.participant = null;
        this.creationDate = new Date();
        this.choixDate = choixDate;
        this.choix = choix;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public ChoixVote getChoix() {
        return choix;
    }

    public void setChoix(ChoixVote choix) {
        this.choix = choix;
    }

    public String getChoixDate() {
        return choixDate;
    }

    public void setChoixDate(String choixDate) {
        this.choixDate = choixDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return id == vote.id &&
                Objects.equals(participant, vote.participant) &&
                Objects.equals(creationDate, vote.creationDate) &&
                choix == vote.choix &&
                Objects.equals(choixDate, vote.choixDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, participant, creationDate, choix, choixDate);
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", participant=" + participant +
                ", creationDate=" + creationDate +
                ", choix=" + choix +
                ", choixDate=" + choixDate +
                '}';
    }
}
