package com.sondages.controller;

import com.sondages.exception.ResourceNotFoundException;
import com.sondages.model.Sondage;
import com.sondages.model.SondagePost;
import com.sondages.repository.SondageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sondage")
public class SondageController {
    @Autowired
    SondageRepository sondageRepository;

    private Sondage sondagePostToSondage(SondagePost sondagePost) {
        return new Sondage(sondagePost.getNom(), sondagePost.getDescription(), sondagePost.getDateLimite(), sondagePost.getDates());
    }

    @GetMapping("")
    public List<Sondage> getAllSondages() {
        return sondageRepository.findAll();
    }

    
    @PostMapping("")
    public Sondage createSondage(@Valid @RequestBody SondagePost sondage) {
        return sondageRepository.save(sondagePostToSondage(sondage));
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
            @RequestBody String date) throws ParseException {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        // 2022-03-04T13:55:46.820Z
        sondage.addDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(date));
        Sondage updatedSondage = sondageRepository.save(sondage);
        return updatedSondage;
    }

    @DeleteMapping("/supprimerDate/{id}/{date_id}")
    public Sondage deleteDate(
            @PathVariable("id") Long sondageId,
            @PathVariable("date_id") Long dateId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        sondageRepository.deleteById(sondageId);
        sondage.deleteDate(dateId);
        Sondage updatedSondage = sondageRepository.save(sondage);
        return updatedSondage;
    }

    @GetMapping("/{id}/dates")
    public List<Date> getDates(
            @PathVariable("id") Long sondageId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));

        return sondage.getDates();
    }
}



