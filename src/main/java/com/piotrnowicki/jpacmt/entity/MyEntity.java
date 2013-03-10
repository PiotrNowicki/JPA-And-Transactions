package com.piotrnowicki.jpacmt.entity;

import javax.persistence.*;

@Entity
@SequenceGenerator(name = "MySEQ", initialValue = 1)
@NamedQuery(name = "MyEntity.FIND_ALL", query = "SELECT e FROM MyEntity e")
public class MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MySEQ")
    private Long id;

    private String name;

    private String code;

    @Column(length = 10)
    private String content;

    /**
     * For JPA purposes only.
     */
    MyEntity() {
    }

    public MyEntity(String name, String code, String content) {
        this.name = name;
        this.code = code;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MyEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
