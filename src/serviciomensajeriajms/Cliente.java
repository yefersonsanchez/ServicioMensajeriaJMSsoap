package serviciomensajeriajms;

import clientews.servicio.ServicioConvertidorInterfaz;
import clientews.servicio.ServicioConvertidorService;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Cliente implements ExceptionListener {

    void processConsumer() {

        String clientID = "Yefer";

        try {

            //create a connectionFactory
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    "tcp://localhost:61616");

            //create a connection
            Connection connection = connectionFactory.createConnection();
            // connection.start();

            connection.setExceptionListener(this);

            //create a session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //create the destination (topic o queue)
            Destination destination = session.createQueue("MYQUEUE");
            //Destination destination = session.createTopic("MYTOPIC");

            //create a messageConsumer from the session to the topic o queue
            MessageConsumer consumer = session.createConsumer(destination);

            //MessageConsumer consumer = session.createDurableSubscriber((Topic) destination,"Edwin");
            consumer.setMessageListener(listener);//manejador de eventos para gestionar todos los mensajes
            connection.start();

            //wait for a message 
            /*
			   Message message = consumer.receive(1000);
			   
			   while(message != null) {
				
				   Thread.sleep(5000);
				   
				   if(message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
					System.out.println("Received: "+ text);
					   
				   }else {
					   
					   System.out.println("Received: "+ message);
					   
				   }
				   message = consumer.receiveNoWait();
			   }
			   consumer.close();
			   session.close();
			   connection.close();
			   
             */
        } catch (Exception e) {

            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
    }

    MessageListener listener = new MessageListener() {

        public void onMessage(Message msg) {

            if (msg instanceof TextMessage) {

                TextMessage textMessage = (TextMessage) msg;
                String text = null;
                String menJson = null;
                try {
                    text = textMessage.getText();

                    //------Aqui escribo codigo para llamar web services
                    /*  
                ServicioConvertidorInterfaz a = new ServicioConvertidorService().getServicioConvertidorPort();
                
                menJson= a.convertir(text);
                     */
                    //------- aqui finaliza codigo para llamar web services
                    ServicioConvertidorInterfaz a = new ServicioConvertidorService().getServicioConvertidorPort();

                    menJson = a.convertir(text);

                } catch (JMSException e) {
                    //Todo Auto-generated catch block
                    e.printStackTrace();
                }
                //System.out.println("Recivido: "+ text);
                System.out.println("convertido: " + menJson);
            } else {

                //System.out.println("No Recivido "+ msg);
                System.out.println("Fallo todo");
            }
        }
    };

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occurred. Shutting down client.");

    }

    public static void main(String[] args) throws Exception {
        Cliente c = new Cliente();
        System.out.println("Running Consumer...");

        c.processConsumer();

    }
}
