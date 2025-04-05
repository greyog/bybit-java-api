package eth_usdt_spot_grid;

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
import com.bybit.api.client.domain.trade.response.OrderEntry;
import com.bybit.api.client.log.LogOption;
import com.bybit.api.client.restApi.BybitApiAccountRestClient;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;
import common.ResponseValidator;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String SYMBOL = System.getenv("GRID_SYMBOL");
    private static final BigDecimal MAX_PRICE = new BigDecimal(System.getenv("MAX_PRICE"));
    private static final BigDecimal MIN_PRICE = new BigDecimal(System.getenv("MIN_PRICE"));
    private static final BigDecimal FEE_PERCENT = new BigDecimal(System.getenv("FEE_PERCENT"));
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
        var minOrderValue = instrumentInfo.getLotSizeFilter().getMinOrderAmt();
        var minOrderQty = instrumentInfo.getLotSizeFilter().getMinOrderQty();
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
                case SELL, BUY -> {
                    lowestAskPrice = (lastTradePrice.add(GRID_HEIGHT));
                    highestBidPrice = (lastTradePrice.subtract(GRID_HEIGHT));
//                    lowestAskPrice = lowestAskPrice.max(lastTradePrice.add(GRID_HEIGHT));
//                    highestBidPrice = highestBidPrice.min(lastTradePrice.subtract(GRID_HEIGHT));
                }
//                case BUY -> highestBidPrice = highestBidPrice.min(lastTradePrice.subtract(GRID_HEIGHT));

                case null, default -> System.err.println("Unknown Side: " + lastSide);
            }
        }

        var bidOrderPrices = calcBidOrderPrices(highestBidPrice);
//        var maxBidPrice = bidOrderPrices.stream()
//                .max(BigDecimal::compareTo)
//                .orElse(lowestAskPrice.subtract(GRID_HEIGHT));
        var askOrderPrices = calcAskOrderPrices(lowestAskPrice);

        cancelAllOrders(tradeClient); // first for equity estimation

        var walletBalance = getWalletBalance(accountClient);

        var orderQty = calcOrderQty(walletBalance, askOrderPrices, basePrecisionScale, bidOrderPrices, minOrderQty, minOrderValue);

        var resultAskPrices = new ArrayList<BigDecimal>();
        if (!askOrderPrices.isEmpty()) {
            askOrderPrices.sort(BigDecimal::compareTo);
            var balance = walletBalance.baseCoinEquity;
            int i = 0;
            while (balance.compareTo(BigDecimal.ZERO) > 0 && i < askOrderPrices.size()) {
                resultAskPrices.add(askOrderPrices.get(i));
                i++;
                balance = balance.subtract(orderQty);
            }
        }

        var onePlusFee = BigDecimal.valueOf(1 + FEE_PERCENT.doubleValue() / 100);
        var bidOrderQty = onePlusFee
                .multiply(orderQty)
                .setScale(basePrecisionScale, RoundingMode.CEILING);
        System.out.println("ask orderQty = " + orderQty + ", bid orderQty = " + bidOrderQty);

        var resultBidPrices = new ArrayList<BigDecimal>();
        if (!bidOrderPrices.isEmpty()) {
            bidOrderPrices.sort(BigDecimal::compareTo);
            bidOrderPrices = bidOrderPrices.reversed();
            var balance = walletBalance.quoteCoinEquity;
            int i = 0;
            while (balance.compareTo(BigDecimal.ZERO) > 0 && i < bidOrderPrices.size()) {
                var price = bidOrderPrices.get(i);
                resultBidPrices.add(price);
                i++;
                var orderValue = bidOrderQty
                        .multiply(price);
                balance = balance.subtract(orderValue);
            }
        }
        System.out.println("resultAskPrices = " + resultAskPrices);
        System.out.println("resultBidPrices = " + resultBidPrices);
        var askOrders = prepareAskOrders(resultAskPrices, orderQty);
        var bidOrders = prepareBidOrders(resultBidPrices, bidOrderQty);
        var allOrders = new ArrayList<TradeOrderRequest>();
        allOrders.addAll(askOrders);
        allOrders.addAll(bidOrders);
        var midPrice = lowestAskPrice.add(highestBidPrice).divide(BigDecimal.TWO, tickScale, RoundingMode.HALF_UP);
        var allOrdersSortedFiltered = allOrders.stream()
                .sorted((o1, o2) -> {
                    var o1Price = new BigDecimal(o1.getPrice());
                    var o2Price = new BigDecimal(o2.getPrice());
                    var o1Offset = midPrice.subtract(o1Price).abs();
                    var o2Offset = midPrice.subtract(o2Price).abs();
                    return o1Offset.compareTo(o2Offset);
                })
                .toList();
//        allOrdersSortedFiltered.forEach(System.out::println);
        placeBatchOrders(allOrdersSortedFiltered, tradeClient);

//        placeBatchOrders(askOrders, tradeClient);
//        placeBatchOrders(bidOrders, tradeClient);

    }

    @NotNull
    private static BigDecimal calcOrderQty(WalletBalance walletBalance, List<BigDecimal> askOrderPrices,
                                           int basePrecisionScale,
                                           List<BigDecimal> bidOrderPrices,
                                           BigDecimal minOrderQty, BigDecimal minOrderValue) {
        var askSize = askOrderPrices.isEmpty()
                ? BigDecimal.valueOf(Long.MAX_VALUE)
                : walletBalance.baseCoinEquity().divide(BigDecimal.valueOf(askOrderPrices.size()), basePrecisionScale, RoundingMode.DOWN);
        System.out.println("askSize calculated on grid = " + askSize);
        BigDecimal bidSize;
        if (bidOrderPrices.isEmpty()) {
            bidSize = BigDecimal.valueOf(Long.MAX_VALUE);
        } else {
            var divisor = 0.0;
            for (BigDecimal price : bidOrderPrices) {
                divisor = divisor + price.doubleValue();
            }
            System.out.println("Bid size divisor = " + divisor);
            var feeMultiplier = 1 - FEE_PERCENT.doubleValue() / 100;
            var bidSizeDouble = walletBalance.quoteCoinEquity().doubleValue() / divisor * feeMultiplier;
            bidSize = BigDecimal.valueOf(bidSizeDouble)
                    .setScale(basePrecisionScale, RoundingMode.DOWN);
        }
        System.out.println("bidSize calculated on grid = " + bidSize);

        var minAskPrice = askOrderPrices.stream()
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.valueOf(Long.MAX_VALUE));
        var minOrderQtyByValueAndMinAskPrice = minOrderValue.divide(minAskPrice, basePrecisionScale, RoundingMode.CEILING);
        System.out.println("minOrderQtyByValueAndMinAskPrice = " + minOrderQtyByValueAndMinAskPrice);

        var minBidPrice = bidOrderPrices.stream()
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.valueOf(Long.MAX_VALUE));
        var minOrderQtyByValueAndMinBidPrice = minOrderValue.divide(minBidPrice, basePrecisionScale, RoundingMode.CEILING);
        System.out.println("minOrderQtyByValueAndMinBidPrice = " + minOrderQtyByValueAndMinBidPrice);

        var minOrderQtyByValueAndMinPrice = minOrderQtyByValueAndMinBidPrice.max(minOrderQtyByValueAndMinAskPrice);

        askSize = askSize.max(minOrderQtyByValueAndMinPrice);
        bidSize = bidSize.max(minOrderQtyByValueAndMinPrice);
        System.out.println("askSize = " + askSize + ", bidSize = " + bidSize);
        return askSize.min(bidSize);
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
                                                            BigDecimal size) {
        if (bidOrderPrices.isEmpty()) return List.of();
        var bidOrders = bidOrderPrices.stream()
                .map(price -> TradeOrderRequest.builder()
                        .category(CategoryType.SPOT)
                        .symbol(SYMBOL)
                        .marketUnit(MarketUnit.QUOTE_COIN.getValue())
                        .side(Side.BUY)
                        .orderType(TradeOrderType.LIMIT)
                        .price(price.toString())
                        .qty(size.toString())
                        .tpLimitPrice(price.add(GRID_HEIGHT).toString())
//                        .triggerPrice(price.add(GRID_HEIGHT).toString())
                        .tpOrderType(TradeOrderType.LIMIT)
//                        .tpslMode(TpslMode.FULL)
                        .build())
                .toList();
        return bidOrders;
    }

    @NotNull
    private static List<TradeOrderRequest> prepareAskOrders(List<BigDecimal> askOrderPrices, BigDecimal size) {
        if (askOrderPrices.isEmpty()) return List.of();
        var askOrders = askOrderPrices.stream()
                .map(price -> TradeOrderRequest.builder()
                        .category(CategoryType.SPOT)
                        .symbol(SYMBOL)
                        .side(Side.SELL)
                        .marketUnit(MarketUnit.BASE_COIN.getValue())
                        .orderType(TradeOrderType.LIMIT)
                        .price(price.toString())
                        .qty(size.toString())
                        .tpLimitPrice(price.subtract(GRID_HEIGHT).toString())
//                        .triggerPrice(price.subtract(GRID_HEIGHT).toString())
                        .tpOrderType(TradeOrderType.LIMIT)
//                        .tpslMode(TpslMode.FULL)
                        .build())
                .toList();
        return askOrders;
    }

    @NotNull
    private static List<BigDecimal> calcBidOrderPrices(BigDecimal highestBidPrice) {
        var bidOrderPrice = MIN_PRICE;
        var bidOrderPrices = new ArrayList<BigDecimal>();
        while (bidOrderPrice.compareTo(highestBidPrice) <= 0) {
            bidOrderPrices.add(bidOrderPrice);
            bidOrderPrice = bidOrderPrice.add(GRID_HEIGHT);
        }
        List<BigDecimal> reversed = bidOrderPrices.reversed();
        System.out.println("bidOrderPrices = " + reversed);
        return reversed;
    }

    @NotNull
    private static List<BigDecimal> calcAskOrderPrices(BigDecimal lowestAskPrice) {
        var askOrderPrice = lowestAskPrice;
        var askOrderPrices = new ArrayList<BigDecimal>();
        while (askOrderPrice.compareTo(MAX_PRICE) <= 0) {
            askOrderPrices.add(askOrderPrice);
            askOrderPrice = askOrderPrice.add(GRID_HEIGHT);
        }
        List<BigDecimal> reversed = askOrderPrices.reversed();
        System.out.println("askOrderPrices = " + reversed);
        return reversed;
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

    public static InstrumentEntry getInstrumentInfo(BybitApiMarketRestClient marketDataClient) {
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

    public static void placeBatchOrders(List<TradeOrderRequest> tradeOrderRequests, BybitApiTradeRestClient tradeClient) {
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
