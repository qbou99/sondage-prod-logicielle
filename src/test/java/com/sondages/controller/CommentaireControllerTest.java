package com.sondages.controller;

import com.sondages.dto.CommentaireDto;
import com.sondages.exception.BadRequestException;
import com.sondages.exception.ResourceNotFoundException;
import com.sondages.model.Commentaire;
import com.sondages.model.Participant;
import com.sondages.model.Sondage;
import com.sondages.repository.CommentaireRepository;
import com.sondages.repository.ParticipantRepository;
import com.sondages.repository.SondageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentaireControllerTest {

    @InjectMocks
    CommentaireController commentaireController;

    @Mock
    CommentaireRepository commentaireRepository;

    @Mock
    SondageRepository sondageRepository;

    @Mock
    ParticipantRepository participantRepository;

    private static final long UNKNOWN_ID = 1337L;

    @Test
    void givenCommentaires_whenGetAll_thenCommentaires() {
        //given
        Commentaire c1 = new Commentaire("Commentaire 1");
        Commentaire c2 = new Commentaire("Commentaire 2");
        List<Commentaire> commentaires = Arrays.asList(c1, c2);

        when(commentaireRepository.findAll()).thenReturn(commentaires);

        //when
        List<Commentaire> result = commentaireController.getAllCommentaires();

        //then
        assertThat(result).hasSize(2);

        assertThat(result.get(0).getTexte())
                .isEqualTo(c1.getTexte());

        assertThat(result.get(1).getTexte())
                .isEqualTo(c2.getTexte());
    }

    @Test
    void givenCommentaire1_whenGet1_thenCommentaire1() {
        //given
        Commentaire c1 = new Commentaire("Commentaire 1");

        when(commentaireRepository.findById(1L)).thenReturn(Optional.of(c1));

        //when
        Commentaire result = commentaireController.getCommentaireById(1L);

        //then
        assertThat(result.getTexte())
                .isEqualTo(c1.getTexte());
    }

    @Test
    void givenUnknownCommentaireId_whenGetById_thenException() {
        //given
        when(commentaireRepository.findById(UNKNOWN_ID)).thenReturn(Optional.empty());

        //when
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> commentaireController.getCommentaireById(UNKNOWN_ID));

        //then
        assertThat(e.getMessage()).isEqualTo("Commentaire not found with id : '1337'");
    }

    @Test
    void givenCommentaire_whenUpdate_thenCommentaireUpdated() {
        //given
        Commentaire c1 = new Commentaire("Commentaire 1");

        when(commentaireRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(commentaireRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        //when
        CommentaireDto c2 = new CommentaireDto("Commentaire 2");

        Commentaire result = commentaireController.updateCommentaire(1L, c2);

        //then
        assertThat(result.getTexte()).isEqualTo(c2.getTexte());
    }

    @Test
    void givenCommentaire_whenDelete_thenCommentaireDeleted() {
        //given
        Commentaire c1 = new Commentaire("Commentaire 1");
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        c1.setSondageId(1L);
        s1.addCommentaire(c1);

        when(commentaireRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));

        //when
        ResponseEntity<?> result = commentaireController.deleteCommentaire(1L);

        //then
        assertThat(result.getStatusCodeValue()).isEqualTo(ResponseEntity.ok().build().getStatusCodeValue());
        assertThat(s1.getCommentaires()).isEmpty();
    }

    @Test
    void givenSondageOuvertAndParticipant_whenAddCommentaire_thenCommentaireAddedToSondage() {
        //given
        Participant p1 = new Participant("Jo");
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.setDateLimite(new Date(Long.MAX_VALUE));
        s1.setEstOuvert(true);

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));
        when(participantRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(commentaireRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(sondageRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        //when
        Commentaire result = commentaireController.vote(1L, 1L, "Un commentaire");

        //then
        assertThat(result.getTexte()).isEqualTo("Un commentaire");
        assertThat(result.getSondageId()).isEqualTo(1L);
        assertThat(result.getParticipant().getNom()).isEqualTo("Jo");
        assertThat(s1.getCommentaires()).contains(result);
    }

    @Test
    void givenSondageFermeAndParticipant_whenAddCommentaire_thenException() {
        //given
        Participant p1 = new Participant("Jo");
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.setDateLimite(new Date(1L));
        s1.setEstOuvert(true);

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));
        when(participantRepository.findById(1L)).thenReturn(Optional.of(p1));
        when(sondageRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        //when
        BadRequestException e = assertThrows(BadRequestException.class, () -> commentaireController.vote(1L, 1L, "Un commentaire"));

        //then
        assertThat(e.getMessage()).isEqualTo("Bad request : 'Sondage fermé'");
    }
}