package com.springboot.project.citycab.strategies.manager;

import com.springboot.project.citycab.constants.enums.PaymentMethod;
import com.springboot.project.citycab.strategies.PaymentStrategy;
import com.springboot.project.citycab.strategies.impl.CashPaymentStrategy;
import com.springboot.project.citycab.strategies.impl.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final WalletPaymentStrategy walletPaymentStrategy;
    private final CashPaymentStrategy cashPaymentStrategy;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case WALLET -> walletPaymentStrategy;
            case CASH -> cashPaymentStrategy;
        };
    }
}
