package com.sondages.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SondagePost {
    private String nom;
    private String description;
    private Date dateLimite;
    private List<Date> dates;

    public SondagePost(String nom, String description, Date dateLimite, List<Date> dates) {
        this.nom = nom;
        this.description = description;
        this.dateLimite = dateLimite;
        this.dates = dates;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SondagePost that = (SondagePost) o;
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
        return "SondagePost{" +
                "nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", dateLimite=" + dateLimite +
                ", dates=" + dates +
                '}';
    }
}
