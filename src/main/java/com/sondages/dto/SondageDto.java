package com.sondages.dto;

import com.sondages.model.Sondage;

import javax.persistence.ElementCollection;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SondageDto {
    @NotBlank
    private String nom;

    @NotBlank
    private String description;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLimite;

    @ElementCollection
    private List<String> dates;

    public Sondage dtoToEntity() {
        Sondage sondage = new Sondage();
        sondage.setNom(this.nom);
        sondage.setDescription(this.description);
        sondage.setDateLimite(this.dateLimite);
        sondage.setDates(this.dates);
        return sondage;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SondageDto that = (SondageDto) o;
        return Objects.equals(nom, that.nom) &&
                Objects.equals(description, that.description) &&
                Objects.equals(dateLimite, that.dateLimite) &&
                Objects.equals(dates, that.dates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, description, dateLimite, dates);
    }

    @Override
    public String toString() {
        return "SondageDto{" +
                "nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", dateLimite=" + dateLimite +
                ", dates=" + dates +
                '}';
    }
}
