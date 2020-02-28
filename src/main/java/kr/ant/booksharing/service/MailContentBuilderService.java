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
}
