package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.RemovedJudgment;

public interface RemovedJudgmentRepository extends JpaRepository<RemovedJudgment, Long> {

}
