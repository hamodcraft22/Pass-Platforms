package polytechnic.bh.PassPlatforms_Backend.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import polytechnic.bh.PassPlatforms_Backend.Entity.School;

import java.util.List;

public interface SchoolRepo extends JpaRepository<School, String>
{
    // get all avlb schools (schools that are offering being offered)
    @Transactional
    @Query(value = "select * from pp_school where schoolid in (select DISTINCT(schoolid) from pp_course where courseid IN (select DISTINCT(courseid) from pp_offeredcourse))", nativeQuery = true)
    List<School> findAvlbSchools();

    // get all rev schools (schools that are offering revisions)
    @Transactional
    @Query(value = "select * from pp_school where schoolid in (select DISTINCT(schoolid) from pp_course where courseid IN (select DISTINCT(courseid) from pp_booking where typeid = 'R'))", nativeQuery = true)
    List<School> findRevSchools();
}
