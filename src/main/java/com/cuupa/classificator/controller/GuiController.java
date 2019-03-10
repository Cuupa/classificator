package com.cuupa.classificator.controller;

import com.cuupa.classificator.gui.GuiProcess;
import com.cuupa.classificator.services.Classificator;
import com.cuupa.classificator.services.kb.semantic.SemanticResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class GuiController {

    private final Classificator classificator;

    public GuiController(Classificator classificator) {
        this.classificator = classificator;
    }

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("guiProcess", new GuiProcess());
        return "index";
    }

    @RequestMapping(value = "/guiProcess", method = RequestMethod.POST)
    public String guiProcess(@ModelAttribute GuiProcess guiProcess) {
        List<SemanticResult> result = classificator.classify(guiProcess.getInputText());
        guiProcess.setResult(result);
        return "index";
    }
}
