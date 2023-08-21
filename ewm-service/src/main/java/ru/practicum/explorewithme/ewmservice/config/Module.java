package ru.practicum.explorewithme.ewmservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import ru.practicum.explorewithme.stats.client.HttpStatsClient;
import ru.practicum.explorewithme.stats.client.StatsClient;

@Configuration
public class Module {

    @Bean
    public StatsClient statsClient(
            @Value("${stats-server.url}") String statsServiceUrl, RestTemplateBuilder restTemplateBuilder
    ) {
        return new HttpStatsClient(statsServiceUrl, restTemplateBuilder);
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }
}
