package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;

public interface RawSourceCtJudgmentRepository extends JpaRepository<RawSourceCtJudgment, Integer> {

}
