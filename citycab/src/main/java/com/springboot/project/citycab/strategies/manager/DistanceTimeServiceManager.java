package com.springboot.project.citycab.strategies.manager;

import com.springboot.project.citycab.dto.DistanceTimeResponseDTO;
import com.springboot.project.citycab.exceptions.DistanceRestClientServiceException;
import com.springboot.project.citycab.services.DistanceTimeService;
import com.springboot.project.citycab.services.impl.distancetime.DistanceTimeTimeServiceHereRoutingImpl;
import com.springboot.project.citycab.services.impl.distancetime.DistanceTimeTimeServiceOSRMImpl;
import com.springboot.project.citycab.services.impl.distancetime.DistanceTimeTimeServiceOtherRoutingImpl;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistanceTimeServiceManager {

    private final DistanceTimeTimeServiceOSRMImpl distanceTimeServiceOSRM;
    private final DistanceTimeTimeServiceHereRoutingImpl distanceTimeServiceHereRoutingImpl;
    private final DistanceTimeTimeServiceOtherRoutingImpl distanceTimeServiceOtherRoutingImpl;

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private static final int TIMEOUT_SECONDS = 1;

    public DistanceTimeResponseDTO calculateDistanceTime(Point src, Point dest) {
        return attemptWithFallback(src, dest, distanceTimeServiceOSRM, "OSRM",
                () -> attemptWithFallback(src, dest, distanceTimeServiceHereRoutingImpl, "Here Routing",
                        () -> attemptWithFallback(src, dest, distanceTimeServiceOtherRoutingImpl, "Other Routing",
                                () -> {
                                    throw new DistanceRestClientServiceException("All services failed to calculate distance and time.");
                                })));
    }

    private DistanceTimeResponseDTO attemptWithFallback(Point src, Point dest, DistanceTimeService service, String name, Callable<DistanceTimeResponseDTO> fallback) {
        Future<DistanceTimeResponseDTO> future = executor.submit(() -> service.calculateDistanceTime(src, dest));

        try {
            return future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException te) {
            log.warn("{} API timed out after {} seconds. Falling back.", name, TIMEOUT_SECONDS);
            future.cancel(true);
        } catch (ExecutionException ee) {
            log.warn("{} API failed: {}. Falling back.", name, ee.getCause().getMessage());
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            log.warn("Thread interrupted during {} API call. Falling back.", name);
        }

        try {
            return fallback.call();
        } catch (Exception e) {
            throw new DistanceRestClientServiceException("Error during fallback execution: " + e.getMessage(), e);
        }
    }

    @PreDestroy
    private void shutdownExecutor() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
