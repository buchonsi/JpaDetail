package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.Member;
import jpastudy.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    /**
     * 조회 V1: 응답 값으로 엔티티를 직접 외부에 노출한다.
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 수정 API
     */
    @PatchMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {
         memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    //조회 V2: 응답 값으로 엔티티가 아닌 별도의 DTO를 반환한다 (List<MemberDTO>)
    @GetMapping("/api/v2/members")
    public List<MemberDto> membersV2() {
        //db에서 member가져와 List 생성
        List<Member> findmembers = memberService.findMembers();

        //List<Member> ---> List<MemberDTO> 변환
        List<MemberDto> memberDtoList = findmembers.stream()        //Stream<Member>
                .map(member -> new MemberDto(member.getName()))     //Stream<MemberDTO>
                .collect(toList());//List<MemberDTO>

        return memberDtoList;
    }

    //조회 V2: 응답 값으로 엔티티가 아닌 별도의 DTO를 반환한다 (Result)
    @GetMapping("/api/v2.1/members")
    public Result membersV2_1() {
        //db에서 member가져와 List 생성
        List<Member> findMembers = memberService.findMembers();

        //List<Member> ---> List<MemberDTO> 변환
        List<MemberDto> memberDtoList = findMembers.stream()        //Stream<Member>
                .map(member -> new MemberDto(member.getName()))     //Stream<MemberDTO>
                .collect(toList());                                 //List<MemberDTO>

        return new Result(memberDtoList.size(),memberDtoList);
    }
    //응답과 요청에 사용항 DTO InnerClass 선언

    //회원생성 DTO
    @Data
    static class CreateMemberRequest   {
        @NotEmpty
        private String name;
    }
    @Data
    static class CreateMemberResponse   {
        private final Long id;
    }

    //회원수정 DTO
    @Data
    static class UpdateMemberRequest {
        private String name;
    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
    //stream사용
    @Data
    @AllArgsConstructor
    class Result<T> {
        private int count;
        private T data;
    }
    @Data
    @AllArgsConstructor
    class MemberDto {
        private String name;
    }

}