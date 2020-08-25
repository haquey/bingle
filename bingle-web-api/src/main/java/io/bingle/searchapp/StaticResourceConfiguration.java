package io.bingle.searchapp;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfiguration implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String workingDir = System.getProperty("user.dir") + "/LocalStorage/";

    registry.addResourceHandler("/LocalStorage/**").addResourceLocations("file:" + workingDir);
  }
}
