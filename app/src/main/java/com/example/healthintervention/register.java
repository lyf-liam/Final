package com.example.healthintervention;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class register extends AppCompatActivity {

    //要连接的数据库url,注意：此处连接的应该是服务器上的MySQl的地址
    String url = "jdbc:mysql://42.192.249.36:3306/data";
    //连接数据库使用的用户名
    String sqlName = "root";
    //连接的数据库时使用的密码
    String sqlPassword = "root";
    Connection connection=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //获取
        EditText usernameEdt = findViewById(R.id.userName);
        EditText userpasswordEdt = findViewById(R.id.userpassword);
        EditText realnameEdt = findViewById(R.id.realName);
        EditText teleEdt = findViewById(R.id.telephone);
        Button ready = findViewById(R.id.ready);
        Button cancle = findViewById(R.id.cancel);

        //点击确定按钮
        ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                   @Override
                    public void run() {
                        Looper.prepare();
                        //获取输入框的数据
                        String username=usernameEdt.getText().toString();
                        String userpassword=userpasswordEdt.getText().toString();
                        String realname=realnameEdt.getText().toString();
                        String tele=teleEdt.getText().toString();

                        try {
                            //1、加载驱动
                            Class.forName("com.mysql.jdbc.Driver").newInstance();
                            System.out.println("驱动加载成功！！！");

                            //2、获取与数据库的连接
                            connection = DriverManager.getConnection(url, sqlName, sqlPassword);
                            System.out.println("连接数据库成功！！！");

                            //3.sql添加数据语句
                            String sql = "INSERT INTO userinfo (username , password , realname , telephone ) VALUES ( ?, ?, ?, ?)";
                            if (!username.equals("")&&!userpassword.equals("")&&!realname.equals("")&&!tele.equals("")){//判断输入框是否有数据
                                //4.获取用于向数据库发送sql语句的ps
                                PreparedStatement ps=connection.prepareStatement(sql);
                                //获取输入框的数据 添加到mysql数据库
                                ps.setString(1,username);
                                ps.setString(2,userpassword);
                                ps.setString(3,realname);
                                ps.setString(4,tele);
                                ps.executeUpdate();//更新数据库
                                Intent intent=new Intent(register.this,login.class);
                                startActivity(intent);
                                Toast.makeText(register.this,"注册成功",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(register.this,"不能为空",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Problem getting connection"+ e);
                            Log.i("Tag", String.valueOf(e));
                        }
                        finally {
                            if(connection!=null){
                                try {
                                    connection.close();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                        Looper.loop();
                    }
                }).start();

            }
        });

    }
}
