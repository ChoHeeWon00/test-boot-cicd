package com.ex01.basic.repository;

import com.ex01.basic.dto.MemberDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    private ArrayList<MemberDTO> DB;
    public MemberRepository(){
        DB = new ArrayList<>();
        DB.add( new MemberDTO(0,"aaas","aaas","USER") );
        DB.add( new MemberDTO(1,"bbbs","bbbs","USER") );
        DB.add( new MemberDTO(2,"cccs","cccs","USER") );
    }
    public void repositoryTest(){
        System.out.println("repository test 실행");
    }
    public List<MemberDTO> findAll(){
        return DB;
    }
    public Optional<MemberDTO> findById(int id ){
        return DB.stream()
                .filter( dto -> dto.getId() == id )
                .findFirst();
    }
    public void save( MemberDTO memberDTO ){
        DB.add( memberDTO );
    }
    public void save(int id, MemberDTO memberDTO){
        DB.set(id, memberDTO);
    }
    public boolean deleteById(int id){
        //삭제 성공시 true, 실패시 false
        return DB.removeIf( m -> m.getId() == id );
    }
    public Optional<MemberDTO> findByUsername(String username){
        return DB.stream()
                .filter( dto -> dto.getUsername().equals(username) )
                .findFirst();
    }
}
