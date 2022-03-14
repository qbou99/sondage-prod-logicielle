package com.sondages.controller;

import com.sondages.exception.BadRequestException;
import com.sondages.exception.ResourceNotFoundException;
import com.sondages.model.*;
import com.sondages.repository.ParticipantRepository;
import com.sondages.repository.SondageRepository;
import com.sondages.repository.VoteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteControllerTest {

    @InjectMocks
    VoteController voteController;

    @Mock
    VoteRepository voteRepository;

    @Mock
    SondageRepository sondageRepository;

    @Mock
    ParticipantRepository participantRepository;

    private static final long UNKNOWN_ID = 1337L;

    @Test
    void givenVotes_whenGetAll_thenVotes() {
        //given
        Vote v1 = new Vote("2022-03-12");
        Vote v2 = new Vote("2022-03-13");
        List<Vote> votes = Arrays.asList(v1, v2);

        when(voteRepository.findAll()).thenReturn(votes);

        //when
        List<Vote> result = voteController.getAllVotes();

        //then
        assertThat(result).hasSize(2);

        assertThat(result.get(0).getChoixDate())
                .isEqualTo(v1.getChoixDate());

        assertThat(result.get(1).getChoixDate())
                .isEqualTo(v2.getChoixDate());
    }

    @Test
    void givenVote1_whenGet1_thenVote1() {
        //given
        Vote v1 = new Vote("2022-03-12");

        when(voteRepository.findById(1L)).thenReturn(Optional.of(v1));

        //when
        Vote result = voteController.getVoteById(1L);

        //then
        assertThat(result.getChoixDate())
                .isEqualTo(v1.getChoixDate());
    }

    @Test
    void givenUnknownVoteId_whenGetById_thenException() {
        //given
        when(voteRepository.findById(UNKNOWN_ID)).thenReturn(Optional.empty());

        //when
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> voteController.getVoteById(UNKNOWN_ID));

        //then
        assertThat(e.getMessage()).isEqualTo("Vote not found with id : '1337'");
    }

    @Test
    void givenSondageOuvertAndParticipantAndChoix_whenVote_thenVoteAdded() {
        //given
        Participant p1 = new Participant("Jo");
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.setDateLimite(new Date(Long.MAX_VALUE));
        s1.setEstOuvert(true);
        s1.addDate("2022-03-12");

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));
        when(participantRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(voteRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(sondageRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        //when
        Vote result = voteController.vote(1L, 1L, ChoixVote.DISPONIBLE, "2022-03-12");

        //then
        assertThat(result.getParticipant().getNom()).isEqualTo(p1.getNom());
        assertThat(result.getChoixDate()).isEqualTo("2022-03-12");
        assertThat(result.getChoix()).isEqualTo(ChoixVote.DISPONIBLE);
        assertThat(s1.getVotes()).contains(result);
    }

    @Test
    void givenSondageFermeAndParticipantAndChoix_whenVote_thenException() {
        //given
        Participant p1 = new Participant("Jo");
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.setDateLimite(new Date(1L));
        s1.setEstOuvert(true);
        s1.addDate("2022-03-12");

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));
        when(participantRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(sondageRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        //when
        BadRequestException e = assertThrows(BadRequestException.class, () -> voteController.vote(1L, 1L, ChoixVote.DISPONIBLE, "2022-03-12"));

        //then
        assertThat(e.getMessage()).isEqualTo("Bad request : 'Sondage fermé'");
    }

    @Test
    void givenSondageOuvertAndParticipantAndChoix_whenVoteUnknownDate_thenException() {
        //given
        Participant p1 = new Participant("Jo");
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.setDateLimite(new Date(1L));
        s1.setEstOuvert(true);
        s1.addDate("2022-03-12");

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));
        when(participantRepository.findById(1L)).thenReturn(Optional.of(p1));

        //when
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> voteController.vote(1L, 1L, ChoixVote.DISPONIBLE, "2022-03-13"));

        //then
        assertThat(e.getMessage()).isEqualTo("Sondage not found with date : '2022-03-13'");
    }
}