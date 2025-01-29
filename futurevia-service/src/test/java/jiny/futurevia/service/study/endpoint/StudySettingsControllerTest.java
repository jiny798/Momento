package jiny.futurevia.service.study.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import jiny.futurevia.service.WithAccount;
import jiny.futurevia.service.account.domain.entity.Account;
import jiny.futurevia.service.account.domain.entity.Zone;
import jiny.futurevia.service.account.endpoint.dto.TagForm;
import jiny.futurevia.service.account.endpoint.dto.ZoneForm;
import jiny.futurevia.service.account.infra.repository.AccountRepository;
import jiny.futurevia.service.study.application.StudyService;
import jiny.futurevia.service.study.domain.entity.Study;
import jiny.futurevia.service.study.form.StudyForm;
import jiny.futurevia.service.study.infra.repository.StudyRepository;
import jiny.futurevia.service.tag.domain.entity.Tag;
import jiny.futurevia.service.tag.infra.repository.TagRepository;
import jiny.futurevia.service.zone.infra.repository.ZoneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class StudySettingsControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired AccountRepository accountRepository;
    @Autowired StudyRepository studyRepository;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ZoneRepository zoneRepository;
    @Autowired StudyService studyService;
    @Autowired
    ObjectMapper objectMapper;
    private final String studyPath = "study-test";

    @BeforeEach
    void beforeEach() {
        Account account = accountRepository.findByNickname("jiny798");
        studyService.createNewStudy(StudyForm.builder()
                .path(studyPath)
                .shortDescription("short-description")
                .fullDescription("full-description")
                .title("title")
                .build(), account);
    }

    @AfterEach
    void afterEach() {
        studyRepository.deleteAll();
    }

    // 스터디 설명
    @Test
    @DisplayName("스터디 세팅 폼 조회: 설명")
    @WithAccount("jiny798")
    void studyForm() throws Exception {
        mockMvc.perform(get("/study/" + studyPath + "/settings/description"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/settings/description"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("studyDescriptionForm"));
    }

    @Test
    @DisplayName("스터디 세팅 수정: 설명")
    @WithAccount("jiny798")
    void createStudy() throws Exception {
        Account account = accountRepository.findByNickname("jiny798");
        String shortDescriptionToBeUpdated = "short-description-test";
        String fullDescriptionToBeUpdated = "full-description-test";
        mockMvc.perform(post("/study/" + studyPath + "/settings/description")
                        .param("shortDescription", shortDescriptionToBeUpdated)
                        .param("fullDescription", fullDescriptionToBeUpdated)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + studyPath + "/settings/description"));
        Study study = studyService.getStudy(studyPath);
        assertEquals(shortDescriptionToBeUpdated, study.getShortDescription());
        assertEquals(fullDescriptionToBeUpdated, study.getFullDescription());
    }

    // 배너
    @Test
    @DisplayName("스터디 세팅 폼 조회: 배너")
    @WithAccount("jiny798")
    void studySettingFormBanner() throws Exception {
        mockMvc.perform(get("/study/" + studyPath + "/settings/banner"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/settings/banner"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("study"));
    }

    @Test
    @DisplayName("스터디 배너 업데이트")
    @WithAccount("jiny798")
    void updateStudyBanner() throws Exception {
        mockMvc.perform(post("/study/" + studyPath + "/settings/banner")
                        .param("image", "image-test")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + studyPath + "/settings/banner"));
    }


    @Test
    @DisplayName("스터디 배너 사용")
    @WithAccount("jiny798")
    void enableStudyBanner() throws Exception {
        mockMvc.perform(post("/study/" + studyPath + "/settings/banner/enable")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + studyPath + "/settings/banner"));
        Study study = studyRepository.findByPath(studyPath);
        assertTrue(study.useBanner());
    }

    @Test
    @DisplayName("스터디 배너 미사용")
    @WithAccount("jiny798")
    void disableStudyBanner() throws Exception {
        mockMvc.perform(post("/study/" + studyPath + "/settings/banner/disable")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + studyPath + "/settings/banner"));
        Study study = studyRepository.findByPath(studyPath);
        assertFalse(study.useBanner());
    }

    @Test
    @DisplayName("스터디 세팅 폼 조회 (태그)")
    @WithAccount("jiny798")
    void studySettingFormTag() throws Exception {
        mockMvc.perform(get("/study/" + studyPath + "/settings/tags"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/settings/tags"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("study"))
                .andExpect(model().attributeExists("tags"))
                .andExpect(model().attributeExists("whitelist"));
    }

    @Test
    @DisplayName("스터디 태그 삭제")
    @WithAccount("jiny798")
    void removeStudyTag() throws Exception {
        Study study = studyRepository.findStudyWithTagsByPath(studyPath);
        String tagTitle = "newTag";
        Tag tag = tagRepository.save(Tag.builder()
                .title(tagTitle)
                .build());
        studyService.addTag(study, tag);

        TagForm tagForm = TagForm.builder()
                .tagTitle(tagTitle)
                .build();

        mockMvc.perform(post("/study/" + studyPath + "/settings/tags/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagForm))
                        .with(csrf()))
                .andExpect(status().isOk());

        assertFalse(study.getTags().contains(tag));
    }

    @Test
    @DisplayName("스터디 태그 추가")
    @WithAccount("jiny798")
    void addStudyTag() throws Exception {
        String tagTitle = "newTag";
        TagForm tagForm = TagForm.builder()
                .tagTitle(tagTitle)
                .build();
        mockMvc.perform(post("/study/" + studyPath + "/settings/tags/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagForm))
                        .with(csrf()))
                .andExpect(status().isOk());
        Study study = studyRepository.findStudyWithTagsByPath(studyPath);
        Tag tag = tagRepository.findByTitle(tagTitle).orElse(null);
        assertNotNull(tag);
        assertTrue(study.getTags().contains(tag));
    }

    @Test
    @DisplayName("스터디 세팅 폼 조회(활동 지역)")
    @WithAccount("jiny798")
    void studySettingFormZone() throws Exception {
        mockMvc.perform(get("/study/" + studyPath + "/settings/zones"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/settings/zones"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("study"))
                .andExpect(model().attributeExists("zones"))
                .andExpect(model().attributeExists("whitelist"));
    }

    @Test
    @DisplayName("스터디 지역 추가")
    @WithAccount("jiny798")
    void addStudyZone() throws Exception {
        Zone testZone = Zone.builder().city("서울").localNameOfCity("강남구").province("켈리포니아").build();
        zoneRepository.save(testZone);
        ZoneForm zoneForm = ZoneForm.builder()
                .zoneName(testZone.toString())
                .build();
        mockMvc.perform(post("/study/" + studyPath + "/settings/zones/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zoneForm))
                        .with(csrf()))
                .andExpect(status().isOk());
        Study study = studyRepository.findStudyWithZonesByPath(studyPath);
        assertTrue(study.getZones().contains(testZone));
    }

    @Test
    @DisplayName("스터디 지역 삭제")
    @WithAccount("jiny798")
    void removeStudyZone() throws Exception {
        Study study = studyRepository.findStudyWithZonesByPath(studyPath);
        Zone testZone = Zone.builder().city("서울").localNameOfCity("강남구").province("켈리포니아").build();
        zoneRepository.save(testZone);
        studyService.addZone(study, testZone);
        ZoneForm zoneForm = ZoneForm.builder()
                .zoneName(testZone.toString())
                .build();
        mockMvc.perform(post("/study/" + studyPath + "/settings/zones/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zoneForm))
                        .with(csrf()))
                .andExpect(status().isOk());
        assertFalse(study.getZones().contains(testZone));
    }

    @Test
    @DisplayName("스터디 세팅 화면")
    @WithAccount("jiny798")
    void studySettingFormStudy() throws Exception {
        mockMvc.perform(get("/study/"+ studyPath + "/settings/study"))
            .andExpect(status().isOk())
            .andExpect(view().name("study/settings/study"))
            .andExpect(model().attributeExists("account"))
            .andExpect(model().attributeExists("study"));
    }

    @Test
    @DisplayName("스터디 공개")
    @WithAccount("jiny798")
    void publishStudy() throws Exception {
        mockMvc.perform(post("/study/" + studyPath + "/settings/study/publish")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/study/" + studyPath + "/settings/study"))
            .andExpect(flash().attributeExists("message"));
        Study study = studyRepository.findByPath(studyPath);
        assertTrue(study.isPublished());
    }

    @Test
    @DisplayName("스터디 종료")
    @WithAccount("jiny798")
    void closeStudy() throws Exception {
        Study study = studyRepository.findByPath(studyPath);
        studyService.publish(study);
        mockMvc.perform(post("/study/" + studyPath + "/settings/study/close")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/study/" + studyPath + "/settings/study"))
            .andExpect(flash().attributeExists("message"));
        assertTrue(study.isClosed());
    }

    @Test
    @DisplayName("스터디 팀원 모집 시작")
    @WithAccount("jiny798")
    void startRecruit() throws Exception {
        Study study = studyRepository.findByPath(studyPath);
        studyService.publish(study);
        mockMvc.perform(post("/study/" + studyPath + "/settings/recruit/start")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/study/" + studyPath + "/settings/study"))
            .andExpect(flash().attributeExists("message"));
        assertTrue(study.isRecruiting());
    }

    @Test
    @DisplayName("스터디 팀원 모집 중지: 1시간 이내 요청 시 실패")
    @WithAccount("jiny798")
    void stopRecruit() throws Exception {
        Study study = studyRepository.findByPath(studyPath);
        studyService.publish(study);
        studyService.startRecruit(study);

        // 모집 후, 한시간 이내 중지 시 실패
        mockMvc.perform(post("/study/" + studyPath + "/settings/recruit/stop")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/study/" + studyPath + "/settings/study"))
            .andExpect(flash().attributeExists("message"));
        assertTrue(study.isRecruiting());
    }

    @Test
    @DisplayName("스터디 경로 변경")
    @WithAccount("jiny798")
    void updateStudyPath() throws Exception {
        String newPath = "new-path";
        mockMvc.perform(post("/study/" + studyPath + "/settings/study/path")
                .param("newPath", newPath)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/study/" + newPath + "/settings/study"))
            .andExpect(flash().attributeExists("message"));
        Study study = studyRepository.findByPath(newPath);
        assertEquals(newPath, study.getPath());
    }

    @Test
    @DisplayName("스터디 이름 변경")
    @WithAccount("jiny798")
    void updateStudyTitle() throws Exception {
        String newTitle = "newTitle";
        mockMvc.perform(post("/study/" + studyPath + "/settings/study/title")
                .param("newTitle", newTitle)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/study/" + studyPath + "/settings/study"))
            .andExpect(flash().attributeExists("message"));
        Study study = studyRepository.findByPath(studyPath);
        assertEquals(newTitle, study.getTitle());
    }

    @Test
    @DisplayName("스터디 삭제")
    @WithAccount("jiny798")
    void removeStudy() throws Exception {
        mockMvc.perform(post("/study/" + studyPath + "/settings/study/remove")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
        assertNull(studyRepository.findByPath(studyPath));
    }
}