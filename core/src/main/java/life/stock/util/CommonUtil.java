package life.stock.util;

import cn.hutool.core.io.FileUtil;

public class CommonUtil {
    /**
     * 将内容写入到文件中
     * @param filePathname 文件完整路径
     * @param content 内容
     */
    public static String writeToFile(String filePathname, String content) {
        FileUtil.writeUtf8String(content, filePathname);
        return filePathname;
    }
}
