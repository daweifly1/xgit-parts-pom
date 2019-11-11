package cn.com.xgit.gw;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class StaticFilePathConfig implements WebMvcConfigurer {////implements WebMvcConfigurer //extends WebMvcConfigurationSupport

//    @Value("${file.staticAccessPath}")
//    private String staticAccessPath;
//    @Value("${file.uploadFolder}")
//    private String uploadFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String outside_static = "C:/privateSpace/xgit-parts-pom/manage-web/dist/";

        registry.addResourceHandler("/static/**").addResourceLocations("file:" + outside_static
        );
    }

}