package rs.edu.raf.StockService.data.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuturesContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String code;

    @Column(updatable = false)
    private int contractSize;

    @Column(updatable = false)
    private String contractUnit;

    private int openInterest;

    @Column(updatable = false)
    private long settlementDate;

    private float getMaintenanceMargin() {
        return contractSize * 0.1f;
    }
}
