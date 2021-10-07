package entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uid;

    private String name;
    private String email;

    @Column(length = 32)
    private String password;
    @Column(length = 50)
    private String uuid;
}
