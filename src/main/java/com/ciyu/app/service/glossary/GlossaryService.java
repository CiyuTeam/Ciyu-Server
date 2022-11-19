package com.ciyu.app.service.glossary;

import com.ciyu.app.pojo.Glossary;

public interface GlossaryService {
    Iterable<Glossary> findAllGlossaries();

    Glossary findGlossaryById(String id);
}
