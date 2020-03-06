package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.SellItem;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class MailContentBuilderService {

    @Autowired
    private TemplateEngine templateEngine;

    public String buildEmailAuth(String userName, String code) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("code", code);
        return templateEngine.process("email", context);
    }

    public String buildTransRequest(SellItem sellItem, String userName, String buyerNickname) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("buyerNickname", buyerNickname);
        context.setVariable("title", sellItem.getTitle());
        context.setVariable("imageUrl", sellItem.getImageUrl());
        context.setVariable("regiPrice", sellItem.getRegiPrice());
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy.MM.dd");
        Date regiTime = sellItem.getRegiTime();
        context.setVariable("regiTime", format.format(regiTime));
        context.setVariable("sellerId", sellItem.getSellerId());
        return templateEngine.process("transRequest", context);
    }

    public String buildSellerBoogleBoxInfoInputRequest(SellItem sellItem, String userName, String buyerNickname) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("buyerNickname", buyerNickname);
        context.setVariable("title", sellItem.getTitle());
        context.setVariable("imageUrl", sellItem.getImageUrl());
        context.setVariable("regiPrice", sellItem.getRegiPrice());
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy.MM.dd");
        Date regiTime = sellItem.getRegiTime();
        context.setVariable("regiTime", format.format(regiTime));
        context.setVariable("sellerId", sellItem.getSellerId());
        return templateEngine.process("sellerBoogleBoxInfoInputRequest", context);
    }

    public String buildBuyerPaymentRequest(SellItem sellItem, String userName, String sellerNickname) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("sellerNickname", sellerNickname);
        context.setVariable("title", sellItem.getTitle());
        context.setVariable("imageUrl", sellItem.getImageUrl());
        context.setVariable("regiPrice", sellItem.getRegiPrice());
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy.MM.dd");
        Date regiTime = sellItem.getRegiTime();
        context.setVariable("regiTime", format.format(regiTime));
        context.setVariable("sellerId", sellItem.getSellerId());
        return templateEngine.process("buyerPaymentRequest", context);
    }

    public String buildBuyerConfirmBoogleBoxInfoRequest(SellItem sellItem, String userName, String sellerNickname,
                                                        String boogleBoxId, String boogleBoxPassword) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("sellerNickname", sellerNickname);
        context.setVariable("title", sellItem.getTitle());
        context.setVariable("imageUrl", sellItem.getImageUrl());
        context.setVariable("regiPrice", sellItem.getRegiPrice());
        context.setVariable("boogleBoxId", boogleBoxId);
        context.setVariable("boogleBoxPassword", boogleBoxPassword);
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy.MM.dd");
        Date regiTime = sellItem.getRegiTime();
        context.setVariable("regiTime", format.format(regiTime));
        context.setVariable("sellerId", sellItem.getSellerId());
        return templateEngine.process("buyerConfirmBoogleBoxInfoRequest", context);
    }

    public String buildSellerConfirmReceiveProductAndMoneyRequest(SellItem sellItem, String userName, String buyerNickname,
                                                                  String sellerAccountInfo) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("buyerNickname", buyerNickname);
        context.setVariable("title", sellItem.getTitle());
        context.setVariable("imageUrl", sellItem.getImageUrl());
        context.setVariable("regiPrice", sellItem.getRegiPrice());
        context.setVariable("sellerAccountInfo", sellerAccountInfo);
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy.MM.dd");
        Date regiTime = sellItem.getRegiTime();
        context.setVariable("regiTime", format.format(regiTime));
        context.setVariable("sellerId", sellItem.getSellerId());
        return templateEngine.process("sellerConfirmReceiveProductAndMoneyRequest.html", context);
    }

}
