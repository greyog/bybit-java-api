package eth_btc_stat_arb;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.TradeOrderType;
import com.bybit.api.client.domain.trade.Side;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.log.LogOption;
import com.bybit.api.client.service.BybitApiClientFactory;

import java.util.List;

public class Test extends Main {

    public static void main(String[] args) {
        var factory = BybitApiClientFactory.newInstance(
                System.getenv("API_KEY"),
                System.getenv("API_SECRET"),
                "TRUE".equals(System.getenv("IS_REAL")) ? BybitApiConfig.MAINNET_DOMAIN
                        : BybitApiConfig.DEMO_TRADING_DOMAIN,
                "TRUE".equals(System.getenv("DEBUG")),
                LogOption.OKHTTP3.getLogOptionType());
        var tradeClient = factory.newTradeRestClient();

        var r = List.of(TradeOrderRequest.builder()
                        .category(CategoryType.SPOT)
                        .symbol("ETHUSDT")
                        .side(Side.SELL)
                        .orderType(TradeOrderType.LIMIT)
                        .qty("0.00257")
                        .price("1947.4")
                        .tpLimitPrice("1942.7")
                        .triggerPrice("1943")
                        .tpOrderType(TradeOrderType.LIMIT)
//                        .marketUnit("baseCoin")
                .build()
        );
//        Main.placeBatchOrders(r, tradeClient);
//        Main.getInstrumentInfo(factory.newMarketDataRestClient());
//        tradeClient.createOrder(r.getFirst());
    }
}
