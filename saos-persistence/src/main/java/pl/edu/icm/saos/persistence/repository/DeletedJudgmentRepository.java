package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.DeletedJudgment;

public interface DeletedJudgmentRepository extends JpaRepository<DeletedJudgment, Long> {

}
