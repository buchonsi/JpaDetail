package jpastudy.jpashop.web;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberForm {
//  NotEmpty는 공백은 못 거른다, NotBlank는 공백도 거름  
//  @NotEmpty(message = "회원 이름은 필수 입니다")
    @NotBlank(message = "회원 이름은 필수 입니다")
    private String name;
    private String city;
    private String street;
    private String zipcode;
}