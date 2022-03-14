package com.sondages.dto;

import com.sondages.model.Commentaire;
import com.sondages.model.Participant;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CommentaireDto {
    @ManyToOne
    private Participant participant;

    @NotNull
    private Long sondageId;

    @NotBlank
    private String texte;

    public CommentaireDto() {
        this.participant = null;
        this.texte = "";
    }

    public CommentaireDto(String texte) {
        this.participant = null;
        this.texte = texte;
    }

    public CommentaireDto(String texte, Participant participant, Long sondageId) {
        this.participant = participant;
        this.texte = texte;
        this.sondageId = sondageId;
    }

    public Commentaire dtoToEntity() {
        Commentaire commentaire = new Commentaire();
        commentaire.setParticipant(this.participant);
        commentaire.setSondageId(this.sondageId);
        commentaire.setTexte(this.texte);
        return commentaire;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentaireDto that = (CommentaireDto) o;
        return Objects.equals(participant, that.participant) &&
                Objects.equals(sondageId, that.sondageId) &&
                Objects.equals(texte, that.texte);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participant, sondageId, texte);
    }

    @Override
    public String toString() {
        return "CommentaireDto{" +
                "participant=" + participant +
                ", sondageId=" + sondageId +
                ", texte='" + texte + '\'' +
                '}';
    }
}
