package com.project.user.management.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@ToString
@Getter
@Setter
@Entity
@Table(name = "tbl_user_role_apps_menu", schema = "public")
public class RoleMenu implements Serializable {

    @EmbeddedId
    private RoleMenuKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("user_role_id")
    @JoinColumn(name = "user_role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menu_id")
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "rm_read_write")
    private Integer readWrite;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "rm_create_time")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "rm_update_time")
    private Date updateTime;

}
