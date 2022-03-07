package com.sondages.controller;

import com.sondages.exception.ResourceNotFoundException;
import com.sondages.model.Sondage;
import com.sondages.repository.SondageRepository;
import io.swagger.v3.oas.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/sondage")
public class SondageController {
    @Autowired
    SondageRepository sondageRepository;

    @Operation(summary = "Récupère tout les sondages", description = "Permet de retrouver les informations concernant tout les sondages")
    @GetMapping("")
    public List<Sondage> getAllSondages() {
        return sondageRepository.findAll();
    }

    @Operation(summary = "Créé un sondage", description = "Permet de créer un sondage à l'aide d'un document json")
    @PostMapping("")
    public Sondage createSondage(@Valid @RequestBody Sondage sondage) {
        return sondageRepository.save(sondage);
    }

    @Operation(summary = "Récupère un sondage en fonction de son id", description = "Permet de retrouver les informations concernant un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @GetMapping("/{id}")
    public Sondage getSondageById(@PathVariable(value = "id") Long sondageId) {
        return sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
    }

    @Operation(summary = "Modifie un sondage en fonction de son id", description = "Permet de modifier les informations concernant un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @PutMapping("/{id}")
    public Sondage updateSondage(@PathVariable(value = "id") Long sondageId,
                                 @Valid @RequestBody Sondage updatedSondage) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        sondage.setNom(updatedSondage.getNom());
        sondage.setDescription(updatedSondage.getDescription());
        sondage.setDateLimite(updatedSondage.getDateLimite());
        sondage.setDates(updatedSondage.getDates());
        return sondageRepository.save(sondage);
    }

    @Operation(summary = "Supprime un sondage en fonction de son id", description = "Permet de supprimer un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSondage(@PathVariable(value = "id") Long sondageId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        sondageRepository.delete(sondage);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Ferme un sondage en fonction de son id", description = "Permet de fermer un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @PostMapping("/{id}")
    public Sondage closeSondage(
            @PathVariable("id") Long sondageId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        sondage.setEstOuvert(false);
        Sondage updatedSondage = sondageRepository.save(sondage);
        return updatedSondage;
    }

    @Operation(summary = "Ajoute une date à un sondage en fonction de son id", description = "Permet d'ajouter une date en plus de celle qui existe à un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @PostMapping("/ajouterDate/{id}")
    public Sondage addDate(
            @PathVariable("id") Long sondageId,
            @RequestBody String date) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        sondage.addDate(date);
        Sondage updatedSondage = sondageRepository.save(sondage);
        return updatedSondage;
    }

    @Operation(summary = "Supprime une date à un sondage en fonction de son id et de l'id de la date", description = "Permet de supprimer une date qui existe à un sondage spécifique")
    @Parameters({@Parameter(name = "id", description = "L'id du sondage", example = "1"), @Parameter(name = "date_id", description = "L'id de la date", example = "0")})
    @DeleteMapping("/supprimerDate/{id}/{date_id}")
    public Sondage deleteDate(
            @PathVariable("id") Long sondageId,
            @PathVariable("date_id") int dateId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        sondage.deleteDate(dateId);
        Sondage updatedSondage = sondageRepository.save(sondage);
        return updatedSondage;
    }

    @Operation(summary = "Récupère les dates d'un sondage en fonction de son id", description = "Permet de récupérer toutes les dates qui existent pour un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @GetMapping("/{id}/dates")
    public List<String> getDates(
            @PathVariable("id") Long sondageId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        return sondage.getDates();
    }

    @Operation(summary = "Ajoute un commentaire dans un sondage en fonction de son id", description = "Permet d'ajouter un commentaire en plus de ceux qui existent pour un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @PostMapping("/ajouterCommentaire/{id}")
    public Sondage addCommentaire(
            @PathVariable("id") Long sondageId,
            @RequestBody String commentaire) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        sondage.addCommentaire(commentaire);
        Sondage updatedSondage = sondageRepository.save(sondage);
        return updatedSondage;
    }

    @Operation(summary = "Supprime un commentaire dans un sondage en fonction de son id et de l'id du commentaire", description = "Permet de supprimer un commentaire qui existe pour un sondage spécifique")
    @Parameters({@Parameter(name = "id", description = "L'id du sondage", example = "1"), @Parameter(name = "commentaire_id", description = "L'id du commentaire", example = "0")})
    @DeleteMapping("/supprimerCommentaire/{id}/{commentaire_id}")
    public Sondage deleteCommentaire(
            @PathVariable("id") Long sondageId,
            @PathVariable("commentaire_id") int commentaireId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        sondage.deleteCommentaire(commentaireId);
        Sondage updatedSondage = sondageRepository.save(sondage);
        return updatedSondage;
    }

    @Operation(summary = "Modifie un commentaire dans un sondage en fonction de son id et de l'id du commentaire", description = "Permet de modifier un commentaire qui existe pour un sondage spécifique")
    @Parameters({@Parameter(name = "id", description = "L'id du sondage", example = "1"), @Parameter(name = "commentaire_id", description = "L'id du commentaire", example = "0")})
    @PutMapping("/modifierCommentaire/{id}/{commentaire_id}")
    public Sondage modifieCommentaire(
            @RequestBody String commentaire,
            @PathVariable("id") Long sondageId,
            @PathVariable("commentaire_id") int commentaireId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        sondage.deleteCommentaire(commentaireId);
        sondage.addCommentaire(commentaire);
        Sondage updatedSondage = sondageRepository.save(sondage);
        return updatedSondage;
    }
}



