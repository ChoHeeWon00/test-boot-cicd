package com.ex01.basic.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class MemberFileServiceImpl {
    private static final String DIR = "uploads/";
    public String saveFile( MultipartFile file ){
        String fileName = null;
        if( file == null || file.isEmpty() ) fileName = "nan";
        else { //UUID : 중복되지 않은 값을 생성하는 기능
            fileName = UUID.randomUUID().toString()
                    + "-" + file.getOriginalFilename();
            //import java.nio.file.Path;
            Path path = Paths.get(DIR + fileName );
            try { //getParent : 파일 전인 부모 폴더 경로까지 나옴( uploads 폴더 )
                Files.createDirectories( path.getParent() ); //폴더 없으면 생성
                if( !file.isEmpty() ) file.transferTo(path); //파일 저정
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return fileName;
    }
    public byte[] getImage(String fileName){
        Path filePath = Paths.get(DIR + fileName );
        if( !Files.exists(filePath) ) throw new RuntimeException("파일 없음");
        byte[] imageBytes = {0};
        try {
            imageBytes = Files.readAllBytes( filePath );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return imageBytes;
    }
    public void deleteFile(String fileName){
        System.out.println("fileName : "+fileName);
        Path path = Paths.get(DIR + fileName);
        try {
            Files.deleteIfExists( path ); //파일이 존재하면 삭제
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
