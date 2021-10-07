package entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class PayIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int payIn_id;

    private int payment_amount;
    private String payment_detail;
    private int cu_id;
    private int o_id;

    @OneToOne
    private Receipt receipt; //o_receipt_number --> fiş no

    private LocalDateTime date;






}
