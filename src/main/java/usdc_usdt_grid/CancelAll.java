package usdc_usdt_grid;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.log.LogOption;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;

public class CancelAll {

    private static final String SYMBOL = System.getenv("SYMBOL");

    public static void main(String[] args) {
        var factory = BybitApiClientFactory.newInstance(
                System.getenv("API_KEY"),
                System.getenv("API_SECRET"),
                BybitApiConfig.DEMO_TRADING_DOMAIN,
                true,
                LogOption.OKHTTP3.getLogOptionType());
        var tradeClient = factory.newTradeRestClient();
        cancelAll(tradeClient);
    }

    public static void cancelAll(BybitApiTradeRestClient client) {
        var cancelAllOrdersRequest = TradeOrderRequest.builder()
                .category(CategoryType.SPOT)
                .symbol(SYMBOL)
                .build();
        var response = client.cancelAllOrder(cancelAllOrdersRequest);
    }

}
