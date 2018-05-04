package jasper;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static server.Constants.WEB_ROOT;

public class ServletGenerator {
    private String result = "";
    private static String dependency = "lib/javax.servlet.jar";
    private static String docRoot = WEB_ROOT+"/servlet/";

    public String  generate(String className,String importPart,String declarationPart,String initPart,String doGetPart,String doPostPart,String destoryPart,String header){
        result += importPart +
                "import java.io.*;\n" +
                "import javax.servlet.*;\n" +
                "import javax.servlet.http.*;\n\n"+
                "public class "+ className +" extends HttpServlet {\n\n";
        result += declarationPart+"\n";
        result += ("public void init() throws ServletException" +
                "{\n"+
                initPart+
                "}\n\n");
        result += ("public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\n"+
                header+"\n"+
                "PrintWriter out = response.getWriter();\n"+
                "HttpSession session = request.getSession();\n"+
                doGetPart+
                "}\n\n"
        );
        result += ("public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {\n"+
                header+"\n"+
                "PrintWriter out = response.getWriter();\n"+
                "HttpSession session = request.getSession();\n"+
                doPostPart+
                "}\n\n"
        );
        result += ("public void destroy() {\n"+
                destoryPart+
                "}\n");

        result += "}";

        /*try {
            result = CodePretty.pretty(result);
        }
        catch (Exception w){
            w.printStackTrace();
        }*/

        File f = createJavaFile(className,result);
        excuteCompile("javac -cp "+ dependency + " " + createJavaFile(className,result).getPath());
        return result;
    }

    public File createJavaFile(String className,String result) {
        String dir = docRoot;
        String fileName= dir + className + ".java";
        File file = new File(fileName);
        if(!file.exists()) {
            try {
                file.createNewFile();
                PrintStream ps = new PrintStream(new FileOutputStream(file));
                ps.println(result);

                CodePretty.pretty(file,file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void excuteCompile(String cmd) {
        System.out.println("cmd command: "+cmd);
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

