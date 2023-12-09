package mool.moolapp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
public class MoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoolApplication.class, args);
	}

}

@RestController
class HelloController {

	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
}
