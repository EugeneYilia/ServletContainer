package jasper;

import java.io.*;
import java.util.StringTokenizer;

public class ServletParser {
    /*this is a parse class aim to parse jsp file to servlet.java file
     *
     * https://github.com/XUranus
     *
     * */

    private String className;
    private String jspContent;
    private String header;

    public ServletParser(File file) {
        String content = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = in.readLine()) != null) {
                content = content + line + "\n";
            }
        } catch (IOException e) {
            System.out.println("Parser Construct Err");
        }

        String className = file.getName();// 可以加后缀
        className = className.replace(".", "_");
        //System.out.println("this file name is "+className);
        String jspContent = content;
        this.className = className;
        this.jspContent = jspContent;
    }

    public ServletParser(String className, String jspContent) {
        this.className = className;
        this.jspContent = jspContent;
    }

    public String parse() throws Exception {
        String str = jspContent;

        String importPart = "";
        String doGetPartString = "";
        String definitionPartString = "";
        String servicePartString = "";
        int htmlContentBegin = 0;

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '<') {
                if (str.charAt(i + 1) == '%') {
                    if (str.charAt(i + 2) == '=') {
                        //jsp expression tag <%=  %>
                        int end = getStringIndex(i + 3, str, "%>");
                        doGetPartString += htmlContentString(str, htmlContentBegin, i - 1);
                        htmlContentBegin = end + 2;
                        doGetPartString += expresstionsString(str, i + 3, end - 1);
                        i = end + 1;
                        continue;
                    } else if (str.charAt(i + 2) == '!') {
                        //jsp declaration tag <%!  %>
                        int end = getStringIndex(i + 3, str, "%>");
                        doGetPartString += htmlContentString(str, htmlContentBegin, i - 1);
                        htmlContentBegin = end + 2;
                        definitionPartString += declarationsString(str, i + 3, end - 1);
                        i = end + 1;
                        continue;
                    } else if (str.charAt(i + 2) == '@') {
                        //jsp directive tag <%@  %>
                        int end = getStringIndex(i + 3, str, "%>");
                        doGetPartString += htmlContentString(str, htmlContentBegin, i - 1);
                        htmlContentBegin = end + 2;
                        jspDirectiveString(str, i + 3, end - 1);
                        servicePartString += jspDirectiveString(str, i + 3, end - 1);
                        i = end + 1;
                        continue;
                    } else if (str.charAt(i + 2) == '-' && str.charAt(i + 3) == '-') {
                        //jsp comment tag <%-- --%>
                        //do nothing
                        int end = getStringIndex(i + 3, str, "%>");
                        doGetPartString += htmlContentString(str, htmlContentBegin, i - 1);
                        htmlContentBegin = end + 2;
                        i = end + 1;
                        continue;
                    } else {
                        //scriptlets tag
                        int end = getStringIndex(i + 2, str, "%>");
                        doGetPartString += htmlContentString(str, htmlContentBegin, i - 1);
                        htmlContentBegin = end + 2;
                        doGetPartString += scriptletsString(str, i + 2, end - 1);
                        i = end + 1;
                        continue;
                    }
                } else if (stringMatches(str, i, "<jsp:")) {
                    //JSP standard action like <jsp:usebean>
                    int end = getStringIndex(i + 5, str, ">");
                    doGetPartString += htmlContentString(str, htmlContentBegin, i - 1);
                    htmlContentBegin = end + 1;
                    //
                    // System.out.println(scriptletsString(str,i+5,end-1));
                    i = end;
                    //temp debug solution !!!
                    continue;
                }
            }
        }
      /*  return "--------------------DeclationPart---------------------\n\n"
                +definitionPartString
                +"\n\n--------------------Service() Part--------------------\n\n"
                + servicePartString
                +"\n\n-----------------doGet()/doPost() Part----------------\n\n"
                + doGetPartString;
        */

        return new ServletGenerator().generate(className, importPart, definitionPartString, "", servicePartString + "\n" + doGetPartString, servicePartString + "\n" + doGetPartString, "", header);
    }


    String scriptletsString(String str, int begin, int end) {
        //handle <jsp: ... >
        return removeSideBlanks(str.substring(begin, end + 1)) + "\n";
    }

    String jspDirectiveString(String str, int begin, int end) {
        //handle <%@ ... %>
        String content = removeSideBlanks(str.substring(begin, end + 1));
        String directType = content.split(" ")[0];
        String parameter = content.split(" ")[1];
        switch (directType) {
            case "page":
                if (parameter.startsWith("contentType")) {
                    String contentType = parameter.substring(12, parameter.length());
                    String part1 = contentType.substring(1, contentType.indexOf("/"));
                    String part2 = contentType.substring(contentType.indexOf("/") + 1, contentType.indexOf(";"));
                    if (part1.equals("html") && part2.equals("text")) {
                        contentType = "\"" + part2 + "/" + part1 + contentType.substring(contentType.indexOf(";"));
                    }
                    //System.out.println(newContentType);
                    header = "response.setContentType(" + contentType + ");";
                    return "";
                }
                break;
            case "import":
                break;
            case "include":
                break;
            case "taglib":
                break;
            default:
                break;
        }
        return "[jspDirectiveString Error:" + content + "]";
    }


    /**************************************   codes and logical below is easy...   ******************************************************/

    String expresstionsString(String str, int begin, int end) {
        //handle <%! ... %>
        //example : "out.println(content);"
        return "out.println(" + removeSideLineFeed(removeSideBlanks(str.substring(begin, end + 1))) + ");" + "\n";
    }

    String declarationsString(String str, int begin, int end) {
        //handle <%! ... %>
        //class globe definition of function and variables
        return removeSideBlanks(str.substring(begin, end + 1)) + "\n";
    }


    String htmlContentString(String str,int begin,int end) {
        //handle default ; unrecognized tags
        if(begin >= end) return "";
        String res = "";
        String html = removeSideBlanksAndLineFeeds(str.substring(begin,end+1));
        StringTokenizer st = new StringTokenizer(html,"\n");
        while (st.hasMoreTokens()) {
            String temp = st.nextToken();
            if(!temp.trim().isEmpty()) {
                res +=  "out.println(\""+temp+"\");"+"\n";
            }
        }
        return res;
    }

    String removeSideLineFeed(String str) {
        //this function is used to remove the blank in each side of the string
        int begin = 0;
        while (str.charAt(begin) == '\n') begin++;
        int end = str.length();
        while (str.charAt(end - 1) == '\n') end--;
        return str.substring(begin, end);
    }

    String removeSideBlanks(String str) {
        //this function is used to remove the blank in each side of the string
        int begin = 0;
        while (str.charAt(begin) == ' ') begin++;
        int end = str.length();
        while (str.charAt(end - 1) == ' ') end--;
        return str.substring(begin, end);
    }

    String removeSideBlanksAndLineFeeds(String str) {
        //转义
        str = str.replace("\"","\\\"");
        str = str.replace("    ","\\t");
        return str;
        //this function is used to remove the blank in each side of the string
        /*int begin = 0;
        while (str.charAt(begin)==' '||str.charAt(begin)=='\n') begin++;
        int end = str.length();
        while (str.charAt(end-1)==' '||str.charAt(end-1)=='\n') end--;
        return str.substring(begin,end);*/
    }

    private boolean stringMatches(String str, int index, String module) {
        //return whether a string starts at index has a prefix module
        if (module.length() + index >= str.length()) return false;
        for (int i = 0; i < module.length(); i++) {
            if (str.charAt(index + i) != module.charAt(i)) return false;
        }
        return true;
    }

    private int getStringIndex(int start, String str, String subStr) {
        /*
        search str[start,str.length())
        return the index that sub_string appear in the string
        if found return the index of the first char
        if not found return -1
        */
        for (int i = start; i < str.length() - subStr.length() + 1; i++) {
            boolean flag = true;
            for (int j = 0; j < subStr.length(); j++) {
                if (subStr.charAt(j) != str.charAt(i + j)) {
                    flag = false;
                    break;
                }
            }
            if (flag) return i;
        }
        return -1;
    }
}