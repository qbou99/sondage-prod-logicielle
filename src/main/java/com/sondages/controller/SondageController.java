package com.sondages.controller;

import com.sondages.dto.SondageDto;
import com.sondages.exception.ResourceNotFoundException;
import com.sondages.model.ChoixVote;
import com.sondages.model.Commentaire;
import com.sondages.model.Sondage;
import com.sondages.model.Vote;
import com.sondages.repository.SondageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/sondage")
public class SondageController {
    private final SondageRepository sondageRepository;

    private static final String CLASS_NAME = "Sondage";

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
    public Sondage createSondage(
            @Valid
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Le body avec les informations que contiendra le sondage créé")
                    SondageDto sondageDto) {
        return sondageRepository.save(sondageDto.dtoToEntity());
    }

    @Operation(summary = "Récupère un sondage en fonction de son id", description = "Permet de retrouver les informations concernant un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @GetMapping("/{id}")
    public Sondage getSondageById(@PathVariable(value = "id") Long sondageId) {
        return sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException(CLASS_NAME, "id", sondageId));
    }

    @Operation(summary = "Modifie un sondage en fonction de son id", description = "Permet de modifier les informations concernant un sondage spécifique (ne permet pas la modification des dates par sécurité)")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @PutMapping("/{id}")
    public Sondage updateSondage(
            @PathVariable(value = "id") Long sondageId,
            @Valid
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Le body avec les informations que contiendra le sondage modifié (les dates d'un sondage ne peuvent pas être modifiées)")
                    SondageDto updatedSondageDto) {
        Sondage sondage = getSondageById(sondageId);

        sondage.setNom(updatedSondageDto.getNom());
        sondage.setDescription(updatedSondageDto.getDescription());
        sondage.setDateLimite(updatedSondageDto.getDateLimite());
        return sondageRepository.save(sondage);
    }

    @Operation(summary = "Supprime un sondage en fonction de son id", description = "Permet de supprimer un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @DeleteMapping("/{id}")
    public ResponseEntity<Sondage> deleteSondage(@PathVariable(value = "id") Long sondageId) {
        Sondage sondage = getSondageById(sondageId);

        sondageRepository.delete(sondage);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Ferme un sondage en fonction de son id", description = "Permet de fermer un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @PostMapping("/{id}")
    public Sondage closeSondage(
            @PathVariable("id") Long sondageId) {
        Sondage sondage = getSondageById(sondageId);

        sondage.setEstOuvert(false);
        return sondageRepository.save(sondage);
    }

    @Operation(summary = "Ajoute une date à un sondage en fonction de son id", description = "Permet d'ajouter une date en plus de celle qui existe à un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @PostMapping("/ajouterDate/{id}")
    public Sondage addDate(
            @PathVariable("id") Long sondageId,
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "La date en plus avec laquelle le sondage sera mis à jour")
                    String date) {
        Sondage sondage = getSondageById(sondageId);

        sondage.addDate(date);
        return sondageRepository.save(sondage);
    }

    @Operation(summary = "Supprime une date à un sondage en fonction de son id et de la date choisie",
            description = "Permet de supprimer une date qui existe à un sondage spécifique (supprime également les votes contenant cette date)")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @DeleteMapping("/supprimerDate/{id}")
    public Sondage deleteDate(
            @PathVariable("id") Long sondageId,
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "La date que l'on souhaite supprimée du sondage")
                    String date) {
        Sondage sondage = getSondageById(sondageId);

        sondage.deleteDate(date);

        ArrayList<Vote> voteRemove = new ArrayList<>();
        for (Vote vote : sondage.getVotes()) {
            if (vote.getChoixDate().equals(date)) {
                voteRemove.add(vote);
            }
        }
        sondage.getVotes().removeAll(voteRemove);

        return sondageRepository.save(sondage);
    }

    @Operation(summary = "Récupère les dates d'un sondage en fonction de son id", description = "Permet de récupérer toutes les dates qui existent pour un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @GetMapping("/{id}/dates")
    public List<String> getDates(
            @PathVariable("id") Long sondageId) {
        Sondage sondage = getSondageById(sondageId);

        return sondage.getDates();
    }

    @Operation(summary = "Récupère les commentaires d'un sondage en fonction de son id", description = "Permet de récupérer tout les commentaires qui existent pour un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @GetMapping("/{id}/commentaires")
    public List<Commentaire> getCommentaires(
            @PathVariable("id") Long sondageId) {
        Sondage sondage = getSondageById(sondageId);

        return sondage.getCommentaires();
    }

    @Operation(summary = "Récupère les votes d'un sondage en fonction de son id", description = "Permet de récupérer tout les votes qui existent pour un sondage spécifique")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @GetMapping("/{id}/votes")
    public List<Vote> getVotes(
            @PathVariable("id") Long sondageId) {
        Sondage sondage = getSondageById(sondageId);

        return sondage.getVotes();
    }

    @Operation(summary = "Récupère la date qui a le plus de vote Disponible pour un sondage", description = "Permet de récupérer la date à laquelle le plus de monde sera présent de manière certaine")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @GetMapping("/{id}/meilleureDateDispo")
    public String getMeilleureDateCertain(
            @PathVariable("id") Long sondageId) {
        List<ChoixVote> choixVotes = new ArrayList<>();
        choixVotes.add(ChoixVote.DISPONIBLE);
        return getMeilleureDate(choixVotes, sondageId);
    }

    @Operation(summary = "Récupère la date qui a le plus de vote Disponible et PeutEtre pour un sondage", description = "Permet de récupérer la date à laquelle le plus de monde sera présent de manière eventuelle")
    @Parameter(name = "id", description = "L'id du sondage", example = "1")
    @GetMapping("/{id}/meilleureDateDispoEtPeutetre")
    public String getMeilleureDateEventuellement(
            @PathVariable("id") Long sondageId) {
        List<ChoixVote> choixVotes = new ArrayList<>();
        choixVotes.add(ChoixVote.DISPONIBLE);
        choixVotes.add(ChoixVote.PEUT_ETRE);
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
        Sondage sondage = getSondageById(sondageId);

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
        Optional<Map.Entry<String, Integer>> value = countDispo.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue));
        if (value.isEmpty())
            throw new ResourceNotFoundException("Map", "value", value);
        Map.Entry<String, Integer> entry = value.get();
        return entry.getKey();
    }
}



