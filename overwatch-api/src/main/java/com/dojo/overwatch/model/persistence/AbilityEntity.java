package com.dojo.overwatch.model.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ability")
@AllArgsConstructor
@NoArgsConstructor
public class AbilityEntity {

    @Id
    private Integer id;

    private String name;

    @Column(length = 1000)
    private String description;

    private Boolean isUltimate;

}
