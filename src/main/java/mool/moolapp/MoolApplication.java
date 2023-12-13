package mool.moolapp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SpringBootApplication
public class MoolApplication {

	@RequestMapping("/hello")
	String hello() {
		return "hello";
	}
	public static void main(String[] args) {
		SpringApplication.run(MoolApplication.class, args);
	}

}
