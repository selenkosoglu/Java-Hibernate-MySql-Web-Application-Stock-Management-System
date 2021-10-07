package entities;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class PayOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int payOut_id;

    private String payOutTitle;
    private int payOutType;
    private int payOutTotal;
    private String payOutDetail;


}
