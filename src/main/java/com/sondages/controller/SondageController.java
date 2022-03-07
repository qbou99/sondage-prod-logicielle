package com.sondages.controller;

import com.sondages.exception.ResourceNotFoundException;
import com.sondages.model.Sondage;
import com.sondages.repository.SondageRepository;
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

    @GetMapping("")
    public List<Sondage> getAllSondages() {
        return sondageRepository.findAll();
    }

    
    @PostMapping("")
    public Sondage createSondage(@Valid @RequestBody Sondage sondage) {
        return sondageRepository.save(sondage);
    }

    @GetMapping("/{id}")
    public Sondage getSondageById(@PathVariable(value = "id") Long sondageId) {
        return sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
    }

    @PutMapping("/{id}")
    public Sondage updateSondage(@PathVariable(value = "id") Long sondageId,
                                 @Valid @RequestBody Sondage updatedSondage) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        sondage.setNom(updatedSondage.getNom());
        sondage.setEstOuvert(updatedSondage.isEstOuvert());
        sondage.setCommentaires(updatedSondage.getCommentaires());
        sondage.setDateLimite(updatedSondage.getDateLimite());
        sondage.setDates(updatedSondage.getDates());
        return sondageRepository.save(sondage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSondage(@PathVariable(value = "id") Long sondageId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        sondageRepository.delete(sondage);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}")
    public Sondage closeSondage(
            @PathVariable("id") Long sondageId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        sondage.setEstOuvert(false);
        Sondage updatedSondage = sondageRepository.save(sondage);
        return updatedSondage;
    }

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

    @GetMapping("/{id}/dates")
    public List<String> getDates(
            @PathVariable("id") Long sondageId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        return sondage.getDates();
    }

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



