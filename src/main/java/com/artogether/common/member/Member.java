package com.artogether.common.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "姓名請勿空白")
    private String name;

    @Column(nullable = false, unique = true)
    @Email(message = "Email格式有誤")
    @NotBlank(message = "Email請勿空白")
    private String email;

    @Column(name = "register_date", updatable = false, insertable = false)
    @CreationTimestamp
    private Timestamp registerDate;

    @Past(message = "生日日期需早於今天")
    private Date birthday;
    private String gender;
    private String prefer;

    @Length(min=10,max=10, message = "手機號碼長度有誤")
    private String phone;

    @Column(nullable = false)
    @NotBlank(message = "密碼請勿空白")
    private String password;
    private Byte status;

    @Column(name = "oath_provider")
    private String oathProvider;

    @Column(name = "oath_user_id")
    private String oathUserId;




}
