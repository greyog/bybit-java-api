package sold_option_basic_hedger;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.position.response.PositionEntry;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.domain.trade.response.OrderEntry;
import com.bybit.api.client.log.LogOption;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static sold_option_basic_hedger.Main.HEDGE_SYMBOL;

public class Test {

    public static void main(String[] args) {
        var factory = BybitApiClientFactory.newInstance(
                System.getenv("API_KEY"),
                System.getenv("API_SECRET"),
                BybitApiConfig.DEMO_TRADING_DOMAIN,
                true,
                LogOption.OKHTTP3.getLogOptionType());
        var tradeClient = factory.newTradeRestClient();
        BybitApiPositionRestClient positionRestClient = factory.newPositionRestClient();

//        List<PositionEntry> futuresPositions = getPositions(CategoryType.LINEAR, positionRestClient, HEDGE_SYMBOL);
//        System.out.println(futuresPositions);

        var h = getOrderHistory(CategoryType.LINEAR, tradeClient, HEDGE_SYMBOL);
        h.sort(Comparator.comparing(OrderEntry::getCreatedTime).reversed());
        System.out.println("h = " + h.stream().limit(2).collect(Collectors.toList()));
    }

    private static List<OrderEntry> getOrderHistory(CategoryType categoryType, BybitApiTradeRestClient client,
                                              String symbol) {
        List<OrderEntry> orderEntries = new ArrayList<>();
        String nextPageCursor = null;
        var requestBuilder = TradeOrderRequest.builder()
                .category(categoryType)
                .symbol(symbol);
        do {
            var ordersInfo = client.getOrderHistory(requestBuilder
                    .cursor(nextPageCursor)
                            .limit(5)
                    .build());
//            checkResult(ordersInfo);
            orderEntries.addAll(ordersInfo.getResult().getOrderEntries());
            nextPageCursor = ordersInfo.getResult().getNextPageCursor();
        } while (!nextPageCursor.isEmpty());
        return orderEntries;
    }
}
