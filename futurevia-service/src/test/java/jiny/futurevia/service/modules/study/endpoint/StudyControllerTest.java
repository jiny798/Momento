package jiny.futurevia.service.modules.study.endpoint;

import jiny.futurevia.service.WithAccount;
import jiny.futurevia.service.infra.IntegrationTest;
import jiny.futurevia.service.modules.account.domain.entity.Account;
import jiny.futurevia.service.modules.account.infra.repository.AccountRepository;
import jiny.futurevia.service.modules.study.application.StudyService;
import jiny.futurevia.service.modules.study.domain.entity.Study;
import jiny.futurevia.service.modules.study.form.StudyForm;
import jiny.futurevia.service.modules.study.infra.repository.StudyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
class StudyControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    StudyRepository studyRepository;
    @Autowired
    StudyService studyService;

    @Test
    @DisplayName("스터디 생성 폼")
    @WithAccount("jiny798")
    void studyForm() throws Exception {
        mockMvc.perform(get("/new-study"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("studyForm"));
    }

    @Test
    @DisplayName("스터디 생성: 정상")
    @WithAccount("jiny798")
    void createStudy() throws Exception {
        String studyPath = "study-test";
        mockMvc.perform(post("/new-study")
                        .param("path", studyPath)
                        .param("title", "study-title")
                        .param("shortDescription", "short-description")
                        .param("fullDescription", "fullDescription")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + studyPath));
        assertTrue(studyRepository.existsByPath(studyPath));
    }

    @Test
    @DisplayName("스터디 생성: 오류")
    @WithAccount("jiny798")
    void createStudyWithError() throws Exception {
        String studyPath = "s";
        mockMvc.perform(post("/new-study")
                        .param("path", studyPath)
                        .param("title", "study-title")
                        .param("shortDescription", "short-description")
                        .param("fullDescription", "fullDescription")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("스터디 뷰")
    @WithAccount("jiny798")
    void studyView() throws Exception {
        Account account = accountRepository.findByNickname("jiny798");
        String studyPath = "study-path";
        studyService.createNewStudy(StudyForm.builder()
                .path(studyPath)
                .title("study-test")
                .shortDescription("description test")
                .fullDescription("description test")
                .build(), account);
        mockMvc.perform(get("/study/" + studyPath))
                .andExpect(status().isOk())
                .andExpect(view().name("study/view"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("study"));
    }

    @Test
    @DisplayName("스터디 멤버 조회")
    @WithAccount("jiny798")
    void studyMemberView() throws Exception {
        Account account = accountRepository.findByNickname("jiny798");
        String studyPath = "study-test";
        studyService.createNewStudy(StudyForm.builder()
                .path(studyPath)
                .title("study-title")
                .shortDescription("short-description")
                .fullDescription("full-description")
                .build(), account);
        mockMvc.perform(get("/study/" + studyPath + "/members"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/members"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("study"));
    }

    @Test
    @DisplayName("스터디 가입")
    @WithAccount(value = {"jiny798", "member798"})
    void joinStudy() throws Exception {
        // 스터디 생성
        Account manager = accountRepository.findByNickname("jiny798");
        String studyPath = "study-path";
        Study study = studyService.createNewStudy(StudyForm.builder()
            .path(studyPath)
            .title("study-title")
            .shortDescription("short-description")
            .fullDescription("full-description")
            .build(), manager);

        mockMvc.perform(get("/study/" + studyPath + "/join"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/study/" + studyPath + "/members"));
        Account member = accountRepository.findByNickname("member798");
        assertTrue(study.getMembers().contains(member));
    }

    @Test
    @DisplayName("스터디 탈퇴")
    @WithAccount(value = {"jiny798", "member798"})
    void leaveStudy() throws Exception {
        // 스터디 생성
        Account manager = accountRepository.findByNickname("jiny798");
        String studyPath = "study-path";
        Study study = studyService.createNewStudy(StudyForm.builder()
            .path(studyPath)
            .title("study-title")
            .shortDescription("short-description")
            .fullDescription("full-description")
            .build(), manager);
        // 스터디 가입
        Account member = accountRepository.findByNickname("member798");
        studyService.addMember(study, member);

        // 스터디 탈퇴
        mockMvc.perform(get("/study/" + studyPath + "/leave"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/study/" + studyPath + "/members"));
        assertFalse(study.getMembers().contains(member));
    }
}