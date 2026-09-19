package br.edu.unisenai.rangonaregua;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.edu.unisenai.rangonaregua.MainActivity;
import br.edu.unisenai.rangonaregua.R;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtSenha;
    Button btnEntrar, btnCriarConta, btnRecuperarSenha;
    FirebaseAuth autenticar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnCriarConta = findViewById(R.id.btnCriarConta);
        btnRecuperarSenha = findViewById(R.id.btnRecuperarSenha);
        
        btnEntrar.setOnClickListener(v -> entrar());
        btnCriarConta.setOnClickListener(v -> criarConta());
        btnRecuperarSenha.setOnClickListener(v -> recuperarSenha());

        // acessar ja logado
        autenticar = FirebaseAuth.getInstance(); 
        if (autenticar.getCurrentUser() != null) {
        Intent rota = new Intent(this, MainActivity.class);
        startActivity(rota);
        finish();
        }

    }

    private void recuperarSenha() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = edtEmail.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "E-mail enviada", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void entrar() {

        if (edtEmail.getText().toString().isEmpty()) {
            edtEmail.setError("Obrigada!");
            return;
        }

        if (edtSenha.getText().toString().isEmpty()) {
            edtSenha.setError("Obrigada!");
            return;
        }

        autenticar.signInWithEmailAndPassword(
                        edtEmail.getText().toString(), edtSenha.getText().toString())
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthWeakPasswordException) {
                        Toast.makeText(this, "Senha fraca", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(authResult -> {
                    Intent rota = new Intent(this, MainActivity.class);
                    startActivity(rota);
                    finish();
                });
    }

    private void criarConta() {

        autenticar.createUserWithEmailAndPassword(
                        edtEmail.getText().toString(), edtSenha.getText().toString())
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthWeakPasswordException) {
                        Toast.makeText(this, "Senha fraca", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnSuccessListener(authResult -> {
                    Intent rota = new Intent(this, MainActivity.class);
                    startActivity(rota);
                    finish();
                });
    }
}
