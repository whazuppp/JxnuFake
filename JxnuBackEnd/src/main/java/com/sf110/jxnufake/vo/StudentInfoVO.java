package com.sf110.jxnufake.vo;

import java.time.LocalDate;

public record StudentInfoVO(
        Integer id,
        String username,
        String name,
        Short gender,
        String image,
        LocalDate entrydate,
        String stuId,
        Integer classId,
        String className
) {
}
