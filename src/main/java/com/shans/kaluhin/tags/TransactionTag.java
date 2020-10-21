package com.shans.kaluhin.tags;

import com.shans.kaluhin.entity.Billing;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class TransactionTag extends TagSupport {
    private List<Billing> billings;
    private int counter;

    @Override
    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        counter = 0;

        try {
            out.print("<button type=\"button\" class=\"list-group-item list-group-item-action active\">\n" +
                    "                    <fmt:message key=\"balance.transactions\"/>\n" +
                    "                </button>");
        } catch (IOException exception) {
            throw new JspException(exception);
        }

        return SKIP_BODY;
    }

    @Override
    public int doAfterBody() throws JspException {
        JspWriter out = pageContext.getOut();

        try {
            out.print(String.format("<tr><td colspan='3'>Row number #%d</td></tr>", counter++));
        } catch (IOException e) {
            throw new JspException(e);
        }

        if (counter > billings.size()) {
            return SKIP_BODY;
        } else {
            return EVAL_BODY_AGAIN;
        }
    }

    @Override
    public int doEndTag() throws JspException {
        JspWriter out = pageContext.getOut();

        try {
            out.print("</tbody></table>");
        } catch (IOException e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

    public List<Billing> getBillings() {
        return billings;
    }

    public void setBillings(List<Billing> billings) {
        this.billings = billings;
    }
}
