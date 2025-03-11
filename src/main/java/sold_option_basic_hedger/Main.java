package sold_option_basic_hedger;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.GenericResponse;
import com.bybit.api.client.domain.TradeOrderType;
import com.bybit.api.client.domain.TriggerBy;
import com.bybit.api.client.domain.market.OptionType;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.market.response.tickers.TickerEntry;
import com.bybit.api.client.domain.position.TpslMode;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.domain.position.response.PositionEntry;
import com.bybit.api.client.domain.trade.Side;
import com.bybit.api.client.domain.trade.TriggerDirection;
import com.bybit.api.client.domain.trade.request.BatchOrderRequest;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.domain.trade.response.OrderEntry;
import com.bybit.api.client.exception.BybitApiException;
import com.bybit.api.client.log.LogOption;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;

import common.ResponseValidator;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterators;

import javax.sql.CommonDataSource;

public class Main {

    public static final String HEDGE_SYMBOL = System.getenv("HEDGE_SYMBOL");
    private static final BigDecimal SL_OFFSET = BigDecimal.ZERO;
    private static final BigDecimal TRIGGER_SL_OFFSET = BigDecimal.valueOf(0.05);

    public static void main(String[] args) {
        var factory = BybitApiClientFactory.newInstance(
                System.getenv("API_KEY"),
                System.getenv("API_SECRET"),
                "TRUE".equals(System.getenv("IS_REAL")) ? BybitApiConfig.MAINNET_DOMAIN
                        : BybitApiConfig.DEMO_TRADING_DOMAIN,
                "TRUE".equals(System.getenv("DEBUG")),
                LogOption.OKHTTP3.getLogOptionType());
        var tradeClient = factory.newTradeRestClient();
        var positionRestClient = factory.newPositionRestClient();
        var marketRestClient = factory.newMarketDataRestClient();

        List<PositionEntry> optionPositions = getPositions(CategoryType.OPTION, positionRestClient, null);
        System.out.println("Options positions:");
        printPositions(optionPositions);

        List<PositionEntry> futuresPositions = getPositions(CategoryType.LINEAR, positionRestClient, HEDGE_SYMBOL);
        System.out.println("Futures positions:");
        printPositions(futuresPositions);

        Optional<PositionEntry> futuresPosition = futuresPositions.stream()
                .filter(positionEntry -> positionEntry.getSize().compareTo(BigDecimal.ZERO) != 0)
                .findFirst();

        var hedgeFuturesPrice = getHedgeTickerInfo(marketRestClient).getLastPrice();
        System.out.println("hedgeFuturesPrice = " + hedgeFuturesPrice);

        var targetFuturesPosition = BigDecimal.ZERO;
        // sold Call hedge
        var slSellPriceQty = new HashMap<String, BigDecimal>();
        var triggerBuyPriceQty = new HashMap<String, BigDecimal>();
        // sold Put hedge
        var slBuyPriceQty = new HashMap<String, BigDecimal>();
        var triggerSellPriceQty = new HashMap<String, BigDecimal>();


        for (PositionEntry pos : optionPositions) {
            if (Side.BUY.equals(pos.getSide())) {
                continue; // we will hedge only sold option
            }
            var typeAndStrike = getOptionTypeAndStrikePrice(pos.getSymbol());
            var strikePrice = typeAndStrike.getRight();
            var type = typeAndStrike.getLeft();
            switch (type) {
                case CALL:
                    if (strikePrice.compareTo(hedgeFuturesPrice) >= 0) { // strike price above last, place trigger
                        var oldSize = triggerBuyPriceQty.getOrDefault(strikePrice.toString(), BigDecimal.ZERO);
                        var newSize = oldSize.add(pos.getSize());
                        triggerBuyPriceQty.put(strikePrice.toString(), newSize);
                    } else { // strike price below last, need to hedge immediately
                        targetFuturesPosition = targetFuturesPosition.add(pos.getSize());
                        var slPrice = strikePrice.subtract(SL_OFFSET);
                        var oldSize = slSellPriceQty.getOrDefault(slPrice.toString(), BigDecimal.ZERO);
                        var newSize = oldSize
                                .add(pos.getSize());
                        slSellPriceQty.put(slPrice.toString(), newSize);
                    }
                    break;
                case PUT:
                    if (strikePrice.compareTo(hedgeFuturesPrice) > 0) { // strike price above last price, need to hedge immediately
                        targetFuturesPosition = targetFuturesPosition.subtract(pos.getSize());
                        var slPrice = strikePrice.add(SL_OFFSET);
                        var oldSize = slBuyPriceQty.getOrDefault(slPrice.toString(), BigDecimal.ZERO);
                        var newSize = oldSize
                                .add(pos.getSize());
                        slBuyPriceQty.put(slPrice.toString(), newSize);
                    } else { // strike price below last, place trigger
                        var oldSize = triggerSellPriceQty.getOrDefault(strikePrice.toString(), BigDecimal.ZERO);
                        var newSize = oldSize.add(pos.getSize());
                        triggerSellPriceQty.put(strikePrice.toString(), newSize);
                    }
            }
        }


        System.out.println("targetFuturesPosition = " + targetFuturesPosition);
        System.out.println("To hedge sold Calls:");
        System.out.println("slSellPriceQty = " + slSellPriceQty);
        System.out.println("triggerBuyPriceQty = " + triggerBuyPriceQty);
        System.out.println("To hedge sold Puts:");
        System.out.println("slBuyPriceQty = " + slBuyPriceQty);
        System.out.println("triggerSellPriceQty = " + triggerSellPriceQty);

        var currentFuturesPosition = futuresPosition.map(positionEntry -> {
            switch (positionEntry.getSide()) {
                case BUY:
                    return positionEntry.getSize();
                case SELL:
                    return positionEntry.getSize().multiply(BigDecimal.valueOf(-1));
            }
            return null;
        }).orElse(BigDecimal.ZERO);
        System.out.println("currentFuturesPosition = " + currentFuturesPosition);
        if (targetFuturesPosition.compareTo(currentFuturesPosition) != 0) {
            System.out.println("Need to change futures position");
            var futuresPositionDelta = targetFuturesPosition.subtract(currentFuturesPosition);
            System.out.println("futuresPositionDelta = " + futuresPositionDelta);
            Side side = null;
            switch (futuresPositionDelta.signum()) {
                case -1: side = Side.SELL; break;
                case 1: side = Side.BUY; break;
            }
            System.out.println("hedge order side = " + side);
            placeMarketOrder(futuresPositionDelta.abs(), side, tradeClient);
        } else {
            System.out.println("Don't need to change futures position");
        }

        cancelAllFuturesOrders(tradeClient);
        var tradeOrderRequests = new ArrayList<TradeOrderRequest>();
        // hedge sold Calls
        slSellPriceQty.forEach((slPrice, slSize) -> {
            tradeOrderRequests.add(
                    placeStopLossOrder(new BigDecimal(slPrice), slSize, Side.SELL)
            );
        });
        triggerBuyPriceQty.forEach((triggerPrice, triggerSize) -> {
            tradeOrderRequests.add(
                    placeTriggerOrder(new BigDecimal(triggerPrice), triggerSize, Side.BUY)
            );
        });
        // hedge sold Puts
        slBuyPriceQty.forEach((slPrice, slSize) -> {
            tradeOrderRequests.add(
                    placeStopLossOrder(new BigDecimal(slPrice), slSize, Side.BUY)
            );
        });
        triggerSellPriceQty.forEach((triggerPrice, triggerSize) -> {
            tradeOrderRequests.add(
                    placeTriggerOrder(new BigDecimal(triggerPrice), triggerSize, Side.SELL)
            );
        });
        System.out.println("tradeOrderRequests.size() = " + tradeOrderRequests.size());
        placeBatchOrders(tradeOrderRequests, tradeClient);

    }

    private static void printPositions(List<PositionEntry> positions) {
        positions.stream()
                .map(pos -> String.format("Symbol: %s, side: %s, size: %s, avgPrice: %s",
                        pos.getSymbol(), pos.getSide(), pos.getSize(), pos.getAvgPrice()))
                .forEach(System.out::println);
    }

    private static void placeBatchOrders(ArrayList<TradeOrderRequest> tradeOrderRequests, BybitApiTradeRestClient tradeClient) {
        if (tradeOrderRequests.isEmpty()) {
            System.out.println("There is no orders");
            return;
        }
        int maxBatchSize = 20;
        int slow = 0;
        for (int i = 1; i < tradeOrderRequests.size(); i++) {
            if (i % maxBatchSize == 0) {
                var batchOrderResult = tradeClient.createBatchOrder(BatchOrderRequest.builder()
                        .category(CategoryType.LINEAR)
                        .request(tradeOrderRequests.subList(slow, i))
                        .build());
                ResponseValidator.checkResult(batchOrderResult);
                slow = i;
            }
        }
        var batchOrderResult = tradeClient.createBatchOrder(BatchOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .request(tradeOrderRequests.subList(slow, tradeOrderRequests.size()))
                .build());
        ResponseValidator.checkResult(batchOrderResult);
    }

    private static TradeOrderRequest placeTriggerOrder(BigDecimal strikePrice, BigDecimal size, Side side) {
        var newTriggerOrderRequest = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(HEDGE_SYMBOL)
                .qty(size.toString())
                .side(side)
                .orderType(TradeOrderType.MARKET)
                .triggerPrice(strikePrice.toString())
                .triggerBy(TriggerBy.LAST_PRICE)
                .triggerDirection(side == Side.BUY ? TriggerDirection.RISE_TO_TRIGGER_PRICE
                        : TriggerDirection.FALL_TO_TRIGGER_PRICE)
                .stopLoss((side == Side.BUY ? strikePrice.subtract(TRIGGER_SL_OFFSET)
                        : strikePrice.add(TRIGGER_SL_OFFSET))
                        .toString())
                .build();
        System.out.println("newTriggerOrderRequest = " + newTriggerOrderRequest);
        return newTriggerOrderRequest;
    }

    private static void placeMarketOrder(BigDecimal size, Side side,
                                         BybitApiTradeRestClient tradeClient) {
        var newMarketOrderRequest = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(HEDGE_SYMBOL)
                .qty(size.toString())
                .side(side)
                .orderType(TradeOrderType.MARKET)
                .build();
        System.out.println("newMarketOrderRequest = " + newMarketOrderRequest);
        var order = tradeClient.createOrder(newMarketOrderRequest);
        ResponseValidator.checkResult(order);
    }

    private static TradeOrderRequest placeStopLossOrder(BigDecimal slPrice, BigDecimal size, Side side) {
        var newSlOrderRequest = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(HEDGE_SYMBOL)
                .qty(size.toString())
                .side(side)
                .orderType(TradeOrderType.MARKET)
                .triggerPrice(slPrice.toString())
                .triggerDirection(side == Side.BUY ? TriggerDirection.RISE_TO_TRIGGER_PRICE
                        : TriggerDirection.FALL_TO_TRIGGER_PRICE)
                .tpslMode(TpslMode.PARTIAL)
                .build();
        System.out.println("newSlOrderRequest = " + newSlOrderRequest);
        return newSlOrderRequest;
    }

    private static void closeFuturesPosition(PositionEntry futPos, BybitApiTradeRestClient tradeClient) {
        var closeFuturesPositionRequest = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(futPos.getSymbol())
                .qty(futPos.getSize().abs().toString())
                .side(futPos.getSide() == Side.BUY ? Side.SELL : Side.BUY)
                .orderType(TradeOrderType.MARKET)
                .build();
        var order = tradeClient.createOrder(closeFuturesPositionRequest);
        ResponseValidator.checkResult(order);
    }

    public static void cancelAllFuturesOrders(BybitApiTradeRestClient tradeClient) {
        var cancelAllOrdersRequest = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(HEDGE_SYMBOL)
                .build();
        System.out.println("cancelAllOrdersRequest = " + cancelAllOrdersRequest);
        var order = tradeClient.cancelAllOrder(cancelAllOrdersRequest);
        ResponseValidator.checkResult(order);
    }

    @NotNull
    public static List<PositionEntry> getPositions(CategoryType categoryType, BybitApiPositionRestClient positionRestClient,
                                                   String symbol) {
        List<PositionEntry> positionEntries = new ArrayList<>();
        String nextPageCursor = null;
        PositionDataRequest.PositionDataRequestBuilder requestBuilder = PositionDataRequest.builder()
                .category(categoryType)
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

    @NotNull
    private static List<OrderEntry> getOrders(CategoryType categoryType, BybitApiTradeRestClient client,
                                              String symbol) {
        List<OrderEntry> orderEntries = new ArrayList<>();
        String nextPageCursor = null;
        var requestBuilder = TradeOrderRequest.builder()
                .category(categoryType)
                .symbol(symbol);
        do {
            var ordersInfo = client.getOpenOrders(requestBuilder
                    .cursor(nextPageCursor)
                    .build());
            ResponseValidator.checkResult(ordersInfo);
            orderEntries.addAll(ordersInfo.getResult().getOrderEntries());
            nextPageCursor = ordersInfo.getResult().getNextPageCursor();
        } while (!nextPageCursor.isEmpty());
        return orderEntries;
    }

    private static TickerEntry getHedgeTickerInfo(BybitApiMarketRestClient marketRestClient) {
        var request = MarketDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(HEDGE_SYMBOL)
                .build();
        var result = marketRestClient.getMarketTickers(request);
        ResponseValidator.checkResult(result);
        return result.getResult().getTickerEntries().get(0);
    }


    private static Pair<OptionType, BigDecimal> getOptionTypeAndStrikePrice(String optionName) {
//        SOL-24FEB25-170-C
        var splitted = optionName.split("-");
        var price = new BigDecimal(splitted[2]);
        switch (splitted[3]) {
            case "C" : return Pair.of(OptionType.CALL, price);
            case "P" : return Pair.of(OptionType.PUT, price);
            default: throw new IllegalArgumentException("Can't recognise Option type of symbol " + optionName);
        }
    }

}
