package rs.edu.raf.StockService.data.dto.options;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptionChain {
    private List<OptionResult> result;
    private String error;
}
