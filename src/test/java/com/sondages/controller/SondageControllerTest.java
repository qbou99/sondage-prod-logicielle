package com.sondages.controller;

import com.sondages.dto.SondageDto;
import com.sondages.exception.ResourceNotFoundException;
import com.sondages.model.ChoixVote;
import com.sondages.model.Commentaire;
import com.sondages.model.Sondage;
import com.sondages.model.Vote;
import com.sondages.repository.SondageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SondageControllerTest {

    @InjectMocks
    SondageController sondageController;

    @Mock
    SondageRepository sondageRepository;

    @Captor
    ArgumentCaptor<Sondage> sondageArgumentCaptor;

    private static final long UNKNOWN_ID = 1337L;

    /*
    @Test
    void given_when_then() {
        //given

        //when

        //then
    }
     */

    @Test
    void givenSondages_whenGetAll_thenSondages() {
        //given
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        Sondage s2 = new Sondage("Sondage 2", "Un 2e sondage");
        List<Sondage> sondages = Arrays.asList(s1, s2);

        when(sondageRepository.findAll()).thenReturn(sondages);

        //when
        List<Sondage> result = sondageController.getAllSondages();

        //then
        assertThat(result).hasSize(2);

        assertThat(result.get(0).getNom())
                .isEqualTo(s1.getNom());

        assertThat(result.get(1).getNom())
                .isEqualTo(s2.getNom());
    }

    @Test
    void givenSondage_whenAdd_thenSondageAdded() {
        //given
        SondageDto s = new SondageDto("Sondage 1", "Un sondage");

        when(sondageRepository.save(any(Sondage.class))).thenReturn(s.dtoToEntity());

        //when
        Sondage result = sondageController.createSondage(s);

        //then
        assertThat(result.getNom())
                .isEqualTo(s.getNom());
    }

    @Test
    void givenSondage1_whenGet1_thenSondage1() {
        //given
        Sondage s = new Sondage("Sondage 1", "Un sondage");

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s));

        //when
        Sondage result = sondageController.getSondageById(1L);

        //then
        assertThat(result.getNom())
                .isEqualTo(s.getNom());
    }

    @Test
    void givenUnknownSondageId_whenGetById_thenException() {
        //given
        when(sondageRepository.findById(UNKNOWN_ID)).thenReturn(Optional.empty());

        //when
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> sondageController.getSondageById(UNKNOWN_ID));

        //then
        assertThat(e.getMessage()).isEqualTo("Sondage not found with id : '1337'");
    }

    @Test
    void givenSondage_whenUpdate_thenSondageUpdated() {
        //given
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.setDateLimite(new Date(1647098725));

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));
        when(sondageRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        //when
        SondageDto s2 = new SondageDto("Sondage 1 V2", "Un meilleur sondage");
        s2.setDateLimite(new Date(1647098972));

        Sondage result = sondageController.updateSondage(1L, s2);

        //then
        assertThat(result.getNom()).isEqualTo(s2.getNom());
        assertThat(result.getDescription()).isEqualTo(s2.getDescription());
        assertThat(result.getDateLimite().compareTo(s2.getDateLimite())).isEqualByComparingTo(0);
    }

    @Test
    void givenSondage_whenDelete_thenSondageDeleted() {
        //given
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));

        //when
        ResponseEntity<?> result = sondageController.deleteSondage(1L);

        //then
        verify(sondageRepository).delete(sondageArgumentCaptor.capture());

        assertThat(result.getStatusCodeValue()).isEqualTo(ResponseEntity.ok().build().getStatusCodeValue());
        assertThat(sondageArgumentCaptor.getValue().getNom()).isEqualTo(s1.getNom());
    }

    @Test
    void givenSondage_whenClose_thenSondageClosed() {
        //given
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));
        when(sondageRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        //when
        Sondage result = sondageController.closeSondage(1L);

        //then
        assertThat(result.isEstOuvert()).isFalse();
    }

    @Test
    void givenSondage_whenAddDate_thenDateAdded() {
        //given
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));
        when(sondageRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        //when
        Sondage result = sondageController.addDate(1L, "2022-03-12");

        //then
        assertThat(result.getDates()).hasSize(1);
        assertThat(result.getDates()).contains("2022-03-12");
    }

    @Test
    void givenSondageWithDate_whenRemoveDate_thenSondageWithoutDate() {
        //given
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.addDate("2022-03-12");
        s1.addDate("2022-03-13");
        s1.addVote(new Vote("2022-03-12"));
        s1.addVote(new Vote("2022-03-13"));

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));
        when(sondageRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        //when
        Sondage result = sondageController.deleteDate(1L, "2022-03-12");

        //then
        assertThat(result.getDates()).hasSize(1);
        assertThat(result.getDates()).doesNotContain("2022-03-12");
        assertThat(result.getVotes()).hasSize(1);
        assertThat(result.getVotes().stream().filter(v -> v.getChoixDate().equals("2022-03-12")).findAny()).isEmpty();
    }

    @Test
    void givenSondageWithDates_whenGetDates_thenDates() {
        //given
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.addDate("2022-03-12");
        s1.addDate("2022-03-13");

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));

        //when
        List<String> result = sondageController.getDates(1L);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0)).isEqualTo("2022-03-12");
        assertThat(result.get(1)).isEqualTo("2022-03-13");
    }

    @Test
    void givenSondageWithComentaires_whenGetComentaires_thenComentaires() {
        //given
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.addCommentaire(new Commentaire("Commentaire 1"));
        s1.addCommentaire(new Commentaire("Commentaire 2"));

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));

        //when
        List<Commentaire> result = sondageController.getCommentaires(1L);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTexte()).isEqualTo("Commentaire 1");
        assertThat(result.get(1).getTexte()).isEqualTo("Commentaire 2");
    }

    @Test
    void givenSondageWithVotes_whenGetVotes_thenVotes() {
        //given
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.addVote(new Vote("2022-03-12"));
        s1.addVote(new Vote("2022-03-13"));

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));

        //when
        List<Vote> result = sondageController.getVotes(1L);

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getChoixDate()).isEqualTo("2022-03-12");
        assertThat(result.get(1).getChoixDate()).isEqualTo("2022-03-13");
    }

    @Test
    void givenSondageWithVotesAndListTypeChoix_whenMeilleurDate_thenDate() {
        //given
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.addDate("2022-03-12");
        s1.addDate("2022-03-13");
        s1.addVote(new Vote("2022-03-12", ChoixVote.DISPONIBLE));
        s1.addVote(new Vote("2022-03-12", ChoixVote.DISPONIBLE));
        s1.addVote(new Vote("2022-03-13", ChoixVote.DISPONIBLE));

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));

        //when
        String result = sondageController.getMeilleureDate(Arrays.asList(ChoixVote.DISPONIBLE), 1L);

        //then
        assertThat(result).isEqualTo("2022-03-12");
    }

    @Test
    void givenSondageWithVotes_whenMeilleurDateCertain_thenDate() {
        //given
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.addDate("2022-03-12");
        s1.addDate("2022-03-13");
        s1.addVote(new Vote("2022-03-12", ChoixVote.DISPONIBLE));
        s1.addVote(new Vote("2022-03-12", ChoixVote.DISPONIBLE));
        s1.addVote(new Vote("2022-03-13", ChoixVote.PEUT_ETRE));
        s1.addVote(new Vote("2022-03-13", ChoixVote.PEUT_ETRE));
        s1.addVote(new Vote("2022-03-13", ChoixVote.PEUT_ETRE));

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));

        //when
        String result = sondageController.getMeilleureDateCertain(1L);

        //then
        assertThat(result).isEqualTo("2022-03-12");
    }

    @Test
    void givenSondageWithVotes_whenMeilleurDateEventuellement_thenDate() {
        //given
        Sondage s1 = new Sondage("Sondage 1", "Un sondage");
        s1.addDate("2022-03-12");
        s1.addDate("2022-03-13");
        s1.addVote(new Vote("2022-03-12", ChoixVote.DISPONIBLE));
        s1.addVote(new Vote("2022-03-12", ChoixVote.DISPONIBLE));
        s1.addVote(new Vote("2022-03-13", ChoixVote.PEUT_ETRE));
        s1.addVote(new Vote("2022-03-13", ChoixVote.PEUT_ETRE));
        s1.addVote(new Vote("2022-03-13", ChoixVote.PEUT_ETRE));

        when(sondageRepository.findById(1L)).thenReturn(Optional.of(s1));

        //when
        String result = sondageController.getMeilleureDateEventuellement(1L);

        //then
        assertThat(result).isEqualTo("2022-03-13");
    }
}