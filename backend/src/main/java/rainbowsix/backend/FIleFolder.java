package rainbowsix.backend;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FIleFolder {
    private String virtualPath; //动态变化的路径
    private String[] fileNames;
}
