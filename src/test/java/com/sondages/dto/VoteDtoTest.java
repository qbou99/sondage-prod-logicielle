package com.sondages.dto;

import com.sondages.model.ChoixVote;
import com.sondages.model.Participant;
import com.sondages.model.Vote;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class VoteDtoTest {

    @Test
    void givenParticipantDto_whenDtoToEntity_thenParticipantEntity() {
        //given
        VoteDto v1 = new VoteDto("2022-03-12", new Participant("Participant 1"), ChoixVote.DISPONIBLE);

        //when
        Vote v2 = v1.dtoToEntity();

        //then
        assertThat(v1.getChoixDate()).isEqualTo(v2.getChoixDate());
        assertThat(v1.getParticipant().getNom()).isEqualTo(v2.getParticipant().getNom());
        assertThat(v1.getChoix()).isEqualTo(v2.getChoix());
    }
}