package com.ciyu.app.service.meaning;

import com.ciyu.app.pojo.Meaning;
import com.ciyu.app.repository.MeaningRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class MeaningServiceImpl implements MeaningService{
    private final MeaningRepo meaningRepo;

    @Override
    public Meaning findMeaningById(String id) {
        return meaningRepo.findById(id).orElseThrow(() -> new RuntimeException("Meaning Not Found"));
    }
}
