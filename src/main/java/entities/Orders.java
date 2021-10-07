package entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int o_id;
    private int o_amount;
    private int o_customer;
    private int o_product;

    private long o_receipt_number;


    private int box_status;

    @OneToOne
    private Products product;



    @OneToOne
    private Customer customer;








}
