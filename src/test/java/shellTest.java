import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class shellTest {

    public static Map execCommand(String... str) {
        Map<Integer, String> map = new HashMap<>();
        ProcessBuilder pb = new ProcessBuilder(str);
        pb.redirectErrorStream(true);
        Process process = null;
        try {
            process = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader reader = null;
        if (process != null) {
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        }

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (reader != null) {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (process != null) {
                process.waitFor();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (process != null) {
            map.put(0, String.valueOf(process.exitValue()));
        }

        try {
            map.put(1, stringBuilder.toString());
        } catch (StringIndexOutOfBoundsException e) {
            if (stringBuilder.toString().length() == 0) {
                return map;
            }
        }
        return map;
    }


    @Test
    public void toastCheck(){

        String OS = System.getProperty("os.name").toLowerCase();
        // Windows path sorunu nedeniyle localde çalışmak için manual path tanımlaması yapıldı.
        // local kullanım için düzenleme yapılabilir.

        String command = "D:\\Android\\sdk\\platform-tools\\adb.exe shell uiautomator events";


        // TODO : Commit ederken commandı kaldır adb path ve shell commandı ekleyerek commit et

        String userName = (System.getProperty("user.name"));
        String adbPath = "C:\\Users\\"+userName+"\\AppData\\Local\\Android\\sdk\\platform-tools\\";
        String adbShellCommand = "adb shell uiautomator events";
        String result = null;
        String exitCode = null;
        String expectedCondition = "android.widget.Toast";


        System.out.println("Operating System : " + OS);


        if(OS.indexOf("win") >= 0) {

            String[] callCmd = {"cmd.exe", "/c", command};

            //String[] callCmd = {"cmd.exe", "/c", adbPath+adbShellCommand};

            exitCode = execCommand(callCmd).get(0).toString();

            result = execCommand(callCmd).get(1).toString();

        }

        if(OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 ){

            String[] callTerm = {"sh", "-c", adbShellCommand};

            exitCode = execCommand(callTerm).get(0).toString();

            result = execCommand(callTerm).get(1).toString();

        }

        System.out.println("exit code: " + exitCode);

        System.out.println("command result:\n" + result);


        if(result.contains(expectedCondition)){

            Assert.assertTrue(true,expectedCondition);

            System.out.println("TEST OK");


        }else {

            Assert.fail("android.widget.Toast bulunamadı");
        }
    }
}
