package com.sondages.dto;

import com.sondages.model.ChoixVote;
import com.sondages.model.Participant;
import com.sondages.model.Vote;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class VoteDto {
    @ManyToOne
    private Participant participant;

    @NotNull
    private ChoixVote choix;

    @NotNull
    private String choixDate;

    public Vote dtoToEntity() {
        Vote vote = new Vote();
        vote.setParticipant(this.participant);
        vote.setChoix(this.choix);
        vote.setChoixDate(this.choixDate);
        return vote;
    }

    public VoteDto(String choixDate) {
        this.participant = null;
        this.choixDate = choixDate;
    }

    public VoteDto(String choixDate, Participant participant, ChoixVote choixVote) {
        this.participant = participant;
        this.choixDate = choixDate;
        this.choix = choixVote;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public ChoixVote getChoix() {
        return choix;
    }

    public void setChoix(ChoixVote choix) {
        this.choix = choix;
    }

    public String getChoixDate() {
        return choixDate;
    }

    public void setChoixDate(String choixDate) {
        this.choixDate = choixDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteDto voteDto = (VoteDto) o;
        return Objects.equals(participant, voteDto.participant) &&
                choix == voteDto.choix &&
                Objects.equals(choixDate, voteDto.choixDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participant, choix, choixDate);
    }

    @Override
    public String toString() {
        return "VoteDto{" +
                "participant=" + participant +
                ", choix=" + choix +
                ", choixDate='" + choixDate + '\'' +
                '}';
    }
}
