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
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
public class VoteController {

    private final VoteRepository voteRepository;
    private final ParticipantRepository participantRepository;
    private final SondageRepository sondageRepository;

    public VoteController(VoteRepository voteRepository, ParticipantRepository participantRepository, SondageRepository sondageRepository) {
        this.voteRepository = voteRepository;
        this.participantRepository = participantRepository;
        this.sondageRepository = sondageRepository;
    }

    @Operation(summary = "Permet de voter pour une date à un sondage")
    @Parameters({
            @Parameter(name = "sondageId", description = "L'id du sondage", example = "1"),
            @Parameter(name = "participantId", description = "L'id du participant", example = "1"),
            @Parameter(name = "choixVote", description = "La disponibilité à la date votée", example = "Disponible")
    })
    @PostMapping("/{sondageId}/{participantId}/{choixVote}")
    public Vote vote(@PathVariable("sondageId") Long sondageId,
                     @PathVariable("participantId") Long participantId,
                     @PathVariable("choixVote") ChoixVote choixVote,
                     @Parameter(name = "date", description = "La date sondée", example = "jeudi")
                     @RequestBody String date) {

        Sondage sondage = sondageRepository.findById(sondageId)
                .orElseThrow(() -> new ResourceNotFoundException("Sondage", "id", sondageId));
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResourceNotFoundException("Participant", "id", participantId));

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
}
