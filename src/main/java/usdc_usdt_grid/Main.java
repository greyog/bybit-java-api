package usdc_usdt_grid;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.market.response.instrumentInfo.InstrumentEntry;
import com.bybit.api.client.log.LogOption;
import com.bybit.api.client.service.BybitApiClientFactory;
import common.ResponseValidator;

public class Main {

    private static final String SYMBOL = System.getenv("GRID_SYMBOL");
    private static final int ORDER_COUNT = 5;

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
//         var orderRequest = TradeOrderRequest.builder()
//                 .category(CategoryType.SPOT)
//                 .symbol(SYMBOL)
//                 .build();
// //        System.out.println("orderRequest = " + orderRequest);
//         var orderResponse = tradeClient.getOpenOrders(orderRequest).getResult();
//         var myOrders = new ArrayList<>(orderResponse.getOrderEntries());
//         while (!orderResponse.getNextPageCursor().isEmpty()) {
//             orderRequest.setCursor(orderResponse.getNextPageCursor());
//             orderResponse = tradeClient.getOpenOrders(orderRequest).getResult();
//             myOrders.addAll(orderResponse.getOrderEntries());
//         }
//         System.out.println("myOrders = " + myOrders);
//         var myBidOrders = new ArrayList<OrderEntry>();
//         var myAskOrders = new ArrayList<OrderEntry>();
//         myOrders.forEach(orderEntry -> {
//             if (orderEntry.getOrderType().equals(Side.BUY.getTransactionSide())) {
//                 myBidOrders.add(orderEntry);
//             } else if (orderEntry.getOrderType().equals(Side.SELL.getTransactionSide())) {
//                 myAskOrders.add(orderEntry);
//             }
//         });
//         myBidOrders.sort(Comparator.comparing(OrderEntry::getPrice));
//         myAskOrders.sort((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()));

        var instrumentInfoRequest = MarketDataRequest.builder()
                .category(CategoryType.SPOT)
                .symbol(SYMBOL)
                .build();
        var instrumentsInfoResponse = marketDataClient.getInstrumentsInfo(instrumentInfoRequest);
        ResponseValidator.checkResult(instrumentsInfoResponse);
        System.out.println(instrumentsInfoResponse.getResult());

        var instrumentInfo = instrumentsInfoResponse.getResult().getInstrumentEntries().getFirst();
        var tickSize = instrumentInfo.getPriceFilter().getTickSize();

        var orderbookRequest = MarketDataRequest.builder()
                .category(CategoryType.SPOT)
                .symbol(SYMBOL)
                .build();
        var marketOrderBookRaw = marketDataClient.getMarketOrderBook(orderbookRequest);
//        System.out.println(ResponseUtil.toPrettyString(marketOrderBookRaw));
//        var marketOrderBookResult = ResponseUtil.toResult(marketOrderBookRaw, OrderbookResult.class);
//
//        var maxBidPriceStr = marketOrderBookResult.getOrderbookBidEntries().getFirst().getBidPrice();
//        var minAskPriceStr = marketOrderBookResult.getOrderBookAskEntries().getFirst().getAskPrice();
//        var bidPrice = new BigDecimal(maxBidPriceStr).setScale(tickSize.scale(), RoundingMode.HALF_UP);
//        var askPrice = new BigDecimal(minAskPriceStr).setScale(tickSize.scale(), RoundingMode.HALF_UP);

//        var hasToCancelAll = false;
//        if (!myBidOrders.isEmpty()) {
//            var myMaxBidPriceStr = myBidOrders.getFirst().getPrice();
//            var myMaxBidPrice = new BigDecimal(myMaxBidPriceStr).setScale(tickSize.scale(), RoundingMode.HALF_UP);
//            if (myMaxBidPrice.compareTo(bidPrice) < 0) {
//                hasToCancelAll = true;
//            }
//        }
//        if (!myAskOrders.isEmpty()) {
//            var myMinAskPriceStr = myAskOrders.getFirst().getPrice();
//            var myMinAskPrice = new BigDecimal(myMinAskPriceStr).setScale(tickSize.scale(), RoundingMode.HALF_UP);
//            if (myMinAskPrice.compareTo(askPrice) > 0) {
//                hasToCancelAll = true;
//            }
//        }
//        if (myBidOrders.size() != ORDER_COUNT || myAskOrders.size() != ORDER_COUNT) {
//            hasToCancelAll = true;
//        }
//        if (hasToCancelAll) {
//            CancelAll.cancelAll(tradeClient);
//        }
//
//        var bidOrders = new ArrayList<TradeOrderRequest>();
//        for (int i = 0; i < ORDER_COUNT; i++) {
//            var tradeOrderRequest = TradeOrderRequest.builder()
//                    .category(CategoryType.SPOT)
//                    .symbol(SYMBOL)
//                    .side(Side.BUY)
//                    .orderType(TradeOrderType.LIMIT)
//                    .qty("1.1")
//                    .price(bidPrice.toString())
//                    .timeInForce(TimeInForce.GOOD_TILL_CANCEL)
//                    .build();
//            bidOrders.add(tradeOrderRequest);
//            bidPrice = bidPrice.subtract(tickSize);
//        }
//        var createBatchOrdersBuy = BatchOrderRequest.builder()
//                .category(CategoryType.SPOT)
//                .request(bidOrders)
//                .build();
//        var batchOrderBuyRaw = tradeClient.createBatchOrder(createBatchOrdersBuy);
//        System.out.println(ResponseUtil.toResult(batchOrderBuyRaw, OrderResult.class));

//        var askOrders = new ArrayList<TradeOrderRequest>();
//        for (int i = 0; i < ORDER_COUNT; i++) {
//            var tradeOrderRequest = TradeOrderRequest.builder()
//                    .category(CategoryType.SPOT)
//                    .symbol(SYMBOL)
//                    .side(Side.SELL)
//                    .orderType(TradeOrderType.LIMIT)
//                    .qty("1.1")
//                    .price(askPrice.toString())
//                    .timeInForce(TimeInForce.GOOD_TILL_CANCEL)
//                    .build();
//            askOrders.add(tradeOrderRequest);
//            askPrice = askPrice.add(tickSize);
//        }
//        var createBatchOrdersSell = BatchOrderRequest.builder()
//                .category(CategoryType.SPOT)
//                .request(askOrders)
//                .build();
//        var batchOrderSellRaw = tradeClient.createBatchOrder(createBatchOrdersSell);
//        System.out.println(ResponseUtil.toResult(batchOrderSellRaw, OrderResult.class));
    }
}
