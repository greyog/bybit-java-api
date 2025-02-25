package sold_option_basic_hedger;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.GenericResponse;
import com.bybit.api.client.domain.TradeOrderType;
import com.bybit.api.client.domain.TriggerBy;
import com.bybit.api.client.domain.market.OptionType;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.domain.position.response.PositionEntry;
import com.bybit.api.client.domain.trade.Side;
import com.bybit.api.client.domain.trade.TriggerDirection;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.domain.trade.response.OrderEntry;
import com.bybit.api.client.exception.BybitApiException;
import com.bybit.api.client.log.LogOption;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import com.bybit.api.client.service.BybitApiClientFactory;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    public static final String HEDGE_SYMBOL = "SOLPERP";

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

        Optional<PositionEntry> futuresPosition = futuresPositions.stream()
                .filter(positionEntry -> positionEntry.getSize().compareTo(BigDecimal.ZERO) != 0)
                .findFirst();

        if (optionPositions.isEmpty() && futuresPosition.isPresent()) {
            closeFuturesPosition(futuresPosition, tradeClient);
        }

        optionPositions.forEach(pos -> {
            var typeAndStrike = getOptionTypeAndStrikePrice(pos.getSymbol());
            var strikePrice = typeAndStrike.getRight();
            var type = typeAndStrike.getLeft();
            var deltaPerOne = pos.getDelta().divide(pos.getSize(), RoundingMode.HALF_UP);
            switch (type) {
                case CALL:
                    if (deltaPerOne.compareTo(BigDecimal.valueOf(-0.5)) > 0) {
                        if (futuresPosition.isPresent()) {
                            closeFuturesPosition(futuresPosition, tradeClient);
                        }
                        placeOrderToHedgeSoldCall(strikePrice, pos.getSize(), futuresOrders, tradeClient);
                    }
//                    else {
//                        checkPositions();
//                    }
                    break;
                case PUT:
                    throw new NotImplementedException("Can't hedge put yet");
            }
        });

    }

    private static void placeOrderToHedgeSoldCall(BigDecimal strikePrice, BigDecimal size,
                                                  List<OrderEntry> futuresOrders, BybitApiTradeRestClient tradeClient) {
        var newOrderRequest = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(HEDGE_SYMBOL)
                .qty(size.toString())
                .side(Side.BUY)
                .orderType(TradeOrderType.MARKET)
                .triggerPrice(strikePrice.toString())
                .triggerBy(TriggerBy.LAST_PRICE)
                .triggerDirection(TriggerDirection.RISE_TO_TRIGGER_PRICE.getIndex())
                .stopLoss(strikePrice.subtract(BigDecimal.valueOf(0.05)).toString())
                .build();
        futuresOrders.stream()
                .filter(order -> newOrderRequest.getSide().equals(order.getSide()))
                .filter(order -> newOrderRequest.getTriggerPrice().compareTo(order.getTriggerPrice()) == 0)
                .filter(order -> order.getQty().compareTo(size) == 0)
        var order = tradeClient.createOrder(newOrderRequest);
        checkResult(order);

    }

    private static void closeFuturesPosition(Optional<PositionEntry> futuresPosition, BybitApiTradeRestClient tradeClient) {
        var futPos = futuresPosition.get();
        var closeFuturesPositionRequest = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(futPos.getSymbol())
                .qty(futPos.getSize().abs().toString())
                .side(futPos.getSide() == Side.BUY ? Side.SELL : Side.BUY)
                .orderType(TradeOrderType.MARKET)
                .build();
        tradeClient.createOrder(closeFuturesPositionRequest);
    }

    @NotNull
    private static List<PositionEntry> getPositions(CategoryType categoryType, BybitApiPositionRestClient positionRestClient,
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
