package com.ex01.basic.controller;

import com.ex01.basic.dto.BasicDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BasicRestController {
    private ArrayList<BasicDTO> list;
    public BasicRestController(){
        System.out.println("생성자 실행");
        list = new ArrayList<>();
        for(int i=0 ; i<5 ; i++){
            list.add( new BasicDTO("aaa"+i, "test",i) );
        }
    }
    @GetMapping("/api/test01")
    public ResponseEntity<String> getTest01(HttpServletRequest request){
        String clientIp = request.getRemoteAddr();
        String msg = "get방식 요청 => "+clientIp;
        return ResponseEntity.ok( msg );
    }
    @GetMapping("/api/test02")
    public ResponseEntity<List> getTest02(){
        ArrayList<String> list = new ArrayList<>();
        list.add("aaa"); list.add("bbb");
        return ResponseEntity.ok( list );
    }
    @PostMapping("/api/test01")
    public ResponseEntity<String> post01(){
        return ResponseEntity.ok("PostMapping");
    }
    @PutMapping("/api/test01")
    public ResponseEntity<String> put01(){
        return ResponseEntity.ok("PutMapping");
    }
    @DeleteMapping("/api/test01")
    public ResponseEntity<String> delete01(){
        return ResponseEntity.ok("DeleteMapping");
    }
    @GetMapping("api/val/{username}")
    public ResponseEntity getMember(@PathVariable("username") String name){
        System.out.println("username : "+name);
        return ResponseEntity.ok(name);
    }
    @GetMapping("api/val")
    public ResponseEntity getMember02(@RequestParam("username") String name){
        System.out.println("username : "+name);
        return ResponseEntity.ok(name);
    }
    @GetMapping("api/members")
    public ResponseEntity<List<BasicDTO>> getList(){
        return ResponseEntity.ok(list);
    }
    @GetMapping("api/members/{username}")
    public ResponseEntity<BasicDTO> getOne(@PathVariable("username")String username){
        BasicDTO dto = list.stream()
                .filter( mem -> mem.getUsername().equals(username) )
                .findFirst().orElse( null );
        return ResponseEntity.ok(dto);
    }


}
