package br.com.ecommerce;

import br.com.ecommerce.dispatcher.KafkaDispatcher;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderServlet extends HttpServlet {

    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    @Override
    public void destroy() {
        super.destroy();
        orderDispatcher.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var email = req.getParameter("email");
        var amount = new BigDecimal(req.getParameter("amount"));
        var orderId = UUID.randomUUID().toString();

        var order = new Order(orderId, amount, email);
        try {
            orderDispatcher.send("ECOMMERCE_NEW_ORDER", email, order, new CorrelationId(NewOrderServlet.class.getSimpleName()));
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println("New order send.");
    }
}
