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
import com.bybit.api.client.domain.position.TpslMode;
import com.bybit.api.client.domain.trade.MarketUnit;
import com.bybit.api.client.domain.trade.Side;
import com.bybit.api.client.domain.trade.request.BatchOrderRequest;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.domain.trade.response.OrderEntry;
import com.bybit.api.client.log.LogOption;
import com.bybit.api.client.restApi.BybitApiAccountRestClient;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;
import common.ResponseValidator;
import org.jetbrains.annotations.NotNull;

public class Main {

    private static final String SYMBOL = System.getenv("GRID_SYMBOL");
    private static final BigDecimal MAX_PRICE = new BigDecimal(System.getenv("MAX_PRICE"));
    private static final BigDecimal MIN_PRICE = new BigDecimal(System.getenv("MIN_PRICE"));
    //    private static final BigDecimal START_PRICE = new BigDecimal(System.getenv("START_PRICE"));
//    private static final BigDecimal TRADE_AMOUNT = new BigDecimal(System.getenv("TRADE_AMOUNT"));
    private static final BigDecimal GRID_HEIGHT = new BigDecimal(System.getenv("GRID_HEIGHT"));
    private static final String BASE_COIN = System.getenv("BASE_COIN"); // BTCUSDT -> BTC
    private static final String QUOTE_COIN = System.getenv("QUOTE_COIN"); // BTCUSDT -> USDT


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
        var gridCount = MAX_PRICE.subtract(MIN_PRICE).divideToIntegralValue(GRID_HEIGHT);
        System.out.println("gridCount = " + gridCount);
        var calculatedMaxPrice = GRID_HEIGHT.multiply(gridCount).add(MIN_PRICE);
        System.out.println("calculatedMaxPrice = " + calculatedMaxPrice);

        var marketBestPrices = getMarketBestPrices(marketDataClient);

        var lowestAskPrice = marketBestPrices.ask1Price;
        var highestBidPrice = marketBestPrices.bid1Price;

        var tradeHistory = tradeClient.getTradeHistory(TradeOrderRequest.builder()
                .category(CategoryType.SPOT)
                .symbol(SYMBOL)
                .limit(1)
                .build());
        ResponseValidator.checkResult(tradeHistory);
        if (tradeHistory.getResult() != null
                && tradeHistory.getResult().getOrderEntries() != null
                && !tradeHistory.getResult().getOrderEntries().isEmpty()) {
            OrderEntry lastTrade = tradeHistory.getResult().getOrderEntries().getFirst();
            Side lastSide = lastTrade.getSide();
            BigDecimal lastTradePrice = lastTrade.getOrderPrice();
            System.out.println("lastSide = " + lastSide + ", lastTradePrice = " + lastTradePrice.toString());
//            BigDecimal execQty = lastTrade.getExecQty();
//            BigDecimal execValue = lastTrade.getExecValue();
//            System.out.println("execQty = " + execQty + ", execValue = " + execValue);
            switch (lastSide) {
                case SELL -> lowestAskPrice = lowestAskPrice.max(lastTradePrice.add(GRID_HEIGHT));
                case BUY -> highestBidPrice = highestBidPrice.min(lastTradePrice.subtract(GRID_HEIGHT));
                case null, default -> System.err.println("Unknown Side: " + lastSide);
            }
        }

        var askOrderPrices = calcAskOrderPrices(lowestAskPrice);
        var bidOrderPrices = calcBidOrderPrices(highestBidPrice);

        cancelAllOrders(tradeClient); // first for equity estimation

        var walletBalance = getWalletBalance(accountClient);

        var askOrders = prepareAskOrders(askOrderPrices, walletBalance.baseCoinEquity(), basePrecisionScale);
        var bidOrders = prepareBidOrders(bidOrderPrices, walletBalance.quoteCoinEquity(), basePrecisionScale);
        var allOrders = new ArrayList<TradeOrderRequest>();
        allOrders.addAll(askOrders);
        allOrders.addAll(bidOrders);

        placeBatchOrders(allOrders, tradeClient);

    }

    @NotNull
    private static WalletBalance getWalletBalance(BybitApiAccountRestClient accountClient) {
        var walletBalance = accountClient.getWalletBalance(AccountDataRequest.builder()
                .accountType(AccountType.UNIFIED)
                .coins(String.join(",", BASE_COIN, QUOTE_COIN))
                .build());
        ResponseValidator.checkResult(walletBalance);
        var baseCoinEquity = walletBalance.getResult().getTickerEntries().getFirst().getCoin().stream()
                .filter(coin -> BASE_COIN.equals(coin.getCoin()))
                .findFirst()
                .map(Coin::getEquity)
                .orElseThrow(() -> new IllegalStateException("%s not found".formatted(BASE_COIN)));
        var quoteCoinEquity = walletBalance.getResult().getTickerEntries().getFirst().getCoin().stream()
                .filter(coin -> QUOTE_COIN.equals(coin.getCoin()))
                .findFirst()
                .map(Coin::getEquity)
                .orElseThrow(() -> new IllegalStateException("%s not found".formatted(QUOTE_COIN)));
        System.out.printf("baseCoinEquity = %s %s; quoteCoinEquity = %s %s%n", baseCoinEquity, BASE_COIN, quoteCoinEquity, QUOTE_COIN);
        return new WalletBalance(baseCoinEquity, quoteCoinEquity);
    }

    private record WalletBalance(BigDecimal baseCoinEquity, BigDecimal quoteCoinEquity) {
    }

    @NotNull
    private static List<TradeOrderRequest> prepareBidOrders(List<BigDecimal> bidOrderPrices,
                                                            BigDecimal coinEquity,
                                                            int precisionScale) {
        if (bidOrderPrices.isEmpty()) return List.of();
        var divisor = 1.0;
        for (BigDecimal price : bidOrderPrices) {
            divisor = divisor + 1/price.doubleValue();
        }
        System.out.println("Bid size divisor = " + divisor);
        var bidSizeDouble = coinEquity.doubleValue() / divisor;
        var bidSize = BigDecimal.valueOf(bidSizeDouble).setScale(precisionScale, RoundingMode.DOWN);
        System.out.println("bidSizeDouble = " + bidSizeDouble + ",bidSize = " + bidSize);
        var bidOrders = bidOrderPrices.stream()
                .map(price -> TradeOrderRequest.builder()
                        .category(CategoryType.SPOT)
                        .symbol(SYMBOL)
                        .marketUnit(MarketUnit.QUOTE_COIN.getValue())
                        .side(Side.BUY)
                        .orderType(TradeOrderType.LIMIT)
                        .price(price.toString())
                        .qty(bidSize.toString())
                        .tpLimitPrice(price.add(GRID_HEIGHT).toString())
                        .triggerPrice(price.add(GRID_HEIGHT).toString())
                        .tpOrderType(TradeOrderType.LIMIT)
                        .tpslMode(TpslMode.FULL)
                        .build())
                .toList();
        return bidOrders;
    }

    @NotNull
    private static List<TradeOrderRequest> prepareAskOrders(List<BigDecimal> askOrderPrices,
                                                            BigDecimal coinEquity,
                                                            int precisionScale) {
        if (askOrderPrices.isEmpty()) return List.of();
        var askSize = coinEquity.divide(BigDecimal.valueOf(askOrderPrices.size()), precisionScale, RoundingMode.DOWN);
        System.out.println("askSize = " + askSize);
        var askOrders = askOrderPrices.stream()
                .map(price -> TradeOrderRequest.builder()
                        .category(CategoryType.SPOT)
                        .symbol(SYMBOL)
                        .side(Side.SELL)
                        .marketUnit(MarketUnit.BASE_COIN.getValue())
                        .orderType(TradeOrderType.LIMIT)
                        .price(price.toString())
                        .qty(askSize.toString())
                        .tpLimitPrice(price.subtract(GRID_HEIGHT).toString())
                        .triggerPrice(price.subtract(GRID_HEIGHT).toString())
                        .tpOrderType(TradeOrderType.LIMIT)
                        .tpslMode(TpslMode.FULL)
                        .build())
                .toList();
        return askOrders;
    }

    @NotNull
    private static ArrayList<BigDecimal> calcBidOrderPrices(BigDecimal highestBidPrice) {
        var bidOrderPrice = MIN_PRICE;
        var bidOrderPrices = new ArrayList<BigDecimal>();
        while (bidOrderPrice.compareTo(highestBidPrice) <= 0) {
            bidOrderPrices.add(bidOrderPrice);
            bidOrderPrice = bidOrderPrice.add(GRID_HEIGHT);
        }
        System.out.println("bidOrderPrices = " + bidOrderPrices);
        return bidOrderPrices;
    }

    @NotNull
    private static ArrayList<BigDecimal> calcAskOrderPrices(BigDecimal lowestAskPrice) {
        var askOrderPrice = MAX_PRICE;
        var askOrderPrices = new ArrayList<BigDecimal>();
        while (askOrderPrice.compareTo(lowestAskPrice) >= 0) {
            askOrderPrices.add(askOrderPrice);
            askOrderPrice = askOrderPrice.subtract(GRID_HEIGHT);
        }
        System.out.println("askOrderPrices = " + askOrderPrices);
        return askOrderPrices;
    }

    @NotNull
    private static MarketBestPrices getMarketBestPrices(BybitApiMarketRestClient marketDataClient) {
        var marketDataRequest = MarketDataRequest.builder()
                .category(CategoryType.SPOT)
                .symbol(SYMBOL)
                .build();
        var marketTickers = marketDataClient.getMarketTickers(marketDataRequest);
        ResponseValidator.checkResult(marketTickers);
        var bid1Price = marketTickers.getResult().getTickerEntries().getFirst().getBid1Price();
        var ask1Price = marketTickers.getResult().getTickerEntries().getFirst().getAsk1Price();
        System.out.printf("ask1Price = %s, bid1Price = %s%n", ask1Price, bid1Price);
        MarketBestPrices result = new MarketBestPrices(bid1Price, ask1Price);
        return result;
    }

    private record MarketBestPrices(BigDecimal bid1Price, BigDecimal ask1Price) {
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
        System.out.println("cancelAllOrdersRequest");
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
