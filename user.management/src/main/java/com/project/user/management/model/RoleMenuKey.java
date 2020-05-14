package com.project.user.management.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class RoleMenuKey implements Serializable {

    @Column(name = "user_role_id")
    private Integer roleId;

    @Column(name = "menu_id")
    private Integer menuId;

}
