package br.com.tclinica.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(br.com.tclinica.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.Doctor.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.Patient.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.Accountant.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.DoctorSchedule.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.DoctorSchedule.class.getName() + ".availableWeekdays", jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.AvailableWeekdays.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.Appointment.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.Appointment.class.getName() + ".paymentInstallments", jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.PaymentInstallment.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.Healthcare.class.getName(), jcacheConfiguration);
            cm.createCache(br.com.tclinica.domain.Healthcare.class.getName() + ".paymentInstallments", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
