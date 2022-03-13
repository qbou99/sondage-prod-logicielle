package com.sondages.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String presentationProjet() {
        return "Projet sondage réalisé par Quentin Boucksom et Johann Conicella";
    }
}
