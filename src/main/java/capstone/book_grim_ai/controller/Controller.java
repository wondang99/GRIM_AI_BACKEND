package capstone.book_grim_ai.controller;

import capstone.book_grim_ai.service.Service;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class Controller {
    private final Service service;

    @GetMapping
    public String test(){
        return "this is test";
    }

    @PostMapping(value = "",consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ResponseEntity<byte[]> createCharacter(@RequestPart(value = "prompt") String prompt,
                                                  @RequestPart(value = "image") MultipartFile img) throws IOException {
        // ControlNet 돌리기

        File file = new File("/home/origin_img/" + img.getOriginalFilename());

        img.transferTo(file);
        ProcessBuilder pb = new ProcessBuilder("sudo python3.10 ~/ControlNet-with-Anything-v4/book_grim.py -img " + file.getPath() + " -p \""+ prompt +"\"");
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process controlnet = pb.start();
        try {
            controlnet.waitFor();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        InputStream imageStream = new FileInputStream("~/ControlNet-with-anything-v4/results/output.png");
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();
        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
    }
}
