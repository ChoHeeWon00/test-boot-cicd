package com.ex01.basic.controller;

import com.ex01.basic.dto.LoginDTO;
import com.ex01.basic.dto.MemberDTO;
import com.ex01.basic.dto.MemberRegDTO;
import com.ex01.basic.exception.InvalidLoginException;
import com.ex01.basic.exception.MemberDuplicateException;
import com.ex01.basic.exception.MemberNotFoundException;
import com.ex01.basic.service.MemberFileServiceImpl;
import com.ex01.basic.service.MemberServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("members")
@Slf4j
@Tag(name = "MemberAPI", description = "회원 도메인 API")
public class MemberController {

    private MemberServiceImpl memberServiceImpl;
    public MemberController( MemberServiceImpl memberServiceImpl ){
        System.out.println("controller 생성자 실행");
        this.memberServiceImpl = memberServiceImpl;
    }

    @GetMapping
    @Operation(summary = "전체 회원 조회", description = "회원 목록을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 목록 반환 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(  value = """
                            { 
                                "list" : [ {"id" : 1,"username" : "aaa","password" : "aaa","role" : "USER"}],
                                "totalPages" : 10,
                                "currentPage" : 1
                            }
                            """ )

                            /*
                            array = @ArraySchema(
                                    schema = @Schema(implementation = MemberDTO.class )
                            )

                             */
                    )
            ),
            @ApiResponse(responseCode = "404", description = "데이터가 없는 경우",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject( value = """
                                        { "list" : [], "totalPages" : 0, "currentPage" : 1}
                                    """ )
                    )
            )

    })
    public ResponseEntity<Map<String, Object>> getList(@RequestParam(defaultValue = "0") int start){
        log.info("모든 목록 확인");
        return ResponseEntity.ok( memberServiceImpl.getList(start) );
    }
    @GetMapping("/{id}")
    @Operation(summary = "특정 회원 조회", description = "회원 ID를 이용해 특정 회원의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema( implementation = MemberDTO.class )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "사용자 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "null")
                    )
            )
    })
    public ResponseEntity<MemberDTO> getOne(@PathVariable("id") int id){
        log.info("특정 사용자 확인 : {}", id);
        return ResponseEntity.ok( memberServiceImpl.getOne(id) );
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원 추가", description = "폼 데이터 - 회원을 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원 추가 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "추가 성공"))),
            @ApiResponse(responseCode = "409", description = "회원 중복 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class, example = "이미 존재하는 사용자입니다")))
    })

    public ResponseEntity<String> insert(
            @RequestParam(value="file", required = false) MultipartFile file,
            @ParameterObject @ModelAttribute MemberRegDTO memberRegDTO){
            log.info("사용자 추가 : {}", memberRegDTO.getUsername() );
            memberServiceImpl.insert( memberRegDTO, file );
            return ResponseEntity.status(HttpStatus.CREATED).body("추가 성공");
    }
    @PutMapping(value="/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "회원 수정", description = "폼 데이터 - 특정 ID의 회원 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "수정 성공 (내용 없음)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Void.class )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Void.class )
                    )
            )
    })

    public ResponseEntity<Void> modify(@PathVariable("id") int id,
                                       @ParameterObject @ModelAttribute MemberDTO memberDTO,
                                       @RequestParam(value="file", required = false) MultipartFile file){
        memberServiceImpl.modify(id, memberDTO, file);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    WebClient webClient = WebClient.create();

    @DeleteMapping("/{id}")
    @Operation(summary = "회원 삭제", description = "특정 ID의 회원을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공 (내용 없음)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Void.class)
                    )
            ),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Void.class)
                    )
            )
    })

    public ResponseEntity<Void> deleteMember(@PathVariable("id") int id ,
                                             @RequestBody String fileName){
        memberServiceImpl.delMember( id, fileName );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PostMapping("/login")
    @Operation( summary = "로그인", description = "회원의 ID와 비밀번호를 검증하여 로그인합니다." )
    @ApiResponses({
            @ApiResponse( responseCode = "200", description = "로그인 성공",
                    content = @Content( mediaType = "application/json",
                            schema = @Schema( implementation = Boolean.class ),
                            examples = @ExampleObject(value = "true") ) ),
            @ApiResponse( responseCode = "401", description = "아이디 또는 비밀번호가 올바르지 않음",
                    content = @Content( mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class),
                            examples = @ExampleObject(value = "false") ) )
    })
    public ResponseEntity<Boolean> login(@RequestBody LoginDTO loginDTO){
        memberServiceImpl.login( loginDTO.getUsername(), loginDTO.getPassword() );
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
    @Autowired
    MemberFileServiceImpl memberFileService;
    @Operation( summary = "회원 이미지 조회", description = "회원의 프로필 이미지를 다운로드합니다" )
    @ApiResponses({
            @ApiResponse( responseCode = "200", description = "이미지 조회 성공",
                    content = @Content(
                            mediaType = "image/*",
                            schema = @Schema(implementation = Byte.class),
                            examples = @ExampleObject(value = "이미지 bytes")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "이미지 없음")
    })
    @GetMapping("{fileName}/image")
    public ResponseEntity<byte[]> getMemberImage(@PathVariable String fileName){
        byte[] imageByte = null;
        try{
            imageByte = memberFileService.getImage(fileName);
        }catch ( RuntimeException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body( imageByte );
    }


}
