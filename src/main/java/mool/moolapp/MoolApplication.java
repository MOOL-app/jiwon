package mool.moolapp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
public class MoolApplication {

	@GetMapping("/helloo")
	public String hello() {
		return "hello";
	}
	public static void main(String[] args) {
		SpringApplication.run(MoolApplication.class, args);
	}

}

