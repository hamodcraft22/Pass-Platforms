package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.SlotType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlotTypeDao
{
    private char typeid;
    private String typename;

    public SlotTypeDao(SlotType slotType)
    {
        this.typeid = slotType.getTypeid();
        this.typename = slotType.getTypename();
    }
}
