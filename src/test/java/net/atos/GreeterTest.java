package net.atos;

import org.arquillian.cube.q.api.ContainerChaos;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@RunWith(Arquillian.class)
public class GreeterTest {

  private final String contextPath = "qtest";

  private static URL url;

  @ArquillianResource
  ContainerChaos containerChaos;

  @Test
  @InSequence(2)
  public void should_remain_available() throws Exception {
    containerChaos
      .onCubeDockerHost()
        .killRandomly(
          ContainerChaos.ContainersType.containers("qtest01","qtest02","qtest02"),  /*regularExpression("qtest*"),*/
          ContainerChaos.IntervalType.intervalInSeconds(4),
          ContainerChaos.KillSignal.SIGTERM
        )
        .exec();

    url = new URL("http://10.0.2.15:8080/"+contextPath+"/");
    System.out.println("URL: "+url);

    given().
    when().
      get(url.toExternalForm()).
    then().
      body(is("Hello World!"));
  }

/*
  @Test
  @InSequence(3)
  public void should_return_default_greeting() {
    given().
    when().
      get(url.toExternalForm() + contextPath + "/").
    then().
      body(is("Hello World!"));
  }

  @Test
  @InSequence(4)
  public void should_return_request_logged() {
    given().
    when().
      get(url.toExternalForm() + contextPath + "/" + "?name=Bar").
    then().
      statusCode(202);
  }

  @Test
  @InSequence(5)
  public void should_return_personal_greeting() {
    given().
    when().
      get(url.toExternalForm() + contextPath + "/" + "?name=Foo").
    then().
      statusCode(200).
      body(is("Hello, Foo!"));
  }
*/

  /***
   * This is a dummy method used solely to
   * generate JaCoCo stats by running a test
   * in the remote container JVM
   *
   */
  @Test
  @InSequence(999)
  public void dummy_to_capture_stats() {
    Assert.assertTrue(true);
  }

  /*
  @After
  public void writeOutJacocoData() {
    try {
      Class rtClass = Thread.currentThread().getContextClassLoader().getParent().loadClass("org.jacoco.agent.rt.RT");
      Object jacocoAgent = rtClass.getMethod("getAgent", null).invoke(null);
      Method dumpMethod = jacocoAgent.getClass().getMethod("dump", boolean.class);
      dumpMethod.invoke(jacocoAgent, false);
    } catch(ClassNotFoundException e) {
      System.out.println("DEBUG: no jacoco agent attached to this jvm");
    } catch (Exception e) {
      System.out.println("ERROR: while trying to dump jacoco data: " + e);
    }
  }
  */
}
