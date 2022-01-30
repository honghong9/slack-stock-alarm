package kr.slack.integration.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRequestRepository extends JpaRepository<AlarmRequest, Long> {

    List<AlarmRequest> findAllByProcessed(Boolean processed);

}