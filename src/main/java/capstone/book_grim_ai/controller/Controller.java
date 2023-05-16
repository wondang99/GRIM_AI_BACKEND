package capstone.book_grim_ai.controller;

import capstone.book_grim_ai.service.Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Controller {
    private final Service service;
    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @GetMapping
    public String test(){
        return "this is test";
    }

    @PostMapping(value = "",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public @ResponseBody byte[] createCharacter(@RequestPart(value = "prompt") String prompt,
                                                  @RequestPart(value = "image") MultipartFile img) throws IOException {
        log.debug("start create character...");
        try {
            log.debug("multipart img : " + img.getBytes());
            log.debug("originalFileName : " + img.getOriginalFilename());
            // ControlNet 돌리기
            File file = new File("/home/origin_img/" + img.getOriginalFilename());
            img.transferTo(file);
            if(file.exists()){
                log.debug("file exist!");
            }
            log.debug("created image file...");
            File logs = new File("/home/origin_img/log");
            ProcessBuilder pb = new ProcessBuilder("sudo","python3.10", "/home/ControlNet-with-Anything-v4/book_grim.py", "-img", file.getPath(),"-p", prompt);
            pb.redirectOutput(logs);
            Process controlnet = pb.start();
            log.debug("start the process...");
            controlnet.waitFor();

            log.debug("end process...");

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        byte[] bytes = Files.readAllBytes(Paths.get("/home/ControlNet-with-Anything-v4/results/output.png"));
        log.debug("response... : " + bytes.toString());

        return bytes;
    }
}
