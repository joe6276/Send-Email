package com.example.sendemail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import  javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import java.io.File;
import java.io.IOException;
import java.util.Properties;


public class MainActivity extends AppCompatActivity {
EditText email,Txt_messages;
Button sendi,send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.email);
        Txt_messages=findViewById(R.id.message);
        send=findViewById(R.id.send);
        sendi=findViewById(R.id.sendi);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        sendi.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(MainActivity.this,MainActivity2.class);
                        startActivity(intent);
                    }
                }
        );

        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final  String username="email";
                        final String password="password";
                        final String messagesend=Txt_messages.getText().toString();
                        Properties properties= new Properties();
                        properties.put("mail.smtp.auth","true");
                        properties.put("mail.smtp.starttls.enable","true");
                        properties.put("mail.smtp.host","smtp.gmail.com");
                        properties.put("mail.smtp.port",587);

                        Session session=Session.getInstance(properties,new javax.mail.Authenticator(){
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username,password);
                            }
                        });
                        try{
                            Message message=new MimeMessage(session);
                            message.setFrom(new InternetAddress(username));
                            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email.getText().toString()));
                            message.setSubject("HERE WE ARE");
                            message.setText(messagesend);

                            Multipart multipart= new MimeMultipart();

                            MimeBodyPart textPart= new MimeBodyPart();
                            textPart.setText("My Body");

                            MimeBodyPart attachment= new MimeBodyPart();
                            attachment.attachFile(Environment.getExternalStorageDirectory()+"/first.pdf");

                            multipart.addBodyPart(textPart);
                            multipart.addBodyPart(attachment);

                            message.setContent(multipart);

                            Transport.send(message);
                            Toast.makeText(getApplicationContext(),"Send Success",Toast.LENGTH_LONG).show();
                        }catch (MessagingException | IOException e){
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
        StrictMode.ThreadPolicy policy= new  StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
