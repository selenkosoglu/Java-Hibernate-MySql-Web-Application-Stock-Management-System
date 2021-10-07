package entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class JointTablesReceipt {

    @Id
    private long o_receipt_number;

    private int receipt_total;


}
