package com.sondages.controller;

import com.sondages.exception.BadRequestException;
import com.sondages.exception.ResourceNotFoundException;
import com.sondages.model.ChoixVote;
import com.sondages.model.Participant;
import com.sondages.model.Sondage;
import com.sondages.model.Vote;
import com.sondages.repository.ParticipantRepository;
import com.sondages.repository.SondageRepository;
import com.sondages.repository.VoteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vote")
public class VoteController {

    private final VoteRepository voteRepository;
    private final ParticipantController participantController;
    private final SondageRepository sondageRepository;
    private final SondageController sondageController;

    public VoteController(VoteRepository voteRepository, ParticipantRepository participantRepository, SondageRepository sondageRepository) {
        this.voteRepository = voteRepository;
        this.participantController = new ParticipantController(participantRepository);
        this.sondageRepository = sondageRepository;
        this.sondageController = new SondageController(sondageRepository);
    }

    @Operation(summary = "Permet de voter pour une date à un sondage")
    @PostMapping("/{sondageId}/{participantId}/{choixVote}")
    public Vote vote(@Parameter(name = "sondageId", description = "L'id du sondage", example = "1")
                     @PathVariable("sondageId") Long sondageId,
                     @Parameter(name = "participantId", description = "L'id du participant", example = "1")
                     @PathVariable("participantId") Long participantId,
                     @Parameter(name = "choixVote", description = "La disponibilité à la date votée", example = "Disponible")
                     @PathVariable("choixVote") ChoixVote choixVote,
                     @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "La date sondée")
                             String date) {

        Sondage sondage = sondageController.getSondageById(sondageId);
        Participant participant = participantController.getParticipantById(participantId);

        Vote vote = new Vote();

        if (!sondage.getDates().contains(date)) {
            throw new ResourceNotFoundException("Sondage", "date", date);

        } else if (!sondage.isEstOuvert() || vote.getCreationDate().after(sondage.getDateLimite())) {
            sondage.setEstOuvert(false);
            sondageRepository.save(sondage);
            throw new BadRequestException("Sondage fermé");
        }
        vote.setParticipant(participant);
        vote.setChoixDate(date);
        vote.setChoix(choixVote);
        Vote addVote = voteRepository.save(vote);

        sondage.addVote(addVote);
        sondageRepository.save(sondage);

        return addVote;
    }

    @Operation(summary = "Récupère tout les votes", description = "Permet de retrouver les informations concernant tout les votes")
    @GetMapping("")
    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    @Operation(summary = "Récupère un vote en fonction de son id", description = "Permet de retrouver les informations concernant un vote spécifique")
    @Parameter(name = "id", description = "L'id du vote", example = "1")
    @GetMapping("/{id}")
    public Vote getVoteById(@PathVariable(value = "id") Long voteId) {
        return voteRepository.findById(voteId)
                .orElseThrow(() -> new ResourceNotFoundException("Vote", "id", voteId));
    }
}
