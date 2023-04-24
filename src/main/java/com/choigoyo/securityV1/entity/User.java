package com.choigoyo.securityV1.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@NoArgsConstructor // 기본 생성자
@Entity
@Data // getter and setter ..
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    @CreationTimestamp
    private Timestamp createDate;

    // oauth 로그인을 한 사용자와 일반 사용자의 구분을 위해 추가
    private String provider;
    private String providerId;

    @Builder // 생성자를 통한 필드 초기화
    public User(String username, String password, String email, String role, Timestamp createDate, String provider, String providerId) {
        // 회원가입을 위한 생성자
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createDate = createDate;
        this.provider = provider;
        this.providerId = providerId;
    }
}
