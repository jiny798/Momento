//package jiny.futurevia.service.infra.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebConfig implements WebMvcConfigurer {
////    private final NotificationInterceptor notificationInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        List<String> staticResourcesPath = Stream.of(StaticResourceLocation.values())
//                .flatMap(StaticResourceLocation::getPatterns)
//                .collect(Collectors.toList());
//        staticResourcesPath.add("/node_modules/**");
//
////        registry.addInterceptor(notificationInterceptor)
////                .excludePathPatterns(staticResourcesPath);
//    }
//}