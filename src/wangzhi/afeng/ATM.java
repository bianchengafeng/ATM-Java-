package wangzhi.afeng;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATM {
    private ArrayList<Account> accounts=new ArrayList<>();

    private Scanner sc=new Scanner(System.in);
    private Account loginAcc;
    public void start(){
        while (true) {
            System.out.println("===欢迎进入系统===");
            System.out.println("1、用户登录");
            System.out.println("2、用户开户");
            System.out.println("请选择：");
            int command=sc.nextInt();
            switch(command){
                case 1:
                    //登录
                    login();
                    break;
                case 2:
                    //开户
                    createAccount();
                    break;
                default:
                    System.out.println("没有该操作");

            }
        }
    }
    private void login(){
        System.out.println("==系统登陆==");

        if(accounts.size()==0){
            System.out.println("系统无账号");
            return;
        }


        while (true) {
            System.out.println("请输入卡号：");
            String cardId=sc.next();

            Account acc=getAccountCardId(cardId);
            //先存一下输入的账户，再去进行判断和其他操作
            if(acc==null){
                System.out.println("此卡号不存在");

            }else{
                while (true) {
                    System.out.println("输入密码：");
                    String password=sc.next();
                    if(acc.getPassword().equals(password)){
                        loginAcc=acc;
                        System.out.println(acc.getUserName()+",您登录成功~~~\n卡号为：\n"+acc.getCardId());
                        showUserCommand();
                        return;
                    }else{
                        System.out.println("密码不对，再次输入");
                    }
                }
            }
        }

    }
    private void showUserCommand(){
        while (true) {
            System.out.println("==="+loginAcc.getUserName()+"进行操作： ==");
            System.out.println("1、查询账户");
            System.out.println("2、存款");
            System.out.println("3、取款");
            System.out.println("4、转账");
            System.out.println("5、修改密码");
            System.out.println("6、退出");
            System.out.println("7、注销当前账户");
            System.out.println("请选择：");
            int command=sc.nextInt();
            switch (command){
                case 1:
                    //查询
                    showLoginAccount();
                    break;

                case 2:
                    //存款
                    depositMoney();
                    break;
                case 3:
                    //取款
                    drawMoney();
                    break;
                case 4:
                    //转账
                    transferMoney();
                    break;
                case 5:
                    //密码修改
                    updatePassword();
                    break;
                case 6:
                    //退出
                    System.out.println(loginAcc.getUserName()+"，您成功退出系统");
                    return;
                case 7:
                    //注销
                    if(deleteAccount()){
                        return;
                    }
                    break;
                default:
                    System.out.println("操作不存在");
            }
        }
    }

    private void updatePassword() {
        System.out.println("==账户密码修改==");
        while (true) {
            System.out.println("请输入原密码：");
            String password=sc.next();

            if(loginAcc.getPassword().equals(password)){
                while (true) {
                    System.out.println("请输入新密码：");
                    String newPassword=sc.next();

                    System.out.println("请确认新密码（再输一次新密码）：");
                    String okPassword=sc.next();

                    if(okPassword.equals(newPassword)){
                        loginAcc.setPassword(okPassword);
                        System.out.println("密码修改成功");
                        return;
                    }else{
                        System.out.println("两次密码不一致");
                    }
                }
            }else{
                System.out.println("密码错误！！！");
            }
        }

    }

    private boolean deleteAccount() {
        System.out.println("==销户操作==");
        System.out.println("请问您要销户吗？y/n");
        String command=sc.next();
        switch (command){
            case "y":
            case "Y":
                if(loginAcc.getMoney()==0){
                    accounts.remove(loginAcc);
                    System.out.println("销户成功~~");
                    return true;
                }else{
                    System.out.println("您的帐户余额不为空，无法继续销户");
                    return false;
                }
            default:
                System.out.println("账户继续保留，未进行销户处理");
                return false;
        }
    }
    private void transferMoney() {
        System.out.println("==用户转账==");
        if(accounts.size()<2){
            System.out.println("当前账户仅有您自己，无法进行转账操作。。。");
            return;
        }

        if(loginAcc.getMoney()==0){
            System.out.println("您的余额为0，无法转账。。。");
            return;
        }

        while (true) {
            System.out.println("请输入对方的卡号：");
            String cardId=sc.next();

            Account acc=getAccountCardId(cardId);
            if(acc == null){
                System.out.println("您输入的卡号不存在");
            }else{

                while (true) {
                    String name="*"+acc.getUserName().substring(1);
                    System.out.println("请您输入["+name+"]的姓氏");
                    String preName=sc.next();
                    if(acc.getUserName().startsWith(preName)){
                        System.out.println("请输入转账金额");
                        double money=sc.nextDouble();
                        if(money<= loginAcc.getMoney()){
                            loginAcc.setMoney(loginAcc.getMoney()-money);
                            acc.setMoney(acc.getMoney()+money);
                            System.out.println("转账操作成功~~");
                            return;
                        }else{
                            System.out.println("您的金额不足以支持此次转账，您的当前余额为"+loginAcc.getMoney());
                        }
                    }
                    else{
                        System.out.println("您输入的姓氏不匹配");
                    }
                }
            }
        }
    }
    private void drawMoney() {

            System.out.println("==取钱操作==");
            if(loginAcc.getMoney()<100){
                System.out.println("账户余额不足100元，暂时不允许取钱");
                return;
            }

        while (true) {
            System.out.println("请输入取款金额");
            double money=sc.nextDouble();

            if(loginAcc.getMoney()>=money){
                if(money>loginAcc.getLimit()){
                    System.out.println("您当前单次取款金额超过了每次限额，您单次最多可取："+loginAcc.getLimit());
                }else{
                    loginAcc.setMoney(loginAcc.getMoney()-money);
                    System.out.println("您此次取款："+money+"\n当前余额为："+loginAcc.getMoney());
                    break;
                }
            }else{
                System.out.println("余额不足，当前您账户的余额为："+loginAcc.getMoney());
            }
        }

    }
    private void depositMoney() {
        //存钱
        System.out.println("==存钱操作==");
        System.out.println("请输入金额：");
        double money=sc.nextDouble();

        loginAcc.setMoney(loginAcc.getMoney()+money);
        System.out.println("您存钱"+money+"余额为"+loginAcc.getMoney());
    }
    private void showLoginAccount(){
        System.out.println("==账户信息如下==");
        System.out.println("卡号："+loginAcc.getCardId());
        System.out.println("户主："+loginAcc.getUserName());
        System.out.println("性别："+loginAcc.getSex());
        System.out.println("余额："+loginAcc.getMoney());
        System.out.println("单次取现额度"+loginAcc.getLimit());
    }
    private void createAccount(){
        Account acc=new Account();
        System.out.println("请您开始开户操作：");
        System.out.println("请输入账户名称：");
        String name=sc.next();
        acc.setUserName(name);

        while (true) {
            System.out.println("请输入性别：");
            char sex=sc.next().charAt(0);
            if(sex=='男'||sex=='女'){
                acc.setSex(sex);
                break;
            }else{
                System.out.println("性别输入有误，请再次输入：");
            }
        }

        while (true) {
            System.out.println("请输入您的密码：");
            String password=sc.next();
            System.out.println("请 确认 您的密码：");
            String okPassword=sc.next();

            if(okPassword.equals(password)){
                acc.setPassword(password);
                break;
            }else{
                System.out.println("两次输入不一致，请重新操作：");
            }
        }

        System.out.println("请输入设置您的取款限额");
        double limit= sc.nextDouble();
        acc.setLimit(limit);

        String newCardId=createCardId();
        acc.setCardId(newCardId);

        accounts.add(acc);
        System.out.println(acc.getUserName()+",您现在开户成功！！\n您的卡号为："+acc.getCardId());
    }
    private String createCardId(){
        while (true) {
            String cardId="";
            Random r=new Random();
            for (int i = 0; i < 8; i++) {
                int date=r.nextInt(10);
                cardId +=date;
            }
            Account acc=getAccountCardId(cardId);
            if(acc==null){
                return cardId;
            }
        }
    }
    private Account getAccountCardId(String cardId){
        for (int i = 0; i < accounts.size(); i++) {
            Account acc=accounts.get(i);
            if(acc.getCardId().equals(cardId)){
                return acc;
            }
        }
        return null;
        //若查无此id，则返回null
    }
    
}
