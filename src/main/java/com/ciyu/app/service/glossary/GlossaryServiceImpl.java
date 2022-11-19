package com.ciyu.app.service.glossary;

import com.ciyu.app.pojo.Glossary;
import com.ciyu.app.repository.GlossaryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class GlossaryServiceImpl implements GlossaryService {
    private final GlossaryRepo glossaryRepo;

    @Override
    public Iterable<Glossary> findAllGlossaries() {
        return glossaryRepo.findAll();
    }

    @Override
    public Glossary findGlossaryById(String id) {
        return glossaryRepo.findById(id).orElseThrow(() -> new RuntimeException("Glossary Not Found."));
    }
}
