package sold_option_basic_hedger;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.GenericResponse;
import com.bybit.api.client.domain.TradeOrderType;
import com.bybit.api.client.domain.TriggerBy;
import com.bybit.api.client.domain.market.OptionType;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.market.response.tickers.TickerEntry;
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
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Main {

    public static final String HEDGE_SYMBOL = "SOLPERP";
    private static final BigDecimal SL_OFFSET = BigDecimal.ZERO;
    private static final BigDecimal TRIGGER_SL_OFFSET = BigDecimal.valueOf(0.05);

    public static void main(String[] args) {
        var factory = BybitApiClientFactory.newInstance(
                System.getenv("API_KEY"),
                System.getenv("API_SECRET"),
                BybitApiConfig.DEMO_TRADING_DOMAIN,
                true,
                LogOption.OKHTTP3.getLogOptionType());
        var tradeClient = factory.newTradeRestClient();
        BybitApiPositionRestClient positionRestClient = factory.newPositionRestClient();

        List<PositionEntry> optionPositions = getPositions(CategoryType.OPTION, positionRestClient, null);
        System.out.println(optionPositions);

        List<PositionEntry> futuresPositions = getPositions(CategoryType.LINEAR, positionRestClient, HEDGE_SYMBOL);
        System.out.println(futuresPositions);

        List<OrderEntry> futuresOrders = getOrders(CategoryType.LINEAR, tradeClient, HEDGE_SYMBOL);
        futuresOrders.forEach(o -> System.out.printf("type %s, price %s, triggrePrice %s, qty %s%n",
                o.getOrderType(), o.getPrice(), o.getTriggerPrice(), o.getQty()));

        Optional<PositionEntry> futuresPosition = futuresPositions.stream()
                .filter(positionEntry -> positionEntry.getSize().compareTo(BigDecimal.ZERO) != 0)
                .findFirst();

//        if (optionPositions.isEmpty()) {
//            if (!futuresOrders.isEmpty()) {
//                cancelAllFuturesOrders(tradeClient);
//            }
//            futuresPosition.ifPresent(positionEntry -> closeFuturesPosition(futuresPosition.get(), tradeClient));
//            return;
//        }

// todo get ticker price

        var targetFuturesPosition = BigDecimal.ZERO;
        var slSellPriceQty = new HashMap<String, BigDecimal>();
        var triggerBuyPriceQty = new HashMap<String, BigDecimal>();

        for (PositionEntry pos : optionPositions) {
            var typeAndStrike = getOptionTypeAndStrikePrice(pos.getSymbol());
            var strikePrice = typeAndStrike.getRight();
            var type = typeAndStrike.getLeft();
            var deltaPerOne = pos.getDelta().divide(pos.getSize(), RoundingMode.HALF_UP);
            switch (type) {
                case CALL:
                    if (deltaPerOne.compareTo(BigDecimal.valueOf(-0.5)) > 0) {
                        triggerBuyPriceQty.put(strikePrice.toString(),
                                slSellPriceQty.getOrDefault(strikePrice.toString(), BigDecimal.ZERO)
                                        .add(pos.getSize()));
                    } else {
                        targetFuturesPosition = targetFuturesPosition.add(pos.getSize());
                        var slPrice = strikePrice.subtract(SL_OFFSET);
                        slSellPriceQty.put(slPrice.toString(),
                                slSellPriceQty.getOrDefault(slPrice.toString(), BigDecimal.ZERO)
                                        .add(pos.getSize()));
                    }
                    break;
                case PUT:
                    throw new NotImplementedException("Can't hedge put yet");
            }
        }


        System.out.println("targetFuturesPosition = " + targetFuturesPosition);
        System.out.println("slSellPriceQty = " + slSellPriceQty);
        System.out.println("triggerBuyPriceQty = " + triggerBuyPriceQty);

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
        if (targetFuturesPosition.compareTo(currentFuturesPosition) != 0) { // need to change fut position
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
        var batchOrderRequest = BatchOrderRequest.builder();
        slSellPriceQty.forEach((slPrice, slSize) -> {
            placeStopLossOrder(new BigDecimal(slPrice), slSize, Side.SELL, tradeClient);
        });

        triggerBuyPriceQty.forEach((triggerPrice, triggerSize) -> {
            placeTriggerOrder(new BigDecimal(triggerPrice), triggerSize, Side.BUY, tradeClient);
        });
// todo batch order
    }

    private static void placeTriggerOrder(BigDecimal strikePrice, BigDecimal size, Side side,
                                                         BybitApiTradeRestClient tradeClient) {
        var newTriggerOrderRequest = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(HEDGE_SYMBOL)
                .qty(size.toString())
                .side(side)
                .orderType(TradeOrderType.MARKET)
                .triggerPrice(strikePrice.toString())
                .triggerBy(TriggerBy.LAST_PRICE)
                .triggerDirection(TriggerDirection.RISE_TO_TRIGGER_PRICE.getIndex())
                .stopLoss(strikePrice.subtract(TRIGGER_SL_OFFSET).toString())
                .build();
        System.out.println("newTriggerOrderRequest = " + newTriggerOrderRequest);
        var order = tradeClient.createOrder(newTriggerOrderRequest);
        checkResult(order);
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
        checkResult(order);
    }

    private static void placeStopLossOrder(BigDecimal slPrice, BigDecimal size, Side side,
                                         BybitApiTradeRestClient tradeClient) {
        var newSlOrderRequest = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(HEDGE_SYMBOL)
                .qty("1")
                .side(Side.SELL)
                .orderType(TradeOrderType.MARKET)
                .triggerPrice("130")
                .triggerDirection(2)
                .tpslMode("Partial")
                .build();
        System.out.println("newSlOrderRequest = " + newSlOrderRequest);
        var slOrder = tradeClient.createOrder(newSlOrderRequest);
        checkResult(slOrder);
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
        checkResult(order);
    }

    public static void cancelAllFuturesOrders(BybitApiTradeRestClient tradeClient) {
        var cancelAllOrdersRequest = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(HEDGE_SYMBOL)
                .build();
        System.out.println("cancelAllOrdersRequest = " + cancelAllOrdersRequest);
        var order = tradeClient.cancelAllOrder(cancelAllOrdersRequest);
        checkResult(order);
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
            checkResult(optionPositionInfo);
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
            checkResult(ordersInfo);
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
        checkResult(result);
        return result.getResult().getTickerEntries().getFirst();
    }

    private static void checkResult(GenericResponse<?> response) {
        if (response.getRetCode() != 0) {
            throw new BybitApiException("Code: " + response.getRetCode()
                                        + " , message: " + response.getRetMsg());
        }
    }

    private static Pair<OptionType, BigDecimal> getOptionTypeAndStrikePrice(String optionName) {
//        SOL-24FEB25-170-C
        var splitted = optionName.split("-");
        var price = new BigDecimal(splitted[2]);
        switch (splitted[3]) {
            case "C" : return Pair.of(OptionType.CALL, price);
            case "P" : return Pair.of(OptionType.PUT, price);
            default: throw new IllegalArgumentException("Can't recognise Option type of sumbol " + optionName);
        }
    }

}
