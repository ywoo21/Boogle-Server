package kr.ant.booksharing.service;

import kr.ant.booksharing.domain.SellItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class MailContentBuilderService {
    @Autowired
    private TemplateEngine templateEngine;

    public String buildEmailAuth(String message) {
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("email", context);
    }

    public String buildTransRequest(SellItem sellItem) {
        Context context = new Context();
        context.setVariable("title", sellItem.getTitle());
        context.setVariable("regiPrice", sellItem.getRegiPrice());
        context.setVariable("regiTime", sellItem.getRegiTime().toString());
        context.setVariable("sellerId", sellItem.getSellerId());
        return templateEngine.process("transRequest", context);
    }
}
