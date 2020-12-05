package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.graalvm.compiler.replacements.aarch64.AArch64CountTrailingZerosNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusImplTest {
    private MessageBus messageBus;

    @BeforeEach
    void setUp() {
        messageBus = new MessageBusImpl();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void subscribeEvent() {
    }

    @Test
    void subscribeBroadcast() {
    }

    @Test
    void complete() {
        MicroService s = new HanSoloMicroservice();
        messageBus.register(s);
        AttackEvent event = new AttackEvent();
        messageBus.subscribeEvent(event.getClass(),s);
        Future<Boolean> result= messageBus.sendEvent(event);
        messageBus.complete(event, true);
        assertTrue(result.isDone());
    }

    @Test
    void sendBroadcast() {
        Message broad = null;
        Message broad2 = null;
        MicroService s = new HanSoloMicroservice();
        messageBus.register(s);
        MicroService s2 = new C3POMicroservice();
        messageBus.register(s2);
        Broadcast broadcast =  new BroadcastImpl();
        messageBus.subscribeBroadcast(Broadcast.class,s);
        messageBus.subscribeBroadcast(Broadcast.class,s2);
        s.sendBroadcast(broadcast);
        try {
            broad = messageBus.awaitMessage(s);
            broad2 = messageBus.awaitMessage(s2);
        }

        catch(Exception e){
        }

        assertEquals(broadcast,broad);
        assertEquals(broadcast,broad2);

    }

    @Test
    void sendEvent() throws InterruptedException {
        Message message = null;
        MicroService s = new HanSoloMicroservice();
        messageBus.register(s);
        AttackEvent event = new AttackEvent();
        messageBus.subscribeEvent(event.getClass(),s);
        s.sendEvent(event);
        try {
             message = messageBus.awaitMessage(s);
        }

        catch(Exception e){
        }

        assertEquals(event,message);
    }

    @Test
    void register() {

    }

    @Test
    void unregister() {
        //do not do
    }

    @Test
    void awaitMessage() {
        Message message = null;
        MicroService someMicroService = new C3POMicroservice();
        messageBus.register(someMicroService);

        try{
            message = messageBus.awaitMessage(someMicroService);
            fail();
        }
        catch(Exception e){

        }

    }
}