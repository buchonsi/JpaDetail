package jpastudy.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.aspectj.weaver.ast.Or;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	@Bean
	Hibernate5Module hibernate5Module() {
		return new Hibernate5Module();
		//강제로 Lazy 의존 관계에 있는 객체를 가져온다
//		Hibernate5Module hibernate5Module = new Hibernate5Module();
//		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING,true);
//		return hibernate5Module;
		
	}

}
