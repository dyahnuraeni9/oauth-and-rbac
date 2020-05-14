package com.project.user.management.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "tbl_user_role", schema = "public")
public class Role implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -9213567719485843900L;

    @Id
    @Column(name = "user_role_id", unique = true)
    @SequenceGenerator(name = "pk_sequence", sequenceName = "tbl_user_role_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    private Integer id;

    @Column(name = "holding_id")
    private Integer holdingId;

    @Column(name = "com_id")
    private Integer comId;

    @Column(name = "com_name")
    private String comName;

    @Column(name = "role_desc")
    private String roleDesc;

    @Column(name = "role_title")
    private String roleTitle;

    @Column(name = "role_access")
    private int roleAccess;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "role_create_time")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "role_update_time")
    private Date updateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "role_delete_time")
    private Date deleteTime;

    @Column(name = "role_is_delete")
    private int isDelete;

    @Column(name = "holding_name")
    private String holdingName;

    @Column(name = "role_type")
    private int roleType;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<RoleMenu> roleMenus = new HashSet<>();

}
