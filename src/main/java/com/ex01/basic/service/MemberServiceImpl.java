package com.ex01.basic.service;

import com.ex01.basic.dto.MemberDTO;
import com.ex01.basic.dto.MemberRegDTO;
import com.ex01.basic.entity.MemberEntity;
import com.ex01.basic.exception.InvalidLoginException;
import com.ex01.basic.exception.MemberDuplicateException;
import com.ex01.basic.exception.MemberNotFoundException;
import com.ex01.basic.repository.MemRepository;
import com.ex01.basic.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl {
    @Autowired
    private MemberRepository memberRepository;
    private final MemRepository memRepository;
    private final MemberFileServiceImpl memberFileService;
    public void serviceTest(){
        System.out.println("service : "+memberRepository);
        memberRepository.repositoryTest();
    }
    @Transactional(readOnly = true)
    public Map< String , Object > getList(int start){
        int size = 3;
        Pageable pageable = PageRequest.of(start, size, Sort.by(Sort.Order.desc("id")) );
        Page<MemberEntity> page = memRepository.findAll( pageable );
        if (page.isEmpty())
            throw new MemberNotFoundException();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", page.getContent().stream().map( MemberDTO::new ).toList() );
        map.put("totalPages", page.getTotalPages() );
        //처음 페이지가 0이므로 + 1
        map.put("currentPage", page.getNumber() + 1 );
        return map;
    }
    @Transactional(readOnly = true)
    public MemberDTO getOne(int id){
        return memRepository.findById( id )
                .map(MemberDTO::new)
                .orElseThrow(()-> new MemberNotFoundException("없다~"));
    }
    public void insert(MemberRegDTO memberRegDTO, MultipartFile file){
        if ( memRepository.existsByUsername( memberRegDTO.getUsername()) ) {
            throw new MemberDuplicateException(memberRegDTO.getUsername());
        }
        String fileName = memberFileService.saveFile(file);
        memberRegDTO.setFileName( fileName );
        MemberEntity memberEntity = new MemberEntity();
        BeanUtils.copyProperties(memberRegDTO, memberEntity);
        memRepository.save(memberEntity);
    }
    public void modify(int id, MemberDTO memberDTO, MultipartFile file) {
        MemberEntity memberEntity = memRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
        String changeFileName = memberFileService.saveFile(file);
        if( !changeFileName.equals("nan") ){
            memberFileService.deleteFile( memberEntity.getFileName() );
            memberDTO.setFileName( changeFileName );
        }
        BeanUtils.copyProperties(memberDTO, memberEntity);
    }
    public void delMember( int id , String fileName ){
        if(!memRepository.existsById(id))
            throw new MemberNotFoundException();
        memRepository.deleteById(id);
        memberFileService.deleteFile(fileName);
    }
    public void login(String username, String password){
        memRepository.findByUsername( username ).stream()
                .findFirst()
                .ifPresentOrElse(
                        m -> {
                            if(!m.getPassword().equals(password))
                                throw new InvalidLoginException();
                        },
                        () -> { throw new InvalidLoginException(); }
                );
    }
}
