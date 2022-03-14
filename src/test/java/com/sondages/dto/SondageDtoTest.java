package com.sondages.dto;

import com.sondages.model.Sondage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SondageDtoTest {

    @Test
    void givenParticipantDto_whenDtoToEntity_thenParticipantEntity() {
        //given
        SondageDto s1 = new SondageDto("Sondage 1", "Un sondage", new Date(1647098725), Arrays.asList("2022-03-12"));

        //when
        Sondage s2 = s1.dtoToEntity();

        //then
        assertThat(s1.getNom()).isEqualTo(s2.getNom());
        assertThat(s1.getDescription()).isEqualTo(s2.getDescription());
        assertThat(s1.getDateLimite()).isEqualTo(s2.getDateLimite());
        assertThat(s1.getDates()).hasSize(s2.getDates().size());
        assertThat(s1.getDates().get(0)).isEqualTo(s2.getDates().get(0));
    }
}