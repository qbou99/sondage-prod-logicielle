package com.sondages.controller;

import com.sondages.dto.ParticipantDto;
import com.sondages.exception.ResourceNotFoundException;
import com.sondages.model.Participant;
import com.sondages.repository.ParticipantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParticipantControllerTest {

    @InjectMocks
    ParticipantController participantController;

    @Mock
    ParticipantRepository participantRepository;

    private static final long UNKNOWN_ID = 1337L;

    @Test
    void givenParticipants_whenGetAll_thenParticipants() {
        //given
        Participant p1 = new Participant("Jo");
        Participant p2 = new Participant("Anne");
        List<Participant> participants = Arrays.asList(p1, p2);

        when(participantRepository.findAll()).thenReturn(participants);

        //when
        List<Participant> result = participantController.getAllParticipants();

        //then
        assertThat(result).hasSize(2);

        assertThat(result.get(0).getNom())
                .isEqualTo(p1.getNom());

        assertThat(result.get(1).getNom())
                .isEqualTo(p2.getNom());
    }

    @Test
    void givenParticipant1_whenGet1_thenParticipant1() {
        //given
        Participant p1 = new Participant("Jo");

        when(participantRepository.findById(1L)).thenReturn(Optional.of(p1));

        //when
        Participant result = participantController.getParticipantById(1L);

        //then
        assertThat(result.getNom())
                .isEqualTo(p1.getNom());
    }

    @Test
    void givenUnknownParticipantId_whenGetById_thenException() {
        //given
        when(participantRepository.findById(UNKNOWN_ID)).thenReturn(Optional.empty());

        //when
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> participantController.getParticipantById(UNKNOWN_ID));

        //then
        assertThat(e.getMessage()).isEqualTo("Participant not found with id : '1337'");
    }

    @Test
    void givenSondage_whenAdd_thenSondageAdded() {
        //given
        ParticipantDto p1 = new ParticipantDto("Jo");

        when(participantRepository.save(any(Participant.class))).thenReturn(p1.dtoToEntity());

        //when
        Participant result = participantController.createParticipant(p1);

        //then
        assertThat(result.getNom())
                .isEqualTo(p1.getNom());
    }

}