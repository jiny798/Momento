//package jiny.futurevia.service.modules.notification.infra.interceptor;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jiny.futurevia.service.modules.account.domain.dto.AccountContext;
//import jiny.futurevia.service.modules.account.domain.entity.Account;
//import jiny.futurevia.service.modules.notification.infra.repository.NotificationRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.view.RedirectView;
//
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class NotificationInterceptor implements HandlerInterceptor {
//
//    private final NotificationRepository notificationRepository;
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (modelAndView != null && !isRedirectView(modelAndView)  // 리다이렉트가 아니어야함
//                && authentication != null && isTypeOfUserAccount(authentication)) { // 인증 정보 존재
//
//            // 해당 사용자 알림 조회
//            Account account = ((AccountContext) authentication.getPrincipal()).getAccount();
//            long count = notificationRepository.countByAccountAndChecked(account, false);
//
//            modelAndView.addObject("hasNotification", count > 0); // (5)
//        }
//    }
//
//    private boolean isRedirectView(ModelAndView modelAndView) {
//        Optional<ModelAndView> optionalModelAndView = Optional.ofNullable(modelAndView);
//        return startsWithRedirect(optionalModelAndView) || isTypeOfRedirectView(optionalModelAndView);
//    }
//
//    private Boolean startsWithRedirect(Optional<ModelAndView> optionalModelAndView) {
//        return optionalModelAndView.map(ModelAndView::getViewName)
//                .map(viewName -> viewName.startsWith("redirect:"))
//                .orElse(false);
//    }
//
//    private Boolean isTypeOfRedirectView(Optional<ModelAndView> optionalModelAndView) {
//        return optionalModelAndView.map(ModelAndView::getView)
//                .map(v -> v instanceof RedirectView)
//                .orElse(false);
//    }
//
//    private boolean isTypeOfUserAccount(Authentication authentication) {
//        return authentication.getPrincipal() instanceof AccountContext;
//    }
//}