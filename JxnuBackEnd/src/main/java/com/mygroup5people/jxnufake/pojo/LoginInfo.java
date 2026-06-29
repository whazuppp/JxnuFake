package com.mygroup5people.jxnufake.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 封装登录结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    private Integer id;            // 主键ID，对应 int unsigned
    private String username;       // 用户名，对应 varchar
    private String name;           // 姓名，对应 varchar
    private Integer gender;        // 性别（1 男，2 女），对应 tinyint
    private String image;          // 头像路径
    private String entrydate;      // 入学时间，建议用 String 方便 JSON 显示（或 java.time.LocalDate）
    private String stuId;          // 学号，对应 varchar
    private String token;          // JWT 登录令牌

    public LoginInfo(Integer id, Long stuId, String username, String name,  String token) {
        this.id = id;
        this.stuId = stuId.toString();
        this.username = username;
        this.name = name;
        this.token = token;
    }
}