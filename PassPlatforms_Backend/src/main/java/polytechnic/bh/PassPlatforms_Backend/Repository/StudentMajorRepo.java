package polytechnic.bh.PassPlatforms_Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import polytechnic.bh.PassPlatforms_Backend.Entity.StudentMajor;

public interface StudentMajorRepo extends JpaRepository<StudentMajor, Integer> {
}
