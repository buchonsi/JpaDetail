package jpastudy.jpashop;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class StreamTest {

    @Test
    public void stream(){
        List<User> users = List.of(new User("유저1", 15), new User("유저2", 25), new User("유저3", 35));
        //User의 name만 뽑아서 List<String>으로 출력하기
        List<String> nameList =
                        users.stream()      //return Stream<User>;
//                             .map(user -> user.getName())   //return Stream<String>
                             .map(User :: getName)
                             .collect(Collectors.toList());
        nameList.forEach(name -> System.out.println(name));
        nameList.forEach(System.out :: println);

        System.out.println("==============================================================");
        //스무살 이상이 유저의 이름을 추출해서 List<String>으로 출력하라
        users.stream()
                .filter(user -> user.getAge() >= 20)
                .forEach(user -> System.out.println(user.getName()));

        List<String> names = users.stream()
                .filter(user -> user.getAge() >= 20)
                .map(User::getName)
                .collect(Collectors.toList());

        names.forEach(System.out::println);

        //User의 나이 합계
        int sum = users.stream()
                .mapToInt(user -> user.getAge())    // return IntStream
                .sum();
        System.out.println("나이 합계: " + sum);
    }

    @Data
    @AllArgsConstructor
    static class User {
        private String name;
        private int age;

    }
}
