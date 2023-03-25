package io.github.maciejwojcik913.RegistrationAndLogin.user;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
public class UserRole implements Serializable {
    public static final long serialVersionUID = 13L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String name;

    /**
     * Used by Hibernate.
     */
    public UserRole() {
    }

    public UserRole(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
