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

    @PostMapping(value = "",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ResponseEntity<byte[]> createCharacter(@RequestPart(value = "prompt") String prompt,
                                                  @RequestPart(value = "image") MultipartFile img) throws IOException {
        log.debug("start create character...");
        try {
            // ControlNet 돌리기
            File file = new File("/home/origin_img/" + img.getOriginalFilename());
            img.transferTo(file);
            log.debug("created image file...");
            ProcessBuilder pb = new ProcessBuilder("sudo","python3.10", "/home/ControlNet-with-Anything-v4/book_grim.py", "-img", file.getPath(),"-p", prompt);
            Process controlnet = pb.start();
            log.debug("start the process...");
            controlnet.waitFor(60, TimeUnit.SECONDS);
            BufferedReader stdOut = new BufferedReader( new InputStreamReader(controlnet.getInputStream()) );
            String str;
            StringBuilder builder = new StringBuilder();
            while( (str = stdOut.readLine()) != null ) {
                builder.append(str);
                builder.append(System.getProperty("line.separator"));
            }
            log.debug("resposne : " + builder.toString());
            log.debug("end process...");

        } catch (InterruptedException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        InputStream imageStream = new FileInputStream("/home/ControlNet-with-Anything-v4/results/output.png");
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        log.debug("response... : " + imageByteArray.toString());
        imageStream.close();
        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }
}
