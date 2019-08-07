package com.cuupa.classificator.controller;

import com.cuupa.classificator.gui.GuiProcess;
import com.cuupa.classificator.services.Classificator;
import com.cuupa.classificator.services.kb.KnowledgeManager;
import com.cuupa.classificator.services.kb.SemanticResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class GuiController {

    private final Classificator classificator;
	private KnowledgeManager manager;

    public GuiController(Classificator classificator, KnowledgeManager manager) {
        this.classificator = classificator;
		this.manager = manager;
    }

    @NotNull
    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String index(@NotNull Model model) {
        model.addAttribute("guiProcess", new GuiProcess());
        return "index";
    }

    @NotNull
    @RequestMapping(value = "/guiProcess", method = RequestMethod.POST)
    public String guiProcess(@NotNull @ModelAttribute GuiProcess guiProcess) {
        List<SemanticResult> result = classificator.classify(guiProcess.getInputText());
        guiProcess.setResult(result);
        return "index";
    }

    @NotNull
    @RequestMapping(value ="/reloadKB", method = RequestMethod.POST)
    public String reloadKB() {
        manager.reloadKB();
        return "index";
    }
}
