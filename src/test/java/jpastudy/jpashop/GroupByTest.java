package jpastudy.jpashop;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupByTest {

    @Test
    public void groupby(){
        List<Dish> dishList = Arrays.asList(
                new Dish("pork", 700, Type.MEAT),
                new Dish("spagetti", 500, Type.NOODLE),
                new Dish("tomato", 200, Type.VEGE),
                new Dish("onion", 150, Type.VEGE)
        );
        
        //Dish List의 이름을 List 출력
        //이름만 뽑기
        List<String> nameList = dishList.stream()
                .map(Dish::getName)
                .collect(Collectors.toList());
        //이름출력
        nameList.forEach(System.out::println);

        System.out.println("===============================================");

        //Dish이름을 구분자를 포함한 문자열을 출력하기
        String nameStrs = dishList.stream()
                .map(dish -> dish.getName())
                .collect(Collectors.joining("/"));
        System.out.println(nameStrs);

        System.out.println("===============================================");

        //Dish 칼로리 합계, 평균
        Integer totalCalory = dishList.stream()
                .collect(Collectors.summingInt(dish -> dish.getCalory()));
        System.out.println("칼로리 합계는" + totalCalory);

        IntSummaryStatistics statistic = dishList.stream()
                .collect(Collectors.summarizingInt(Dish::getCalory));
        System.out.println(statistic);

        System.out.println("===============================================");

        //Dish type별로 그룹핑
        Map<Type, List<Dish>> dishTypeGroup = dishList.stream()
                .collect(Collectors.groupingBy(dish -> dish.getType()));
        System.out.println(dishTypeGroup);

    }

    static class Dish{
        String name;
        int calory;
        Type type;

        public Dish(String name, int calory, Type type) {
            this.name = name;
            this.calory = calory;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public int getCalory() {
            return calory;
        }

        public Type getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Dish{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    enum Type{
        MEAT, NOODLE, VEGE
    }
}
