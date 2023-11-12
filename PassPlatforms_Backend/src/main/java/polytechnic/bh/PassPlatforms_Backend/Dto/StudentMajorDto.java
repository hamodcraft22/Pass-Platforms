package polytechnic.bh.PassPlatforms_Backend.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import polytechnic.bh.PassPlatforms_Backend.Entity.StudentMajor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentMajorDto {
    private int stumajorid;
    private String isminor;
    private UserDto user;
    private MajorDto major;

    public StudentMajorDto(StudentMajor studentMajor) {
        this.stumajorid = studentMajor.getStumajorid();
        this.isminor = studentMajor.getIsminor();
        this.user = new UserDto(studentMajor.getUser());
        this.major = new MajorDto(studentMajor.getMajor());
    }
}
