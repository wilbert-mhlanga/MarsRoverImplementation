package zw.ac.uz.mct2019.teamrupise.marsrover.controller.service;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import zw.ac.uz.mct2019.teamrupise.marsrover.controller.exception.MarsRoverControllerException;
import zw.ac.uz.mct2019.teamrupise.marsrover.controller.model.MapInformation;

import java.nio.charset.Charset;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(JUnitParamsRunner.class)
@SuppressWarnings("unchecked")
public class MessageProcessingServiceImplTest {

    MessageProcessingServiceImpl messageProcessingService;

    @Before
    public void setUp() {
        messageProcessingService = new MessageProcessingServiceImpl();
    }

    @Test(expected = MarsRoverControllerException.class)
    @Parameters(method = "getInvalidMessages")
    public void shouldExceptionIfInitializationMessageIsInvalid(String message) {
        messageProcessingService.processInitializationData(message);
    }


    @Test(expected = MarsRoverControllerException.class)
    @Parameters(method = "getAdverseEventMessages")
    public void shouldThrowExceptionForAdverseEvents(String message) {
        messageProcessingService.processAdverseEvent(message);
    }

    @Test(expected = MarsRoverControllerException.class)
    public void shouldThrowExceptionIfInitializationMessageIsNull() {
        messageProcessingService.processInitializationData(null);
    }

    @Test
    public void shouldSetMapInformationIfInitializationMessageIsValid() {
        messageProcessingService.processInitializationData("I 500.000 500.000 30000 30.000 60.000 20.000 20.0 60.0");
        MapInformation mapInformation=MapInformation.getInstance();
        assertThat(mapInformation.getSpanOfXAxis(), equalTo(500.000));
        assertThat(mapInformation.getSpanOfYAxis(), equalTo(500.000));
        assertThat(mapInformation.getTimeLimitInMilliSeconds(), equalTo(30000.0));
        assertThat(mapInformation.getMinRangeOfVehiceVisualSensorsInMeters(), equalTo(30.000));
        assertThat(mapInformation.getMaxRangeOfVehiceVisualSensorsInMeters(), equalTo(60.000));
        assertThat(mapInformation.getMaxVehicleSpeedInMetersPerSecond(), equalTo(20.000));
        assertThat(mapInformation.getMaxTurningSpeedInDegreesPerSecond(), equalTo(20.0));
        assertThat(mapInformation.getMaxHardTurningSpeedInDegreesPerSecond(), equalTo(60.0));
    }


    private Object[] getInvalidMessages() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String randomString = new String(array, Charset.forName("UTF-8"));
        String invalidInitialializationTag="D 500.000 500.000 30000 30.000 60.000 20.000 20.0 60.0";
        String tooLongToken="I 500.000 500.000 30000 30.000 60.000 20.000 20.0 60.0 60.0";
        String tooShortToken="I 500.000 500.000 30000 30.000 60.000 20.000 20.0";
        return new Object[]{"",randomString,tooLongToken,tooShortToken,invalidInitialializationTag};
    }


    private Object[] getAdverseEventMessages() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String killedByMartian="K 20000";
        String fellIntoCrater="C 20000";
        String hitABoulderorMapEdge="B 20000";
        String timeout="E 20000";
        return new Object[]{killedByMartian,fellIntoCrater,hitABoulderorMapEdge,timeout};
    }

}
