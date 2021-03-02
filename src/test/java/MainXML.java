import com.google.common.io.Resources;
import one.util.streamex.StreamEx;
import org.junit.Test;
import ru.javaops.masterjava.com.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainXML {
    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);

    private static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getValue).thenComparing(User::getEmail);

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    @Test
    public void testPayload() throws Exception {
//        JaxbParserTest.class.getResourceAsStream("/city.xml")
        //вариант 1
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());

        //вариант 2
        URL payloadUrl = Resources.getResource("payload.xml");
        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        parser.setSchema(Schemas.ofClasspath("payload.xsd"));
        Payload payload1;
        try (InputStream is = payloadUrl.openStream()) {
            payload1 = parser.unmarshal(is);
        }



        String strPayload = JAXB_PARSER.marshal(payload);
        JAXB_PARSER.validate(strPayload);
        //System.out.println(strPayload);

        List<User> users = payload.getUsers().getUser();
        for (User user: users
             ) {
            System.out.println("тест: " + user.getFullName());
        }

        Payload.Projects project = payload.getProjects();
        List<Project> projectList = project.getProject();

        for (int i = 0; i<projectList.size(); i++){
            System.out.println("TestPro: " + projectList.get(i).getName());
        }
        projectList.stream().filter((p) -> p.getName().equals("Энтерпрайз")).forEach(System.out::println);
        System.out.println("тест111: " + project.getProject().get(0).getGroup().get(0).getName());
        System.out.println("тест111: " + project.getProject().get(1).getDescription());

        //users.forEach(System.out::println);
        //System.out.println("тест: " + users.get(0).getFullName());
    }

     public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Required argument: projectName");
            System.exit(1);
        }
        String projectName = args[0];
        URL payloadUrl = Resources.getResource("payload.xml");

        Set<User> users = parseByJaxb(projectName, payloadUrl);
        users.forEach(System.out::println);

    }

    private static Set<User> parseByJaxb(String projectName, URL payloadUrl) throws Exception {
        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        parser.setSchema(Schemas.ofClasspath("payload.xsd"));
        Payload payload;
        try (InputStream is = payloadUrl.openStream()) {
            payload = parser.unmarshal(is);
        }

        Project project = StreamEx.of(payload.getProjects().getProject())
                .filter(p -> p.getName().equals(projectName))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid project name '" + projectName + '\''));

        final Set<Project.Group> groups = new HashSet<>(project.getGroup());  // identity compare
        return StreamEx.of(payload.getUsers().getUser())
                .filter(u -> !Collections.disjoint(groups, u.getGroupRefs()))
                .collect(
                        Collectors.toCollection(() -> new TreeSet<>(USER_COMPARATOR))
                );


    }
}
