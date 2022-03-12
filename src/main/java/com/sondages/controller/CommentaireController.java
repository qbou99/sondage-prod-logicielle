package com.sondages.controller;

import com.sondages.exception.BadRequestException;
import com.sondages.exception.ResourceNotFoundException;
import com.sondages.model.Commentaire;
import com.sondages.model.Participant;
import com.sondages.model.Sondage;
import com.sondages.repository.CommentaireRepository;
import com.sondages.repository.ParticipantRepository;
import com.sondages.repository.SondageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/commentaire")
public class CommentaireController {
    private final CommentaireRepository commentaireRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantController participantController;
    private final SondageRepository sondageRepository;
    private final SondageController sondageController;

    private static final String CLASS_NAME = "Commentaire";

    public CommentaireController(CommentaireRepository commentaireRepository, ParticipantRepository participantRepository, SondageRepository sondageRepository) {
        this.commentaireRepository = commentaireRepository;
        this.participantRepository = participantRepository;
        this.participantController = new ParticipantController(participantRepository);
        this.sondageRepository = sondageRepository;
        this.sondageController = new SondageController(sondageRepository);
    }

    @Operation(summary = "Permet à un participant de commenter un sondage (le sondage doit être ouvert)")
    @PostMapping("/{sondageId}/{participantId}/")
    public Commentaire vote(@Parameter(name = "sondageId", description = "L'id du sondage", example = "1")
                            @PathVariable("sondageId") Long sondageId,
                            @Parameter(name = "participantId", description = "L'id du participant", example = "1")
                            @PathVariable("participantId") Long participantId,
                            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Le texte associé au commentaire")
                                    String texte) {

        Sondage sondage = sondageController.getSondageById(sondageId);

        Participant participant = participantController.getParticipantById(participantId);

        Commentaire commentaire = new Commentaire();

        if (!sondage.isEstOuvert() || commentaire.getCreationDate().after(sondage.getDateLimite())) {
            sondage.setEstOuvert(false);
            sondageRepository.save(sondage);
            throw new BadRequestException("Sondage fermé");
        }
        commentaire.setParticipant(participant);
        commentaire.setSondageId(sondageId);
        commentaire.setTexte(texte);
        Commentaire addCommentaire = commentaireRepository.save(commentaire);

        sondage.addCommentaire(addCommentaire);
        sondageRepository.save(sondage);

        return addCommentaire;
    }

    @Operation(summary = "Récupère tout les commentaires", description = "Permet de retrouver les informations concernant tout les commentaires")
    @GetMapping("")
    public List<Commentaire> getAllCommentaires() {
        return commentaireRepository.findAll();
    }

    @Operation(summary = "Récupère un commentaire en fonction de son id", description = "Permet de retrouver les informations concernant un commentaire spécifique")
    @Parameter(name = "id", description = "L'id du commentaire", example = "1")
    @GetMapping("/{id}")
    public Commentaire getCommentaireById(@PathVariable(value = "id") Long commentaireId) {
        return commentaireRepository.findById(commentaireId)
                .orElseThrow(() -> new ResourceNotFoundException(CLASS_NAME, "id", commentaireId));
    }

    @Operation(summary = "Modifie un commentaire en fonction de son id", description = "Permet de modifier les informations concernant un commentaire spécifique")
    @Parameter(name = "id", description = "L'id du commentaire", example = "1")
    @PutMapping("/{id}")
    public Commentaire updateCommentaire(
            @PathVariable(value = "id") Long commentaireId,
            @Valid
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Le body avec les informations que contiendra le commentaire modifié (seul le texte peut changer)")
                    Commentaire updatedCommentaire) {
        Commentaire commmentaire = getCommentaireById(commentaireId);

        commmentaire.setTexte(updatedCommentaire.getTexte());
        return commentaireRepository.save(commmentaire);
    }

    @Operation(summary = "Supprime un commentaire en fonction de son id", description = "Permet de supprimer un commentaire spécifique")
    @Parameter(name = "id", description = "L'id du commentaire", example = "1")
    @DeleteMapping("/{id}")
    public ResponseEntity<Commentaire> deleteCommentaire(@PathVariable(value = "id") Long commentaireId) {
        Commentaire commentaire = getCommentaireById(commentaireId);

        Sondage sondage = sondageController.getSondageById(commentaire.getSondageId());

        sondage.deleteCommentaire(commentaire);
        sondageRepository.save(sondage);
        commentaireRepository.delete(commentaire);
        return ResponseEntity.ok().build();
    }
}
