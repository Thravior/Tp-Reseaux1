import java.util.function.*;
import java.util.HashMap;

public class CommandClient {
    String exit(String s);
    String mkdir(String s);
    String cd(String s);
    String ls(String s);
    String download(String s);
    String upload(String s);

    Function<String, String> exit = CommandControl::exit;
}
