package com.sondages.controller;

import com.sondages.model.Participant;
import com.sondages.repository.ParticipantRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
    private final ParticipantRepository participantRepository;

    public ParticipantController(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Operation(summary = "Récupère tout les participants", description = "Permet de retrouver les informations concernant tout les participants")
    @GetMapping("")
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @Operation(summary = "Créé un participant", description = "Permet de créer un participant à l'aide d'un document json")
    @PostMapping("")
    public Participant createParticipant(@Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Le body avec les informations que contiendra le participant créé") Participant participant) {
        return participantRepository.save(participant);
    }
}
