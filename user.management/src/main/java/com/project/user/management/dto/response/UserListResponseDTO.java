package com.project.user.management.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.user.management.model.Country;
import com.project.user.management.model.Role;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserListResponseDTO {

    private Long id;
    private Role role;
    private Country country;
    private String email;
    private int emailVerify;
    private String username;
    private String mobile;
    private String employeeNo;
    private Integer isActive;
    private Integer isDelete;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+7")
    private Date updateTime;

}
