package com.sondages.dto;

import com.sondages.model.Commentaire;
import com.sondages.model.Participant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CommentaireDtoTest {

    @Test
    void givenCommentaireDto_whenDtoToEntity_thenCommentaireEntity() {
        //given
        CommentaireDto c1 = new CommentaireDto("Un commentaire", new Participant("Partipant 1"), 1L);

        //when
        Commentaire c2 = c1.dtoToEntity();

        //then
        assertThat(c1.getTexte()).isEqualTo(c2.getTexte());
        assertThat(c1.getParticipant().getNom()).isEqualTo(c2.getParticipant().getNom());
        assertThat(c1.getSondageId()).isEqualTo(c2.getSondageId());
    }
}