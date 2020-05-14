package com.project.user.management.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@ToString
@Getter
@Setter
@Entity
@Table(name = "tbl_user", schema = "public")
public class User implements UserDetails , Serializable{

    private static final long serialVersionUID = 1665610291412023709L;

    @Id
    @Column(name = "user_id", unique = true)
    @SequenceGenerator(name = "pk_sequence", sequenceName = "tbl_user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pk_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "country_code_id")
    private Country country;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_email_verifiy")
    private int emailVerify;

    @Column(name = "user_username")
    private String username;

    @Column(name = "user_password")
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "user_password_create_date")
    private Date passwordCreateDate;

    @Column(name = "user_mobile")
    private String mobile;

    @Column(name = "user_employee_no")
    private String employeeNo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "user_last_login_attemp")
    private Date lastLoginAttempt;

    @Column(name = "user_login_attemp")
    private Integer loginAttempt;

    @Temporal(TemporalType.DATE)
    @Column(name = "user_disclaimer_date")
    private Date disclaimerDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "user_create_time")
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "user_update_time")
    private Date updateTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "user_delete_time")
    private Date deleteTime;

    @Column(name = "user_is_delete")
    private int isDelete;

    @Column(name = "user_is_active")
    private int isActive;
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

}
