package com.sondages.dto;

import com.sondages.model.Participant;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class ParticipantDto {
    @NotBlank
    private String nom;

    public ParticipantDto() {
        this.nom = "";
    }

    public ParticipantDto(String nom) {
        this.nom = nom;
    }

    public Participant dtoToEntity() {
        Participant participant = new Participant();
        participant.setNom(this.nom);
        return participant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantDto that = (ParticipantDto) o;
        return Objects.equals(nom, that.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }

    @Override
    public String toString() {
        return "ParticipantDto{" +
                "nom='" + nom + '\'' +
                '}';
    }
}
