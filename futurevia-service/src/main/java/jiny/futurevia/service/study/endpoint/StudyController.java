package jiny.futurevia.service.study.endpoint;

import jakarta.validation.Valid;
import jiny.futurevia.service.account.domain.entity.Account;
import jiny.futurevia.service.account.support.CurrentUser;
import jiny.futurevia.service.study.application.StudyService;
import jiny.futurevia.service.study.domain.entity.Study;
import jiny.futurevia.service.study.form.StudyForm;
import jiny.futurevia.service.study.form.validator.StudyFormValidator;
import jiny.futurevia.service.study.infra.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;
    private final StudyFormValidator studyFormValidator;
    private final StudyRepository studyRepository;


    @InitBinder("studyForm")
    public void studyFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(studyFormValidator);
    }


    @GetMapping("/new-study")
    public String newStudyForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute(new StudyForm());
        return "study/form";
    }


    @PostMapping("/new-study")
    public String newStudySubmit(@CurrentUser Account account, @Valid StudyForm studyForm, Errors errors) {
        if (errors.hasErrors()) {
            return "study/form";
        }
        Study newStudy = studyService.createNewStudy(studyForm, account);
        return "redirect:/study/" + URLEncoder.encode(newStudy.getPath(), StandardCharsets.UTF_8);
    }

    @GetMapping("/study/{path}")
    public String viewStudy(@CurrentUser Account account, @PathVariable String path, Model model) {
        log.info("get study");
        model.addAttribute(account);
        model.addAttribute(studyRepository.findByPath(path));
        log.info("end study");
        return "study/view";
    }
}