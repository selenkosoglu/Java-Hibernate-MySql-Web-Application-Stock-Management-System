package entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int receipt_id;


    private int receipt_total;
    private int receipt_payment;
    private LocalDateTime date;



    @OneToMany
    private List<Orders> boxActions;




}
