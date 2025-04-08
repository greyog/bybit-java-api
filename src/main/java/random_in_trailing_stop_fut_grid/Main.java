package random_in_trailing_stop_fut_grid;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.TradeOrderType;
import com.bybit.api.client.domain.account.AccountType;
import com.bybit.api.client.domain.account.request.AccountDataRequest;
import com.bybit.api.client.domain.account.response.walletBalance.Coin;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.market.response.instrumentInfo.InstrumentEntry;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.domain.position.response.PositionEntry;
import com.bybit.api.client.domain.trade.MarketUnit;
import com.bybit.api.client.domain.trade.Side;
import com.bybit.api.client.domain.trade.request.BatchOrderRequest;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.domain.trade.response.OrderEntry;
import com.bybit.api.client.log.LogOption;
import com.bybit.api.client.restApi.BybitApiAccountRestClient;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;
import common.ResponseValidator;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String SYMBOL = System.getenv("SYMBOL");
    private static final BigDecimal FEE_PERCENT = new BigDecimal(System.getenv("FEE_PERCENT"));
//    private static final BigDecimal SL_PERCENT = new BigDecimal(System.getenv("SL_PERCENT"));
//    private static final String ATR_TIMEFRAME = System.getenv("ATR_TIMEFRAME");
//    private static final String ATR_PERIOD = System.getenv("ATR_PERIOD");

    private static final CategoryType CATEGORY = CategoryType.LINEAR;


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
        var positionRestClient = factory.newPositionRestClient();

        var instrumentInfo = getInstrumentInfo(marketDataClient);
        var minOrderValue = instrumentInfo.getLotSizeFilter().getMinNotionalValue();
        var minOrderQty = instrumentInfo.getLotSizeFilter().getMinOrderQty();
        var tickSize = instrumentInfo.getPriceFilter().getTickSize();
        int tickScale = tickSize.scale();
        System.out.println("tickSize = " + tickSize + ", tickSize.scale() = " + tickScale);

        var marketBestPrices = getMarketBestPrices(marketDataClient);
        var midPrice = marketBestPrices.ask1Price
                .add(marketBestPrices.bid1Price)
                .divide(BigDecimal.TWO, tickScale, RoundingMode.HALF_UP);
        var minOrderQtyByMinNotionalValue = minOrderValue
                .divide(midPrice, minOrderQty.scale(), RoundingMode.CEILING);
        var minOrderQtyByInstrumentParams = minOrderQty.max(minOrderQtyByMinNotionalValue);
        System.out.println("minOrderQtyByInstrumentParams = " + minOrderQtyByInstrumentParams);

        var futuresPositions = getPositions(positionRestClient, SYMBOL);
        printPositions(futuresPositions);
        var position = futuresPositions.stream()
                .filter(p -> p.getSide() != null)
                .filter(p -> p.getSize().compareTo(BigDecimal.ZERO) != 0)
                .findFirst();
        if (position.isPresent()) {
            System.out.println("Position exists. Nothing to do");
            return;
        }


//        var walletBalance = getWalletBalance(accountClient);
//
//        var askOrderQty = calcOrderQty(walletBalance, askOrderPrices, basePrecisionScale, bidOrderPrices, minOrderQty, minOrderValue);
//
//        var resultAskPrices = new ArrayList<BigDecimal>();
//        if (!askOrderPrices.isEmpty()) {
//            askOrderPrices.sort(BigDecimal::compareTo);
//            var balance = walletBalance.baseCoinEquity;
//            int i = 0;
//            while (balance.compareTo(BigDecimal.ZERO) > 0 && i < askOrderPrices.size()) {
//                resultAskPrices.add(askOrderPrices.get(i));
//                i++;
//                balance = balance.subtract(askOrderQty);
//            }
//        }
//
//        var oneMinusFee = BigDecimal.valueOf(1 - FEE_PERCENT.doubleValue() / 100);
//        var bidOrderQty = askOrderQty
//                .divide(oneMinusFee, basePrecisionScale, RoundingMode.CEILING);
//        System.out.println("ask orderQty = " + askOrderQty + ", bid orderQty = " + bidOrderQty);
//
//        var minGridHeight = MAX_PRICE
//                .multiply(bidOrderQty
//                        .divide(askOrderQty, 10, RoundingMode.HALF_UP)
//                        .divide(oneMinusFee, 10, RoundingMode.HALF_UP)
//                        .subtract(BigDecimal.ONE))
//                .setScale(tickScale, RoundingMode.CEILING);
//        System.out.println("minGridHeight = " + minGridHeight);
//        if (GRID_HEIGHT.compareTo(minGridHeight) <= 0) {
//            System.err.println("+++++++++++++WARNING!++++++++++++++++ " +
//                    "GRID_HEIGHT %s is less than Minimum profitable grid height %s".formatted(GRID_HEIGHT, minGridHeight));
//        }
//
//        var resultBidPrices = new ArrayList<BigDecimal>();
//        if (!bidOrderPrices.isEmpty()) {
//            bidOrderPrices.sort(BigDecimal::compareTo);
//            bidOrderPrices = bidOrderPrices.reversed();
//            var balance = walletBalance.quoteCoinEquity;
//            int i = 0;
//            while (balance.compareTo(BigDecimal.ZERO) > 0 && i < bidOrderPrices.size()) {
//                var price = bidOrderPrices.get(i);
//                resultBidPrices.add(price);
//                i++;
//                var orderValue = bidOrderQty
//                        .multiply(price);
//                balance = balance.subtract(orderValue);
//            }
//        }
//        System.out.println("resultAskPrices = " + resultAskPrices);
//        System.out.println("resultBidPrices = " + resultBidPrices);
//        var askOrders = prepareAskOrders(resultAskPrices, askOrderQty);
//        var bidOrders = prepareBidOrders(resultBidPrices, bidOrderQty);
//        var allOrders = new ArrayList<TradeOrderRequest>();
//        allOrders.addAll(askOrders);
//        allOrders.addAll(bidOrders);
//        var midPrice = lowestAskPrice.add(highestBidPrice).divide(BigDecimal.TWO, tickScale, RoundingMode.HALF_UP);
//        var allOrdersSortedFiltered = allOrders.stream()
//                .sorted((o1, o2) -> {
//                    var o1Price = new BigDecimal(o1.getPrice());
//                    var o2Price = new BigDecimal(o2.getPrice());
//                    var o1Offset = midPrice.subtract(o1Price).abs();
//                    var o2Offset = midPrice.subtract(o2Price).abs();
//                    return o1Offset.compareTo(o2Offset);
//                })
//                .toList();
////        allOrdersSortedFiltered.forEach(System.out::println);
//        placeBatchOrders(allOrdersSortedFiltered, tradeClient);

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

//    @NotNull
//    private static WalletBalance getWalletBalance(BybitApiAccountRestClient accountClient) {
//        var walletBalance = accountClient.getWalletBalance(AccountDataRequest.builder()
//                .accountType(AccountType.UNIFIED)
//                .coins(String.join(",", QUOTE_COIN))
//                .build());
//        ResponseValidator.checkResult(walletBalance);
//        var baseCoinEquity = walletBalance.getResult().getTickerEntries().getFirst().getCoin().stream()
//                .filter(coin -> BASE_COIN.equals(coin.getCoin()))
//                .findFirst()
//                .map(Coin::getEquity)
//                .orElseThrow(() -> new IllegalStateException("%s not found".formatted(BASE_COIN)));
//        var quoteCoinEquity = walletBalance.getResult().getTickerEntries().getFirst().getCoin().stream()
//                .filter(coin -> QUOTE_COIN.equals(coin.getCoin()))
//                .findFirst()
//                .map(Coin::getEquity)
//                .orElseThrow(() -> new IllegalStateException("%s not found".formatted(QUOTE_COIN)));
//        System.out.printf("baseCoinEquity = %s %s; quoteCoinEquity = %s %s%n", baseCoinEquity, BASE_COIN, quoteCoinEquity, QUOTE_COIN);
//        return new WalletBalance(baseCoinEquity, quoteCoinEquity);
//    }

    private record WalletBalance(BigDecimal baseCoinEquity, BigDecimal quoteCoinEquity) {
    }

    @NotNull
    private static List<TradeOrderRequest> prepareBidOrders(List<BigDecimal> bidOrderPrices,
                                                            BigDecimal size) {
        if (bidOrderPrices.isEmpty()) return List.of();
        var bidOrders = bidOrderPrices.stream()
                .map(price -> TradeOrderRequest.builder()
                        .category(CATEGORY)
                        .symbol(SYMBOL)
                        .marketUnit(MarketUnit.QUOTE_COIN.getValue())
                        .side(Side.BUY)
                        .orderType(TradeOrderType.LIMIT)
                        .price(price.toString())
                        .qty(size.toString())
//                        .tpLimitPrice(price.add(GRID_HEIGHT).toString())
//                        .triggerPrice(price.add(GRID_HEIGHT).toString())
//                        .tpOrderType(TradeOrderType.LIMIT)
//                        .tpslMode(TpslMode.FULL)
                        .build())
                .toList();
        return bidOrders;
    }

    @NotNull
    private static MarketBestPrices getMarketBestPrices(BybitApiMarketRestClient marketDataClient) {
        var marketDataRequest = MarketDataRequest.builder()
                .category(CATEGORY)
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
                .category(CATEGORY)
                .symbol(SYMBOL)
                .build();
        var instrumentsInfoResponse = marketDataClient.getInstrumentsInfo(instrumentInfoRequest);
        ResponseValidator.checkResult(instrumentsInfoResponse);
        System.out.println(instrumentsInfoResponse.getResult().toString());

        return instrumentsInfoResponse.getResult().getInstrumentEntries().getFirst();
    }

    @NotNull
    public static List<PositionEntry> getPositions(BybitApiPositionRestClient positionRestClient,
                                                   String symbol) {
        List<PositionEntry> positionEntries = new ArrayList<>();
        String nextPageCursor = null;
        PositionDataRequest.PositionDataRequestBuilder requestBuilder = PositionDataRequest.builder()
                .category(CATEGORY)
                .symbol(symbol);
        do {
            var optionPositionInfo = positionRestClient.getPositionInfo(requestBuilder
                    .cursor(nextPageCursor)
                    .build());
            ResponseValidator.checkResult(optionPositionInfo);
            positionEntries.addAll(optionPositionInfo.getResult().getPositionEntries());
            nextPageCursor = optionPositionInfo.getResult().getNextPageCursor();
        } while (!nextPageCursor.isEmpty());
        return positionEntries;
    }

    private static void printPositions(List<PositionEntry> positions) {
        positions.stream()
                .map(pos -> String.format("Symbol: %s, side: %s, size: %s, avgPrice: %s",
                        pos.getSymbol(), pos.getSide(), pos.getSize(), pos.getAvgPrice()))
                .forEach(System.out::println);
    }

    public static void cancelAllOrders(BybitApiTradeRestClient tradeClient) {
        var cancelAllOrdersRequest = TradeOrderRequest.builder()
                .category(CATEGORY)
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
                        .category(CATEGORY)
                        .request(tradeOrderRequests.subList(slow, i))
                        .build());
                ResponseValidator.checkResult(batchOrderResult);
                slow = i;
            }
        }
        var batchOrderResult = tradeClient.createBatchOrder(BatchOrderRequest.builder()
                .category(CATEGORY)
                .request(tradeOrderRequests.subList(slow, tradeOrderRequests.size()))
                .build());
        ResponseValidator.checkResult(batchOrderResult);
    }

}
