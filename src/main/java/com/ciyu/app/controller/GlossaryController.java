package com.ciyu.app.controller;

import com.ciyu.app.exception.CurrentGlossaryNotSetException;
import com.ciyu.app.pojo.Glossary;
import com.ciyu.app.pojo.User;
import com.ciyu.app.security.CurrentUser;
import com.ciyu.app.service.glossary.GlossaryService;
import com.ciyu.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/glossaries")
public class GlossaryController {
    private final GlossaryService glossaryService;
    private final UserService userService;

    @GetMapping
    Iterable<Glossary> getGlossaries() {
        Iterable<Glossary> glossaries = glossaryService.findAllGlossaries();
        glossaries.forEach(glossary -> glossary.setWords(null));
        return glossaries;
    }

    @GetMapping("/current")
    public Glossary getCurrentGlossary(@CurrentUser User user) {
        if (user.getGlossary() == null)
            throw new CurrentGlossaryNotSetException();
        return glossaryService.findGlossaryById(user.getGlossary().getId());
    }

    @GetMapping("/{id}")
    public Glossary getGlossaryById(@PathVariable String id) {
        return glossaryService.findGlossaryById(id);
    }

    @PutMapping("/current")
    public void setCurrentGlossary(@CurrentUser User user, @RequestBody Glossary glossary) {
        glossary = glossaryService.findGlossaryById(glossary.getId());
        user.setGlossary(glossary);
        userService.saveUser(user);
    }
}
