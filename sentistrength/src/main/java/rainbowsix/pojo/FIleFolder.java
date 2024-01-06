package rainbowsix.pojo;

/**
 * 文件夹.
 */
public class FIleFolder {
    /** 动态变化的路径. */
    private String virtualPath; //动态变化的路径
    /** 文件夹内的文件名列表. */
    private String[] fileNames;

    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    public void setVirtualPath(String virtualPath) {
        this.virtualPath = virtualPath;
    }

    public String getVirtualPath() {
        return virtualPath;
    }

    public String[] getFileNames() {
        return fileNames;
    }
}
