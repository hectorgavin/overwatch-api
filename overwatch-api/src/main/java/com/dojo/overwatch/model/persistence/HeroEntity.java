package com.dojo.overwatch.model.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "hero")
@AllArgsConstructor
@NoArgsConstructor
public class HeroEntity {

    @Id
    private Integer id;

    private String name;

    private String realName;

    private Integer health;

    private Integer armour;

    private Integer shield;

    @OneToMany
    private List<AbilityEntity> abilities = new ArrayList<>();

}
