package com.voting.votingapp.services;

import com.voting.votingapp.model.OptionVote;
import com.voting.votingapp.model.Poll;
import com.voting.votingapp.repositories.PollRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PollService {
    private PollRepository pollRepository;

    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public Poll createPoll(Poll poll){
        return pollRepository.save(poll);
    }

    public List<Poll> getAllPolls(){
        return pollRepository.findAll();
    }

    public Optional<Poll> getPollById(Long id){
        return pollRepository.findById(id);
    }

    public void vote(Long pollId, int optionIndex) {
        // Get Poll from DB
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found"));

        // Get All Options
        List<OptionVote> options = poll.getOptions();

        // If index for vote is not valid, throw error
        if (optionIndex < 0 || optionIndex >= options.size()) {
            throw new IllegalArgumentException("Invalid option index");
        }

        // Get Selected Option
        OptionVote selectedOption = options.get(optionIndex);

        // Increment vote for selected option
        selectedOption.setVoteCount(selectedOption.getVoteCount() + 1);

        // Save incremented option into the database
        pollRepository.save(poll);
    }
}
