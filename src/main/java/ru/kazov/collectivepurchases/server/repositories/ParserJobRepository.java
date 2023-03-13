package ru.kazov.collectivepurchases.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserJob;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserJobStatus;

import java.util.Optional;

public interface ParserJobRepository extends JpaRepository<ParserJob, Long> {

    Optional<ParserJob> findFirstByStatusOrderByCreatedAtAsc(ParserJobStatus status);

    default Optional<ParserJob> findFirstPending() {
        return findFirstByStatusOrderByCreatedAtAsc(ParserJobStatus.PENDING);
    }


}