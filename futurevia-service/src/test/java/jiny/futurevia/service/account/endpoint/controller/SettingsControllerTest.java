package jiny.futurevia.service.account.endpoint.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jiny.futurevia.service.WithAccount;
import jiny.futurevia.service.account.application.AccountService;
import jiny.futurevia.service.account.domain.entity.Account;
import jiny.futurevia.service.account.domain.entity.Zone;
import jiny.futurevia.service.account.endpoint.controller.dto.TagForm;
import jiny.futurevia.service.account.endpoint.controller.dto.ZoneForm;
import jiny.futurevia.service.account.infra.repository.AccountRepository;
import jiny.futurevia.service.tag.domain.entity.Tag;
import jiny.futurevia.service.tag.infra.repository.TagRepository;
import jiny.futurevia.service.zone.infra.repository.ZoneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ZoneRepository zoneRepository;
    @Autowired
    ObjectMapper objectMapper;

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
        zoneRepository.deleteAll();
    }

    @Test
    @DisplayName("프로필 수정: 정상")
    @WithAccount("jiny798")
    void updateProfile() throws Exception {
        String bio = "소개 내용";
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                        .param("bio", bio)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(flash().attributeExists("message"));
        Account jaime = accountRepository.findByNickname("jiny798");
        assertEquals(bio, jaime.getProfile().getBio());
    }

    @Test
    @DisplayName("프로필 수정: 입력값 초과")
    @WithAccount("jiny798")
    void updateProfileWithError() throws Exception {
        String bio = "가나다라마바사아자차카가나다라마바사아자차카가나다라마바사아자차카안녕하세요.";
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                        .param("bio", bio)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));

        Account account = accountRepository.findByNickname("jiny798");
        assertNull(account.getProfile().getBio());
    }

    @Test
    @DisplayName("프로필 조회")
    @WithAccount("jiny798")
    void updateProfileForm() throws Exception {
        String bio = "안녕하세요";
        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @Test
    @DisplayName("알림 설정 수정 폼")
    @WithAccount("jiny798")
    void updateNotificationForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_NOTIFICATION_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_NOTIFICATION_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("notificationForm"));

    }

    @Test
    @DisplayName("알림 설정 수정: 입력값 정상")
    @WithAccount("jiny798")
    void updateNotification() throws Exception {
        mockMvc.perform(post(SettingsController.SETTINGS_NOTIFICATION_URL)
                        .param("studyCreatedByEmail", "true")
                        .param("studyCreatedByWeb", "true")
                        .param("studyRegistrationResultByEmail", "true")
                        .param("studyRegistrationResultByWeb", "true")
                        .param("studyUpdatedByEmail", "true")
                        .param("studyUpdatedByWeb", "true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_NOTIFICATION_URL))
                .andExpect(flash().attributeExists("message"));
        Account account = accountRepository.findByNickname("jiny798");
        assertTrue(account.getNotificationSetting().isStudyCreatedByEmail());
        assertTrue(account.getNotificationSetting().isStudyCreatedByWeb());
        assertTrue(account.getNotificationSetting().isStudyRegistrationResultByEmail());
        assertTrue(account.getNotificationSetting().isStudyRegistrationResultByWeb());
        assertTrue(account.getNotificationSetting().isStudyUpdatedByEmail());
        assertTrue(account.getNotificationSetting().isStudyUpdatedByWeb());
    }

    @Test
    @DisplayName("닉네임 수정 폼")
    @WithAccount("jiny798")
    void updateNicknameForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_ACCOUNT_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_ACCOUNT_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("nicknameForm"));
    }

    @Test
    @DisplayName("닉네임 수정: 입력값 정상")
    @WithAccount("jiny798")
    void updateNickname() throws Exception {
        String newNickname = "jiny1234";
        mockMvc.perform(post(SettingsController.SETTINGS_ACCOUNT_URL)
                        .param("nickname", newNickname)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_ACCOUNT_URL))
                .andExpect(flash().attributeExists("message"));
        Account account = accountRepository.findByNickname(newNickname);
        assertEquals(newNickname, account.getNickname());
    }

    @Test
    @DisplayName("닉네임 수정: 에러(길이)")
    @WithAccount("jiny798")
    void updateNicknameWithShortNickname() throws Exception {
        String newNickname = "ab";
        mockMvc.perform(post(SettingsController.SETTINGS_ACCOUNT_URL)
                        .param("nickname", newNickname)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_ACCOUNT_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("nicknameForm"))
                .andExpect(model().attributeExists("account"));
    }

    @Test
    @DisplayName("닉네임 수정: 에러(중복)")
    @WithAccount("jiny798")
    void updateNicknameWithDuplicatedNickname() throws Exception {
        String newNickname = "jiny798";
        mockMvc.perform(post(SettingsController.SETTINGS_ACCOUNT_URL)
                        .param("nickname", newNickname)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_ACCOUNT_VIEW_NAME))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeExists("nicknameForm"))
                .andExpect(model().attributeExists("account"));
    }

    @Test
    @DisplayName("프로필: 태그 수정 폼")
    @WithAccount("jiny798")
    void updateTagForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_TAGS_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_TAGS_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("tags"));
    }

    @Test
    @DisplayName("프로필: 태그 추가")
    @WithAccount("jiny798")
    void addTag() throws Exception {
        TagForm tagForm = new TagForm();
        String strTag = "TagTest";
        tagForm.setTagTitle(strTag);

        mockMvc.perform(post(SettingsController.SETTINGS_TAGS_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagForm))
                        .with(csrf()))
                .andExpect(status().isOk());

        Tag tag =  tagRepository.findByTitle(strTag).orElse(null);

        assertNotNull(tag);
        assertTrue(accountRepository.findByNickname("jiny798").getTags().contains(tag));
    }

    @Test
    @DisplayName("프로필: 태그 삭제")
    @WithAccount("jiny798")
    void removeTag() throws Exception {

        Account account = accountRepository.findByNickname("jiny798");
        Tag tag = tagRepository.save(Tag.builder().title("newTag").build());
        accountService.addTag(account, tag);

        assertTrue(account.getTags().contains(tag));

        TagForm tagForm = new TagForm();
        String tagTitle = "newTag";
        tagForm.setTagTitle(tagTitle);
        mockMvc.perform(post(SettingsController.SETTINGS_TAGS_URL + "/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tagForm))
                        .with(csrf()))
                .andExpect(status().isOk());

        assertFalse(account.getTags().contains(tag));
    }

    @Test
    @DisplayName("프로필: 지역 정보 수정 화면")
    @WithAccount("jiny798")
    void updateZonesForm() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_ZONE_URL))
                .andExpect(view().name(SettingsController.SETTINGS_ZONE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("zones"));
    }

    @Test
    @DisplayName("프로필: 지역 정보 추가")
    @WithAccount("jiny798")
    void addZone() throws Exception {
        // given
        Zone testZone = Zone.builder().city("test").localNameOfCity("테스트시").province("테스트주").build();
        zoneRepository.save(testZone);

        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());
        mockMvc.perform(post(SettingsController.SETTINGS_ZONE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zoneForm))
                        .with(csrf()))
                .andExpect(status().isOk());
        Account account = accountRepository.findByNickname("jiny798");
        assertTrue(account.getZones().contains(testZone));
    }

    @Test
    @DisplayName("프로필: 지역 정보 삭제")
    @WithAccount("jiny798")
    void removeZone() throws Exception {
        // given
        Account jaime = accountRepository.findByNickname("jiny798");
        Zone testZone = Zone.builder().city("test").localNameOfCity("테스트시").province("테스트주").build();
        zoneRepository.save(testZone);
        accountService.addZone(jaime, testZone);
        assertTrue(jaime.getZones().contains(testZone));

        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());
        mockMvc.perform(post(SettingsController.SETTINGS_ZONE_URL + "/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(zoneForm))
                        .with(csrf()))
                .andExpect(status().isOk());
        assertFalse(jaime.getZones().contains(testZone));
    }

}