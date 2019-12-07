package com.example.security_essentials;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import javax.crypto.spec.IvParameterSpec;
import android.content.SharedPreferences;
import android.content.SharedPreferences;


public class Register extends AppCompatActivity {
    static String llavesparaencriptar = "01234567abcdefgh";
    static String newllavesparaencriptar = "PBKDF2WithHmacSHA256";
    private static SecretKeySpec secret;
    byte[] cifrado = new byte[0];
    String decifrado;
    private FirebaseAuth mAuth;
    private static String salt = "ssshhhhhhhhhhh!!!!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        final Button registrar = (Button) findViewById(R.id.IdbtnRegistrar);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView mail = (TextView) findViewById(R.id.IdCorreoRegistrar);
                TextView passw = (TextView) findViewById(R.id.IdPasswordRegister);
                TextView repetir = (TextView) findViewById(R.id.IdRepetirPassword);

                if(mail.getText().toString().isEmpty()){
                    Toast.makeText(Register.this, "Digite El Campo Correo", Toast.LENGTH_LONG).show();
                }
                else
                if (passw.getText().toString().equals(repetir.getText().toString())){

                    String newcifrado = encrypt(passw.getText().toString(), llavesparaencriptar) ;
                    String newdecifrado = decrypt(newcifrado, llavesparaencriptar) ;
                    createUser (mail.getText().toString(),decifrado);
                    /*
                    try{
                        SecretKey secret = generateKey();
                        cifrado = encryptMsg(passw.getText().toString(), generateKey());
                        decifrado = decrryptMsg(cifrado, generateKey());

                        createUser (mail.getText().toString(),decifrado);

                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }*/

                }else{
                    Toast.makeText(Register.this, "Password no Coinciden" , Toast.LENGTH_LONG).show();
                }
            }
        });

        //ir a login
        iralogin();
    }

    private void createUser(String email, String password){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.createUserWithEmailAndPassword(email, decifrado)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent cambio = new Intent(Register.this, MainActivity.class);
                            startActivity(cambio);
                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static SecretKey generateKey()throws NoSuchAlgorithmException, InvalidKeyException{
        return secret = new SecretKeySpec(llavesparaencriptar.getBytes(),"AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decrryptMsg(byte[] cipherText, SecretKey secret) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }

    private void iralogin(){
        Button btn = (Button) findViewById(R.id.IdBtnIraLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cambio = new Intent(Register.this, MainActivity.class);
                startActivity(cambio);
            }
        });
    }

    ///////////////////////////////////  Nuevos Metodos ///////////////////////////////////////////

    public static String encrypt(String password, String secret)
    {
        try
        {
            byte[] iv =  new byte[0];
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance(newllavesparaencriptar);
            KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(password.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error mientras se encripta: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String password, String secret){
        try
        {
            byte[] iv = new byte[0];
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance(newllavesparaencriptar);
            KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(password)));
        }
        catch (Exception e) {
            System.out.println("Error mientras se desecncripta: " + e.toString());
        }
        return null;
    }
}
