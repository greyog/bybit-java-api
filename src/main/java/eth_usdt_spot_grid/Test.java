package eth_usdt_spot_grid;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.TradeOrderType;
import com.bybit.api.client.domain.trade.OrderStatus;
import com.bybit.api.client.domain.trade.Side;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.domain.trade.response.OrderEntry;
import com.bybit.api.client.log.LogOption;
import com.bybit.api.client.service.BybitApiClientFactory;
import common.ResponseValidator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Test extends Main{

    public static void main(String[] args) {
        var factory = BybitApiClientFactory.newInstance(
                System.getenv("API_KEY"),
                System.getenv("API_SECRET"),
                "TRUE".equals(System.getenv("IS_REAL")) ? BybitApiConfig.MAINNET_DOMAIN
                        : BybitApiConfig.DEMO_TRADING_DOMAIN,
                "TRUE".equals(System.getenv("DEBUG")),
                LogOption.OKHTTP3.getLogOptionType());
        var tradeClient = factory.newTradeRestClient();

        var openAskOrders = new ArrayList<OrderEntry>();
        var openBidOrders = new ArrayList<OrderEntry>();
        String nextPageCursor = null;
        do {
            var openOrdersResponse = tradeClient.getOpenOrders(TradeOrderRequest.builder()
                    .category(CategoryType.SPOT)
                    .symbol("ETHUSDT")
                    .cursor(nextPageCursor)
//                    .orderStatus(OrderStatus.ACTIVE)
                    .build());
            ResponseValidator.checkResult(openOrdersResponse);
            openOrdersResponse.getResult().getOrderEntries().forEach(orderEntry -> {
                switch (orderEntry.getSide()) {
                    case BUY -> openBidOrders.add(orderEntry);
                    case SELL -> openAskOrders.add(orderEntry);
                }
            });
            nextPageCursor = openOrdersResponse.getResult().getNextPageCursor();
        } while (nextPageCursor != null && !nextPageCursor.isEmpty());
        var openAskPrices = openAskOrders.stream()
                .map(OrderEntry::getPrice)
//                .map(BigDecimal::toString)
                .collect(Collectors.toSet());
        System.out.println("openAskPrices = " + openAskPrices);
        var openBidPrices = openBidOrders.stream()
                .map(OrderEntry::getPrice)
                .collect(Collectors.toSet());
        System.out.println("openBidPrices = " + openBidPrices);
        System.out.println("openBidPrices.size() + openAskPrices.size() = " + (openBidPrices.size() + openAskPrices.size()));
    }
}
