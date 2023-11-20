package polytechnic.bh.PassPlatforms_Backend.Dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.School;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchoolDao
{
    private String schoolid;
    private String schoolname;
    private String schooldesc;

    public SchoolDao(School school)
    {
        this.schoolid = school.getSchoolid();
        this.schoolname = school.getSchoolname();
        this.schooldesc = school.getSchooldesc();
    }
}
