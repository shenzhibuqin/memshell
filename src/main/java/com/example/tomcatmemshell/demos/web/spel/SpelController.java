package com.example.tomcatmemshell.demos.web.spel;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;


@Controller
public class SpelController {


    @RequestMapping(value = "/spel", method = RequestMethod.POST)
    @ResponseBody
    public String spel(@RequestParam("id") String id) {

        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression(id);
        Object res = exp.getValue();
        if (res == null) {
            return "return null";
        } else {
            return res.toString();
        }
//        T(org.springframework.cglib.core.ReflectUtils).defineClass('org.test.spel.StringUtil',T(org.springframework.util.Base64Utils).decodeFromString('aa'),new javax.management.loading.MLet(new java.net.URL[0],T(java.lang.Thread).currentThread().getContextClassLoader())).newInstance()


    }



}
