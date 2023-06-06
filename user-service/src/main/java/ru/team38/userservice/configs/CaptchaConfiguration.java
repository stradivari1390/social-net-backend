package ru.team38.userservice.configs;

import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.gimpy.DropShadowGimpyRenderer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaptchaConfiguration {
    @Bean
    public DefaultTextProducer textProducer(CaptchaProperties properties) {
        return new DefaultTextProducer(properties.getLength(), properties.getChars());
    }

    @Bean
    public DefaultWordRenderer wordRenderer() {
        return new DefaultWordRenderer();
    }

    @Bean
    public GradiatedBackgroundProducer backgroundProducer() {
        return new GradiatedBackgroundProducer();
    }

    @Bean
    public DropShadowGimpyRenderer gimpyRenderer() {
        return new DropShadowGimpyRenderer();
    }
}