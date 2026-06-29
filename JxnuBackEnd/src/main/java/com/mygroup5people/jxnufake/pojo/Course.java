package com.mygroup5people.jxnufake.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private Integer id;
    private String name;
    private Integer number;//每周课时数
    private String teacherName;//授课老师
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
