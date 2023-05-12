package capstone.book_grim_ai;

import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;

@SpringBootApplication
public class BookGrimAiApplication {
	public static void main(String[] args) {;
		SpringApplication.run(BookGrimAiApplication.class, args);
	}

}
