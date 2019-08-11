package zw.ac.uz.mct2019.teamrupise.marsrover.controller.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import zw.ac.uz.mct2019.teamrupise.marsrover.controller.model.ConnectionRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Socket.class, ConnectionServiceImpl.class})
public class ValidConnectionTest {

    private ConnectionServiceImpl connectionService;
    private MessageProcessingService messageProcessingService = mock(MessageProcessingService.class);
    Socket socket;

    @Before
    public void setUp() throws Exception {
        connectionService = ConnectionServiceImpl.getInstance(messageProcessingService);
        socket = Mockito.mock(Socket.class);
        connectionService.setClientSocket(socket);
        PowerMockito.whenNew(Socket.class)
                .withArguments(Matchers.anyString(), Matchers.anyInt())
                .thenReturn(socket);
    }


    @Test
    public void shouldProcessMessageAccordingToTagOnMessageReceipt() throws Exception {
        final String message="I 500.000 500.000 30000 30.000 60.000 20.000 20.0 60.0;" +
                "T 0 -- -187.500 187.500 125.0 0.000;" +
                "T 0 -- -187.500 187.500 126.0 0.000;" +
                "K 2000;"+
                "B 2000;"+
                "E 2000;"+
                "C 2000;";
        when(socket.getInputStream()).thenReturn(new ByteArrayInputStream(message.getBytes()));
        when(socket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        connectionService.receiveAndProcessMessage();
        verify(messageProcessingService,times(1)).processInitializationData("I 500.000 500.000 30000 30.000 60.000 20.000 20.0 60.0");
        verify(messageProcessingService,times(1)).processTelemetryStream("T 0 -- -187.500 187.500 125.0 0.000");
        verify(messageProcessingService,times(1)).processTelemetryStream("T 0 -- -187.500 187.500 126.0 0.000");
        verify(messageProcessingService,times(1)).processAdverseEvent("K 2000");
        verify(messageProcessingService,times(1)).processAdverseEvent("B 2000");
        verify(messageProcessingService,times(1)).processAdverseEvent("E 2000");
        verify(messageProcessingService,times(1)).processAdverseEvent("C 2000");
    }

    @Test
    public void shouldReturnNewSocketAndSetClientSocketIfConnectionRequestIsValid() throws Exception {
        ConnectionRequest connectionRequest = new ConnectionRequest("localhost", 8080);
        connectionService.startConnection(connectionRequest);
        PowerMockito.verifyNew(Socket.class).withArguments(connectionRequest.getHostName(), connectionRequest.getPort());
        Assert.assertEquals(socket, connectionService.getClientSocket());
    }
}
