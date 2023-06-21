package ru.team38.userservice.services;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.gimpy.DropShadowGimpyRenderer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.team38.common.dto.CaptchaDto;
import ru.team38.userservice.MockCaptchaBase;
import ru.team38.userservice.exceptions.CaptchaCreationException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public final class CaptchaService {
    private final DefaultTextProducer textProducer;
    private final DefaultWordRenderer wordRenderer;
    private final GradiatedBackgroundProducer backgroundProducer;
    private final DropShadowGimpyRenderer gimpyRenderer;
    private final MockCaptchaBase mockCaptchaBase;

    public CaptchaDto createCaptcha() throws CaptchaCreationException {
        log.info("Executing createCaptcha request");
        try {
            String captchaID = UUID.randomUUID().toString();

            Captcha captcha = new Captcha.Builder(200, 50)
                    .addText(textProducer, wordRenderer)
                    .addBackground(backgroundProducer)
                    .gimp(gimpyRenderer)
                    .addNoise()
                    .build();

            String captchaSolution = captcha.getAnswer();
            BufferedImage captchaImage = captcha.getImage();

            mockCaptchaBase.storeCaptcha(captchaID, captchaSolution);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(captchaImage, "png", baos);
            } catch (IOException e) {
                throw new CaptchaCreationException("Failed to encode captcha image to byte array.", e);
            }
            byte[] bytes = baos.toByteArray();
            String encodedImage = Base64.getEncoder().encodeToString(bytes);

            return new CaptchaDto(captchaID, "data:image/png;base64, " + encodedImage);
        } catch (CaptchaCreationException e) {
            log.error("Error executing createCaptcha request", e);
            throw e;
        }
    }
}