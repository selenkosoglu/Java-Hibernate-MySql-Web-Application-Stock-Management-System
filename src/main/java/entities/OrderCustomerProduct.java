package entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class OrderCustomerProduct {

    @Id
    private int o_id ;
    private String p_title;
    private int p_sale_price;
    private int o_amount;
    private String cu_name;
    private String cu_surname;
    private Long o_receipt_number;



}
