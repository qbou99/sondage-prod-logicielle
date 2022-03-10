package com.sondages.controller;

import com.sondages.exception.ResourceNotFoundException;
import com.sondages.model.ChoixVote;
import com.sondages.model.Sondage;
import com.sondages.model.Vote;
import com.sondages.repository.SondageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/sondage")
public class SondageController {
    private final SondageRepository sondageRepository;

    public SondageController(SondageRepository sondageRepository) {
        this.sondageRepository = sondageRepository;
    }


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
        return sondageRepository.save(sondage);
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
        return sondageRepository.save(sondage);
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
        return sondageRepository.save(sondage);
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
        return sondageRepository.save(sondage);
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
        return sondageRepository.save(sondage);
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
        return sondageRepository.save(sondage);
    }

    @Operation(summary = "Récupère les commentaires d'un sondage en fonction de son id", description = "Permet de récupérer tout les commentaires qui existent pour un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @GetMapping("/{id}/commentaires")
    public List<String> getCommentaires(
            @PathVariable("id") Long sondageId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        return sondage.getCommentaires();
    }

    @Operation(summary = "Récupère les votes d'un sondage en fonction de son id", description = "Permet de récupérer tout les votes qui existent pour un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @GetMapping("/{id}/votes")
    public List<Vote> getVotes(
            @PathVariable("id") Long sondageId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        return sondage.getVotes();
    }

    @Operation(summary = "Récupère la date qui a le plus de vote Disponible pour un sondage", description = "Permet de récupérer la date à laquelle le plus de monde sera présent de manière certaine")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @GetMapping("/{id}/meilleureDateDispo")
    public String getMeilleureDateCertain(
            @PathVariable("id") Long sondageId) {
        List<ChoixVote> choixVotes = new ArrayList<>();
        choixVotes.add(ChoixVote.Disponible);
        return getMeilleureDate(choixVotes, sondageId);
    }

    @Operation(summary = "Récupère la date qui a le plus de vote Disponible et PeutEtre pour un sondage", description = "Permet de récupérer la date à laquelle le plus de monde sera présent de manière eventuelle")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @GetMapping("/{id}/meilleureDateDispoEtPeutetre")
    public String getMeilleureDateEventuellement(
            @PathVariable("id") Long sondageId) {
        List<ChoixVote> choixVotes = new ArrayList<>();
        choixVotes.add(ChoixVote.Disponible);
        choixVotes.add(ChoixVote.PeutEtre);
        return getMeilleureDate(choixVotes, sondageId);
    }

    /**
     * Méthode permettant de retrouver la date à laquelle le plus de participant ont voté suivant une/des disponibilité(s)
     *
     * @param choixVotes La liste des disponibilités prises en compte
     * @param sondageId  L'id du sondage sur lequel les votes sont analysés
     * @return La meilleure date d'un sondage par rapport aux disponibilités demandées
     */
    String getMeilleureDate(List<ChoixVote> choixVotes, Long sondageId) {
        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));

        Map<String, Integer> countDispo = new HashMap<>();
        List<String> dates = sondage.getDates();

        for (String date : dates) {
            countDispo.put(date, 0);
        }
        for (Vote vote : sondage.getVotes()) {
            for (ChoixVote choixVote : choixVotes) {
                if (vote.getChoix() == choixVote)
                    countDispo.put(vote.getChoixDate(), countDispo.get(vote.getChoixDate()) + 1);
            }
        }
        Map.Entry<String, Integer> entry = countDispo.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue)).get();
        return entry.getKey();
    }
}



