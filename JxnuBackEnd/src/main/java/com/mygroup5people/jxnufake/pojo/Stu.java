package com.mygroup5people.jxnufake.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stu {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private Short gender;
    private String image;
    private LocalDate entrydate;
    private Long stuId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
