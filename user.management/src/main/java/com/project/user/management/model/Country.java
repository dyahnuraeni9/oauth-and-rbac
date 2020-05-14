package com.project.user.management.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "tbl_user_country", schema = "public")
public class Country implements Serializable {
     /**
     *
     */
    private static final long serialVersionUID = 3754401036302508997L;

    @Id
    @Column(name = "country_code_id", unique = true)
    private Integer id;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "country_description")
    private String countryDescription;

}
