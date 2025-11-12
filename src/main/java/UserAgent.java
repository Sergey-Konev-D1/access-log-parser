public class UserAgent {
    private String operationSystem;
    private String browser;

    public UserAgent(String userAgentString){
        if(userAgentString.contains("Windows")){
            this.operationSystem = "Windows";
        } else if (userAgentString.contains("macOS") || userAgentString.contains("Macintosh")) {
            this.operationSystem = "macOS";
        } else if (userAgentString.contains("Linux")){
            this.operationSystem = "Linux";
        } else {
            this.operationSystem = "Системаа не определена";
        }

        if (userAgentString.contains("Edge") || userAgentString.contains("Edg")){
            this.browser = "Edge";
        } else if (userAgentString.contains("Firefox")) {
            this.browser = "Firefox";
        } else if (userAgentString.contains("OPR") || userAgentString.contains("Opera")) {
            this.browser = "Opera";
        }  else if (userAgentString.contains("Safari")) {
            this.browser = "Safari";
        } else if (userAgentString.contains("Chrome")) {
            this.browser = "Chrome";
        }
    }

    public String getOperationSystem() {
        return operationSystem;
    }

    public String getBrowser() {
        return browser;
    }
}
