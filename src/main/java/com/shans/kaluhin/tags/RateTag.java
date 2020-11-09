package com.shans.kaluhin.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class RateTag extends TagSupport {
    private int rate;
    private int counter;

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        counter = 0;

        try {
            out.print("<div id=\"stars\">");
        } catch (IOException exception) {
            throw new JspException(exception);
        }

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doAfterBody() throws JspException {
        JspWriter out = pageContext.getOut();
        try {
            if(counter < rate) {
                out.print("<label class=\"fa fa-star\"></label>");
            }else if(counter <= 5){
                out.print("<label class=\"fa fa-star-o\"></label>");
            }
            counter++;
        } catch (IOException e) {
            throw new JspException(e);
        }

        if (counter >= 5) {
            return SKIP_BODY;
        } else {
            return EVAL_BODY_AGAIN;
        }
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();

        try {
            out.print("</div>");
        } catch (IOException e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
