package usdc_usdt_grid;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.TradeOrderType;
import com.bybit.api.client.domain.account.AccountType;
import com.bybit.api.client.domain.account.request.AccountDataRequest;
import com.bybit.api.client.domain.account.response.walletBalance.Coin;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.market.response.instrumentInfo.InstrumentEntry;
import com.bybit.api.client.domain.trade.MarketUnit;
import com.bybit.api.client.domain.trade.Side;
import com.bybit.api.client.domain.trade.request.BatchOrderRequest;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.log.LogOption;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;
import common.ResponseValidator;

public class Main {

    private static final String SYMBOL = System.getenv("GRID_SYMBOL");
    private static final BigDecimal MAX_PRICE = new BigDecimal(System.getenv("MAX_PRICE"));
    private static final BigDecimal MIN_PRICE = new BigDecimal(System.getenv("MIN_PRICE"));
//    private static final BigDecimal START_PRICE = new BigDecimal(System.getenv("START_PRICE"));
//    private static final BigDecimal TRADE_AMOUNT = new BigDecimal(System.getenv("TRADE_AMOUNT"));
    private static final BigDecimal GRID_COUNT = new BigDecimal(System.getenv("GRID_COUNT"));
    private static final String BASE_COIN = System.getenv("BASE_COIN");
    private static final String QUOTE_COIN = System.getenv("QUOTE_COIN");


    public static void main(String[] args) {
        var factory = BybitApiClientFactory.newInstance(
                System.getenv("API_KEY"),
                System.getenv("API_SECRET"),
                "TRUE".equals(System.getenv("IS_REAL")) ? BybitApiConfig.MAINNET_DOMAIN
                        : BybitApiConfig.DEMO_TRADING_DOMAIN,
                "TRUE".equals(System.getenv("DEBUG")),
                LogOption.OKHTTP3.getLogOptionType());
        var tradeClient = factory.newTradeRestClient();
        var marketDataClient = factory.newMarketDataRestClient();
        var accountClient = factory.newAccountRestClient();

        var instrumentInfo = getInstrumentInfo(marketDataClient);
        var tickSize = instrumentInfo.getPriceFilter().getTickSize();
        int tickScale = tickSize.scale();
        System.out.println("tickSize = " + tickSize + ", tickSize.scale() = " + tickScale);
        var basePrecision = instrumentInfo.getLotSizeFilter().getBasePrecision();
        int basePrecisionScale = basePrecision.scale();
        var quotePrecision = instrumentInfo.getLotSizeFilter().getQuotePrecision();
        int quotePrecisionScale = quotePrecision.scale();
        System.out.println("basePrecisionScale = " + basePrecisionScale + ", quotePrecisionScale = " + quotePrecisionScale);
        var gridHeight = MAX_PRICE.subtract(MIN_PRICE).divide(GRID_COUNT, tickScale, RoundingMode.HALF_UP);
        System.out.println("gridHeight = " + gridHeight);
        var calculatedMaxPrice = gridHeight.multiply(GRID_COUNT).add(MIN_PRICE);
        System.out.println("calculatedMaxPrice = " + calculatedMaxPrice);
//        var gridLot = TRADE_AMOUNT.divide(GRID_COUNT.add(BigDecimal.ONE), tickScale, RoundingMode.HALF_DOWN);
//        System.out.println("gridLot = " + gridLot);

        var marketDataRequest = MarketDataRequest.builder()
                .category(CategoryType.SPOT)
                .symbol(SYMBOL)
                .build();
        var marketTickers = marketDataClient.getMarketTickers(marketDataRequest);
        ResponseValidator.checkResult(marketTickers);
        var bid1Price = marketTickers.getResult().getTickerEntries().getFirst().getBid1Price();
        var ask1Price = marketTickers.getResult().getTickerEntries().getFirst().getAsk1Price();
        System.out.printf("ask1Price = %s, bid1Price = %s%n", ask1Price, bid1Price);



        var askOrderPrice = MAX_PRICE;
        var askOrderPrices = new ArrayList<BigDecimal>();
        while (askOrderPrice.compareTo(ask1Price) >= 0) {
            askOrderPrices.add(askOrderPrice);
            askOrderPrice = askOrderPrice.subtract(gridHeight);
        }
        System.out.println("askOrderPrices = " + askOrderPrices);

        var bidOrderPrice = MIN_PRICE;
        var bidOrderPrices = new ArrayList<BigDecimal>();
        while (bidOrderPrice.compareTo(bid1Price) <= 0) {
            bidOrderPrices.add(bidOrderPrice);
            bidOrderPrice = bidOrderPrice.add(gridHeight);
        }
        System.out.println("bidOrderPrices = " + bidOrderPrices);

        cancelAllOrders(tradeClient); // first for equity estimation

        var walletBalance = accountClient.getWalletBalance(AccountDataRequest.builder()
                .accountType(AccountType.UNIFIED)
                .coins(String.join(",", BASE_COIN, QUOTE_COIN))
                .build());
        ResponseValidator.checkResult(walletBalance);
        var baseCoinEquity = walletBalance.getResult().getTickerEntries().getFirst().getCoin().stream()
                .filter(coin -> BASE_COIN.equals(coin.getCoin()))
                .findFirst()
                .map(Coin::getEquity)
                .orElseThrow();
        var quoteCoinEquity = walletBalance.getResult().getTickerEntries().getFirst().getCoin().stream()
                .filter(coin -> QUOTE_COIN.equals(coin.getCoin()))
                .findFirst()
                .map(Coin::getEquity)
                .orElseThrow();
        System.out.printf("baseCoinEquity = %s %s; quoteCoinEquity = %s %s%n", baseCoinEquity, BASE_COIN, quoteCoinEquity, QUOTE_COIN);


        var askSize = baseCoinEquity.divide(BigDecimal.valueOf(askOrderPrices.size()), basePrecisionScale, RoundingMode.DOWN);
        var divisor = BigDecimal.ZERO;
        for (BigDecimal price : bidOrderPrices) {
            divisor = divisor.add(BigDecimal.ONE.divide(price, RoundingMode.HALF_UP));
        }
        var bidSize = quoteCoinEquity.divide(divisor, basePrecisionScale, RoundingMode.DOWN);
        System.out.println("askSize = %s, bidSize = %s".formatted(askSize, bidSize));

        var askOrders = askOrderPrices.stream()
                .map(price -> TradeOrderRequest.builder()
                        .category(CategoryType.SPOT)
                        .symbol(SYMBOL)
                        .side(Side.SELL)
                        .marketUnit(MarketUnit.BASE_COIN.getValue())
                        .orderType(TradeOrderType.LIMIT)
                        .price(price.toString())
                        .qty(askSize.toString())
                        .build())
                .toList();
        var bidOrders = bidOrderPrices.stream()
                .map(price -> TradeOrderRequest.builder()
                        .category(CategoryType.SPOT)
                        .symbol(SYMBOL)
                        .marketUnit(MarketUnit.QUOTE_COIN.getValue())
                        .side(Side.BUY)
                        .orderType(TradeOrderType.LIMIT)
                        .price(price.toString())
                        .qty(bidSize.toString())
                        .build())
                .toList();
        var allOrders = new ArrayList<TradeOrderRequest>();
        allOrders.addAll(askOrders);
        allOrders.addAll(bidOrders);

        placeBatchOrders(allOrders, tradeClient);

    }

    private static InstrumentEntry getInstrumentInfo(BybitApiMarketRestClient marketDataClient) {
        var instrumentInfoRequest = MarketDataRequest.builder()
                .category(CategoryType.SPOT)
                .symbol(SYMBOL)
                .build();
        var instrumentsInfoResponse = marketDataClient.getInstrumentsInfo(instrumentInfoRequest);
        ResponseValidator.checkResult(instrumentsInfoResponse);
        System.out.println(instrumentsInfoResponse.getResult().toString());

        return instrumentsInfoResponse.getResult().getInstrumentEntries().getFirst();
    }

    public static void cancelAllOrders(BybitApiTradeRestClient tradeClient) {
        var cancelAllOrdersRequest = TradeOrderRequest.builder()
                .category(CategoryType.SPOT)
                .symbol(SYMBOL)
                .build();
        System.out.println("cancelAllOrdersRequest = " + cancelAllOrdersRequest);
        var order = tradeClient.cancelAllOrder(cancelAllOrdersRequest);
        ResponseValidator.checkResult(order);
    }

    private static void placeBatchOrders(List<TradeOrderRequest> tradeOrderRequests, BybitApiTradeRestClient tradeClient) {
        if (tradeOrderRequests.isEmpty()) {
            System.out.println("There is no orders");
            return;
        }
        int maxBatchSize = 10;
        int slow = 0;
        for (int i = 1; i < tradeOrderRequests.size(); i++) {
            if (i % maxBatchSize == 0) {
                var batchOrderResult = tradeClient.createBatchOrder(BatchOrderRequest.builder()
                        .category(CategoryType.SPOT)
                        .request(tradeOrderRequests.subList(slow, i))
                        .build());
                ResponseValidator.checkResult(batchOrderResult);
                slow = i;
            }
        }
        var batchOrderResult = tradeClient.createBatchOrder(BatchOrderRequest.builder()
                .category(CategoryType.SPOT)
                .request(tradeOrderRequests.subList(slow, tradeOrderRequests.size()))
                .build());
        ResponseValidator.checkResult(batchOrderResult);
    }

}
