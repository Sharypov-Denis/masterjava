import com.google.common.io.Resources;
import org.junit.Test;
import ru.javaops.masterjava.com.ObjectFactory;
import ru.javaops.masterjava.com.Payload;
import ru.javaops.masterjava.com.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainXML {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    @Test
    public void testPayload() throws Exception {
//        JaxbParserTest.class.getResourceAsStream("/city.xml")
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        String strPayload = JAXB_PARSER.marshal(payload);
        JAXB_PARSER.validate(strPayload);
        //System.out.println(strPayload);

        List<User> users = payload.getUsers().getUser();
        for (User user: users
             ) {
            System.out.println("тест: " + user.getFullName());
        }
        users.forEach(System.out::println);
        ///System.out.println("тест: " + users.get(0).getFullName());
    }
}
