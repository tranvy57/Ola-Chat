package vn.edu.iuh.fit.olachatbackend.services;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OtpServiceFactory {

    private final Map<String, OtpService> serviceMap;

    public OtpService getStrategy(String provider) {
        return serviceMap.getOrDefault(provider, serviceMap.get("twilio")); // fallback về twilio nếu không có
    }
}
