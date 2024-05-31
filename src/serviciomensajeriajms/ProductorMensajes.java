package serviciomensajeriajms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ProductorMensajes implements ExceptionListener {

    void processProducer() {

        try {
            //create a connectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    "tcp://localhost:61616");

            //create a connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            //create a session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //create the destination (topic o queue)
            Destination destination = session.createQueue("MYQUEUE");
            //Destination destination = session.createTopic("MYTOPIC");

            //create a messageProducer from the session to the topic o queue
            MessageProducer producer = session.createProducer(destination);

            //create a messages
            String text = "<Catalog>\r\n"
                    + "<Book id=\"bk101\">\r\n"
                    + "<Author>Garghentini, Davide</Author>\r\n"
                    + "<Title>XML Developer's Guide</Title>\r\n"
                    + "<Genre>Computer</Genre>\r\n"
                    + "<Price>44.95</Price>\r\n"
                    + "<PublishDate>2000-10-01</PublishDate>\r\n"
                    + "<Description>An in-depth look at creating applications with XML. </Description>\r\n"
                    + "</Book>\r\n"
                    + "<Book id=\"bk102\">\r\n"
                    + "<Author>Garcia, Debra</Author>\r\n"
                    + "<Title>Midnight Rain</Title>\r\n"
                    + "<Genre>Fantasy</Genre>\r\n"
                    + "<Price>5.95</Price>\r\n"
                    + "<PublishDate>2000-12-16</PublishDate>\r\n"
                    + "<Description>A former architect battles corporate zombies,\r\n"
                    + "an evil sorceress, and her own childhood to become queen\r\n"
                    + "of the world. </Description>\r\n"
                    + "</Book>\r\n"
                    + "</Catalog>";

            String text2 = "<Catalog>\n"
                    + "<Book id=\"bk101\">\n"
                    + "<Author>Garghentini, Davide</Author>\n"
                    + "<Title>XML Developer's Guide</Title>\n"
                    + "<Genre>Computer</Genre>\n"
                    + "<Price>44.95</Price>\n"
                    + "<PublishDate>2000-10-01</PublishDate>\n"
                    + "<Description>An in-depth look at creating applications with XML. </Description>\n"
                    + "</Book>\n"
                    + "<Book id=\"bk102\">\n"
                    + "<Author>Garcia, Debra</Author>\n"
                    + "<Title>Midnight Rain</Title>\n"
                    + "<Genre>Fantasy</Genre>\n"
                    + "<Price>5.95</Price>\n"
                    + "<PublishDate>2000-12-16</PublishDate>\n"
                    + "<Description>A former architect battles corporate zombies,\n"
                    + "an evil sorceress, and her own childhood to become queen\n"
                    + "of the world. </Description>\n"
                    + "</Book>\n"
                    + "</Catalog>";

            TextMessage message = session.createTextMessage(text2);

            //tell the producer to send message
            // System.out.println("Mensaje enviado"+ text);
            producer.send(message);

            //clean up
            session.close();
            connection.close();
        } catch (Exception e) {

            System.out.println("Caught: " + e);
            e.printStackTrace();
        }

    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occurred. Shutting down client.");

    }

    public static void main(String[] args) throws Exception {
        ProductorMensajes p = new ProductorMensajes();
        System.out.println("Running Producer Process...");
        p.processProducer();

    }
}
