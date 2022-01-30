package kr.slack.integration.service.impl;

import kr.slack.integration.domain.AlarmRequest;
import kr.slack.integration.domain.AlarmRequestRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AlarmRequestService {

    private final AlarmRequestRepository alarmRequestRepository;

    public AlarmRequestService(AlarmRequestRepository alarmRequestRepository) {
        this.alarmRequestRepository = alarmRequestRepository;
    }

    @Transactional
    public AlarmRequest createAlarmRequest(String teamId, String channelId, String userId, String responseUrl, String stockSymbol, BigDecimal currentQuote, BigDecimal targetQuote) {

        AlarmRequest alarmRequest = new AlarmRequest(teamId, channelId, userId, responseUrl, stockSymbol, currentQuote, targetQuote);

        alarmRequestRepository.save(alarmRequest);

        return alarmRequest;
    }

    public List<AlarmRequest> getAllAlarmRequests() {
        return alarmRequestRepository.findAll();
    }

    public List<AlarmRequest> getAllNotProcessedAlarmRequests() {
        return alarmRequestRepository.findAllByProcessed(Boolean.FALSE);
    }

    @Transactional
    public Long updateProcessed(Long id, Boolean processed) {

        AlarmRequest request = alarmRequestRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("no alarm request for the id=" + id));

        request.updateProcessed(processed);

        return id;
    }

}
