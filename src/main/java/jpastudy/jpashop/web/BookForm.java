package jpastudy.jpashop.web;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class BookForm {
    private Long id;
    @NotBlank(message = "상품이름은 필수 입.니.다.")
    private String name;
    private int price;
    @Min(value=1 , message = "상품재고 수량은 1개 이상 필수")
    private int stockQuantity;
    private String author;
    private String isbn;
}