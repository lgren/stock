import cn.hutool.extra.mail.GlobalMailAccount;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.http.HttpUtil;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;
import lombok.SneakyThrows;
import org.junit.Test;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailTest {
    @SneakyThrows
    public static void main(String[] args) {
        MailAccount account = GlobalMailAccount.INSTANCE.getAccount();
        Session session = MailUtil.getSession(account, false);
        Store store = session.getStore("pop3");
        store.connect("pop3" + account.getHost().substring(account.getHost().indexOf(".")), account.getFrom(), account.getPass()); //163邮箱程序登录属于第三方登录所以这里的密码是163给的授权密码而并非普通的登录密码

        Folder folder = store.getFolder("INBOX");
        /* Folder.READ_ONLY：只读权限
         * Folder.READ_WRITE：可读可写（可以修改邮件的状态）
         */
        folder.open(Folder.READ_WRITE); //打开收件箱

        folder.addMessageCountListener(new MessageCountListener() {
            @Override
            public void messagesAdded(MessageCountEvent me) {
                System.out.println(me);
            }

            @Override
            public void messagesRemoved(MessageCountEvent me) {
                System.out.println(me);
            }
        });
        folder.addMessageChangedListener(me -> {
            System.out.println(me);
        });
        // // 由于POP3协议无法获知邮件的状态,所以getUnreadMessageCount得到的是收件箱的邮件总数
        // System.out.println("未读邮件数: " + folder.getUnreadMessageCount());
        // // 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
        // System.out.println("删除邮件数: " + folder.getDeletedMessageCount());
        // System.out.println("新邮件: " + folder.getNewMessageCount());

        // 获得收件箱中的邮件总数
        System.out.println("邮件总数" + folder.getMessageCount());

        // 得到收件箱中的所有邮件,并解析
        Message[] messages = folder.getMessages();
        for (Message message : messages) {
            String subject = message.getSubject();// 获得邮件主题
            Address from = message.getFrom()[0];// 获得发送者地址
            System.out.println("邮件的主题为: " + subject + "\t发件人地址为: " + from);
        }
        System.out.println();

        for (; ; ) {
            System.out.println("idle不支持进入轮询");
            Thread.sleep(10000); // sleep for freq milliseconds

            // 注意。getMessageCount时会触发监听器
            folder.getMessageCount();
        }
    }

    @SneakyThrows
    @Test
    public void name() {
        MailAccount account = GlobalMailAccount.INSTANCE.getAccount();
        Session session = MailUtil.getSession(account, false);
        Store store = session.getStore("pop3");
        store.connect("pop3" + account.getHost().substring(account.getHost().indexOf(".")), account.getFrom(), account.getPass()); //163邮箱程序登录属于第三方登录所以这里的密码是163给的授权密码而并非普通的登录密码

        Folder folder = store.getFolder("INBOX");
        /* Folder.READ_ONLY：只读权限
         * Folder.READ_WRITE：可读可写（可以修改邮件的状态）
         */
        folder.open(Folder.READ_WRITE); //打开收件箱

        folder.addMessageCountListener(new MessageCountListener() {
            @Override
            public void messagesAdded(MessageCountEvent me) {
                System.out.println(me);
            }

            @Override
            public void messagesRemoved(MessageCountEvent me) {
                System.out.println(me);
            }
        });
        // // 由于POP3协议无法获知邮件的状态,所以getUnreadMessageCount得到的是收件箱的邮件总数
        // System.out.println("未读邮件数: " + folder.getUnreadMessageCount());
        // // 由于POP3协议无法获知邮件的状态,所以下面得到的结果始终都是为0
        // System.out.println("删除邮件数: " + folder.getDeletedMessageCount());
        // System.out.println("新邮件: " + folder.getNewMessageCount());

        // 获得收件箱中的邮件总数
        System.out.println("邮件总数" + folder.getMessageCount());

        // 得到收件箱中的所有邮件,并解析
        Message[] messages = folder.getMessages();
        for (Message message : messages) {
            String subject = message.getSubject();// 获得邮件主题
            Address from = message.getFrom()[0];// 获得发送者地址
            System.out.println("邮件的主题为: " + subject + "\t发件人地址为: " + from);
        }
        System.out.println();

        HttpUtil.createServer(8888).addAction("/", (req, res)-> res.write("Hello Hutool Server")).start();
    }

    @SneakyThrows
    @Test
    public void imap() {
        String user = "lgren1234@163.com";// 邮箱的用户名
        // String password = "XHAUBJXWSQONHCUD"; // 邮箱的密码
        String password = "KNRPERRXQGANYHIU"; // 邮箱的密码
        // http://config.mail.163.com/settings/imap/index.jsp?uid=lgren1234@163.com
        Properties prop = System.getProperties();
        prop.put("mail.store.protocol", "imap");
        prop.put("mail.imap.host", "imap.163.com");

        Session session = Session.getInstance(prop);

        int total = 0;
        IMAPStore store = (IMAPStore) session.getStore("imap"); // 使用imap会话机制，连接服务器
        store.connect(user, password);
        IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX"); // 收件箱
        folder.open(Folder.READ_WRITE);
        // 获取总邮件数
        total = folder.getMessageCount();
        System.out.println("-----------------共有邮件：" + total + " 封--------------");
        // 得到收件箱文件夹信息，获取邮件列表
        System.out.println("未读邮件数：" + folder.getUnreadMessageCount());
        Message[] messages = folder.getMessages();
    }

//     @Test
//     public void imap2() {
//         String protocol = "imap";
//
//         Properties props = new Properties();
//
//         props.setProperty("mail.transport.protocol", protocol);
//
//         props.setProperty("mail.smtp.host", host); // 发件人的邮箱的 SMTP服务器地址
//
// // 获取连接
//
//         Session session = Session.getInstance(props);
//
//         session.setDebug(false);
//
// // 获取Store对象
//
//         Store store = session.getStore(protocol);
//
//         store.connect(host, userName, password);
//
//         IMAPFolder inbox = (IMAPFolder) store.getFolder("INBOX");
//
// //如果需要在取得邮件数后将邮件置为已读则这里需要使用READ_WRITE,否则READ_ONLY就可以
//
//         inbox.open(Folder.READ_WRITE);
//
// // 全部邮件数
//
//         int messageCount = inbox.getMessageCount();
//
//         System.out.println(messageCount);
//
//         folder.getUnreadMessageCount() 未读取邮件总数
//
//         Message[] messages = inbox.getMessages(folder.getMessageCount() - inbox.getUnreadMessageCount() + 1, inbox.getMessageCount());
//     }
}
