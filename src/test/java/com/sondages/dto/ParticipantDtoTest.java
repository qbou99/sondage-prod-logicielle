package com.sondages.dto;

import com.sondages.model.Participant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ParticipantDtoTest {

    @Test
    void givenParticipantDto_whenDtoToEntity_thenParticipantEntity() {
        //given
        ParticipantDto p1 = new ParticipantDto("Jo");

        //when
        Participant p2 = p1.dtoToEntity();

        //then
        assertThat(p1.getNom()).isEqualTo(p2.getNom());
    }

}