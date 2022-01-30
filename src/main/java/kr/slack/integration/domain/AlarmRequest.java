package kr.slack.integration.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
public class AlarmRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String teamId;

    @Column(nullable = false)
    private String channelId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String responseUrl;

    @Column(nullable = false)
    private String stockSymbol;

    @Column(nullable = false, precision=12, scale=2)
    private BigDecimal originalQuote;

    @Column(nullable = false, precision=12, scale=2)
    private BigDecimal targetQuote;

    @Column(columnDefinition = "boolean default false")
    private Boolean processed;

    public AlarmRequest(String teamId, String channelId, String userId, String responseUrl, String stockSymbol, BigDecimal originalQuote, BigDecimal targetQuote) {
        this.teamId = teamId;
        this.channelId = channelId;
        this.userId = userId;
        this.responseUrl = responseUrl;
        this.stockSymbol = stockSymbol;
        this.originalQuote = originalQuote;
        this.targetQuote = targetQuote;
        this.processed = false;
    }

    public void updateProcessed(Boolean processed) {
        this.processed = processed;
    }
}