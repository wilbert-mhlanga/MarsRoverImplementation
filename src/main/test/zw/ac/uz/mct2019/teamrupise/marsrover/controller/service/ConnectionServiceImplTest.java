package zw.ac.uz.mct2019.teamrupise.marsrover.controller.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import zw.ac.uz.mct2019.teamrupise.marsrover.controller.model.ConnectionRequest;

@RunWith(MockitoJUnitRunner.class)
public class ConnectionServiceImplTest {

    ConnectionService connectionService;

    @Before
    public void setUp(){
        connectionService= ConnectionServiceImpl.getInstance(new MessageProcessingServiceImpl());
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfConnectionRequestIsEmpty() {
        connectionService.startConnection(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfHostnameEmpty() {
        ConnectionRequest connectionRequest=fullRequest();
        connectionRequest.setHostName("");
        connectionService.startConnection(connectionRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfPortIsNegative() {
        ConnectionRequest connectionRequest=fullRequest();
        connectionRequest.setPort(0);
        connectionService.startConnection(connectionRequest);
    }

    private ConnectionRequest fullRequest(){
        return new ConnectionRequest("localhost",8080);
    }

}
